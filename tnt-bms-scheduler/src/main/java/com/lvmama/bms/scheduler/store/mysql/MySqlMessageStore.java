package com.lvmama.bms.scheduler.store.mysql;

import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.core.domain.enums.MsgStatus;
import com.lvmama.bms.scheduler.store.MessageStore;
import com.lvmama.bms.scheduler.store.domain.po.MessagePO;
import com.lvmama.bms.scheduler.store.exception.DupEntryException;
import com.lvmama.bms.scheduler.store.mysql.mapper.MessageMapper;
import com.lvmama.bms.scheduler.store.mysql.mapper.MsgTokenMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Map;
import java.util.Set;

public class MySqlMessageStore extends AbstractMySqlStore implements MessageStore {

    public MySqlMessageStore(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public String replaceOnExist(final MessagePO messagePO) {
         return doInvoke(new Operation<String>() {
            @Override
            public String doOperation(SqlSession sqlSession) {
                try {
                    StringBuffer buffer = new StringBuffer();
                    MessageMapper mapper = sqlSession.getMapper(MessageMapper.class);
                    Long innerMsgId  =  mapper.updateStatus(messagePO.getMsgId(), MsgStatus.RECEIVE.ordinal(),
                            MsgStatus.INVALID.ordinal(), messagePO.getMsgTypeId());
                    buffer.append("tip=replace exist message|innerMsgId=").append(innerMsgId).append(messagePO.toString());
                    if(innerMsgId != null) {
                        MsgTokenMapper msgTokenMapper = sqlSession.getMapper(MsgTokenMapper.class);
                        int count = msgTokenMapper.deleteByMsgId(innerMsgId, messagePO.getMsgTypeId());
                        buffer.append("delete token by replace count: " + count);
                    }
                    sqlSession.commit();
                    return buffer.toString();
                } catch (Exception e) {
                    if(StringUtils.isNotEmpty(e.getMessage())
                            && e.getMessage().contains("Duplicate entry")) {
                        throw new DupEntryException(e);
                    } else {
                        throw e;
                    }
                }
            }
        }, false);
    }

    @Override
    public boolean saveMessageAndToken(final MessagePO messagePO, final Integer[] tokenIds) {
        return doInvoke(new Operation<Boolean>() {
            @Override
            public Boolean doOperation(SqlSession sqlSession) {
                try {
                    MessageMapper messageMapper = sqlSession.getMapper(MessageMapper.class);
                    MsgTokenMapper msgTokenMapper = sqlSession.getMapper(MsgTokenMapper.class);
                    int result = messageMapper.saveMessage(messagePO) +
                            msgTokenMapper.saveMsgToken(messagePO.getId(), tokenIds, messagePO.getMsgTypeId());
                    sqlSession.commit();
                    return result == 2;
                } catch (Exception e) {
                    if(StringUtils.isNotEmpty(e.getMessage())
                            && e.getMessage().contains("Duplicate entry")) {
                        throw new DupEntryException(e);
                    } else {
                        throw e;
                    }
                }
            }
        }, false);
    }

    @Override
    public boolean saveMessage(final MessagePO message) {

        return doInvoke(new Operation<Boolean>() {
            @Override
            public Boolean doOperation(SqlSession sqlSession) {
                try {
                    MessageMapper mapper = sqlSession.getMapper(MessageMapper.class);
                    return mapper.saveMessage(message) == 1;
                } catch (Exception e) {
                    if(StringUtils.isNotEmpty(e.getMessage())
                            && e.getMessage().contains("Duplicate entry")) {
                        throw new DupEntryException(e);
                    } else {
                        throw e;
                    }
                }
            }
        });

    }

    @Override
    public String updateMessage(final MessagePO messagePo) {

        return doInvoke(new Operation<String>() {
            @Override
            public String doOperation(SqlSession sqlSession) {
                MessageMapper mapper = sqlSession.getMapper(MessageMapper.class);
                return mapper.updateMessage(messagePo);
            }
        });

    }

    @Override
    public boolean saveMsgWithCheck(final MessagePO message) {

        return doInvoke(new Operation<Boolean>() {
            @Override
            public Boolean doOperation(SqlSession sqlSession) {
                MessageMapper mapper = sqlSession.getMapper(MessageMapper.class);
                return  mapper.saveMsgWithCheck(message) == 1;
            }
        });
    }

    @Override
    public Map<String, MessagePO> getMessage(final Set<Long> msgIds, final Integer msgTypeId) {

        return doInvoke(new Operation<Map<String, MessagePO>>() {
            @Override
            public Map<String, MessagePO> doOperation(SqlSession sqlSession) {
                MessageMapper mapper = sqlSession.getMapper(MessageMapper.class);
                return mapper.getMessage(msgIds, msgTypeId);
            }
        });

    }


    @Override
    public Long updateStatus(final String msgId, final Integer initialStatus, final Integer targetStatus, final Integer msgTypeId) {
        return doInvoke(new Operation<Long>() {
            @Override
            public Long doOperation(SqlSession sqlSession) {
                MessageMapper mapper = sqlSession.getMapper(MessageMapper.class);
                return mapper.updateStatus(msgId, initialStatus, targetStatus, msgTypeId);
            }
        });
    }

    @Override
    public int batchUpdateStatus(final Set<Long> msgIds, final Integer initialStatus,
                               final Integer targetStatus, final Integer msgTypeId) {
        return doInvoke(new Operation<Integer>() {
            @Override
            public Integer doOperation(SqlSession sqlSession) {
                MessageMapper mapper = sqlSession.getMapper(MessageMapper.class);
                return mapper.batchUpdateStatus(msgIds, initialStatus, targetStatus, msgTypeId);
            }
        });
    }

    @Override
    public int deleteExpiredMsg(final Integer msgTypeId) {
        return doInvoke(new Operation<Integer>() {
            @Override
            public Integer doOperation(SqlSession sqlSession) {
                MessageMapper mapper = sqlSession.getMapper(MessageMapper.class);
                return mapper.deleteExpiredMsg(msgTypeId);
            }
        });
    }

}
