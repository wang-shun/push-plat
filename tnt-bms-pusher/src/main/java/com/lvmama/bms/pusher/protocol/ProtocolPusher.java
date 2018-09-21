package com.lvmama.bms.pusher.protocol;

import com.lvmama.bms.core.domain.Action;
import com.lvmama.bms.core.domain.dto.MessageDTO;

public interface ProtocolPusher {

    Action push(MessageDTO message);

}
