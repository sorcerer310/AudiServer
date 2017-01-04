package com.bsu.jersey.jooq;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;

/**
 * 为连接创建代理
 * Created by fengchong on 2016/12/26.
 */
public class CloseableJooq implements AutoCloseable {
    private Connection connection;
    public CloseableJooq(Connection c){
        connection = c;
    }

    public DSLContext delegate(){
        return DSL.using(connection, SQLDialect.MARIADB);
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
