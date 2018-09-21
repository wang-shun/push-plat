package com.lvmama.tnt.bms.admin.web.mapper;

import com.lvmama.tnt.bms.admin.web.domain.po.MessageDO;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsSendSpeedDO;
import com.lvmama.tnt.bms.admin.web.domain.vo.NewsSpeedDetailVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author longhr
 * @version 2017/11/8 0008
 */
@Repository
public interface NewsSendSpeedMapper {

    List<String> findMessageID(@Param("typeID") String typeID, @Param("msgID") String msgID);

    MessageDO findMsgInfo(@Param("typeID") String typeID, @Param("msgID") String msgID);

    List<NewsSendSpeedDO> findByPage(@Param("msgId") List<String> msgId, @Param("msgTypeID")Long msgTypeID,
                                     @Param("startTime") Long startTime, @Param("endTime") Long endTime, RowBounds rowBounds);

    long totalCount(@Param("msgId") List<String> msgId, @Param("msgTypeID")Long msgTypeID, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    List<NewsSpeedDetailVO> findSpeedDetailByPage(@Param("msgId")String msgId, @Param("distributorName") String distributorName,
                                                  @Param("typeID")String typeID, @Param("status")Integer status, RowBounds rowBounds);

    long totalSpeedDetailCount(@Param("msgId")String msgId, @Param("distributorName") String distributorName, @Param("typeID")String typeID,@Param("status")Integer status);

    int insert(NewsSendSpeedDO newsSendSpeedDO);

    int update(NewsSendSpeedDO newsSendSpeedDO);

    int delete(Long id);


}
