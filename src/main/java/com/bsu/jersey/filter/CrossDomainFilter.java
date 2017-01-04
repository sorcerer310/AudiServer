package com.bsu.jersey.filter;

import com.bsu.jersey.tools.U;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Created by fengchong on 2016/12/3.
 */
@Provider
public class CrossDomainFilter implements ContainerResponseFilter{
    @Context
    HttpServletRequest hsreq;

    private ResourceBundle rbundle = null;
    public CrossDomainFilter(){
        rbundle = ResourceBundle.getBundle("config");
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        System.out.println(U.getRemoteAddress(hsreq)+"===============================>");
        //根据本地IP，判断此处应该设置什么地址
        if(U.getRemoteAddress(hsreq).equals("0:0:0:0:0:0:0:1")) {
            //本地开发使用的ip
            containerResponseContext.getHeaders().add("Access-Control-Allow-Origin", rbundle.getString("develop.dns"));
        }else if(U.getRemoteAddress(hsreq).equals("192.168.31.184")){
            //lb开发使用的ip
            containerResponseContext.getHeaders().add("Access-Control-Allow-Origin", rbundle.getString("lbdevelop.dns"));

        }else if(U.getRemoteAddress(hsreq).equals("113.46.80.26")){
            //小米路由对外ip，可能会经常变化
            containerResponseContext.getHeaders().add("Access-Control-Allow-Origin", rbundle.getString("lbdevelop.dns"));
        } else {
            //服务端使用的ip
            containerResponseContext.getHeaders().add("Access-Control-Allow-Origin", rbundle.getString("server.dns"));
        }

        containerResponseContext.getHeaders().add("Access-Control-Max-Age","17200");
        containerResponseContext.getHeaders().add("Access-Control-Allow-Credentials","true");
    }

}
