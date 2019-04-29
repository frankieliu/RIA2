package com.mycompany.imagej;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Overlay;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;
import ij.plugin.filter.ParticleAnalyzer;
import ij.plugin.frame.RoiManager;
import ij.process.ImageProcessor;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.awt.*;

public class ConvexHull {

    public ImagePlus im;
    public ImageProcessor ip;
    public Roi select;
    public ImagePlus chImage;
    public JsonObject jobj = new JsonObject();
    public int nEFD = 30;

    ConvexHull(ImagePlus im0){
        im = im0.duplicate();
        ResultsTable rt = new ResultsTable();

        // Get bounding box
        im.getProcessor().autoThreshold();
        im.getProcessor().invert();
        ParticleAnalyzer pa = new ParticleAnalyzer(
                ParticleAnalyzer.ADD_TO_MANAGER |
                ParticleAnalyzer.CLEAR_WORKSHEET,
                Measurements.AREA,
                rt, 0, 10e9);
        pa.analyze(im);

        // Find the largest object in the image (the root system) in case their are still multiple objects.
        int index = 0;
        double max = 0;
        for(int i = 0; i < rt.getCounter(); i++){
            if(rt.getValue("Area", i) > max){
                max = rt.getValue("Area", i);
                index = i;
            }
        }

        // Get the convex hull from the ROI manager (the largest object)
        RoiManager manager = RoiManager.getInstance();
        Roi[] roiA = manager.getRoisAsArray();
        select = roiA[index];

        PolygonRoi cv = new PolygonRoi(select.getConvexHull(), Roi.POLYGON);

        ImageProcessor chProcessor = cv.getMask();
        chImage = new ImagePlus();
        chImage.setProcessor(chProcessor);
        chProcessor.autoThreshold();

        // Get shape measurements from the convex hull
        chImage.getProcessor().invert();
        Analyzer.setResultsTable(rt);
        rt.reset();
        pa = new ParticleAnalyzer(ParticleAnalyzer.CLEAR_WORKSHEET, Measurements.CENTER_OF_MASS |
                Measurements.AREA, rt, 0, 10e9);
        pa.analyze(chImage);
        jobj.addProperty("area", rt.getValue("Area", 0));
    }

    /**
     * Elliptic Fourier Descriptor
     * This class implements the Elliptic Fourier Descriptor EFD that is described
     * (and implemented in Matlab code) in REF1 (see chapter 7).  The EFD provides
     * a normalized set oc coefficients that are rotation, translation and scale
     * invariant.  The first coefficient relates to the centroid of the input shape
     * before the EFD is computed and can be ignored.  The second FD coefficient relates
     * to a circle circumscribed about the centroid before the EFD computation.
     * After the EDF computation the second EFD is always 2 and can be ignored.
     * That leaves the remaining EFD coefficients for use in comparing shapes.
     */
    public void getEfd() {
        PolygonRoi roi = new PolygonRoi(select.getConvexHull(), Roi.POLYGON);
        Rectangle rect = roi.getBounds();
        int n = roi.getNCoordinates();
        double[] x = new double[n];
        double[] y = new double[n];
        int[] xp = roi.getXCoordinates();
        int[] yp = roi.getYCoordinates();
        for (int i = 0; i < n; i++) {
            x[i] = (double) (rect.x + xp[i]);
            y[i] = (double) (rect.y + yp[i]);
        }
        EllipticFD efd1 = new EllipticFD(x, y, nEFD);

        JsonArray ja = new JsonArray();
        // Number of fourier coefficients
        for (int i = 0; i < efd1.nFD; i++) {
            JsonObject jefd = new JsonObject();
            jefd.addProperty("ax", efd1.ax[i]);
            jefd.addProperty("ay", efd1.ay[i]);
            jefd.addProperty("bx", efd1.bx[i]);
            jefd.addProperty("by", efd1.by[i]);
            jefd.addProperty("efd", efd1.efd[i]);
            ja.add(jefd);
        }
        jobj.add("efd", ja);
    }

    public ImagePlus overlay() {
        // Make shape
        Roi roiToOverlay = new PolygonRoi(select.getConvexHull(), Roi.POLYGON);
        roiToOverlay.setStrokeColor(Color.blue);
        roiToOverlay.setStrokeWidth(5);
        Overlay overlay = new Overlay(roiToOverlay);
        im.getProcessor().invert();
        im.setOverlay(overlay);
        im = im.flatten();
        return im;
    }

}
