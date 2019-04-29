package com.mycompany.imagej;

import ij.process.ImageProcessor;

public class Line {

    /**
     * Compute the number of black pixels along a line
     * @param bp imageprocessor
     * @param h a particular y-coordinate for horizontal line
     * @return the number of pixels that are about 125
     */
    public static int count(ImageProcessor bp, int h){
        int n = 0;
        for(int i = 0; i < bp.getWidth(); i++){
            if(bp.getPixel(i, h) > 125) n++;
        }
        return n;
    }

    /**
     * Compute the extent of black pixels along a line
     * @param bp imageprocessor
     * @param h a particular y-coordinate for a horizontal line
     * @return the extent of pixels > 125 in x-direction
     */
    public static int extent(ImageProcessor bp, int h){
        int n = 0;
        int left = -1;
        int right = -1;
        for(int i = 0; i < bp.getWidth(); i++){
            if ((bp.getPixel(i, h) > 125) && (left == -1)) {
                left = i;
            }
            if ((bp.getPixel(i,h) < 125) && (left != -1)) {
                right = i;
            }
        }
        return right-left+1;
    }

    /**
     * Compute the number of black neighbours for a point
     * @param bp imageProcessor
     * @param w the mid x coord for the pixel
     * @param h the mid y coord for the pixel
     * @return the number of neighbours around the pixel
     */
    public static int nNeighbours(ImageProcessor bp, int w, int h){
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
