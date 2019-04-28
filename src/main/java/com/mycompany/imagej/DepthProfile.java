package com.mycompany.imagej;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

public class DepthProfile {

    public ImagePlus im;
    public boolean saveDepth = true;
    public double[][] params;

    DepthProfile(ImagePlus im, ImagePlus sk){
        for(int i = 0; i<2; i++) {
            params = new double[2][];
        }
        ImageProcessor ip = im.getProcessor();
        ip.autoThreshold();
        if(saveDepth) {
            params[0] = new double[ip.getHeight()];
            for(int h = 0; h < ip.getHeight(); h++) {
                int n = Line.count(ip, h);
                params[0][h] = n;
            }
        }
        if(saveDepth) {
            params[1] = new double[ip.getHeight()];
            for(int h = 0; h < ip.getHeight(); h++) {
                int n = Line.extent(ip, h);
                params[1][h] = n;
            }
        }
    }
}
