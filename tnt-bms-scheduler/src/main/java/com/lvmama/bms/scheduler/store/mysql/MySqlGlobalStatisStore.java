package com.lvmama.bms.scheduler.store.mysql;

import com.lvmama.bms.scheduler.store.GlobalStatisStore;
import com.lvmama.bms.scheduler.store.domain.po.statis.GlobalStatisPO;
import com.lvmama.bms.scheduler.store.mysql.mapper.GlobalStatisMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Collection;

public class MySqlGlobalStatisStore extends AbstractMySqlStore implements GlobalStatisStore {


    public MySqlGlobalStatisStore(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public int save(final GlobalStatisPO globalStatis) {
        return doInvoke(new Operation<Integer>() {
            @Override
            public Integer doOperation(SqlSession sqlSession) {
                GlobalStatisMapper globalStatisMapper = sqlSession.getMapper(GlobalStatisMapper.class);
                return globalStatisMapper.save(globalStatis);
            }
        });
    }


}
