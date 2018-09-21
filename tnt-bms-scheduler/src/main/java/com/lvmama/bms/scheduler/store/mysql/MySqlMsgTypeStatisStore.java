package com.lvmama.bms.scheduler.store.mysql;

import com.lvmama.bms.scheduler.store.MsgTypeStatisStore;
import com.lvmama.bms.scheduler.store.domain.po.statis.MsgTypeStatisPO;
import com.lvmama.bms.scheduler.store.mysql.mapper.MsgTypeStatisMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class MySqlMsgTypeStatisStore extends AbstractMySqlStore implements MsgTypeStatisStore {

    public MySqlMsgTypeStatisStore(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public int batchSave(final Collection<MsgTypeStatisPO> msgTypeStatis) {
        return doInvoke(new Operation<Integer>() {
            @Override
            public Integer doOperation(SqlSession sqlSession) {
                MsgTypeStatisMapper msgTypeStatisMapper = sqlSession.getMapper(MsgTypeStatisMapper.class);
                return msgTypeStatisMapper.batchSave(msgTypeStatis);
            }
        });
    }

    @Override
    public List<MsgTypeStatisPO> sum(final Date after, final Date before) {
        return doInvoke(new Operation<List<MsgTypeStatisPO>>() {
            @Override
            public List<MsgTypeStatisPO> doOperation(SqlSession sqlSession) {
                MsgTypeStatisMapper msgTypeStatisMapper = sqlSession.getMapper(MsgTypeStatisMapper.class);
                return msgTypeStatisMapper.sum(after.getTime(), before.getTime());
            }
        });
    }
}
