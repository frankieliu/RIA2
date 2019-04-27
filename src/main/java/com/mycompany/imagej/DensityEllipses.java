package com.mycompany.imagej;

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

    public float area, depth, width, Ymid, Xmid;
    public float[] wMod = {0.125f, 0.25f, 0.375f, 0.5f};
    public float[] dMod = {0.25f, 0.5f, 0.75f, 1f};
    public OvalRoi[] roi = new OvalRoi[dMod.length];
    public float[] params = new float[dMod.length];

    public ImagePlus getDensityEllipses(ImagePlus im) {

        ParticleAnalyzer pa;
        ResultsTable rt = new ResultsTable();
        Analyzer.setResultsTable(rt);

        im.getProcessor().autoThreshold();
        im.getProcessor().invert();
        float areaPrev = 0;
        for (int i = 0; i < 4; i++) {
            roi[i] = new OvalRoi(Xmid - wMod[i] * width, Ymid, dMod[i] * width, dMod[i] * depth);
            im.setRoi(roi[i]);
            rt.reset();
            pa = new ParticleAnalyzer(ParticleAnalyzer.CLEAR_WORKSHEET, Measurements.AREA, rt, 0, 10e9, 0, 1);
            pa.analyze(im);
            float areaSelection = 0;
            for (int j = 0; j < rt.getCounter(); j++) {
                areaSelection += (float) rt.getValue("Area", j);
            }
            float areaProp = (areaSelection - areaPrev) / area;
            params[i] = areaProp;
            areaPrev = areaSelection;
        }
        return im;
    }

    public ImagePlus overlay(ImagePlus im) {
        im.getProcessor().invert();
        for (int i = 0; i < roi.length; i++) {
            roi[i].setStrokeColor(Color.BLUE);
            roi[i].setStrokeWidth(4);
            Overlay Eloverlay = new Overlay(roi[i]);
            im.setOverlay(Eloverlay);
            im = im.flatten();
        }
        return im;
        // IJ.save(im, dirParam.getAbsolutePath() + "/" + baseName + "_ellipses.tiff");
    }

    public static void main(String[] args) {

    }
}
