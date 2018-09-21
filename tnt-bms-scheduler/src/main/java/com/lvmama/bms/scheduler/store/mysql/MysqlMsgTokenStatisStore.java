package com.lvmama.bms.scheduler.store.mysql;

import com.lvmama.bms.scheduler.store.MsgTokenStatisStore;
import com.lvmama.bms.scheduler.store.domain.po.statis.MsgTokenStatisPO;
import com.lvmama.bms.scheduler.store.mysql.mapper.MsgTokenStatisMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class MysqlMsgTokenStatisStore extends AbstractMySqlStore implements MsgTokenStatisStore {

    public MysqlMsgTokenStatisStore(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public int saveOrUpdate(final MsgTokenStatisPO msgTokenStatis) {

        return doInvoke(new Operation<Integer>() {
            @Override
            public Integer doOperation(SqlSession sqlSession) {
                MsgTokenStatisMapper msgTokenStatisMapper = sqlSession.getMapper(MsgTokenStatisMapper.class);
                return msgTokenStatisMapper.saveOrUpdate(msgTokenStatis);
            }
        });

    }
}
