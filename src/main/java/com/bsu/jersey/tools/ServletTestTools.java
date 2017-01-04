package com.bsu.jersey.tools;

import com.bsu.jersey.parser.JSONParser;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by fengchong on 16/6/29.
 */
public class ServletTestTools {
    private static String hostPath = "http://localhost:8090/AudiServer/";
    public static String testUrl(String url,HashMap<String,String> args) throws IOException, SAXException {
        WebConversation wc = new WebConversation();
        WebRequest req = new GetMethodWebRequest(hostPath+url);

//        req.set
        Iterator<String> it = args.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            req.setParameter(key,new String(args.get(key).getBytes("UTF-8"),"ISO-8859-1"));
        }
        try {
            WebResponse resp = wc.getResponse(req);
            return JSONParser.formatJSON(resp.getText());
        }catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }

//        System.out.println(resp.getText());
    }

    /**
     * 打印测试的URL返回的数据
     * @param url
     * @param args
     * @throws IOException
     * @throws SAXException
     */
    public static void printTestUrl(String url,HashMap<String,String> args) throws IOException, SAXException {
        System.out.println(testUrl(url,args));
    }
}
