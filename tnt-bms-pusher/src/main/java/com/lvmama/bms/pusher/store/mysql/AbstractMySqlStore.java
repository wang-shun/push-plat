package com.lvmama.bms.pusher.store.mysql;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class AbstractMySqlStore {

    private SqlSessionFactory sqlSessionFactory;

    protected AbstractMySqlStore(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public interface Operation<T> {
        T doOperation(SqlSession sqlSession);
    }

    protected <T> T doInvoke(Operation<T> operation, boolean... autoCommit) {
        SqlSession sqlSession = sqlSessionFactory.openSession(autoCommit.length <= 0 || autoCommit[0]);
        try {
            return operation.doOperation(sqlSession);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            if(sqlSession!=null) {
                sqlSession.close();
            }
        }
    }

}
