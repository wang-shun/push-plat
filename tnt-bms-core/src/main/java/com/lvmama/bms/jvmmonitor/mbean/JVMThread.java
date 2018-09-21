package com.lvmama.bms.jvmmonitor.mbean;

import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.core.logger.Logger;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author Robert HG (254963746@qq.com) on 9/15/15.
 */
@SuppressWarnings("restriction")
public class JVMThread implements JVMThreadMBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(JVMThread.class);

    private ThreadLocal<Long> lastCPUTimeThreadLocal = new ThreadLocal<Long>();
    private ThreadLocal<Long> lastCPUUpTimeThreadLocal = new ThreadLocal<Long>();

    private OperatingSystemMXBean OperatingSystem;
    private RuntimeMXBean Runtime;

    private static final JVMThread instance = new JVMThread();

    public static JVMThread getInstance() {
        return instance;
    }

    private ThreadMXBean threadMXBean;

    private JVMThread() {
        threadMXBean = ManagementFactory.getThreadMXBean();
        OperatingSystem = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        Runtime = ManagementFactory.getRuntimeMXBean();
    }

    @Override
    public BigDecimal getProcessCpuTimeRate() {
        long cpuTime = OperatingSystem.getProcessCpuTime();
        long upTime = Runtime.getUptime();

        Long lastCPUTime = lastCPUTimeThreadLocal.get();
        if(lastCPUTime==null) {
            lastCPUTime = 0L;
        }

        Long lastCPUUpTime = lastCPUUpTimeThreadLocal.get();
        if(lastCPUUpTime==null) {
            lastCPUUpTime = 0L;
        }

        long elapsedCpu = cpuTime - lastCPUTime;
        long elapsedTime = upTime - lastCPUUpTime;

        lastCPUTimeThreadLocal.set(cpuTime);
        lastCPUUpTimeThreadLocal.set(upTime);

        BigDecimal cpuRate;
        if (elapsedTime <= 0) {
            return new BigDecimal(0);
        }

        //计算百分比
        float cpuUsage = elapsedCpu / (elapsedTime * 1000000F * OperatingSystem.getAvailableProcessors()) * 100;
        cpuRate = new BigDecimal(cpuUsage, new MathContext(4));

        return cpuRate;
    }

    @Override
    public int getDaemonThreadCount() {
        return threadMXBean.getDaemonThreadCount();
    }

    @Override
    public int getThreadCount() {
        return threadMXBean.getThreadCount();
    }

    @Override
    public long getTotalStartedThreadCount() {
        return threadMXBean.getTotalStartedThreadCount();
    }

    @Override
    public int getDeadLockedThreadCount() {
        try {
            long[] deadLockedThreadIds = threadMXBean.findDeadlockedThreads();
            if (deadLockedThreadIds == null) {
                return 0;
            }
            return deadLockedThreadIds.length;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}
