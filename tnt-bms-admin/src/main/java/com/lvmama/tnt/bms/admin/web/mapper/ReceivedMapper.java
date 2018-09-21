package com.lvmama.tnt.bms.admin.web.mapper;

import com.lvmama.tnt.bms.admin.web.domain.po.Order;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface ReceivedMapper {

    @Insert("insert into t_bms_received(name,orderId,amount,status,product,channel,payway,company,visitor,createUser,startMillis,endMillis) " +
            "values(#{name},#{orderId},#{amount},#{status},#{product},#{channel},#{payway},#{company},#{visitor},#{createUser},#{startMillis},#{endMillis})")
    void save(Order order);

    @Insert("insert into t_received(data,create_time) values(#{data},now())")
    void received(String data);
}
