package com.lvmama.bms.scheduler.store.mysql;

import com.lvmama.bms.scheduler.store.MsgPushFailStore;
import com.lvmama.bms.scheduler.store.domain.po.MsgPushFailPO;
import com.lvmama.bms.scheduler.store.mysql.mapper.MsgPushFailMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Date;

public class MySqlMsgPushFailStore extends AbstractMySqlStore implements MsgPushFailStore {

    public MySqlMsgPushFailStore(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public int save(final MsgPushFailPO msgPushFailPO) {
        return doInvoke(new Operation<Integer>() {
            @Override
            public Integer doOperation(SqlSession sqlSession) {
                MsgPushFailMapper msgPushFailMapper = sqlSession.getMapper(MsgPushFailMapper.class);
                return msgPushFailMapper.save(msgPushFailPO);
            }
        });
    }

    @Override
    public int deleteExpiredMsg(final Date deadline) {
        return doInvoke(new Operation<Integer>() {
            @Override
            public Integer doOperation(SqlSession sqlSession) {
                MsgPushFailMapper msgPushFailMapper = sqlSession.getMapper(MsgPushFailMapper.class);
                return msgPushFailMapper.deleteExpiredMsg(deadline);
            }
        });
    }

}
