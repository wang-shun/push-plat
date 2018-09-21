package com.lvmama.bms.core.support;

import com.lvmama.bms.core.AppContext;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.ec.EventInfo;
import com.lvmama.bms.ec.Observer;
import com.lvmama.bms.core.commons.utils.Callable;
import com.lvmama.bms.core.constant.EcTopic;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.ec.EventSubscriber;

/**
 * @author Robert HG (254963746@qq.com) on 3/14/16.
 */
public class NodeShutdownHook {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeShutdownHook.class);

    public static void registerHook(AppContext appContext, final String name, final Callable callback) {
        appContext.getEventCenter().subscribe(new EventSubscriber(name + "_" + appContext.getConfig().getIdentity(), new Observer() {
            @Override
            public void onObserved(EventInfo eventInfo) {
                if (callback != null) {
                    try {
                        callback.call();
                    } catch (Exception e) {
                        LOGGER.warn("Call shutdown hook {} error", name, e);
                    }
                }
            }
        }), EcTopic.NODE_SHUT_DOWN);
    }

}
