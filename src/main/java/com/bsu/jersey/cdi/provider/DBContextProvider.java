package com.bsu.jersey.cdi.provider;

import com.bsu.jersey.jooq.HikariConnectionPool;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.ws.rs.Produces;

//import javax.enterprise.context.ApplicationScoped;
//import javax.enterprise.context.Dependent;
//import javax.enterprise.context.RequestScoped;
//import javax.enterprise.inject.Disposes;
//import javax.enterprise.inject.Produces;

/**
 * DSLContext对象提供者
 * Created by fengchong on 2016/12/21.
 */
//@ApplicationScoped
public class DBContextProvider {
//    private static HikariConnectionPool hcp;
//    static {
//        //初始化hikariCP
//        hcp = new HikariConnectionPool();
//    }

//    @Produces
//    @RequestScoped
//    @DSLContextConnection
//    public DSLContext getDSLContext() {
//        return DSL.using(hcp.getDataSource(), SQLDialect.MARIADB);
//    }


//    public void dispose(@Disposes @DSLContextConnection DSLContext dslContext) {
//        dslContext.close();
//        System.out.println("dslContext.close");
//    }

//    @Produces
//    @Dependent
//    @DSLContextConnectionDependent
//    public DSLContext getDSLContextDependent(){
//        return DSL.using(hcp.getDataSource(),SQLDialect.MARIADB);
//    }


}
