package com.lvmama.bms.scheduler.store.mysql.mapper;

import com.lvmama.bms.scheduler.store.domain.po.statis.TokenStatisPO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

public interface TokenStatisMapper {

    int batchSave(@Param("tokenStatis") Collection<TokenStatisPO> tokenStatis);

}
