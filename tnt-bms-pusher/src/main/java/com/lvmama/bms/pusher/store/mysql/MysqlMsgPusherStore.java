package com.lvmama.bms.pusher.store.mysql;

import com.lvmama.bms.pusher.store.MsgPusherStore;
import com.lvmama.bms.pusher.store.domain.MsgPusherPO;
import com.lvmama.bms.pusher.store.mysql.mapper.MsgPusherMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class MysqlMsgPusherStore extends AbstractMySqlStore implements MsgPusherStore {

    public MysqlMsgPusherStore(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public List<MsgPusherPO> getAllMsgPusher() {
        return doInvoke(new Operation<List<MsgPusherPO>>() {
            @Override
            public List<MsgPusherPO> doOperation(SqlSession sqlSession) {
                MsgPusherMapper mapper = sqlSession.getMapper(MsgPusherMapper.class);
                return mapper.getAllMsgPusher();
            }
        });
    }

    @Override
    public int saveMsgPusher(final MsgPusherPO msgPusher) {
        return doInvoke(new Operation<Integer>() {
            @Override
            public Integer doOperation(SqlSession sqlSession) {
                MsgPusherMapper mapper = sqlSession.getMapper(MsgPusherMapper.class);
                return mapper.saveMsgPusher(msgPusher);
            }
        });
    }

    @Override
    public MsgPusherPO getMsgPusherById(final Integer id) {
        return doInvoke(new Operation<MsgPusherPO>() {
            @Override
            public MsgPusherPO doOperation(SqlSession sqlSession) {
                MsgPusherMapper mapper = sqlSession.getMapper(MsgPusherMapper.class);
                return mapper.getMsgPusherById(id);
            }
        });
    }
}
