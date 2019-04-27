package com.mycompany.imagej;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;

import java.awt.*;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class GeometryOld {

    public static Boolean verbatim;
    public static Boolean saveImages;
    public static File dirParam;
    public static String baseName;
    public static double scale;
    public static double epsilon;
    public static Map<String,Double> params;

    static {
        verbatim = false;
        saveImages = true;
        dirParam = null;
        baseName = "";
        scale = 1.0;
        epsilon = 1e-9f;
        params = new LinkedHashMap<>();
    }

    /**
     * @param im input image
     * @param skeleton its pre-computed skeleton
     */
    public static void getGeometry(ImagePlus im, ImagePlus skeleton){
        if(verbatim) IJ.log("- Get geometry");
		
        im.getProcessor().autoThreshold(); 
        IJ.run(im, "Create Selection", "");

        ResultsTable rt = new ResultsTable();
        Analyzer.setResultsTable(rt);
        rt.reset();     

        Analyzer an = new Analyzer(skeleton, Measurements.AREA | Measurements.AREA_FRACTION | Measurements.RECT, rt);
        rt.reset();        
        an.measure();

        // Area
        params.put("length",
                   ((rt.getValue("%Area", 0) / 100 )* rt.getValue("Area", 0)) / scale);

        an = new Analyzer(im,
                          Measurements.AREA |
                          Measurements.CENTER_OF_MASS |
                          Measurements.RECT, rt);
        rt.reset();        
        an.measure();
        
        // Area
        params.put("area", rt.getValue("Area", 0) / scale);
        
        // Width
        double width = Math.max(rt.getValue("Width", 0), epsilon);
        params.put("width", width / scale);
	    
        // Height
        double depth = Math.max(rt.getValue("Height", 0), epsilon);
        params.put("depth", depth / scale);
	    
        // Ratio
        params.put("width/depth", width / depth);
	    
        //Center of Mass
        double bX = rt.getValue("BX", 0);
        double bY = rt.getValue("BY", 0);

        double comX = (rt.getValue("XM", 0) - bX) / width;
        params.put("comX", comX);
        
        double comY  = (rt.getValue("YM", 0) - bY)  / depth;
        params.put("comY", comY);

        double yMid = rt.getValue("BY", 0);
        double xMid = rt.getValue("XM", 0);
        params.put("xMid", xMid);
        params.put("yMid", yMid);

        //Create rectangles overlay
        if (saveImages){
            Roi roi = new Roi((xMid - 0.5 * width), yMid, width, depth);
            roi.setStrokeColor(Color.blue);
            roi.setStrokeWidth(5);
            Overlay overlay = new Overlay(roi); 
            im.setOverlay(overlay); 
            im = im.flatten(); 
            roi = new Roi(rt.getValue("XM", 0)-5, rt.getValue("YM", 0)-5, 10, 10);
            roi.setStrokeColor(Color.red);
            roi.setStrokeWidth(5);
            overlay = new Overlay(roi);
            im.setOverlay(overlay); 
            im = im.flatten(); 
            IJ.save(im, dirParam.getAbsolutePath()+"/"+baseName+"_geometry.tiff");
        }
	    
        im.close();
        skeleton.close();
    }
}
