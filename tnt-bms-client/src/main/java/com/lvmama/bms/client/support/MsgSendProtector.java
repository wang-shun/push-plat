package com.lvmama.bms.client.support;

import com.lvmama.bms.client.domain.MsgClientAppContext;
import com.lvmama.bms.client.domain.Response;
import com.lvmama.bms.core.commons.concurrent.limiter.RateLimiter;
import com.lvmama.bms.core.constant.Constants;
import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.domain.Message;
import com.lvmama.bms.core.exception.MsgSendException;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用来处理客户端请求过载问题
 *
 * @author Robert HG (254963746@qq.com) on 5/21/15.
 */
public class MsgSendProtector {

    private int maxQPS;
    // 用信号量进行过载保护
    RateLimiter rateLimiter;
    private int acquireTimeout = 100;
    private String errorMsg;

    public MsgSendProtector(MsgClientAppContext appContext) {

        this.maxQPS = appContext.getConfig().getParameter(ExtConfig.JOB_SUBMIT_MAX_QPS,
                Constants.DEFAULT_JOB_SUBMIT_MAX_QPS);
        if (this.maxQPS < 10) {
            this.maxQPS = Constants.DEFAULT_JOB_SUBMIT_MAX_QPS;
        }

        this.errorMsg = "the maxQPS is " + maxQPS +
                " , submit too fast , use " + ExtConfig.JOB_SUBMIT_MAX_QPS +
                " can change the concurrent size .";
        this.acquireTimeout = appContext.getConfig().getParameter(ExtConfig.JOB_SUBMIT_LOCK_ACQUIRE_TIMEOUT, 100);

        this.rateLimiter = RateLimiter.create(this.maxQPS);
    }

    public Response execute(final List<Message> messages, final MsgSendExecutor<Response> msgSendExecutor) throws MsgSendException {
        if (!rateLimiter.tryAcquire(acquireTimeout, TimeUnit.MILLISECONDS)) {
            throw new MsgSendProtectException(maxQPS, errorMsg);
        }
        return msgSendExecutor.execute(messages);
    }

    public int getMaxQPS() {
        return maxQPS;
    }
}
