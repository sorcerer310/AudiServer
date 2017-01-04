package com.bsu.jersey.api;

import com.bsu.jersey.cdi.provider.DSLContextConnection;
import com.bsu.jersey.collector.JSONArrayCollector;
import com.bsu.jersey.jooq.CloseableJooq;
import com.bsu.jersey.jooq.JooqMain;
import com.bsu.jersey.tools.JSONMsg;

import static com.bsu.jersey.jooq.generated.Tables.*;

import org.jooq.DSLContext;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by fengchong on 2016/12/3.
 */
@Path("/city")
public class City {
//    @Inject
//    @DSLContextConnection
//    DSLContext dsl;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCity() {
        try (DSLContext dsl = JooqMain.getDSLContext()) {
//        try(CloseableJooq cj = JooqMain.getDSLConnection()){
//            DSLContext dsl = cj.delegate();

            return JSONMsg.info(1000, dsl.selectDistinct(B_CAR_DEALER.CITY)
                    .from(B_CAR_DEALER)
                    .fetch()
                    .stream()
//                    .map(record->record.getValue(B_CAR_DEALER.CITY).split("/")[0])
                    .map(record -> {
                        String[] citys = record.getValue(B_CAR_DEALER.CITY).split("/");
                        return citys[1] + "/" + citys[0];
                    })
                    .collect(new JSONArrayCollector<>()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
