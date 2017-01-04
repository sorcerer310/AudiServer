package com.bsu.jersey.api;

import com.bsu.jersey.tools.ServletTestTools;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import javax.servlet.Servlet;
import java.util.HashMap;

/**
 * Service Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十二月 12, 2016</pre>
 */
public class ServiceTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getService(@QueryParam("car_type_id") int car_type_id, @QueryParam("kilometer") int kilometer)
     */
    @Test
    public void testGetService() throws Exception {
//        ServletTestTools.printTestUrl("service", new HashMap<String, String>() {{
//            put("car_type_id", "100");
//            put("kilometer", "11119");
//        }});

        ServletTestTools.printTestUrl("service/list",new HashMap<String,String>(){{
            put("car_type_id","106");
        }});
    }


    /**
     * Method: compute_km(Record5<UInteger,BCarServiceServiceItem,UInteger,UInteger,Double> record, int curr_km, int target_km)
     */
    @Test
    public void testCompute_km() throws Exception {

/* 
try { 
   Method method = Service.getClass().getMethod("compute_km", Record5<UInteger,BCarServiceServiceItem,UInteger,UInteger,Double>.class, int.class, int.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

} 
