package com.bsu.jersey.api;

import com.bsu.jersey.cdi.provider.DSLContextConnection;
import com.bsu.jersey.collector.JSONArrayCollector;

import com.bsu.jersey.jooq.CloseableJooq;
import com.bsu.jersey.jooq.JooqMain;
import com.bsu.jersey.tools.JSONMsg;
import com.bsu.jersey.tools.U;
import org.jooq.DSLContext;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.bsu.jersey.jooq.generated.tables.BCarDealer.B_CAR_DEALER;

/**
 * Created by fengchong on 2016/12/5.
 */
@Path("/shop")
public class Shop {
//    @Inject
//    @DSLContextConnection
//    private DSLContext dsl;
//    @Inject
//    private DSLContext dsl;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getShop(@QueryParam("city_name") String city_name) {
        try (DSLContext dsl = JooqMain.getDSLContext()) {
            JSONArray ja_shop = dsl.selectFrom(B_CAR_DEALER)
                    .where(B_CAR_DEALER.CITY.like(city_name + "%"))
                    .fetch()
                    .stream()
                    .map(bcde -> {
                        JSONObject jo = new JSONObject();
                        jo.put("name", bcde.getName());
                        jo.put("address", bcde.getAddress());
                        return jo;
                    })
                    .collect(new JSONArrayCollector<>());
            return JSONMsg.info(1000, ja_shop);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
