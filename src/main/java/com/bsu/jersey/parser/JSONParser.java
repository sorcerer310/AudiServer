package com.bsu.jersey.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * 解吸数据库返回的CachedrowSetImpl对象数据返回json格式数据
 *
 * @author fengchong
 */
public class JSONParser {
    protected static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 将key数据与value数据解析成只有一个字段的简单json数据
     *
     * @param k key
     * @param v value
     * @return
     * @throws JSONException
     */
    public static String parseSimpleJson(String k, String v) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(k, v);
        return jo.toString();
    }

    /**
     * 将key数据与value数据解析成只有一个字段的简单json数据
     *
     * @param k
     * @param v
     * @return
     * @throws JSONException
     */
    public static String parseSimpleJson(String k, int v) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(k, v);
        return jo.toString();
    }

    /**
     * 合并两个json数组的数据,一般用于数据结构相同的两个json数组.
     *
     * @param ja1 第一个json数组
     * @param ja2 第二个json数组
     * @throws JSONException
     * @return 返回合并后的json数组
     */
    public static JSONArray merge2JSONArray(JSONArray ja1, JSONArray ja2) throws JSONException {
        for (int i = 0; i < ja2.length(); i++)
            ja1.put(ja2.getJSONObject(i));
        return ja1;
    }

    /**
     * 将Timestamp类型数据转化为日期时间格式字符串类型
     *
     * @param ts
     * @return
     * @throws SQLException
     */
    protected static String dateTime2Str(Timestamp ts) throws SQLException {
        String value = "";
        if (ts != null) {
            value = dateTimeFormat.format(ts);
        }
        return value;
    }

    /**
     * 将Timestamp类型数据转化为日期格式字符串类型
     *
     * @param ts
     * @return
     * @throws SQLException
     */
    protected static String date2Str(Timestamp ts) throws SQLException {
        String value = "";
        if (ts != null)
            value = dateFormat.format(ts);
        return value;
    }


    /**
     * 格式化json数据
     * @param jsonStr   原始json数据
     * @return          返回的格式化的json数据
     */
    public static String formatJSON(String jsonStr) {
        int level = 0;
        StringBuffer jsonForMatStr = new StringBuffer();
        for (int i = 0; i < jsonStr.length(); i++) {
            char c = jsonStr.charAt(i);
            if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
            }
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c + "\n");
                    level++;
                    break;
                case ',':
//                    jsonForMatStr.append(c + "\n");
                    jsonForMatStr.append(c);
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }
        return jsonForMatStr.toString();
    }

    private static String getLevelStr(int level){
        StringBuffer levelStr = new StringBuffer();
        for(int levelI = 0;levelI<level ; levelI++){
            levelStr.append("\t");
        }
        return levelStr.toString();
    }
}