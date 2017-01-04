package com.bsu.jersey.filter;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengchong on 2016/12/5.
 */
@Provider
public class ResponseEncodeFilter implements ContainerResponseFilter {
    @Context
    HttpServletResponse hresp;


//    @Override
//    public void filter(ClientRequestContext clientRequestContext, ClientResponseContext clientResponseContext) throws IOException {
//
//    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
//        if(containerResponseContext.getHeaders().containsKey("Content-Type"))
//            containerResponseContext.getHeaders().put("Content-Type", new ArrayList<Object>() {{
//                add("application/json");
//                add("charset=utf-8");
//            }});
//        containerResponseContext.getHeaders().add("Content-Type","application/json; charset=utf-8");
//        containerResponseContext.getHeaders().add("Cache-Control","no-cache,must-revalidate");
        hresp.setContentType("text/javascript; charset=utf-8");
        hresp.setHeader("Cache-Control", "no-cache, must-revalidate");
    }
}
