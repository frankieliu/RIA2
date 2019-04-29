package com.mycompany.imagej;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.stream.Collectors;

class BitTest {

    @Test
    void whichBit() {
        /*
            List<Integer> exp_collections = Arrays.stream
                    (new int[]{
                            0b100,
                            0b1000,
                            0b100000,
                            0b10000000
                    }).boxed().collect(Collectors.toList());
        */
        int[] expected = new int[]{
                0b100,
                0b1000,
                0b100000,
                0b10000000
        };
        int or = 0;
        for (Integer a: expected) {
            or |= a;
        }
        int[] actual = Bit.whichBit(or);
        assertArrayEquals(expected, actual);
    }

    @Test
    void oneToMany() {
        Map<Integer, Integer[]> m = new LinkedHashMap<>();
        m.put(1, new Integer[]{1,2,3});
        m.put(2, new Integer[]{4,5});
        assertArrayEquals(new int[]{1,2,3,4,5}, Bit.oneToMany(0b1|0b10, m));
    }
}
