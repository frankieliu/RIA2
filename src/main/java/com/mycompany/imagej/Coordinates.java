package com.mycompany.imagej;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Overlay;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;
import ij.process.ImageProcessor;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.awt.*;

public class Coordinates {

    public ImagePlus im;
    public int nCoord = 10;
    public double[] xCoord = new double[2*nCoord];
    public double[] yCoord = new double[2*nCoord];
    public double[] diffCoord = new double[nCoord];
    public double[] cumulCoord = new double[nCoord];
    public JsonObject jobj = new JsonObject();

    Coordinates(ImagePlus im0, Geom geom) {
        im = im0.duplicate();
        // Get bounding box
        im.getProcessor().autoThreshold();
        IJ.run(im, "Create Selection", "");

        Roi select;
        select = im.getRoi();
        ImageProcessor Shape = im.getProcessor();
        Shape.setRoi(select.getBounds());
        Shape = Shape.crop();
        im.setProcessor(Shape);
        double w = im.getWidth();
        double h = im.getHeight();
        int m = 2 * nCoord;

        //Calculate coordinates
        // Make rectangle (for each rectangle)
        // Get bounding box of rectangle
        // Get coordinates of bounding box
        // Save coordinates

        for (int i = 0; i < nCoord; i++) {
            ImagePlus currentSelection = im.duplicate();
            double factor = (double) i / (nCoord - 1);

            double y;
            if (i == 0) y = 0.01f * h;  //Ymid;
            else if (i == (nCoord - 1)) y = 0.99 * h;
            else y = factor * h;

            currentSelection.setRoi(new Roi(0, y, w, 3));

            ImageProcessor small = currentSelection.getProcessor();
            small = small.crop();
            small.setAutoThreshold("Li");
            currentSelection.setProcessor(small);

            IJ.run(currentSelection, "Create Selection", "");
            ResultsTable rt = new ResultsTable();
            Analyzer.setResultsTable(rt);
            rt.reset();
            Analyzer an = new Analyzer(currentSelection, Measurements.RECT, rt);
            an.measure();

            xCoord[i] = (float) rt.getValue("BX", 0);
            yCoord[i] = (float) y;

            int o = m - i - 1;
            xCoord[o] = (float) (rt.getValue("BX", 0) + rt.getValue("Width", 0));
            yCoord[o] = (float) y;

            // Get the width and the cumul width (inspired from Bucksch et al 2014, Plant Physiology)
            diffCoord[i] = Math.abs(xCoord[i] - xCoord[o]) / w;
            if (i == 0) cumulCoord[i] = diffCoord[i];
            else cumulCoord[i] = cumulCoord[i - 1] + diffCoord[i];

        }
        // if(saveTPS) sendShapeDataToTPS(xCoord, yCoord);
        JsonArray jx = new JsonArray();
        JsonArray jy = new JsonArray();
        JsonArray jd = new JsonArray();
        JsonArray jc = new JsonArray();
        for(int i = 0; i < nCoord*2; i++) jx.add(xCoord[i]);
        for(int i = 0; i < nCoord*2; i++) jy.add(yCoord[i]);
        for(int i = 0; i < nCoord; i++) jd.add(diffCoord[i]);
        for(int i = 0; i < nCoord; i++) jc.add(cumulCoord[i]);
        jobj.add("xCoord", jx);
        jobj.add("yCoord", jy);
        jobj.add("diff", jd);
        jobj.add("cum", jc);
    }

    public ImagePlus overlay() {
        // Make shape
        float[] xf = new float[xCoord.length];
        float[] yf = new float[yCoord.length];
        for (int i = 0; i<2*nCoord; i++) xf[i] = (float) xCoord[i];
        for (int i = 0; i<2*nCoord; i++) yf[i] = (float) yCoord[i];

        PolygonRoi shapeROI = new PolygonRoi(xf, yf, Roi.FREEROI);
        shapeROI.setStrokeColor(Color.blue);
        shapeROI.setStrokeWidth(5);
        Overlay overlay = new Overlay(shapeROI);
        im.setOverlay(overlay);
        im = im.flatten();
        return im;
    }
}
