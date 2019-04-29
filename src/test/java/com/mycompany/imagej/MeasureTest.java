package com.mycompany.imagej;

import ij.measure.Measurements;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class MeasureTest {

    @Test
    void headings() {
        String[] actual =
            Measure.headings(Measurements.AREA | Measurements.MODE | Measurements.CENTROID);
        assertArrayEquals(new String[] {"Area", "Mode", "X", "Y"}, actual);
    }
}