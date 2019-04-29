package com.mycompany.imagej;

import ij.ImagePlus;
import ij.IJ;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.plugin.filter.Analyzer;
import ij.measure.ResultsTable;
import ij.measure.Measurements;

import java.awt.*;
import java.util.*;

public class Geometry {

    public double epsilon= 1e-9f;
    public Geom geo = new Geom();

    /**
     * @param im input image
     * @param skeleton its pre-computed skeleton
     */
    public Geometry(ImagePlus im, ImagePlus skeleton) {
        im.getProcessor().autoThreshold();
        IJ.run(im, "Create Selection", "");
        ResultsTable rt = new ResultsTable();
        Analyzer.setResultsTable(rt);
        Analyzer an;

        an = new Analyzer(skeleton, Measurements.AREA | Measurements.AREA_FRACTION | Measurements.RECT, rt);
        rt.reset(); an.measure();
        geo.length = ((rt.getValue("%Area", 0) / 100) * rt.getValue("Area", 0));

        an = new Analyzer(im, Measurements.AREA | Measurements.CENTER_OF_MASS | Measurements.RECT, rt);
        rt.reset(); an.measure();
        geo.area = rt.getValue("Area", 0);
        geo.width = Math.max(rt.getValue("Width", 0), epsilon);
        geo.height = Math.max(rt.getValue("Height", 0), epsilon);
        geo.widthHeightRatio = geo.width / geo.height;
        geo.bX = rt.getValue("BX", 0);
        geo.bY = rt.getValue("BY", 0);
        geo.comX = (rt.getValue("XM", 0) - geo.bX) / geo.width;
        geo.comY = (rt.getValue("YM", 0) - geo.bY) / geo.height;
        geo.yMid = rt.getValue("BY", 0);
        geo.xMid = rt.getValue("XM", 0);
        im.close();
        skeleton.close();
    }

    public ImagePlus overlay(ImagePlus im){
        //Create rectangles overlay
        Roi roi = new Roi((geo.xMid - 0.5 * geo.width), geo.yMid, geo.width, geo.height);
        roi.setStrokeColor(Color.blue);
        roi.setStrokeWidth(5);
        Overlay overlay = new Overlay(roi);
        im.setOverlay(overlay);
        im = im.flatten();
        roi = new Roi(geo.comX - 5, geo.comY - 5, 10, 10);
        roi.setStrokeColor(Color.red);
        roi.setStrokeWidth(5);
        overlay = new Overlay(roi);
        im.setOverlay(overlay);
        im = im.flatten();
        // IJ.save(im, dirParam.getAbsolutePath()+"/"+baseName+"_geometry.tiff");
        return im;
    }

}
