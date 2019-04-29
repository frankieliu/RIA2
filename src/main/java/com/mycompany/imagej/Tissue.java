package com.mycompany.imagej;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ij.ImagePlus;
import ij.plugin.ImageCalculator;
import ij.plugin.filter.EDM;
import ij.process.ImageProcessor;

class Tissue {
    public ImagePlus im;
    public ImagePlus skel;
    JsonObject jobj = new JsonObject();

    Tissue(ImagePlus im0, ImagePlus skel0) {
        im = im0.duplicate();
        skel = skel0.duplicate();

		ImageProcessor ip = im.getProcessor();
		ip.autoThreshold();

		// distance to the border
        EDM edm = new EDM();
		edm.run(ip);

        im.setProcessor(ip);

        // only keep the skeleton
        ImageCalculator ic = new ImageCalculator();
        im = ic.run("AND create", im, skel);
        // im.setTitle("EDM - Skeleton");
        // im.show();

        ip = im.getProcessor();
        // Frankie:
        // For some reason getMax and getMin don't get the correct values
        // Let's get it ourselves
        int max = ip.get(0,0);
        int min = ip.get(0,0);

	    for(int w = 0; w < ip.getWidth(); w++){
	    	for(int h = 0; h < ip.getHeight(); h++){
	    	    int pix = ip.get(w, h);
                if (pix > max) {
                    max = pix;
                    continue;
                }
                if (pix < min) {
                    min = pix;
                    // continue;
                }
            }
        }
        
        // int max = (int) ip.getMax(); // around 7
        // int min = (int) ip.getMin(); // 0
	    int[] hist = new int[max - min + 1];
        for(int i = min; i <= max; i++){
            hist[i] = 0;
        }
	    for(int w = 0; w < ip.getWidth(); w++){
	    	for(int h = 0; h < ip.getHeight(); h++){
	    	    int pix = ip.get(w, h);
	    	    if ((pix < min) || (pix > max)) {
                    System.out.println("pix = " + pix);
                    // should not happen
                }
	    	    hist[ip.get(w, h)] += 1;
            }
	    }

        JsonArray ja = new JsonArray();
        double tissueVol = 0;
        for(int i = min; i <= max; i++){
            // Calculate tissue volume
            double vol = i*i*Math.PI*hist[i];
            tissueVol += vol;

            JsonObject jo = new JsonObject();
            jo.addProperty("radius", i);
            jo.addProperty("count", hist[i]);
            jo.addProperty("volume", vol);
            jo.addProperty("cumVolume", tissueVol);
            ja.add(jo);
        }
        jobj.add("histogram", ja);
        jobj.addProperty("max", max);
        jobj.addProperty("min", min);
	}
}
