package com.lvmama.bms.pusher.runner;

import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.domain.dto.MessageDTO;
import com.lvmama.bms.core.factory.NamedThreadFactory;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.core.registry.Registry;
import com.lvmama.bms.pusher.domain.MsgPusherAppContext;
import com.lvmama.bms.pusher.expcetion.NoAvailableJobRunnerException;
import com.lvmama.bms.zookeeper.DataListener;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Robert HG (254963746@qq.com) on 8/14/14.
 *         线程池管理
 */
public class RunnerPool {

    private final Logger LOGGER = LoggerFactory.getLogger(RunnerPool.class);

    public enum ThreadPoolType {
        Fast, Slow
    }

    private ThreadPoolExecutor fastThreadPool;
    private ThreadPoolExecutor slowThreadPool;

    private MsgPusherAppContext appContext;

    public RunnerPool(final MsgPusherAppContext appContext) {
        this.appContext = appContext;

        fastThreadPool = initThreadPoolExecutor(ThreadPoolType.Fast);
        slowThreadPool = initThreadPoolExecutor(ThreadPoolType.Slow);

        // 向事件中心注册事件, 改变工作线程大小
        /*appContext.getEventCenter().subscribe(
                new EventSubscriber(appContext.getConfig().getIdentity(), new Observer() {
                    @Override
                    public void onObserved(EventInfo eventInfo) {
                        setWorkThread(appContext.getConfig().getWorkThreads());
                    }
                }), EcTopic.WORK_THREAD_CHANGE);*/
    }

    private ThreadPoolExecutor initThreadPoolExecutor(final ThreadPoolType threadPoolType) {
    	
        Registry registry = appContext.getRegistry();
        
        String path = registry.getAbsolutePath(appContext.getConfig(), ExtConfig.TASK_WORK_THREAD+"."+threadPoolType.name());
        int workThreads = registry.getConfig(path, ThreadPoolType.Fast.equals(threadPoolType) ? appContext.getConfig().getFastWorkThreads(): appContext.getConfig().getSlowWorkThreads());
        
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(workThreads, workThreads, 30, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),           // 直接提交给线程而不保持它们
                new NamedThreadFactory("JobRunnerPool_"+threadPoolType.name()),
                new ThreadPoolExecutor.AbortPolicy());
        
        registry.addListener(path, new DataListener() {
			
			@Override
			public void dataDeleted(String dataPath) throws Exception {
                if(threadPoolType.equals(ThreadPoolType.Fast)) {
                    executor.setCorePoolSize(appContext.getConfig().getFastWorkThreads());
                    executor.setMaximumPoolSize(appContext.getConfig().getFastWorkThreads());
                    LOGGER.info("参数调整"+ExtConfig.TASK_WORK_THREAD+"."+threadPoolType.name()+"="+appContext.getConfig().getFastWorkThreads());
                } else if(threadPoolType.equals(ThreadPoolType.Slow)) {
                    executor.setCorePoolSize(appContext.getConfig().getSlowWorkThreads());
                    executor.setMaximumPoolSize(appContext.getConfig().getSlowWorkThreads());
                    LOGGER.info("参数调整"+ExtConfig.TASK_WORK_THREAD+"."+threadPoolType.name()+"="+appContext.getConfig().getSlowWorkThreads());
                }
			}
			
			@Override
			public void dataChange(String dataPath, Object data) throws Exception {
			    if(data != null) {
                    executor.setCorePoolSize((Integer)data);
                    executor.setMaximumPoolSize((Integer)data);
                    LOGGER.info("参数调整"+ExtConfig.TASK_WORK_THREAD+"."+threadPoolType.name()+"="+data);
                }
			}
		});
        
        return executor;
        
    }

    public void execute(MessageDTO message, RunnerCallback callback) throws NoAvailableJobRunnerException {
        try {
            LOGGER.info("tip=Receive Message success|" + message.clearMsgContent());
            if(message.isSpeedFast()) {
                fastThreadPool.execute(new PushRunner(appContext, message, callback));
            } else {
                slowThreadPool.execute(new PushRunner(appContext, message, callback));
            }
        } catch (RejectedExecutionException e) {
            LOGGER.warn("tip=No more thread to run message|"+message+"|exception=", e);
            throw new NoAvailableJobRunnerException(e);
        }
    }

    /**
     * 得到当前可用的线程数
     */
    public int getAvailablePoolSize(ThreadPoolType poolType) {
        if(ThreadPoolType.Fast.equals(poolType)) {
            return fastThreadPool.getMaximumPoolSize() - fastThreadPool.getActiveCount();
        } else if (ThreadPoolType.Slow.equals(poolType)){
            return slowThreadPool.getMaximumPoolSize() - slowThreadPool.getActiveCount();
        }
        return 0;
    }

    public void setWorkThread(int workThread, ThreadPoolType poolType) {
        if (workThread == 0) {
            throw new IllegalArgumentException("workThread can not be zero!");
        }

        if(ThreadPoolType.Fast.equals(poolType)) {
            fastThreadPool.setMaximumPoolSize(workThread);
            fastThreadPool.setCorePoolSize(workThread);
        } else if(ThreadPoolType.Slow.equals(poolType)) {
            slowThreadPool.setMaximumPoolSize(workThread);
            slowThreadPool.setCorePoolSize(workThread);
        }

        LOGGER.info("workThread update to {}", workThread);
    }

    /**
     * 得到最大线程数
     */
    public int getWorkThread(ThreadPoolType threadPoolType) {
        if(ThreadPoolType.Fast.equals(threadPoolType)) {
            return fastThreadPool.getCorePoolSize();
        } else if(ThreadPoolType.Slow.equals(threadPoolType)) {
            return slowThreadPool.getCorePoolSize();
        }
        return 0;
    }

    /**
     * 执行该方法，线程池的状态立刻变成STOP状态，并试图停止所有正在执行的线程，不再处理还在池队列中等待的任务，当然，它会返回那些未执行的任务。
     * 它试图终止线程的方法是通过调用Thread.interrupt()方法来实现的，但是大家知道，这种方法的作用有限，
     * 如果线程中没有sleep 、wait、Condition、定时锁等应用, interrupt()方法是无法中断当前的线程的。
     * 所以，ShutdownNow()并不代表线程池就一定立即就能退出，它可能必须要等待所有正在执行的任务都执行完成了才能退出。
     * 特殊的时候可以通过使用{@link }来解决
     */
    public void stopWorking() {
        try {
            fastThreadPool.shutdownNow();
            slowThreadPool.shutdownNow();
            Thread.sleep(1000);
            fastThreadPool = initThreadPoolExecutor(ThreadPoolType.Fast);
            slowThreadPool = initThreadPoolExecutor(ThreadPoolType.Slow);
            LOGGER.info("stop working succeed ");
        } catch (Throwable t) {
            LOGGER.error("stop working failed ", t);
        }

    }

    public void shutDown(){
        try {
            fastThreadPool.shutdownNow();
            slowThreadPool.shutdownNow();
            LOGGER.info("stop working succeed ");
        } catch (Throwable t) {
            LOGGER.error("stop working failed ", t);
        }
    }

}
