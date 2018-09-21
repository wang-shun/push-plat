package com.lvmama.tnt.bms.admin.web.service;

import com.lvmama.tnt.bms.admin.web.domain.vo.MessageVO;

/**
 *
 */
public interface ManualPushService {

    void pushNews(MessageVO message);
}
