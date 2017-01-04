package com.bsu.jersey.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by fengchong on 2016/12/3.
 */
@Path("/hello")
public class Hello {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getHello(){
        return "Hello Jersey";
    }

    @GET
    @Produces(MediaType.TEXT_XML)
    public String getXMLHello(){
        return "<?xml version=\"1.0\"?>"+"<hello>Hello Jersey</hello>";
    }
}
