package com.lvmama.bms.pusher;

import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.pusher.config.CfgException;
import com.lvmama.bms.pusher.config.MsgPusherCfg;
import com.lvmama.bms.pusher.config.MsgPusherLoader;

import java.util.Map;

/**
 * pusher启动类
 */
public class PusherRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(PusherRunner.class);
    public static void main(String[] args){
        try {
            MsgPusherCfg cfg = MsgPusherLoader.load(args);

            final MessagePusher pusher = new MessagePusher();
            pusher.setClusterName(cfg.getClusterName());
            pusher.setRegistryAddress(cfg.getRegistryAddress());
            pusher.setFastWorkThreads(cfg.getFastWorkThreads());
            pusher.setSlowWorkThreads(cfg.getSlowWorkThreads());

            for (Map.Entry<String, String> config : cfg.getConfig().entrySet()) {
                pusher.addConfig(config.getKey(), config.getValue());
            }
            /**
             * 启动节点
             */
            pusher.start();

            Runtime.getRuntime().addShutdownHook(new Thread(){
                @Override
                public void run() {
                    pusher.stop();
                }
            });
        } catch (CfgException e) {
            LOGGER.error(e);
        }
    }
}
