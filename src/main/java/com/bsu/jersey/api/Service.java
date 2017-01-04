package com.bsu.jersey.api;

import com.bsu.jersey.cdi.provider.DSLContextConnection;
import com.bsu.jersey.collector.JSONArrayCollector;
import com.bsu.jersey.jooq.CloseableJooq;
import com.bsu.jersey.jooq.HikariConnectionPool;
import com.bsu.jersey.jooq.JooqMain;
import com.bsu.jersey.jooq.generated.enums.BCarServiceServiceItem;
import com.bsu.jersey.tools.JSONMsg;

import static com.bsu.jersey.jooq.generated.Tables.*;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Result;
import org.jooq.types.UInteger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * 保养服务资源
 * Created by fengchong on 2016/12/12.
 */
@Path("/service")
public class Service {
//    @Inject
//    @DSLContextConnection
//    private DSLContext dsl;

    //通过车型与来判断当前需要做的保养服务
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getService(@QueryParam("car_type_id") int car_type_id,
                             @QueryParam("kilometer") int kilometer) {
        int rkm = round28(kilometer);
//            int rkm = Math.round((float) kilometer / 10000) * 10000;                                                         //四舍五入后的里程数
        try(DSLContext dsl = JooqMain.getDSLContext()) {
//        try(CloseableJooq cj = JooqMain.getDSLConnection()){
//            DSLContext dsl = cj.delegate();

            boolean isp = isShowServicePrice(dsl);
//            Stream<Record5<UInteger,BCarServiceServiceItem,UInteger,UInteger,Double>> s = dsl.select(B_CAR_TYPE.SERVICE_CAR_MODELS_ID, B_CAR_SERVICE.SERVICE_ITEM, B_CAR_SERVICE.START_KILOMETER
            Result<Record5<UInteger, BCarServiceServiceItem, UInteger, UInteger, Double>> result = dsl.select(B_CAR_TYPE.SERVICE_CAR_MODELS_ID, B_CAR_SERVICE.SERVICE_ITEM, B_CAR_SERVICE.START_KILOMETER
                    , B_CAR_SERVICE.INTERVAL_KILOMETER, B_CAR_SERVICE.PRICE)
                    .from(B_CAR_TYPE).innerJoin(B_CAR_SERVICE)
                    .on(B_CAR_TYPE.SERVICE_CAR_MODELS_ID.eq(B_CAR_SERVICE.SERVICE_CAR_MODELS_ID))
                    .where(B_CAR_TYPE.ID.eq(UInteger.valueOf(car_type_id)))
                    .fetch();


            JSONArray ja_service_item = result.stream()
                    .filter(record -> compute_km(record, 0, rkm))
                    .map(record -> {
                        JSONObject jo_service_item = new JSONObject();
                        jo_service_item.put("service_item", record.getValue(B_CAR_SERVICE.SERVICE_ITEM));
                        if (isp) {
                            jo_service_item.put("price", record.getValue(B_CAR_SERVICE.PRICE));
                            jo_service_item.put("discount_price", Math.round(record.getValue(B_CAR_SERVICE.PRICE) * 0.8));
                        }
                        return jo_service_item;
                    })
                    .collect(new JSONArrayCollector<>());

            double sumPrice = result.stream()
                    .filter(record -> compute_km(record, 0, rkm))
                    .map(record -> record.getValue(B_CAR_SERVICE.PRICE))
                    .reduce(.0, (sum, item) -> sum + item);
            double distanceSumPrice = result.stream()
                    .filter(record -> compute_km(record, 0, rkm))
                    .map(record -> Math.round(record.getValue(B_CAR_SERVICE.PRICE) * 0.8))
                    .reduce(0l, (sum, item) -> sum + item);

            JSONObject jo_service = new JSONObject();
            jo_service.put("service_kilometer", rkm);
            jo_service.put("sum_price", sumPrice);
            jo_service.put("distance_sum_price", distanceSumPrice);
            jo_service.put("service_items", ja_service_item);

            return JSONMsg.info(1000, jo_service);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getService(@QueryParam("car_type_id") int car_type_id) {
        JSONArray ja_list = new JSONArray();
        try(DSLContext dsl = JooqMain.getDSLContext()) {
//        try(CloseableJooq cj = JooqMain.getDSLConnection()){
//            DSLContext dsl = cj.delegate();

            Result<Record5<UInteger, BCarServiceServiceItem, UInteger, UInteger, Double>> result = dsl.select(B_CAR_TYPE.SERVICE_CAR_MODELS_ID, B_CAR_SERVICE.SERVICE_ITEM, B_CAR_SERVICE.START_KILOMETER
                    , B_CAR_SERVICE.INTERVAL_KILOMETER, B_CAR_SERVICE.PRICE)
                    .from(B_CAR_TYPE).innerJoin(B_CAR_SERVICE)
                    .on(B_CAR_TYPE.SERVICE_CAR_MODELS_ID.eq(B_CAR_SERVICE.SERVICE_CAR_MODELS_ID))
                    .where(B_CAR_TYPE.ID.eq(UInteger.valueOf(car_type_id)))
                    .fetch();

            for (int i = 10000; i <= 200000; i += 10000) {
                int rkm = i;
                double sumPrice = result.stream()
                        .filter(record -> compute_km(record, 0, rkm))
                        .map(record -> record.getValue(B_CAR_SERVICE.PRICE))
                        .reduce(.0, (sum, item) -> sum + item);
                JSONObject jo_sumPrice = new JSONObject();
                jo_sumPrice.put("kilometer",rkm);
                jo_sumPrice.put("sum_price",sumPrice);
                ja_list.put(jo_sumPrice);
            }
            return JSONMsg.info(1000,ja_list);
        }catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * @param curr_km   当前计算的里程数
     * @param target_km 目标里程数
     * @return
     */
    private boolean compute_km(Record5<UInteger, BCarServiceServiceItem, UInteger, UInteger, Double> record, int curr_km, int target_km) {
        //如果开始里程或者间隔里程为0，表示该车型没有当前项目，数据不符合条件返回false
        if (record.getValue(B_CAR_SERVICE.START_KILOMETER).intValue() == 0
                || record.getValue(B_CAR_SERVICE.INTERVAL_KILOMETER).intValue() == 0)
            return false;

        //如果第一次计算设置为开始的公里数
        if (curr_km == 0)
            curr_km = record.getValue(B_CAR_SERVICE.START_KILOMETER).intValue() * 10000;
        else
            curr_km += record.getValue(B_CAR_SERVICE.INTERVAL_KILOMETER).intValue() * 10000;

        //判断如果当前公里数小于目标公里数,进行递归下一次计算
        if (curr_km < target_km) {
            return compute_km(record, curr_km, target_km);
        }
        //如果当前公里数与目标公里数相等,返回true表示当前数据符合要求
        else if (curr_km == target_km) {
            return true;
        }
        //如果当前计算公里数已经超过目标公里数，说明没有数据符合当前公里数，返回false过滤掉该条数据
        else {
            return false;
        }
    }

    /**
     * 2舍8入的计算函数
     *
     * @param kilometer 实际里程
     * @return 返回的整数里程
     */
    private int round28(int kilometer) {
        int million = kilometer / 10000;                                                                                  //万位
        int remainder = kilometer % 10000;                                                                                //万位以下的余数
        million = remainder >= 2000 ? ++million : million;                                                                    //2舍8进
        return million * 10000;
    }

    /**
     * 返回是否显示服务价格
     *
     * @param dsl 查询数据的dsl对象
     * @return 返回是否显示服务价格
     */
    private boolean isShowServicePrice(DSLContext dsl) {
        Record1<Boolean> show_service_price = dsl.select(B_SYSTEM_PARAM.SHOW_SERVICE_PRICE)
                .from(B_SYSTEM_PARAM)
                .fetchOne();
        return show_service_price.getValue(B_SYSTEM_PARAM.SHOW_SERVICE_PRICE);
    }
}
