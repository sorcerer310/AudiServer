package com.bsu.jersey.jooq;

import com.bsu.jersey.collector.JSONArrayCollector;
import com.bsu.jersey.tools.JacksonJsonTool;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import com.bsu.jersey.jooq.generated.tables.*;
import org.jooq.types.UInteger;
import org.json.JSONArray;
import org.json.JSONObject;


import static com.bsu.jersey.jooq.generated.Tables.B_CAR_DEALER;
import static com.bsu.jersey.jooq.generated.Tables.B_CAR_TYPE;
import static com.bsu.jersey.jooq.generated.Tables.B_SERVICE_CAR_MODELS;


/**
 * Created by fengchong on 2016/12/10.
 */
public class JooqTest {
    public static void main(String[] args){
        String url = "jdbc:mysql://101.200.88.50:3306?autoReconnect=true";
        String username = "ad";
        String password = "Audi001";

        try(Connection conn = DriverManager.getConnection(url,username,password)){
            DSLContext dsl = DSL.using(conn, SQLDialect.MARIADB);

//            dsl.insertInto(B_SERVICE_CAR_MODELS,B_SERVICE_CAR_MODELS.SERVICE_CAR_MODELS)
//                    .values("测试车型")
//                    .execute();

            dsl.delete(B_SERVICE_CAR_MODELS)
                    .where(B_SERVICE_CAR_MODELS.ID.equal(UInteger.valueOf(11)))
                    .execute();




//            1	A1车型 1.4 TFSI发动机 S tronic 7速双离合变速器
//            2	A3车型 1.4 TFSI发动机 S tronic 7速双离合变速器
//            3	A3车型 1.8 TFSI发动机 S tronic 7速双离合变速器
//            4	A4L车型 1.8 TFSI发动机 手动变速器
//            5	A3车型 2.0TFSI发动机 S tronic 7速双离合变速器
//            6	A4L车型 1.8 TFSI发动机 multitronic 无级/手动一体式变速器
//            7	A4L车型2.0 TFSI发动机 multitronic 无级/手动一体式变速器
//            8	A4L车型2.0 TFSI发动机S tronic® 7速双离合变速器
//            9	A4L车型3.0 TFSI发动机 S tronic® 7速双离合变速器




//            Result<Record2<String,String>> result = dsl.selectDistinct(B_CAR_TYPE.CAR_MODELS,B_CAR_TYPE.BRAND_TYPE)
//                    .from(B_CAR_TYPE)
//                    .fetch();
//
//            System.out.println(result);

//            JSONArray ja = dsl.select().from(B_CAR_DEALER)
//                    .fetch()
//                    .stream()
//                    .map(record->{
//                        JSONObject jo = new JSONObject();
//                        jo.put("no",record.getValue(B_CAR_DEALER.NO));
//                        jo.put("name",record.getValue(B_CAR_DEALER.NAME));
//                        jo.put("address",record.getValue(B_CAR_DEALER.ADDRESS));
//
////                        JSONObject jo = new JSONObject();
////                        try {
////                              jo = new JSONObject(JacksonJsonTool.quickBean2JSONStringConvert(record));
////                        } catch (IOException e) {
////                            throw new RuntimeException(e);
////                        }
//                        return jo;
//                    })
//                    .collect(new JSONArrayCollector<>());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
