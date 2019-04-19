package com.mycompany.imagej;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.EDM;
import ij.plugin.ImageCalculator;
import ij.process.ImageProcessor;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;
import ij.measure.Measurements;

import ij.gui.Roi;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import java.awt.Color;

class Diameter {
    static boolean verbatim = false;
    
    public static ImagePlus addCircle(int w, int h, int r, ImagePlus ori) {
        
        Roi apex = new OvalRoi(w - r/2, h - r/2, r, r);
        apex.setStrokeColor(Color.blue);
        apex.setStrokeWidth(1);

        Overlay overlay = new Overlay(apex); 
        ori.setOverlay(overlay);
        return ori.flatten(); 	 				
    }

    /**
     * Find max diameter, main stem, and root extents
     * @param im = original image
     * @param skel = skeleton image
     * @param params = output metrics
     */
    public static float[] getDiameters
        (ImagePlus im,
         ImagePlus skel){
		
        if(verbatim) IJ.log("Get diameter values");
		EDM edm = new EDM();
		ImageCalculator ic = new ImageCalculator();
		
		// Create EDM mask
		ImageProcessor ip = im.getProcessor();
		ip.autoThreshold();;
		//ip.invert();

		edm.run(ip);
		im.setProcessor(ip);

        // Frankie: added this to show result of EDM
        ImageProcessor imdp2 = ip.duplicate();
        ImagePlus ipd2 = new ImagePlus("edm.run", imdp2);
        ipd2.show();

        //----------------------------------------------------------------------
        // Max lateral and radial

        ImageProcessor ioverProc = ip.duplicate();
        ImagePlus ioverPlus = new ImagePlus("Over", ioverProc);

        // Step 1: Search for main stem 

        // 1. a) Find the "deepest" point (wmax, hmax)
        int max = -1;
        int wmax = 0;
        int hmax = 0;
        for(int w = 0; w < ip.getWidth(); w++){
	    	for(int h = 0; h < ip.getHeight(); h++){
                int gray = ip.get(w,h);
	    		if(gray > max){
                    max = gray;
                    wmax = w;
                    hmax = h;
                }
            }
        }
        System.out.println("Max: " + max);

        // 1. b) Follow from (wmax, hmax) up, to the stem root
        //       Consider only increasing lateral diameters
        int winit = wmax;
        int hinit = hmax;
        int wdia = 0;

        // diameters
        for (int h = hinit; h >= 0 ; h--) {
            int wleft = 0;
            int wright = 0;

            for (int w = winit; w >=0; w--) { // walk left
                int gray = ip.get(w, h);
                if (gray == 0) break; else wleft = w;
            }
            for (int w = winit; w < ip.getWidth(); w++) { // walk right
                int gray = ip.get(w, h);
                if (gray == 0) break; else wright = w;
            }
            
            int dia = wright - wleft;
            if (dia > wdia) {
                wdia = dia;
                winit = (wright + wleft) / 2;
                hinit = h;
            } else {
                break;
            }
            
        }

        // Step 2: Find the max lateral radius and overl radius
        int maxLateral = 0;
        int maxLateralX = 0;
        int maxLateralY = 0;
                        
        int maxRadial = 0;
        int maxRadialX = 0;
        int maxRadialY = 0;

        // Find the max lateral radius and max radius
        for(int w = 0; w < ip.getWidth(); w++){
	    	for(int h = 0; h < ip.getHeight(); h++){
                int gray = ip.get(w, h);
                if (gray > 0) {
                    int curLateral = Math.abs(w - winit);
                    if (curLateral > maxLateral) {
                        maxLateral = curLateral;
                        maxLateralX = w;
                        maxLateralY = h;
                    }
                    int curRadial = curLateral * curLateral + (h - hinit) * (h - hinit);
                    if (curRadial > maxRadial) {
                        maxRadial = curRadial;
                        maxRadialX = w;
                        maxRadialY = h;
                    }
                }
            }
        }

        // Optional: display overlays
        ioverPlus = Diameter.addCircle(wmax, hmax, 11, ioverPlus);
        ioverPlus = Diameter.addCircle(winit, hinit, 11, ioverPlus);
        ioverPlus = Diameter.addCircle(maxLateralX, maxLateralY, 11, ioverPlus);
        ioverPlus = Diameter.addCircle(maxRadialX, maxRadialY, 11, ioverPlus);
        ioverPlus.setTitle("Overlay"+wmax+" "+hmax+" "+winit+" "+hinit);
        ioverPlus.show();

        //----------------------------------------------------------------------
        // Continue the regular script
        
        // Create EDM Skeleton
		im = ic.run("AND create", im, skel);

        // Frankie: added
        ImagePlus ipd3 = new ImagePlus("AND create", im.getProcessor());
        ipd3.show();

        // Apply threshold
		IJ.setThreshold(im, 1, 255);
		IJ.run(im, "Create Selection", "");

        ResultsTable rt = new ResultsTable();
		Analyzer an = new Analyzer(im, Measurements.MODE | Measurements.MEAN | Measurements.MIN_MAX , rt);
        Analyzer.setResultsTable(rt);
		rt.reset();
		an.measure();

        im.close();
		skel.close();

        float[] params = new float[11];
        int counter = 0;
		params[counter++] = (float) rt.getValue("Max", 0);
		params[counter++] = (float) rt.getValue("Mean", 0);
		params[counter++] = (float) rt.getValue("Mode", 0);
        params[counter++] = (float) winit;
        params[counter++] = (float) hinit;
        params[counter++] = (float) maxLateral;
        params[counter++] = (float) maxLateralX;
        params[counter++] = (float) maxLateralY;
        params[counter++] = (float) Math.sqrt(maxRadial);
        params[counter++] = (float) maxRadialX;
        params[counter++] = (float) maxRadialY;

        return params;
	}
}
