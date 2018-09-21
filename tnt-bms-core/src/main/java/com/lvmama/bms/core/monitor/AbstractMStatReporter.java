package com.lvmama.bms.core.monitor;

import com.lvmama.bms.core.AppContext;
import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.core.cluster.NodeType;
import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.domain.monitor.MData;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.jvmmonitor.JVMMonitor;
import com.lvmama.bms.core.factory.NamedThreadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Robert HG (254963746@qq.com) on 8/30/15.
 */
public abstract class AbstractMStatReporter implements MStatReporter {

    protected final Logger LOGGER = LoggerFactory.getLogger(AbstractMStatReporter.class);

    protected AppContext appContext;
    protected Config config;

    private ScheduledExecutorService executor = Executors
            .newSingleThreadScheduledExecutor(new NamedThreadFactory("LTS-Monitor-data-collector", true));
    private ScheduledFuture<?> scheduledFuture;
    private AtomicBoolean start = new AtomicBoolean(false);

    public AbstractMStatReporter(AppContext appContext) {
        this.appContext = appContext;
        this.config = appContext.getConfig();
    }

    public final void start() {

        // 启动JVM监控
        JVMMonitor.start();

        try {
            if (!config.getParameter(ExtConfig.M_STAT_REPORTER_CLOSED, false)) {
                if (start.compareAndSet(false, true)) {
                    scheduledFuture = executor.scheduleWithFixedDelay(
                            new MStatReportWorker(appContext, this), 1, 1, TimeUnit.SECONDS);
                    LOGGER.info("MStatReporter start succeed.");
                }
            }
        } catch (Exception e) {
            LOGGER.error("MStatReporter start failed.", e);
        }
    }

    /**
     * 用来收集数据
     */
    protected abstract MData collectMData();

    protected abstract NodeType getNodeType();

    public final void stop() {
        try {
            if (start.compareAndSet(true, false)) {
                scheduledFuture.cancel(true);
                executor.shutdown();
                JVMMonitor.stop();
                LOGGER.info("MStatReporter stop succeed.");
            }
        } catch (Exception e) {
            LOGGER.error("MStatReporter stop failed.", e);
        }
    }

}
