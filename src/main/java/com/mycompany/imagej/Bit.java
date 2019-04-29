package com.mycompany.imagej;
import java.util.*;

public class Bit {

    /**
     * @param n bit masked integer to setting different choices
     * @param m map which takes a particular set bit to a map of many int[]
     * @return int[] of the concatenated results from the individual int[]'s from each bit mask mapped to int[]
     */
    public static int[] oneToMany(int n, Map<Integer, Integer[]>m) {
        List<Integer> out = new ArrayList<>();
        int[] bits = whichBit(n);
        for (Integer b: bits) {
            Integer[] many = m.get(b);
            /*
            for (Integer el: many)
                out.add(el);
             */
            out.addAll(Arrays.asList(many));
        }
        return out.stream().mapToInt(i-> i).toArray();
    }

    /**
     * @param n the compressed bit sequence i.e. from 0b0011 to
     * @return the separated bit choices i.e. in the form 0b0001, 0b0010, etc
     */
    public static int[] whichBit(int n){
        List<Integer> out = new ArrayList<>();
        int bit = 0b1;  // masking bit
        int counter = n;  // count down counter
        while (counter != 0) {
            if ((bit & n) > 0) {  // if bit is set
                out.add(bit);
            }
            bit <<= 1;
            counter >>= 1;
        }
        return out.stream().mapToInt(i->i).toArray();
    }
}
