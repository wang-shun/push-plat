package com.lvmama.bms.scheduler.store.mysql;

import com.lvmama.bms.scheduler.store.TokenStore;
import com.lvmama.bms.scheduler.store.domain.po.TokenPO;
import com.lvmama.bms.scheduler.store.mysql.mapper.TokenMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class MySqlTokenStore extends AbstractMySqlStore implements TokenStore {

    public MySqlTokenStore(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public List<TokenPO> queryAllMsgToken() {

        return doInvoke(new Operation<List<TokenPO>>() {
            @Override
            public List<TokenPO> doOperation(SqlSession sqlSession) {
                TokenMapper mapper = sqlSession.getMapper(TokenMapper.class);
                return mapper.queryAllMsgToken();
            }
        });

    }
}
