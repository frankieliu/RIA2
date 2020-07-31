package com.mycompany.imagej;

import com.google.gson.JsonObject;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.OvalRoi;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import ij.process.ImageProcessor;

public class DirectionalityAnalysis {
    public String name;
    public ImagePlus im;
    public ImageProcessor ip;
    public JsonObject jobj = new JsonObject();

    DirectionalityAnalysis(ImagePlus im0, Geom geo){
        name = "Directionality Analysis";
        im = im0.duplicate();

        ip = im.getProcessor();
        ip.autoThreshold();
        ip.setRoi(new OvalRoi(geo.xMid - 0.45 * geo.width, geo.yMid, 0.9 * geo.width, 0.9 * geo.height));
        im.setProcessor(ip);
        Directionality dnlty = new Directionality();

        // Set fields and settings
        int nbins = 10;
        int binStart = -90;

        ImagePlus img = new ImagePlus();

        img.setProcessor(ip.duplicate().rotateLeft());
        dnlty.setImagePlus(img);

        dnlty.setMethod(Directionality.AnalysisMethod.LOCAL_GRADIENT_ORIENTATION);
        dnlty.setBinNumber(nbins);
        dnlty.setBinStart(binStart);
        dnlty.setBuildOrientationMapFlag(true);

        // Do calculation
        dnlty.computeHistograms();
        ResultsTable rs = dnlty.displayResultsTable();
        double angle = 0;
        double tot = 0;
        for(int k = 0; k < rs.getCounter(); k++){
            for(int l = 0; l < (rs.getValueAsDouble(1, k)); l++){
                double direct = rs.getValueAsDouble(0, k);
                double prop = rs.getValueAsDouble(1, k);
                angle += Math.abs(direct)*prop;
                tot += prop;
            }
        }
        rs.reset();

        jobj.addProperty("angle proportion", angle / tot);
    }

}
