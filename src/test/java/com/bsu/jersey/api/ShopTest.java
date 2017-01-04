package com.bsu.jersey.api;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.bsu.jersey.tools.ServletTestTools;

import java.util.HashMap;

/**
 * Shop Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十二月 5, 2016</pre>
 */
public class ShopTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getShop(@QueryParam("city_name")String city_name)
     */
    @Test
    public void testGetShop() throws Exception {
        ServletTestTools.printTestUrl("shop", new HashMap<String, String>() {{
            put("city_name", "长春");
        }});
    }
}
