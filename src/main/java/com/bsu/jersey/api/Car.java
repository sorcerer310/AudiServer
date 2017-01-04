package com.bsu.jersey.api;

import com.bsu.jersey.cdi.provider.DSLContextConnection;
import com.bsu.jersey.collector.JSONArrayCollector;
import com.bsu.jersey.jooq.CloseableJooq;
import com.bsu.jersey.jooq.JooqMain;
import com.bsu.jersey.tools.JSONMsg;

import static com.bsu.jersey.jooq.generated.Tables.*;

import org.jooq.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;

/**
 * Created by fengchong on 2016/12/6.
 */
@Path("/car")
public class Car {
//    @Inject
//    @DSLContextConnection
//    private DSLContext dsl;

    //所有车型数据
    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCarByCity() {
        try (DSLContext dsl = JooqMain.getDSLContext()) {
//        try(CloseableJooq cj = JooqMain.getDSLConnection()){
//            DSLContext dsl = cj.delegate();

            return JSONMsg.info(1000, dsl.selectFrom(B_CAR_TYPE)
                    .fetch()
                    .stream()
                    .map(bct -> {
                        JSONObject jo = new JSONObject();
                        jo.put("brand", bct.getBrand());
                        jo.put("brand_type", bct.getBrandType());
                        jo.put("car_models", bct.getCarModels());
                        jo.put("product_name", bct.getProductName());
                        return jo;
                    })
                    .collect(new JSONArrayCollector<>()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //所有的品牌
    @Path("/brand")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getBrand() {
        try (DSLContext dsl = JooqMain.getDSLContext()) {
//        try(CloseableJooq cj = JooqMain.getDSLConnection()){
//            DSLContext dsl = cj.delegate();

            return JSONMsg.info(1000, dsl.selectDistinct(B_CAR_TYPE.BRAND)
                    .from(B_CAR_TYPE)
                    .fetch()
                    .stream()
                    .map(bct -> bct.getValue(B_CAR_TYPE.BRAND))
                    .collect(new JSONArrayCollector<>()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //所有车系
    @Path("/brand/car_models")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCarModels(@QueryParam("brand") String brand) {
        try (DSLContext dsl = JooqMain.getDSLContext()) {
//        try(CloseableJooq cj = JooqMain.getDSLConnection()){
//            DSLContext dsl = cj.delegate();

            //获得所有品牌类型
            Result<Record1<String>> r_brand_type = dsl.selectDistinct(B_CAR_TYPE.BRAND_TYPE)
                    .from(B_CAR_TYPE)
                    .where(B_CAR_TYPE.BRAND.eq(brand))
                    .fetch();

            //获得所有品牌的所有车系
            Result<Record2<String, String>> r_car_models = dsl.selectDistinct(B_CAR_TYPE.BRAND_TYPE, B_CAR_TYPE.CAR_MODELS)
                    .from(B_CAR_TYPE)
                    .where(B_CAR_TYPE.BRAND.eq(brand))
                    .fetch();

            //将车型数据组装到品牌数据中
            JSONArray ja_brand_type = new JSONArray();
            for (Record1<String> r : r_brand_type) {
                JSONArray ja_car_models = r_car_models.stream()
                        .filter(car_models -> car_models.getValue(B_CAR_TYPE.BRAND_TYPE).equals(r.getValue(B_CAR_TYPE.BRAND_TYPE)))
                        .map(car_models -> car_models.getValue(B_CAR_TYPE.CAR_MODELS))
                        .collect(new JSONArrayCollector<>());

                JSONObject jo_brand_type = new JSONObject();
                jo_brand_type.put(r.getValue(B_CAR_TYPE.BRAND_TYPE), ja_car_models);

                ja_brand_type.put(jo_brand_type);
            }
            ja_brand_type = orderBrandType(brand, ja_brand_type);
            return JSONMsg.info(1000, ja_brand_type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //通过车系大类获得该系车型的小类
    @Path("/brand/car_models/product_name")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getProductName(@QueryParam("car_models") String car_models,
                                 @QueryParam("year") String year) {
        if (year == null) year = "%";
        try (DSLContext dsl = JooqMain.getDSLContext()) {
//        try(CloseableJooq cj = JooqMain.getDSLConnection()){
//            DSLContext dsl = cj.delegate();

            //根据车系获得该车系所有车的型号
            return JSONMsg.info(1000, dsl.select(B_CAR_TYPE.ID, B_CAR_TYPE.PRODUCT_NAME)
                    .from(B_CAR_TYPE)
                    .where(B_CAR_TYPE.CAR_MODELS.eq(car_models)
                            .and(B_CAR_TYPE.YEAR.like(year)))
                    .orderBy(B_CAR_TYPE.PRODUCT_NAME.desc())
                    .fetch()
                    .stream()
                    .map(bct -> {
                        JSONObject jo = new JSONObject();
                        jo.put("id", bct.getValue(B_CAR_TYPE.ID));
                        jo.put("product_name", bct.getValue(B_CAR_TYPE.PRODUCT_NAME));
                        return jo;
                    })
                    .collect(new JSONArrayCollector<>()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private String[] sortKey = new String[]{"一汽大众奥迪", "奥迪(进口)"};

    /**
     * 对车的品牌类型进行排序
     *
     * @param brand 品牌名称
     * @param ja    组装好的车品牌类型对应的数据
     * @return 返回排序好的数据
     */
    private JSONArray orderBrandType(String brand, JSONArray ja) {
        JSONArray ja_new = new JSONArray();
        try (DSLContext dsl = JooqMain.getDSLContext()) {
//        try(CloseableJooq cj = JooqMain.getDSLConnection()){
//            DSLContext dsl = cj.delegate();

            if (brand.equals("奥迪")) {
                for (String sk : sortKey) {
                    for (int i = 0; i < ja.length(); i++) {
                        HashMap<String, JSONArray> hm = (HashMap<String, JSONArray>) ja.toList().get(i);
                        String key = hm.keySet().toArray()[0].toString();
                        if (key.equals(sk)) {
                            JSONObject jo_new = new JSONObject();
                            jo_new.put(sk, hm.get(sk));
                            ja_new.put(jo_new);
                        }
                    }
                }
            }
            return ja_new;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
