package com.lvmama.bms.scheduler.store.mysql;

import com.lvmama.bms.scheduler.store.MsgTypeStore;
import com.lvmama.bms.scheduler.store.domain.po.MsgTypePO;
import com.lvmama.bms.scheduler.store.mysql.mapper.MsgTypeMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class MySqlMsgTypeStore extends AbstractMySqlStore implements MsgTypeStore {

    public MySqlMsgTypeStore(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public List<MsgTypePO> queryAllMsgType() {

        return doInvoke(new Operation<List<MsgTypePO>>() {
            @Override
            public List<MsgTypePO> doOperation(SqlSession sqlSession) {
                MsgTypeMapper mapper = sqlSession.getMapper(MsgTypeMapper.class);
                return mapper.queryAllMsgType();
            }
        });

    }

}
