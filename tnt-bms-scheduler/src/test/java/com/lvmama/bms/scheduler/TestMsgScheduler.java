package com.lvmama.bms.scheduler;

import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.scheduler.config.CfgException;
import com.lvmama.bms.scheduler.config.MsgSchedulerCfg;
import com.lvmama.bms.scheduler.config.MsgSchedulerCfgLoader;

import java.util.Map;

public class TestMsgScheduler {

    public static void main(String[] args) {
        try {
            MsgSchedulerCfg cfg = MsgSchedulerCfgLoader.load(args);

            final MessageScheduler scheduler = new MessageScheduler();
            scheduler.setRegistryAddress(cfg.getRegistryAddress());
            scheduler.setListenPort(cfg.getListenPort());
            scheduler.setClusterName(cfg.getClusterName());
            if (StringUtils.isNotEmpty(cfg.getBindIp())) {
                scheduler.setBindIp(cfg.getBindIp());
            }

            for (Map.Entry<String, String> config : cfg.getConfigs().entrySet()) {
                scheduler.addConfig(config.getKey(), config.getValue());
            }
            // 启动节点
            scheduler.start();

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    scheduler.stop();
                }
            }));

        } catch (CfgException e) {
            System.err.println("MessageScheduler Startup Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
