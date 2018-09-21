package com.lvmama.bms.pusher.runner;

import com.lvmama.bms.core.domain.dto.MessageDTO;
import com.lvmama.bms.pusher.domain.Response;

/**
 * @author Robert HG (254963746@qq.com) on 8/16/14.
 */
public interface RunnerCallback {

    /**
     * 执行完成, 可能是成功, 也可能是失败
     * @return 如果有新的任务, 那么返回新的任务过来
     */
    public MessageDTO runComplete(Response response);

}
