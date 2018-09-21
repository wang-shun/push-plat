package com.lvmama.bms.client.support;

import com.lvmama.bms.core.AppContext;
import com.lvmama.bms.core.cluster.NodeType;
import com.lvmama.bms.core.domain.monitor.MsgClientMData;
import com.lvmama.bms.core.domain.monitor.MData;
import com.lvmama.bms.core.monitor.AbstractMStatReporter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Robert HG (254963746@qq.com) on 3/12/16.
 */
public class MsgClientMStatReporter extends AbstractMStatReporter {

    public MsgClientMStatReporter(AppContext appContext) {
        super(appContext);
    }

    // 提交成功的个数
    private AtomicLong submitSuccessNum = new AtomicLong(0);
    // 提交失败的个数
    private AtomicLong submitFailedNum = new AtomicLong(0);
    // 存储FailStore的个数
    private AtomicLong failStoreNum = new AtomicLong(0);
    // 提交FailStore的个数
    private AtomicLong submitFailStoreNum = new AtomicLong(0);
    // 处理的反馈的个数
    private AtomicLong handleFeedbackNum = new AtomicLong(0);

    public void incSubmitSuccessNum(int num) {
        submitSuccessNum.addAndGet(num);
    }

    public void incSubmitFailedNum(int num) {
        submitFailedNum.addAndGet(num);
    }

    public void incFailStoreNum() {
        failStoreNum.incrementAndGet();
    }

    public void incSubmitFailStoreNum(int num) {
        submitFailStoreNum.addAndGet(num);
    }

    public void incHandleFeedbackNum(int num) {
        handleFeedbackNum.addAndGet(num);
    }

    @Override
    protected MData collectMData() {
        MsgClientMData mData = new MsgClientMData();
        mData.setSubmitSuccessNum(submitSuccessNum.getAndSet(0));
        mData.setSubmitFailedNum(submitFailedNum.getAndSet(0));
        mData.setFailStoreNum(failStoreNum.getAndSet(0));
        mData.setSubmitFailStoreNum(submitFailStoreNum.getAndSet(0));
        mData.setHandleFeedbackNum(handleFeedbackNum.getAndSet(0));
        return mData;
    }

    @Override
    protected NodeType getNodeType() {
        return NodeType.MSG_CLIENT;
    }
}
