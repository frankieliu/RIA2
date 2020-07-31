package com.mycompany.imagej;

import com.google.gson.JsonObject;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.process.ImageProcessor;

import java.awt.Rectangle;
import java.lang.Math;

class Rotate {
	/**
	 * 
	 * @param im ImagePlus
	 */
    public static double[] getWH(ImagePlus im) {
        double[] a = new double[2];
        im.getProcessor().autoThreshold();
        IJ.run(im, "Create Selection", "");
        Roi select = im.getRoi();
        ImageProcessor Shape = im.getProcessor();
        Shape.setRoi(select.getBounds());
        Shape = Shape.crop();
        im.setProcessor(Shape);
        // im.show();
        a[0] = im.getWidth();
        a[1] = im.getHeight();
        return a;
    }


    /**
     * @param im input image
     * @return best rotation angle
     */
    public double getAngle(ImagePlus im) {
        double angle = getAngle(im, -15., 1., 15.);
        angle = getAngle(im, angle, 0.1, angle + 1);
        return angle;
    }

    /**
     * @param im input image
     * @param begin beginning angle
     * @param step step
     * @param end ending angle
     * @return best rotation angle
     */
    public double getAngle(ImagePlus im, double begin, double step, double end){
        double hmin = 1e10;
        double angle = begin;
        if (debug == true)
            System.out.println("Finding min angle");
        do {
            ImagePlus im2 = im.duplicate();
            IJ.run(im2, "Select All", "");
            IJ.run(im2, "Rotate...", "angle=" + angle);
            Roi roi = im2.getRoi();
            Rectangle r = roi.getBounds();
            IJ.run(im2, "Canvas Size...", "width="+r.width+" height="+r.height+
                   " position=Center zero");
            
            ImageProcessor ip = im2.getProcessor();
            ip.setInterpolationMethod(ImageProcessor.BILINEAR);
            ip.rotate(angle);
            im2.setProcessor(ip);
            
            double[] wh = getWH(im2);
            if(wh[1] > hmin) {
                break;
            }
            hmin = wh[1];
            if (debug == true) {
                System.out.println(angle + " " + hmin);
                System.out.flush();
            }
            angle += step;
            if (angle > end) {
                break;
            }
        } while(true);
        return angle - step;
	}

    public static double getVolumeFromExtents(ImagePlus im) {
        ImageProcessor ip = im.getProcessor();
        double vol = 0;
        for(int h = 0; h < ip.getHeight(); h++){
            int n = Line.extent(ip, h);
            vol += (n * n / 4.0) * (Math.PI);
        }
        return vol;
    }

    public String name;
    public ImagePlus im;
    public ImagePlus skel;
    JsonObject jobj = new JsonObject();
    boolean debug;

    Rotate(ImagePlus im0, ImagePlus skel0, Boolean debug0) {
        debug = debug0;
        init(im0, skel0);
    }

    Rotate(ImagePlus im0, ImagePlus skel0) {
        debug = false;
        init(im0, skel0);
    }

    public void init(ImagePlus im0, ImagePlus skel0) {
        name = "Rotate";
        im = im0.duplicate();
        skel = skel0.duplicate();

        jobj.addProperty("volume before rotation", getVolumeFromExtents(im));
        double angle = getAngle(im);
        jobj.addProperty("angle", angle);
        /*
        if (false) {
            IJ.run(im, "Select All", "");
            IJ.run(im, "Rotate...", "angle=" + angle);
            Roi roi = im.getRoi();
            Rectangle r = roi.getBounds();
            IJ.run(im, "Canvas Size...", "width="+r.width+" height="+r.height+
                   " position=Center zero");
        }
        */

        ImageProcessor ip = im.getProcessor();
        ip.setInterpolate(true);
        ip.setInterpolationMethod(ImageProcessor.BILINEAR);
        ip.rotate(angle);
        im.setProcessor(ip);
        im.setTitle("Rotated");

        ImageProcessor skel_ip = skel.getProcessor();
        skel_ip.setInterpolate(true);
        skel_ip.setInterpolationMethod(ImageProcessor.BILINEAR);
        skel_ip.rotate(angle);
        skel.setProcessor(skel_ip);
        skel.setTitle("Skeleton Rotated");

        jobj.addProperty("volume after rotation", getVolumeFromExtents(im));
    }

}
