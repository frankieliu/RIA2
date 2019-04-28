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

import java.awt.*;

public class DensityRectangles {

    public float area, depth, width, Ymid, Xmid;
    public float[] dMod = {0.25f, 0.5f, 0.75f, 1f};
    // public float[] dMod = {0, 0.2f, 0.4f, 0.6f};
    public Roi[] roi = new OvalRoi[dMod.length];
    public float[] params = new float[dMod.length];
    public ImagePlus im;
    public ImageProcessor ip;
    public ResultsTable rt;

    /**
     *
     * @param im
     * @param name
     */
    private void DensityRectangles(ImagePlus im) {
        this.im = im.duplicate();
        ip = this.im.getProcessor();
        rt = new ResultsTable();
        Analyzer.setResultsTable(rt);
        ParticleAnalyzer pa;

        im.getProcessor().autoThreshold();
        im.getProcessor().invert();
        float ar1 = 0;
        for (int i = 0; i < dMod.length; i++) {
            roi[i] = new Roi((Xmid - 0.5 * width), Ymid + (dMod[i] * depth), width, 0.2 * depth);
            im.setRoi(roi[i]);
            rt.reset();
            pa = new ParticleAnalyzer(ParticleAnalyzer.CLEAR_WORKSHEET, Measurements.AREA, rt, 0, 10e9, 0, 1);
            pa.analyze(im);

            ar1 = 0;
            for (int j = 0; j < rt.getCounter(); j++) {
                ar1 += (float) rt.getValue("Area", j);
            }
            params[i] = ar1 / area;

        }
    }

    public ImagePlus overlay() {
        //Create rectangles overlay

        im.getProcessor().invert();
        for (int i = 0; i < roi.length; i++) {
            roi[i].setStrokeColor(Color.blue);
            roi[i].setStrokeWidth(5);
            Overlay overlay = new Overlay(roi[i]);
            im.setOverlay(overlay);
            im = im.flatten();
        }
        // IJ.save(im, dirParam.getAbsolutePath() + "/" + baseName + "_rectangles.tiff");
    }


}
