package com.bsu.jersey.jooq;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by fengchong on 2016/12/10.
 */
public class HikariConnectionPool {
    private HikariDataSource ds;
    public HikariConnectionPool(){
        Properties p = new Properties();
        ResourceBundle rbundle = ResourceBundle.getBundle("hikari");
        rbundle.keySet()
                .stream()
                .forEach(key-> p.setProperty(key,rbundle.getString(key)));
        HikariConfig config = new HikariConfig(p);
        config.setUsername("ad");
        config.setPassword("Audi001");
        config.setConnectionTimeout(30*1000);
        config.setIdleTimeout(60*1000);
        config.setMaxLifetime(60*1000);
        config.setMinimumIdle(1);
        config.addDataSourceProperty("cachePrepStmts","true");
        config.addDataSourceProperty("prepStmtCacheSize","1000");
        config.addDataSourceProperty("prepStmtCacheSqlLimit","2048");
        ds = new HikariDataSource(config);
    }

    public HikariDataSource getDataSource() {
        return ds;
    }

    public Connection getConnection() throws SQLException{
//        Connection c = ds.getConnection();
        return ds.getConnection();
    }

    public void shutdown(){ds.close();}

}
