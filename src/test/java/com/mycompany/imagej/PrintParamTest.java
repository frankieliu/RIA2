package com.mycompany.imagej;
import com.mycompany.imagej.PrintParam;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class PrintParamTest {
    private static PrintParam pp;

    @BeforeAll
    static void setUp() throws Exception {
        System.out.println("Set up!");
        pp = new PrintParam();
        assertNotNull(pp);
    }
    @AfterAll
    static void tearDown() throws Exception {
        System.out.println("Tear down!");
        pp = null;
        assertNull(pp);
    }
    @Test
    void testPrintParam() {
        assertEquals(pp == null, false);
        Map<String, Double> m = new LinkedHashMap<>();
        for(int i=0; i<100; i++){
            m.put(Integer.toString(i), (double) i*i);
        }
        pp.setH(m);
        for(String s: pp.getH().keySet()) {
            int si = Integer.parseInt(s);
            assertEquals(pp.getH().get(s), (double) si*si);
        }
    }

}
