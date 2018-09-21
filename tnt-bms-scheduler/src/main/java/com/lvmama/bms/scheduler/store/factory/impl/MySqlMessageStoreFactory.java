package com.lvmama.bms.scheduler.store.factory.impl;

import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.scheduler.config.MsgSchedulerCfgLoader;
import com.lvmama.bms.scheduler.store.*;
import com.lvmama.bms.scheduler.store.factory.MessageStoreFactory;
import com.lvmama.bms.scheduler.store.mysql.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class MySqlMessageStoreFactory implements MessageStoreFactory {

    private static Logger logger = LoggerFactory.getLogger(MySqlMessageStoreFactory.class);

    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            Properties properties = MsgSchedulerCfgLoader.PROPERTIES;
            //properties.load(new InputStreamReader(MessageStoreFactory.class.getResourceAsStream("/MsgScheduler.cfg")));
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("com/lvmama/bms/scheduler/store/mysql/config/Configuration.xml"), properties);
        } catch (IOException e) {
            logger.error("MySqlSessionFactory init failed", e);
        }
    }


    @Override
    public MessageStore getMessageStore() {
        return new MySqlMessageStore(sqlSessionFactory);
    }

    @Override
    public MsgTokenStore getMsgTokenStore() {
        return new MySqlMsgTokenStore(sqlSessionFactory);
    }

    @Override
    public TokenStore getTokenStore() {
        return new MySqlTokenStore(sqlSessionFactory);
    }

    @Override
    public MsgTypeStore getMsgTypeStore() {
        return new MySqlMsgTypeStore(sqlSessionFactory);
    }

    @Override
    public GlobalStatisStore getGlobalStatisStore() {
        return new MySqlGlobalStatisStore(sqlSessionFactory);
    }

    @Override
    public TokenStatisStore getTokenStatisStore() {
        return new MySqlTokenStatisStore(sqlSessionFactory);
    }

    @Override
    public MsgTypeStatisStore getMsgTypeStatisStore() {
        return new MySqlMsgTypeStatisStore(sqlSessionFactory);
    }

    @Override
    public MsgTokenStatisStore getMsgTokenStatisStore() {
        return new MysqlMsgTokenStatisStore(sqlSessionFactory);
    }

    @Override
    public MsgPushFailStore getMsgPushFailStore() {
        return new MySqlMsgPushFailStore(sqlSessionFactory);
    }

}
