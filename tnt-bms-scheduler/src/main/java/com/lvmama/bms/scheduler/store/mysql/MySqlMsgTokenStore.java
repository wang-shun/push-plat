package com.lvmama.bms.scheduler.store.mysql;

import com.lvmama.bms.scheduler.store.MsgTokenStore;
import com.lvmama.bms.scheduler.store.domain.po.MsgTokenPO;
import com.lvmama.bms.scheduler.store.mysql.mapper.MsgTokenMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class MySqlMsgTokenStore extends AbstractMySqlStore implements MsgTokenStore {

    public MySqlMsgTokenStore(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public boolean saveMsgToken(final Long msgId, final Integer[] tokenIds, final Integer msgTypeId) {

        return doInvoke(new Operation<Boolean>() {
            @Override
            public Boolean doOperation(SqlSession sqlSession) {
                MsgTokenMapper mapper = sqlSession.getMapper(MsgTokenMapper.class);
                return mapper.saveMsgToken(msgId, tokenIds, msgTypeId) == 1;
            }
        });

    }

    @Override
    public List<MsgTokenPO> getMsgToken(final Set<Integer> tokenInclude,
                                        final Set<Integer> tokenExclude,
                                        final Integer dataCount,
                                        final Integer msgTypeId,
                                        final Integer status) {

        return doInvoke(new Operation<List<MsgTokenPO>>() {
            @Override
            public List<MsgTokenPO> doOperation(SqlSession sqlSession) {
                MsgTokenMapper mapper = sqlSession.getMapper(MsgTokenMapper.class);
                return mapper.queryMsgToken(tokenInclude, tokenExclude, dataCount, msgTypeId, status);
            }
        });

    }

    @Override
    public boolean deleteMsgToken(final Long msgTokenId, final Integer msgTypeId) {

        return doInvoke(new Operation<Boolean>() {
            @Override
            public Boolean doOperation(SqlSession sqlSession) {
                MsgTokenMapper mapper = sqlSession.getMapper(MsgTokenMapper.class);
                return mapper.deleteMsgToken(msgTokenId, msgTypeId) > 0;
            }
        });

    }

    @Override
    public boolean updateStatus(final Long msgTokenId, final Integer status, final Integer msgTypeId) {
        return doInvoke(new Operation<Boolean>() {
            @Override
            public Boolean doOperation(SqlSession sqlSession) {
                MsgTokenMapper mapper = sqlSession.getMapper(MsgTokenMapper.class);
                return mapper.updateStatus(msgTokenId, status, msgTypeId);
            }
        });
    }

    @Override
    public boolean batchUpdateStatus(final List<MsgTokenPO> msgTokens, final Integer status, final Integer msgTypeId) {
        return doInvoke(new Operation<Boolean>() {
            @Override
            public Boolean doOperation(SqlSession sqlSession) {
                MsgTokenMapper mapper = sqlSession.getMapper(MsgTokenMapper.class);
                return mapper.batchUpdateStatus(msgTokens, status, msgTypeId);
            }
        });
    }

    @Override
    public List<MsgTokenPO> getMsgToken(final Integer status, final String startTime, final String endTime, final Integer msgTypeId) {
        return doInvoke(new Operation<List<MsgTokenPO>>() {
            @Override
            public List<MsgTokenPO> doOperation(SqlSession sqlSession) {
                MsgTokenMapper mapper = sqlSession.getMapper(MsgTokenMapper.class);
                return mapper.queryMsgTokenBy(status, startTime, endTime, msgTypeId);
            }
        });
    }

    @Override
    public int deleteByMsgId(final Long msgId, final Integer msgTypeId) {
        return doInvoke(new Operation<Integer>() {
            @Override
            public Integer doOperation(SqlSession sqlSession) {
                MsgTokenMapper mapper = sqlSession.getMapper(MsgTokenMapper.class);
                return mapper.deleteByMsgId(msgId, msgTypeId);
            }
        });
    }

    @Override
    public int recoverDeathMsgToken(final Integer initialStatus, final Integer targetStatus, final Date deathTime, final Integer msgTypeId) {
        return doInvoke(new Operation<Integer>() {
            @Override
            public Integer doOperation(SqlSession sqlSession) {
                MsgTokenMapper mapper = sqlSession.getMapper(MsgTokenMapper.class);
                return mapper.recoverDeathMsgToken(initialStatus, targetStatus, deathTime, msgTypeId);
            }
        });
}

    @Override
    public int updateRetryStatus(final Long msgTokenId, final Date retryTime, final Integer retryCount, final Integer status, final Integer msgTypeId) {
        return doInvoke(new Operation<Integer>() {
            @Override
            public Integer doOperation(SqlSession sqlSession) {
                MsgTokenMapper mapper = sqlSession.getMapper(MsgTokenMapper.class);
                return mapper.updateRetryStatus(msgTokenId, retryTime, retryCount, status, msgTypeId);
            }
        });
    }

    @Override
    public int deleteExpiredMsgToken(final Integer tokenId, final Integer msgTypeId) {
        return doInvoke(new Operation<Integer>() {
            @Override
            public Integer doOperation(SqlSession sqlSession) {
                MsgTokenMapper mapper = sqlSession.getMapper(MsgTokenMapper.class);
                return mapper.deleteExpiredMsgToken(tokenId, msgTypeId);
            }
        });
    }
}
