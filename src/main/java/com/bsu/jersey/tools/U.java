package com.bsu.jersey.tools;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Entity;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * 工具类,用来实现一些快捷的操作方法
 *
 * @author fengchong
 */
public class U {

    /**
     * p:print
     * 向页面输出数据
     *
     * @param response response对象
     * @param s        要输出的文字
     * @throws IOException
     */
    public static void p(HttpServletResponse response, String s) {
        try {
            response.getWriter().print(s);
        } catch (IOException e) {
            U.el(U.class.getName(), e);
        }
    }

    /**
     * rl:remove last
     * 移除字符串中最后一个字符并返回对应的字符串
     *
     * @param sb 要处理的字符
     * @return
     */
    public static String rl(StringBuffer sb) {
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 移除字符串中指定长度字符
     *
     * @param s   要处理的字符串
     * @param len 要移除的长度
     * @return 返回处理后的字符串
     */
    public static String rl(String s, int len) {
        return s.substring(0, s.length() - len);
    }

    /**
     * pp:print param
     * 打印出所有传入的参数
     *
     * @return
     */
    public static String pp(HttpServletRequest request) {
        Enumeration<String> e = request.getParameterNames();
        StringBuffer sb = new StringBuffer();
        try {
            while (e.hasMoreElements()) {
                String key = e.nextElement();
                sb.append(key).append("=").append(new String(request.getParameter(key).getBytes("iso-8859-1"), "utf-8")).append("&");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return rl(sb);
    }

    /**
     * 判断是数据是否匹配某模式
     *
     * @param reg  正则表达式
     * @param data 数据
     * @return 返回是否匹配
     */
    public static boolean r_im(String reg, String data) {
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(data);
        return matcher.matches();
    }

    /**
     * 返回匹配的字符数据
     *
     * @param reg  正则表达式
     * @param data 数据
     * @return 返回匹配的数据
     */
    public static String r_gm(String reg, String data) {
        String retval = "";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(data);
        if (matcher.find())
            retval = matcher.group(1);
        return retval;
    }

    /**
     * 通过字段名返回request参数，如果该字段名的参数不存在则返回null
     *
     * @param request request对象
     * @param colname 字段名
     * @return
     */
    public static String getRS(HttpServletRequest request, String colname) {
        String colval = null;
        if (request.getParameter(colname) != null && !request.getParameter(colname).equals(""))
            colval = request.getParameter(colname).toString();
        String retval = null;
        try {
            if (colval != null)
                retval = new String(colval.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retval;
    }

    /**
     * 通过字段名返回request参数,如果该字段名的参数不存在则返回参数defaultval的默认值
     *
     * @param request    request对象
     * @param colname    参数字段名
     * @param defaultval 如果返回值为null,返回的默认值
     * @return
     */
    public static String getRS(HttpServletRequest request, String colname, String defaultval) {
        String retval = getRS(request, colname);
        if (retval == null)
            retval = defaultval;
        return retval;
    }

    /**
     * 通过字段名返回同名的多个request参数,如果该字段不存在则返回null
     *
     * @param request request请求对象
     * @param colname 字段名
     * @return 返回字符串数组形式的多值
     */
    public static String[] getRSS(HttpServletRequest request, String colname) {
        String[] colval = null;
        if (request.getParameterValues(colname) != null)
            colval = request.getParameterValues(colname);
        else
            return null;
        ArrayList<String> retval = new ArrayList<String>();
        try {
            for (int i = 0; i < colval.length; i++) {
                if (colval[i] != null)
                    retval.add(new String(colval[i].getBytes("ISO-8859-1"), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retval.toArray(new String[]{});
    }

    /**
     * 获得request中的所有参数
     *
     * @param r 用户的request请求
     * @return 将request中所有请求转化为String类型的参数
     */
//    public static String getRP2S(HttpServletRequest r) {
//        return r.getParameterMap().entrySet().stream()
//                .map(e -> {
//                    String sv = "";
//                    try {
//                        sv = new String((((Map.Entry<String,String[]>)e).getValue()[0]).getBytes("ISO-8859-1"), "UTF-8");
////                        sv = new String((e.getValue()[0]).getBytes("ISO-8859-1"), "UTF-8");
//                    } catch (UnsupportedEncodingException e1) {
//                        e1.printStackTrace();
//                    }
//                    return new StringBuffer(((Map.Entry<String,String[]>)e).getKey()).append("=").append(sv);
//                })
//                .collect(Collectors.joining("&"));
//    }

    /**
     * 判断一个数组在另一个数组中的位置
     *
     * @param b     源数组
     * @param s     要搜索的数组
     * @param start 开始搜索的位置
     * @return 返回所在位置
     */
    public static int byteIndexOf(byte[] b, byte[] s, int start) {
        int i;
        if (s.length == 0)                                                         //如果要搜索的数组长度为0，直接返回0
            return 0;
        int max = b.length - s.length;                                              //获得要搜索的位置最大值
        if (max < 0)                                                               //如果最大值小于0返回-1 不可搜索
            return -1;
        if (start > max)                                                           //如果开始位置大雨搜索位置最大值，返回-1不可搜索
            return -1;
        if (start < 0)                                                            //如果开始位置小于0，设置开始位置为0
            start = 0;
        search:
        //开始搜索
        for (i = start; i <= max; i++) {
            if (b[i] == s[0])                                                     //如果找到了搜索字符的开始字符对整个搜索字符开始进行比较
            {
                int k = 1;
                while (k < s.length) {
                    if (b[k + i] != s[k])
                        continue search;                                        //如字节数组比较不成功返回search，否则继续比较下一搜索字节数组的字节
                    k++;
                }
                return i;
            }
        }
        return -1;
    }

    /**
     * 用来记录异常日志,和在服务端打印异常堆栈
     *
     * @param c 发生异常的类
     * @param e 异常对象
     */
    public static void el(String c, Exception e) {
        e.printStackTrace();
        LoggerFactory.getLogger(c).error(e.getMessage());

    }

    /**
     * 用来记录异常日志,和在服务器打印异常堆栈,和向用户打印错误消息
     *
     * @param c        发生异常的类
     * @param e        异常对象
     * @param response 打印对象
     * @param jmsgno   json消息编号
     */
    public static void el(String c, Exception e, HttpServletResponse response, int jmsgno) {
        p(response, JSONMsg.info(jmsgno, e.getMessage()));
        el(c, e);
    }

    /**
     * 记录异常日志,并返回消息为1111的json数据
     *
     * @param response 用来打印的对象
     * @param c        发生异常的类
     * @param e        异常对象
     */
    public static void el1111(HttpServletResponse response, Class<?> c, Exception e) {
//        p(response,JSONMsg.info(1111,e.toString()));
        p(response, JSONMsg.info(1111, "服务器繁忙，请您稍后再试！"));
        el(c.getName(), e);
    }

    public static String el1111(Class<?> c, Exception e) {
        el(c.getName(), e);
        return JSONMsg.info(1111, "服务器繁忙，请您稍后再试!");
    }

    /**
     * 通过字段名返回session属性值,如果该字段名参数不存在则返回null
     *
     * @param request request对象
     * @param colname 属性名
     * @return
     */
    public static String getSS(HttpServletRequest request, String colname) {
        String colval = null;
        if (request.getSession(false).getAttribute(colname) != null && !request.getSession(false).getAttribute(colname).equals(""))
            colval = request.getSession(false).getAttribute(colname).toString();
        return colval;
    }

    /**
     * 将字符串转换为数字类型
     *
     * @param value
     * @param defvalue
     * @return
     */
    public static int string2Int(String value, int defvalue) {
        int retval = defvalue;
        if (value != null) {
//			retval = Integer.parseInt(value);
            retval = Integer.valueOf(value);
        }

        return retval;
    }

    /**
     * 将字符串转换为布尔型.如果字符串不符返回false
     *
     * @param value
     * @return
     */
    public static boolean string2Boolean(String value) {
        boolean retval = false;
        if (value != null && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")))
            retval = Boolean.valueOf(value);
        return retval;
    }

    /**
     * 快速排序算法,用来排序排行榜成绩
     *
     * @return 返回排序后的数组
     */
    public static int[] quickSort() {
        return null;
    }

    /**
     * 加密数据
     *
     * @param key
     * @param content
     * @return
     * @throws Exception
     */
    public static String desEncode(String key, String content) throws Exception {
        //可信任随机源
        SecureRandom sr = new SecureRandom();
        //从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        //创建一个密钥工厂,然后用它把DESKeySpec转换成一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);

        //Clipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES");
        //用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        byte[] results = cipher.doFinal(content.getBytes());

        //将字节数组转为字符串
        String strEn = bytes2Hex(results);
        return strEn;
    }

    /**
     * 解密数据
     *
     * @param key
     * @param content
     * @return
     * @throws Exception
     */
    public static String desDecode(String key, String content) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        byte[] b1 = content.getBytes();
        if ((b1.length % 2) != 0)
            throw new IllegalArgumentException("解密数组长度不是偶数");
        byte[] b2 = new byte[b1.length / 2];
        for (int n = 0; n < b1.length; n += 2)
            b2[n / 2] = (byte) Integer.parseInt(new String(b1, n, 2), 16);

        byte[] results = cipher.doFinal(b2);

        return new String(results);
    }

    /**
     * sha256单向加密操作
     *
     * @param content
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String sha256Encode(String content) throws NoSuchAlgorithmException {
        String strEn = "";
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(content.getBytes());
        strEn = bytes2Hex(md.digest());
        return strEn;
    }

    /**
     * 字节数组转字符串
     *
     * @param bts 需要转化的字节数组
     * @return 返回转化后的字符串
     */
    public static String bytes2Hex(byte[] bts) {
        StringBuffer sb = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < bts.length; n++) {
            stmp = Integer.toHexString(bts[n] & 0XFF);
            if (stmp.length() == 1)
                sb.append("0").append(stmp);
            else
                sb.append(stmp);
        }
        return sb.toString();
    }

    private static DateFormat df = new SimpleDateFormat("yyyyMMdd");
    private static Date date = new Date();

    /**
     * 获得当前日期的字符串形式
     *
     * @return 返回日期的字符串
     */
    public static String getNowDate() {
        return df.format(date);
    }

    private static DateFormat df_ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 格式化Timestamp的时间格式
     *
     * @param ts timestamp对象
     * @return 返回格式化好的字符串
     */
    public static String formatTimestamp(Timestamp ts) {
        return df_ts.format(ts);
    }

    private static DateFormat df_time = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 返回到分钟部分的时间对象
     *
     * @param t 要转化为字符串的时间对象
     * @return 返回转化后的字符串
     */
    public static String formatTime(Timestamp t) {
        if (t == null)
            return "";
        return df_time.format(t);
    }


    private static DateFormat df_date = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 将时间转为日期格式
     *
     * @param t 带入的timestamp对象
     * @return 返回
     */
    public static String formatDate(Timestamp t) {
        if (t == null) return "";
        return df_date.format(t);
    }

    /**
     * 获得当前时间的Timestamp对象
     *
     * @return 返回当前时间的Timestamp对象
     */
    public static Timestamp getNowTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获得IP地址
     */
    public static String getRemoteAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 生日转换星座
     *
     * @param birthday 数据为4位生日
     * @return 返回星座的字符串
     */
    public static String birthDay2Constellation(String birthday) throws ParseException {
        if (compare_date(birthday, "0320") == 1 && compare_date(birthday, "0420") == -1)
            return "白羊座";
        else if (compare_date(birthday, "0419") == 1 && compare_date(birthday, "0521") == -1)
            return "金牛座";
        else if (compare_date(birthday, "0520") == 1 && compare_date(birthday, "0622") == -1)
            return "双子座";
        else if (compare_date(birthday, "0621") == 1 && compare_date(birthday, "0723") == -1)
            return "巨蟹座";
        else if (compare_date(birthday, "0722") == 1 && compare_date(birthday, "0823") == -1)
            return "狮子座";
        else if (compare_date(birthday, "0822") == 1 && compare_date(birthday, "0923") == -1)
            return "处女座";
        else if (compare_date(birthday, "0922") == 1 && compare_date(birthday, "1024") == -1)
            return "天秤座";
        else if (compare_date(birthday, "1023") == 1 && compare_date(birthday, "1123") == -1)
            return "天蝎座";
        else if (compare_date(birthday, "1122") == 1 && compare_date(birthday, "1222") == -1)
            return "射手座";
        else if (compare_date(birthday, "1221") == 1 && compare_date(birthday, "0120") == -1)
            return "摩羯座";
        else if (compare_date(birthday, "0119") == 1 && compare_date(birthday, "0219") == -1)
            return "水瓶座";
        else if (compare_date(birthday, "0218") == 1 && compare_date(birthday, "0321") == -1)
            return "双鱼座";
        else
            return "未知";
    }

    /**
     * 比较两个日期
     *
     * @param DATE1 第一日期
     * @param DATE2 第二个日期
     * @return DATE1在DATE2前返回1, DATE1在DATE2后返回-1
     */
    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("MMdd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
//                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
//                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据字符串返回日期对象
     *
     * @param str
     * @return
     */
    public static java.sql.Date str2Date(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date d_service_date = null;
        if (str != null) {
            d_service_date = new java.sql.Date(sdf.parse(str).getTime());
            return d_service_date;
        } else {
            return null;
        }
    }

    /**
     * 根据字符串返回日期对象,精确到分钟
     *
     * @param str
     * @return
     * @throws ParseException
     */
    public static Time str2Time(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Time d_service_time = null;
        if (str != null) {
            d_service_time = new Time(sdf.parse(str).getTime());
            return d_service_time;
        } else {
            return null;
        }
    }

    /**
     * 字符串型的时间转为Timestamp对象
     *
     * @param str 字符串型Timestamp对象
     * @return
     * @throws ParseException
     */
    public static Timestamp str2Timestamp(String str) throws ParseException {
        return new Timestamp(U.str2Time(str).getTime());
    }

//    public static java.sql.Timestamp str2Timestamp(String str) throws ParseException{
//
//    }

    /**
     * 比较两个日期大小
     *
     * @param cal_no  订单中的日期
     * @param cal_now 今天的日期
     * @return 返回间隔的天数，如果为0则表示同一天
     */
    public static int daysOfTwo(Calendar cal_no, Calendar cal_now) {
        int day_no = cal_no.get(Calendar.DAY_OF_YEAR);
        int day_now = cal_now.get(Calendar.DAY_OF_YEAR);
        return day_now - day_no;
    }


    /**
     * 根据某一字段,对json数据进行倒序排序
     *
     * @param ja
     * @return
     */
    public static JSONArray sortJSONArray(JSONArray ja, String sortkey) {
//        List<Map<String,JSONObject>> list = new ArrayList<Map<String, JSONObject>>();
        SortedMap<String, JSONObject> sm = new TreeMap<String, JSONObject>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Timestamp ts1 = Timestamp.valueOf(o1);
                Timestamp ts2 = Timestamp.valueOf(o2);
                return ts2.compareTo(ts1);
            }
        });

        for (int i = 0; i < ja.length(); i++) {
            if (ja.getJSONObject(i).has(sortkey) && ja.getJSONObject(i).getString(sortkey) != null)
                sm.put(ja.getJSONObject(i).getString(sortkey), ja.getJSONObject(i));
        }

        JSONArray ja_ret = new JSONArray();
        Iterator<String> it = sm.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
//            System.out.println(key);
            ja_ret.put(sm.get(key));
        }
        return ja_ret;
    }

    /**
     * 根据省份字段对数据进行优先排序
     *
     * @param ja   要排序的JSONObject数组对象
     * @param sort 排序的顺序
     * @return 返回排序后的JSONArray
     */
    public static JSONArray customSortJSONArray(JSONArray ja, String[] sort) {

        JSONArray ja_ret = new JSONArray();
        ArrayList<Integer> l_out = new ArrayList<>();
        //先把sort中拥有的数据排在前面
        for (int i = 0; i < sort.length; i++) {
            for (int j = 0; j < ja.length(); j++) {
                JSONObject jo = ja.getJSONObject(j);
                //当籍贯数据与排序中的相符,并且当前的籍贯数据还未被加入过返回队列,将其加入返回队列中.
                if (sort[i].equals(jo.getString("province")) && !l_out.contains(j)) {
                    ja_ret.put(jo);
                    l_out.add(j);
                }
            }
        }

        for (int i = 0; i < ja.length(); i++) {
            if (!l_out.contains(i))
                ja_ret.put(ja.getJSONObject(i));
        }

        return ja_ret;
    }

    /**
     * 根据身份证号获得年龄
     *
     * @param number
     * @return
     */
    public static int getAgeByIdCardNumber(String number) {
        String birthyear = number.substring(6, 10);
//        System.out.println(birthyear);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int age = year - Integer.parseInt(birthyear);
        return age;
    }

    /**
     * 生成n位随机数字
     *
     * @param num 指定生成的位数
     * @return 返回指定位数的随机数
     */
    public static String generateRandomArray(int num) {
        String chars = "0123456789";
        char[] rands = new char[num];
        for (int i = 0; i < num; i++) {
            int rand = (int) (Math.random() * 10);
            rands[i] = chars.charAt(rand);
        }
        return String.valueOf(rands);
    }

    /**
     * 拆分逗号分隔的多元素参数并转化为List
     *
     * @param params 要拆分的参数
     * @return 返回拆分后的List集合
     */
    public static List<Integer> splitParams2ListInt(String params) {
        List<String> l_params = splitParams2ListString(params);
        List<Integer> l_int_params = new ArrayList<>();
        for (String str : l_params)
            l_int_params.add(Integer.valueOf(str));
        return l_int_params;
    }

    /**
     * 拆分逗号分隔的多元素字符串，返回容器形式的多参数
     *
     * @param params 待拆分的字符串
     * @return 返回容器形式的多参数
     */
    public static List<String> splitParams2ListString(String params) {
        String[] sa_params = params.split(",");
        List<String> l_params = new ArrayList<>();
        for (String str : sa_params)
            l_params.add(str);
        return l_params;
    }

    /**
     * 判断字符串是否为null，如果为null转为空字符串，否则返回原字符串
     *
     * @param s 要判断的字符串
     * @return 返回空字符串或原字符串
     */
    public static String stringNull2EmptyString(String s) {
        return s == null ? "" : s;
    }
}

