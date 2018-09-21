package com.lvmama.bms.scheduler.store.mysql;

import com.lvmama.bms.scheduler.store.TokenStatisStore;
import com.lvmama.bms.scheduler.store.domain.po.statis.TokenStatisPO;
import com.lvmama.bms.scheduler.store.mysql.mapper.TokenStatisMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Collection;

public class MySqlTokenStatisStore extends AbstractMySqlStore implements TokenStatisStore {

    public MySqlTokenStatisStore(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public int batchSave(final Collection<TokenStatisPO> tokenStatis) {
        return doInvoke(new Operation<Integer>() {
            @Override
            public Integer doOperation(SqlSession sqlSession) {
                TokenStatisMapper tokenStatisMapper = sqlSession.getMapper(TokenStatisMapper.class);
                return tokenStatisMapper.batchSave(tokenStatis);
            }
        });
    }
}
