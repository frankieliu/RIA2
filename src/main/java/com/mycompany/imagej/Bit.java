package com.mycompany.imagej;
import java.util.*;

public class Bit {
    public static List<Integer> whichBit(int n){
        List<Integer> out = new ArrayList<>();
        int bit = 1;  // masking bit
        int counter = n;  // count down counter
        while (counter != 0) {
            if ((bit & n) > 0) {  // if bit is set
                out.add(bit);
            }
            bit <<= 1;
            counter >>= 1;
        }
        return out;
    }
}
