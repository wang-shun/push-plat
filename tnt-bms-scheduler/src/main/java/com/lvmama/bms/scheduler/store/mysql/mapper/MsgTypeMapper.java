package com.lvmama.bms.scheduler.store.mysql.mapper;

        import com.lvmama.bms.scheduler.store.domain.po.MsgTypePO;

        import java.util.List;

public interface MsgTypeMapper {

    List<MsgTypePO> queryAllMsgType();

}
