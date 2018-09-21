package com.lvmama.bms.pusher.runner;

import com.lvmama.bms.cache.RedisTemplate;
import com.lvmama.bms.cache.ShardHashKey;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.core.constant.Constants;
import com.lvmama.bms.core.domain.Action;
import com.lvmama.bms.core.domain.dto.MessageDTO;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.core.support.SystemClock;
import com.lvmama.bms.pusher.domain.MsgPusherAppContext;
import com.lvmama.bms.pusher.domain.Response;
import com.lvmama.bms.pusher.protocol.ProtocolPusher;
import org.apache.commons.logging.Log;
import redis.clients.jedis.Transaction;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * Job Runner 的代理类,
 * 1. 做一些错误处理之类的
 * 2. 监控统计
 * 3. Context信息设置
 *
 * @author Robert HG (254963746@qq.com) on 8/16/14.
 */
public class PushRunner implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushRunner.class);

    private MessageDTO message;
    private RunnerCallback callback;
    private MsgPusherAppContext appContext;

    private RedisTemplate redisTemplate;

    private Logger logger = LoggerFactory.getLogger(PushRunner.class);

    public PushRunner(MsgPusherAppContext appContext,
                      MessageDTO message, RunnerCallback callback) {
        this.appContext = appContext;
        this.callback = callback;
        this.message = message;
        this.redisTemplate = appContext.getRedisTemplate();
    }

    @Override
    public void run() {

        while (message != null) {

            long startTime = SystemClock.now();
            Response response = new Response();
            response.setMessage(message);

            try {
                ProtocolPusher protocolPusher = appContext.getProtocolPusherFactory().getProtocolPusher(message.getPushType());
                if(protocolPusher!=null) {
                    Action action = protocolPusher.push(message);
                    response.setAction(action);
                } else {
                    response.setAction(Action.EXECUTE_FAILED);
                    response.setMsg("The Message Push Type["+message.getPushType()+"] is invalid");
                }

            } catch (Throwable t) {
                StringWriter sw = new StringWriter();
                t.printStackTrace(new PrintWriter(sw));
                response.setAction(Action.EXECUTE_FAILED);
                response.setMsg(sw.toString());
            }

            if (isStopToGetNewMsg()) {
                response.setReceiveNewJob(false);
            }

            response.setTimeCost(SystemClock.now()-startTime);

            this.message = callback.runComplete(response);

        }

    }

    private boolean isStopToGetNewMsg() {
        // 机器资源是否充足
        return !appContext.getConfig().getInternalData(Constants.MACHINE_RES_ENOUGH, true);
    }


}
