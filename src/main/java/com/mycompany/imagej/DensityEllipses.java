package com.mycompany.imagej;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;
import ij.plugin.filter.ParticleAnalyzer;

import java.awt.*;

public class DensityEllipses {
    public String name;
    public ImagePlus im;
    public float[] wMod = {0.125f, 0.25f, 0.375f, 0.5f};
    public float[] dMod = {0.25f, 0.5f, 0.75f, 1f};
    public OvalRoi[] roi = new OvalRoi[dMod.length];
    public JsonObject jobj = new JsonObject();

    DensityEllipses(ImagePlus im0, Geom geo) {
        name = "Density Ellipses";
        im = im0.duplicate();

        ParticleAnalyzer pa;
        ResultsTable rt = new ResultsTable();
        Analyzer.setResultsTable(rt);

        im.getProcessor().autoThreshold();
        im.getProcessor().invert();
        float areaPrev = 0;
        JsonArray ja = new JsonArray();
        for (int i = 0; i < 4; i++) {
            roi[i] = new OvalRoi(geo.xMid - wMod[i] * geo.width, geo.yMid, dMod[i] * geo.width, dMod[i] * geo.height);
            im.setRoi(roi[i]);
            rt.reset();
            pa = new ParticleAnalyzer(ParticleAnalyzer.CLEAR_WORKSHEET, Measurements.AREA, rt, 0, 10e9, 0, 1);
            pa.analyze(im);
            float areaSelection = 0;
            for (int j = 0; j < rt.getCounter(); j++) {
                areaSelection += (float) rt.getValue("Area", j);
            }
            double areaProp = (areaSelection - areaPrev) / geo.area;
            ja.add(areaProp);
            areaPrev = areaSelection;
        }
        jobj.add("area",ja);
    }

    public ImagePlus overlay() {
        im.getProcessor().invert();
        for (OvalRoi r: roi){
            r.setStrokeColor(Color.BLUE);
            r.setStrokeWidth(4);
            Overlay Eloverlay = new Overlay(r);
            im.setOverlay(Eloverlay);
            im = im.flatten();
        }
        im.setTitle(name);
        return im;
        // IJ.save(im, dirParam.getAbsolutePath() + "/" + baseName + "_ellipses.tiff");
    }

}
