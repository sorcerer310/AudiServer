package com.bsu.jersey.jooq;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.SQLException;

/**
 * Created by fengchong on 2016/12/24.
 */
public class JooqMain {
    private static HikariConnectionPool hcp;
    static{
        hcp = new HikariConnectionPool();
    }

//    public static CloseableJooq getDSLConnection() throws SQLException {
//        return new CloseableJooq(hcp.getConnection());

//    }
    public static DSLContext getDSLContext() throws SQLException {
//        DSLContext dsl = DSL.using(hcp.getConnection(), SQLDialect.MARIADB);
        DSLContext dsl = DSL.using(hcp.getDataSource(),SQLDialect.MARIADB);
        return dsl;
    }
}
