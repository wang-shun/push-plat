package com.lvmama.bms.core.store.mysql;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

public class DBCPDataSourceFactory implements DataSourceFactory {

    private BasicDataSource dataSource;

    public DBCPDataSourceFactory() {
        dataSource = new BasicDataSource();
    }

    @Override
    public void setProperties(Properties props) {
        dataSource.setDriverClassName(props.getProperty("driver"));
        dataSource.setUrl(props.getProperty("url"));
        dataSource.setUsername(props.getProperty("username"));
        dataSource.setPassword(props.getProperty("password"));
        dataSource.setMaxTotal(Integer.parseInt(props.getProperty("maxTotal","100")));
        dataSource.setMaxIdle(Integer.parseInt(props.getProperty("maxIdle","100")));
        dataSource.setTestOnBorrow(Boolean.parseBoolean(props.getProperty("testOnBorrow","false")));
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
