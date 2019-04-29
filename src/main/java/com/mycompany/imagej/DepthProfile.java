package com.mycompany.imagej;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

public class DepthProfile {

    public ImagePlus im;
    public JsonObject jobj = new JsonObject();

    DepthProfile(ImagePlus im, ImagePlus sk){
        ImageProcessor ip = im.getProcessor();
        ip.autoThreshold();

        JsonArray jline = new JsonArray();
        for (int h = 0; h < ip.getHeight(); h++) {
            int n = Line.count(ip, h);
            jline.add(n);
        }

        JsonArray jextent = new JsonArray();
        for (int h = 0; h < ip.getHeight(); h++) {
            int n = Line.extent(ip, h);
            jextent.add(n);
        }

        jobj.add("line", jline);
        jobj.add("extent", jextent);
    }
}
