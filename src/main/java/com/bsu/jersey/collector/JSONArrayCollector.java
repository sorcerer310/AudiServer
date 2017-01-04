package com.bsu.jersey.collector;

import org.json.JSONArray;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * json数据的收集器，用来向JSONArray中收集数据
 * Created by fengchong on 2016/11/24.
 */
public class JSONArrayCollector<JSONObject> implements Collector<JSONObject,JSONArray,JSONArray>{
    @Override
    public Supplier<JSONArray> supplier() {
        //返回初始化容器
        return ()->new JSONArray();
    }

    @Override
    public BiConsumer<JSONArray, JSONObject> accumulator() {
        //填充容器操作
        return (ja_collector,value)->{
            ja_collector.put(value);
        };
    }

    @Override
    public BinaryOperator<JSONArray> combiner() {
        //并行使用，多个线程中的容器进行合并
        return null;
//        return (ja1,ja2)->{
//            ja2.forEach(jo->ja1.put(jo));
//            return ja1;
//        };
    }

    @Override
    public Function<JSONArray, JSONArray> finisher() {
        //最后返回容器对象
        return collector->collector;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return new HashSet();
    }

}
