package com.bsu.jersey.api;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import com.bsu.jersey.tools.ServletTestTools;

import java.util.HashMap;

/**
 * Car Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十二月 6, 2016</pre>
 */
public class CarTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getCarByCity()
     */
    @Test
    public void testGetCarByCity() throws Exception {
//        ServletTestTools.printTestUrl("car",new HashMap<String,String>());
//        ServletTestTools.printTestUrl("car/all",new HashMap<String,String>());
//        ServletTestTools.printTestUrl("car/brand",new HashMap<String,String>());
//        ServletTestTools.printTestUrl("car/brand/car_models",new HashMap<String,String>(){{
//            put("brand","奥迪");
//        }});
        ServletTestTools.printTestUrl("car/brand/car_models/product_name",new HashMap<String,String>(){{
            put("car_models","奥迪A1");
            put("year","2014");
        }});

    }
}
