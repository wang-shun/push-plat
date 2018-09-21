package com.lvmama.bms.pusher.store.factory.impl;

import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.pusher.config.MsgPusherLoader;
import com.lvmama.bms.pusher.store.ConvertMapStore;
import com.lvmama.bms.pusher.store.MsgPusherStore;
import com.lvmama.bms.pusher.store.domain.MsgPusherPO;
import com.lvmama.bms.pusher.store.factory.MessageStoreFactory;
import com.lvmama.bms.pusher.store.mysql.MysqlConvertMapStore;
import com.lvmama.bms.pusher.store.mysql.MysqlMsgPusherStore;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class MySqlMessageStoreFactory implements MessageStoreFactory {

    private static Logger logger = LoggerFactory.getLogger(MySqlMessageStoreFactory.class);

    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            Properties properties = MsgPusherLoader.PROPERTIES;
            //properties.load(new InputStreamReader(MessageStoreFactory.class.getResourceAsStream("/MsgPusher.cfg")));
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("com/lvmama/bms/pusher/store/mysql/config/Configuration.xml"), properties);
        } catch (IOException e) {
            logger.error("MySqlSessionFactory init failed", e);
        }
    }

    @Override
    public MsgPusherStore getMsgPushStore() {
        return new MysqlMsgPusherStore(sqlSessionFactory);
    }

    @Override
    public ConvertMapStore getConvertMapperStore() {
        return new MysqlConvertMapStore(sqlSessionFactory);
    }

    /*public static void main(String[] args) {
        try {
            MySqlMessageStoreFactory factory = new MySqlMessageStoreFactory();
            MsgPusherStore msgPusherStore = factory.getMsgPushStore();
            MsgPusherPO msgPusher = new MsgPusherPO();
            msgPusher.setName("rpcPusher");
            msgPusher.setJar(FileUtils.readFileToByteArray(new File("E:\\work\\idea\\qjialong\\tnt-bms\\tnt-bms-pusher\\target\\lib\\ext\\ctrip.jar")));
            msgPusherStore.saveMsgPusher(msgPusher);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

}
