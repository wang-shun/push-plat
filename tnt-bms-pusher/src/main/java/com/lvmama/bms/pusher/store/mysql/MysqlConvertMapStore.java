package com.lvmama.bms.pusher.store.mysql;

import com.lvmama.bms.pusher.store.ConvertMapStore;
import com.lvmama.bms.pusher.store.domain.ConvertMapPO;
import com.lvmama.bms.pusher.store.mysql.mapper.ConvertMapMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class MysqlConvertMapStore extends AbstractMySqlStore implements ConvertMapStore {

    public MysqlConvertMapStore(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public List<ConvertMapPO> getAllConvertMap() {
        return doInvoke(new Operation<List<ConvertMapPO>>() {
            @Override
            public List<ConvertMapPO> doOperation(SqlSession sqlSession) {
                ConvertMapMapper mapper = sqlSession.getMapper(ConvertMapMapper.class);
                return mapper.getAllConvertMap();
            }
        });
    }
}
