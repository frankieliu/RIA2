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

import java.awt.*;

public class ConvexHull {

    ImagePlus im;
    ImageProcessor ip;
    Roi select;
    ImagePlus chImage;
    double[] params = new double[1];
    int nEFD = 30;

    ConvexHull(ImagePlus im0, boolean efd){
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
            };
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

        params[0] = (double) rt.getValue("Area", 0);

    }

    public class Efd {
        double ax;
        double ay;
        double bx;
        double by;
        double efd;

        Efd(double ax0, double ay0, double bx0, double by0, double efd0) {
            ax = ax0;
            ay = ay0;
            bx = bx0;
            by = by0;
            efd = efd0;
        }

    }

    Efd[] paramsEfd;

    public double[] getEfd() {
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

        paramsEfd = new Efd[efd1.nFD];
        for (int i = 0; i < efd1.nFD; i++) {
            paramsEfd(new Efd(efd1.ax[i], efd1.ay[i], efd1.bx[i], efd1.by[i], efd1.efd[i]));
        }
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
