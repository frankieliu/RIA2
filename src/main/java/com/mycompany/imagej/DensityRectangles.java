package com.mycompany.imagej;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;
import ij.plugin.filter.ParticleAnalyzer;
import ij.process.ImageProcessor;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.awt.*;

public class DensityRectangles {

    public String name;
    // public double[] dMod = {0.25f, 0.5f, 0.75f, 1f};
    public double[] dMod = {0, 0.2f, 0.4f, 0.6f};
    public Roi[] roi = new Roi[dMod.length];
    public ImagePlus im;
    public ImageProcessor ip;
    public JsonObject jobj = new JsonObject();

    /**
     *
     * @param im0 input image
     * @param geo Geom object
     */
    DensityRectangles(ImagePlus im0, Geom geo) {
        name = "Density Rectangles";
        im = im0.duplicate();
        ip = im.getProcessor();
        ResultsTable rt = new ResultsTable();
        Analyzer.setResultsTable(rt);
        ParticleAnalyzer pa;

        im.getProcessor().autoThreshold();
        im.getProcessor().invert();
        JsonArray ja = new JsonArray();
        for (int i = 0; i < dMod.length; i++) {
            roi[i] = new Roi(geo.xMid - 0.5 * geo.width, geo.yMid + (dMod[i] * geo.height), geo.width, 0.2 * geo.height);
            im.setRoi(roi[i]);
            rt.reset();
            pa = new ParticleAnalyzer(ParticleAnalyzer.CLEAR_WORKSHEET, Measurements.AREA, rt, 0, 10e9, 0, 1);
            pa.analyze(im);

            double ar1 = 0;
            for (int j = 0; j < rt.getCounter(); j++) {
                ar1 += rt.getValue("Area", j);
            }
            ja.add(ar1/geo.area);
        }
        jobj.add("area", ja);
    }

    public ImagePlus overlay() {
        //Create rectangles overlay

        im.getProcessor().invert();
        for (Roi el: roi) {
            el.setStrokeColor(Color.blue);
            el.setStrokeWidth(5);
            Overlay overlay = new Overlay(el);
            im.setOverlay(overlay);
            im = im.flatten();
        }
        return im;
    }

}
