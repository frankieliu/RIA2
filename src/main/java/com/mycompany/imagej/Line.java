package com.mycompany.imagej;

import ij.process.ImageProcessor;

public class Line {

    /**
     * Compute the number of black pixels along a line
     * @param bp
     * @param h
     * @return
     */
    public static int count(ImageProcessor bp, int h){
        int n = 0;
        int j = h;
        for(int i = 0; i < bp.getWidth(); i++){
            if(bp.getPixel(i, j) > 125) n++;
        }
        return n;
    }

    /**
     * Compute the extent of black pixels along a line
     * @param bp
     * @param h
     * @return
     */
    public static int extent(ImageProcessor bp, int h){
        int n = 0;
        int j = h;
        int left = -1;
        int right = -1;
        for(int i = 0; i < bp.getWidth(); i++){
            if ((bp.getPixel(i, j) > 125) && (left == -1)) {
                left = i;
            }
            if ((bp.getPixel(i,j) < 125) && (left != -1)) {
                right = i;
            }
        }
        return right-left+1;
    }

    /**
     * Compute the number of black neighbours for a point
     * @param bp
     * @param w
     * @param h
     * @return
     */
    private int nNeighbours(ImageProcessor bp, int w, int h){
        int n = 0;
        for(int i = w-1; i <= w+1; i++){
            for(int j = h-1; j <= h+1; j++){
                if(bp.getPixel(i, j) > 125) n++;
                if(n == 3) return n-1;
            }
        }
        return n-1;
    }
}
