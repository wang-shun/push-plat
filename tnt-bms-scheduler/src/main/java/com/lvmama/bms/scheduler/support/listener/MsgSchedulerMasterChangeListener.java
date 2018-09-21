package com.lvmama.bms.scheduler.support.listener;

import com.lvmama.bms.core.cluster.Node;
import com.lvmama.bms.core.listener.MasterChangeListener;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;

/**
 * @author Robert HG (254963746@qq.com) on 8/24/14.
 *         MessageScheduler master 节点变化之后
 */
public class MsgSchedulerMasterChangeListener implements MasterChangeListener {

    private MsgSchedulerAppContext appContext;

    public MsgSchedulerMasterChangeListener(MsgSchedulerAppContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public void change(Node master, boolean isMaster) {

        if (appContext.getConfig().getIdentity().equals(master.getIdentity())) {
            // 如果 master 节点是自己
            appContext.getMsgStatis().start();
//            appContext.getMessageDispatcher().start();
            appContext.getMessageCorrector().start();
            appContext.getMessageCleaner().start();
            appContext.setMaster(true);
        } else {
            // 如果 master 节点不是自己
        }
    }
}
