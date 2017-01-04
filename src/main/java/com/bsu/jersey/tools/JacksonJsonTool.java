package com.bsu.jersey.tools;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * jackson的json工具，用于快速转换对象
 * Created by fengchong on 2016/11/4.
 */
public class JacksonJsonTool {
    /**
     * 快速转换Bean对象转为JSON形式的String
     * @param o                 要转换对象
     * @return
     * @throws IOException
     */
    public static String quickBean2JSONStringConvert(Object o) throws IOException {
        ObjectMapper map = makeObjectMapper();
        String json_string = map.writeValueAsString(o);
        return json_string;
    }

    /**
     * 将JSON形式的String快速转换为Bean对象
     * @param s
     * @return
     * @throws IOException
     */
    public static <T> T quickJSONString2BeanConvert(String s,T t) throws IOException {
        ObjectMapper map = makeObjectMapper();
        T retval = (T) map.readValue(s,t.getClass());
        return retval;
    }

    /**
     * 创建一个ObjectMapper对象
     * @return
     */
    private static ObjectMapper makeObjectMapper(){
        ObjectMapper map  = new ObjectMapper();
        map.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return map;
    }
}
