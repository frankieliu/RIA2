package com.mycompany.imagej;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.stream.Collectors;

class BitTest {

    @Test
    void whichBit() {
        List<Integer> expected = Arrays.stream
            (new int[]{
                    0b100,
                    0b1000,
                    0b100000,
                    0b10000000
            }).boxed().collect(Collectors.toList());

        int or = 0;
        for (Integer a: expected) {
            or |= a;
        }
        List<Integer> actual = Bit.whichBit(or);
        assertEquals(expected, actual);
    }
}
