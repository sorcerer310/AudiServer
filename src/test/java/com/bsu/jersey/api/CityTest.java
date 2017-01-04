package com.bsu.jersey.api;

import com.bsu.jersey.tools.ServletTestTools;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.HashMap;

/**
 * City Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十二月 11, 2016</pre>
 */
public class CityTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getCity()
     */
    @Test
    public void testGetCity() throws Exception {
        ServletTestTools.printTestUrl("city", new HashMap<String,String>());

    }


} 
