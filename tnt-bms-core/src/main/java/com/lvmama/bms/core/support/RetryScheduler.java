package com.lvmama.bms.core.support;

import com.lvmama.bms.cache.RedisTemplate;
import com.lvmama.bms.core.commons.utils.DateUtils;
import com.lvmama.bms.core.domain.LocalFileRetryInfo;
import com.lvmama.bms.core.domain.Pair;
import com.lvmama.bms.core.domain.PushResult;
import com.lvmama.bms.core.json.JSON;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.core.spi.ServiceLoader;
import com.lvmama.bms.ec.EventInfo;
import com.lvmama.bms.ec.Observer;
import com.lvmama.bms.core.AppContext;
import com.lvmama.bms.core.cluster.Node;
import com.lvmama.bms.core.commons.utils.CollectionUtils;
import com.lvmama.bms.core.commons.utils.GenericsUtils;
import com.lvmama.bms.core.constant.EcTopic;
import com.lvmama.bms.core.factory.NamedThreadFactory;
import com.lvmama.bms.core.failstore.AbstractFailStore;
import com.lvmama.bms.core.failstore.FailStoreException;
import com.lvmama.bms.core.failstore.FailStoreFactory;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.ec.EventSubscriber;
import com.lvmama.bms.core.failstore.FailStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Robert HG (254963746@qq.com) on 8/19/14.
 *         重试定时器 (用来发送 给 客户端的反馈信息等)
 */
public abstract class RetryScheduler<T> {

    public static final Logger LOGGER = LoggerFactory.getLogger(RetryScheduler.class);

    private Class<?> type = GenericsUtils.getSuperClassGenericType(this.getClass());

    // 定时检查是否有 师表的反馈任务信息(给客户端的)
    private ScheduledExecutorService RETRY_EXECUTOR_SERVICE;
    private ScheduledExecutorService MASTER_RETRY_EXECUTOR_SERVICE;
    private ScheduledFuture<?> masterScheduledFuture;
    private ScheduledFuture<?> scheduledFuture;
    private AtomicBoolean selfCheckStart = new AtomicBoolean(false);
    private AtomicBoolean masterCheckStart = new AtomicBoolean(false);
    private FailStore failStore;
    // 名称主要是用来记录日志
    private String name;

    // 批量发送的消息数
    private int batchSize = 5;

    private ReentrantLock lock = new ReentrantLock();
    private AppContext appContext;
    private Map<String, LocalFileRetryInfo> retryInfoMap = new ConcurrentHashMap<>();

    public RetryScheduler(String name, final AppContext appContext, String storePath) {
        this.name = name;
        this.appContext = appContext;
        FailStoreFactory failStoreFactory = ServiceLoader.load(FailStoreFactory.class, appContext.getConfig());
        failStore = failStoreFactory.getFailStore(appContext.getConfig(), storePath);
        try {
            failStore.open();
        } catch (FailStoreException e) {
            throw new RuntimeException(e);
        }
        EventSubscriber subscriber = new EventSubscriber(RetryScheduler.class.getSimpleName()
                .concat(appContext.getConfig().getIdentity()),
                new Observer() {
                    @Override
                    public void onObserved(EventInfo eventInfo) {
                        Node masterNode = (Node) eventInfo.getParam("master");
                        if (masterNode != null && masterNode.getIdentity().equals(appContext.getConfig().getIdentity())) {
                            startMasterCheck();
                        } else {
                            stopMasterCheck();
                        }
                    }
                });
        appContext.getEventCenter().subscribe(subscriber, EcTopic.MASTER_CHANGED);

        if (appContext.getMasterElector().isCurrentMaster()) {
            startMasterCheck();
        }
    }

    public RetryScheduler(String name, AppContext appContext, String storePath, int batchSize) {
        this(name, appContext, storePath);
        this.batchSize = batchSize;
    }

    public void start() {
        try {
            if (selfCheckStart.compareAndSet(false, true)) {
                this.RETRY_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("LTS-RetryScheduler-retry", true));
                // 这个时间后面再去优化
                scheduledFuture = RETRY_EXECUTOR_SERVICE.scheduleWithFixedDelay
                        (new CheckSelfRunner(), 10, 30, TimeUnit.SECONDS);
                LOGGER.info("Start {} RetryScheduler success, identity=[{}]", name, appContext.getConfig().getIdentity());
            }
        } catch (Throwable t) {
            LOGGER.error("Start {} RetryScheduler failed, identity=[{}]", name, appContext.getConfig().getIdentity(), t);
        }
    }

    private void startMasterCheck() {
        try {
            if (masterCheckStart.compareAndSet(false, true)) {
                this.MASTER_RETRY_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("LTS-RetryScheduler-master-retry", true));
                // 这个时间后面再去优化
                masterScheduledFuture = MASTER_RETRY_EXECUTOR_SERVICE.
                        scheduleWithFixedDelay(new CheckDeadFailStoreRunner(), 30, 60, TimeUnit.SECONDS);
                LOGGER.info("Start {} master RetryScheduler success, identity=[{}]", name, appContext.getConfig().getIdentity());
            }
        } catch (Throwable t) {
            LOGGER.error("Start {} master RetryScheduler failed, identity=[{}]", name, appContext.getConfig().getIdentity(), t);
        }
    }

    private void stopMasterCheck() {
        try {
            if (masterCheckStart.compareAndSet(true, false)) {
                if (masterScheduledFuture != null) {
                    masterScheduledFuture.cancel(true);
                    masterScheduledFuture = null;
                    MASTER_RETRY_EXECUTOR_SERVICE.shutdown();
                    MASTER_RETRY_EXECUTOR_SERVICE = null;
                }
                LOGGER.info("Stop {} master RetryScheduler success, identity=[{}]", name, appContext.getConfig().getIdentity());
            }
        } catch (Throwable t) {
            LOGGER.error("Stop {} master RetryScheduler failed, identity=[{}]", name, appContext.getConfig().getIdentity(), t);
        }
    }

    public void stop() {
        try {
            if (selfCheckStart.compareAndSet(true, false)) {
                if (scheduledFuture != null) {
                    scheduledFuture.cancel(true);
                    scheduledFuture = null;
                    failStore.close();
                    RETRY_EXECUTOR_SERVICE.shutdown();
                    RETRY_EXECUTOR_SERVICE = null;
                }
                LOGGER.info("Stop {} RetryScheduler success, identity=[{}]", name, appContext.getConfig().getIdentity());
            }
            stopMasterCheck();
        } catch (Throwable t) {
            LOGGER.error("Stop {} RetryScheduler failed, identity=[{}]", name, appContext.getConfig().getIdentity(), t);
        }
    }

    public void destroy() {
        try {
            stop();
            failStore.destroy();
        } catch (FailStoreException e) {
            LOGGER.error("destroy {} RetryScheduler failed, identity=[{}]", name, appContext.getConfig().getIdentity(), e);
        }
    }

    private AtomicBoolean checkSelfRunnerStart = new AtomicBoolean(false);

    /**
     * 定时检查 提交失败任务的Runnable
     */
    private class CheckSelfRunner implements Runnable {

        @Override
        public void run() {

            if (!checkSelfRunnerStart.compareAndSet(false, true)) {
                return;
            }

            try {
                // 1. 检测 远程连接 是否可用
                if (!isRemotingEnable()) {
                    return;
                }

                List<Pair<String, T>> pairs = null;
                do {
                    try {
                        lock.tryLock(1000, TimeUnit.MILLISECONDS);
                        pairs = failStore.fetchTop(batchSize, type);

                        if (CollectionUtils.isEmpty(pairs)) {
                            break;
                        }

                        List<T> values = new ArrayList<T>(pairs.size());
                        List<String> keys = new ArrayList<String>(pairs.size());
                        for (Pair<String, T> pair : pairs) {
                            if (checkRetry(pair.getKey())) {
                                keys.add(pair.getKey());
                                values.add(pair.getValue());
                            }
                        }
                        if (CollectionUtils.isEmpty(keys)){
                            return;
                        }
                        List<String> notNeedRetry = retry(keys, values);
                        if (notNeedRetry.size() > 0) {
                            LOGGER.info("{} RetryScheduler, local files send success, identity=[{}], size: {}, {}",
                                    name, appContext.getConfig().getIdentity(), values.size(), JSON.toJSONString(values));
                            failStore.delete(notNeedRetry);
                        } else {
                            break;
                        }
                    } finally {
                        if (lock.isHeldByCurrentThread()) {
                            lock.unlock();
                        }
                    }
                } while (CollectionUtils.isNotEmpty(pairs));

            } catch (Throwable e) {
                LOGGER.error("Run {} RetryScheduler error , identity=[{}]", name, appContext.getConfig().getIdentity(), e);
            } finally {
                checkSelfRunnerStart.set(false);
            }
        }
    }

    private AtomicBoolean checkDeadFailStoreRunnerStart = new AtomicBoolean(false);

    /**
     * 定时检查 已经down掉的机器的FailStore目录
     */
    private class CheckDeadFailStoreRunner implements Runnable {

        @Override
        public void run() {
            if (!checkDeadFailStoreRunnerStart.compareAndSet(false, true)) {
                return;
            }
            try {
                // 1. 检测 远程连接 是否可用
                if (!isRemotingEnable()) {
                    return;
                }
                List<FailStore> failStores = null;
                if (failStore instanceof AbstractFailStore) {
                    failStores = ((AbstractFailStore) failStore).getDeadFailStores();
                }
                if (CollectionUtils.isEmpty(failStores)) {
                    return;
                }
                for (FailStore store : failStores) {
                    store.open();

                    while (true) {
                        List<Pair<String, T>> pairs = store.fetchTop(batchSize, type);
                        if (CollectionUtils.isEmpty(pairs)) {
                            store.destroy();
                            LOGGER.info("{} RetryScheduler, delete store dir[{}] success, identity=[{}] ", name, store.getPath(), appContext.getConfig().getIdentity());
                            break;
                        }
                        List<T> values = new ArrayList<T>(pairs.size());
                        List<String> keys = new ArrayList<String>(pairs.size());
                        for (Pair<String, T> pair : pairs) {
                            if (checkRetry(pair.getKey())) {
                                keys.add(pair.getKey());
                                values.add(pair.getValue());
                            }
                        }
                        if (CollectionUtils.isEmpty(keys)){
                            return;
                        }
                        List<String> notNeedRetry = retry(keys, values);
                        if (notNeedRetry.size() > 0) {
                            LOGGER.info("{} RetryScheduler, dead local files send success, identity=[{}], size: {}, {}"
                                    , name, appContext.getConfig().getIdentity(), values.size(), JSON.toJSONString(values));
                            store.delete(keys);
                        } else {
                            store.close();
                            break;
                        }
                        try {
                            Thread.sleep(500);
                        } catch (Exception ignored) {
                        }
                    }
                }
            } catch (Throwable e) {
                LOGGER.error("Run {} master RetryScheduler error, identity=[{}] ", name, appContext.getConfig().getIdentity(), e);
            } finally {
                checkDeadFailStoreRunnerStart.set(false);
            }
        }
    }

    private boolean checkRetry(String key) {
        LocalFileRetryInfo retryInfo = retryInfoMap.get(key);
        if (retryInfo == null) {
            return false;
        }
        //重试5次，相差3天
        boolean result = true;
        if (retryInfo.getRetryCount() > 5 || diffNow(retryInfo.getCreateTime()) > 3) {
            try {
                LOGGER.info("delete local fail store: {}", retryInfo);
                retryInfoMap.remove(key);
                failStore.delete(key);
                result = false;
            } catch (FailStoreException e) {
                LOGGER.error(e);
            }
        } else {
            retryInfo.setRetryCount(retryInfo.getRetryCount() + 1);
        }
        return result;
    }

    public int diffNow(Long createTime) {
        return (int) ((SystemClock.now() - createTime)/DateUtils.ONE_DAY_MillIS);
    }

    public void inSchedule(String key, T value) {
        try {
            lock.tryLock();
            retryInfoMap.put(key, new LocalFileRetryInfo(SystemClock.now(), 0));
            failStore.put(key, value);
            LOGGER.info("{} RetryScheduler, local files save success, identity=[{}], {}", name, appContext.getConfig().getIdentity(), JSON.toJSONString(value));
        } catch (FailStoreException e) {
            LOGGER.error("{} RetryScheduler in schedule error, identity=[{}]", name, e, appContext.getConfig().getIdentity());
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }


    /**
     * 远程连接是否可用
     */
    protected abstract boolean isRemotingEnable();

    /**
     * 重试
     */
    protected abstract List<String> retry(List<String> keys, List<T> values);

}
