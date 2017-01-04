package com.bsu.jersey.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by fengchong on 2016/12/3.
 */

@Provider
public class BsuFilter implements ContainerResponseFilter{
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
//        containerResponseContext.getHeaders().add("X-Powered-By","Jersey :-)");
    }
}
