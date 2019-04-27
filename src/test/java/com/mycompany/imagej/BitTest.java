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
                0x10,
                0x1000,
                0x100000,
                0x10000000
            }).boxed().collect(Collectors.toList());

        int or = 0;
        for (Integer a: expected) {
            or |= a;
        }
        List<Integer> actual = Bit.whichBit(or);
        assertEquals(expected, actual);
    }
}
