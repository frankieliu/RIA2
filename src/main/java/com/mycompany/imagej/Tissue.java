package com.mycompany.imagej;

import ij.ImagePlus;
import ij.plugin.ImageCalculator;
import ij.plugin.filter.EDM;
import ij.process.ImageProcessor;

class Tissue {
    public static void getTissue
        (
         ImagePlus im,
         ImagePlus skel
         )
    {
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
        System.out.println("max: " + max + " min: " + min);
        int[] hist = new int[max - min + 1];
        for(int i = min; i <= max; i++){
            hist[i] = 0;
        }
	    for(int w = 0; w < ip.getWidth(); w++){
	    	for(int h = 0; h < ip.getHeight(); h++){
	    	    int pix = ip.get(w, h);
	    	    if ((pix < min) || (pix > max)) {
                    System.out.println("pix = " + pix);
                }
	    	    hist[ip.get(w, h)] += 1;
            }
	    }
        System.out.println("Histogram:");
        double tissueVol = 0;
        System.out.println("radius count vol cumVol");
        for(int i = min; i <= max; i++){
            // Calculate tissue volume
            double vol = i*i*Math.PI*hist[i];
            tissueVol += vol;
            System.out.println
                (
                 i + " " + hist[i] + " " + vol + " " + tissueVol
                 ) ;
        }
        
	}
}
