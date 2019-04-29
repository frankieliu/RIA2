package com.mycompany.imagej;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.OvalRoi;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import ij.process.ImageProcessor;

public class DirectionalityAnalysis {

    public float area, depth, width, Ymid, Xmid;
    public float[] params = new float[1];
    public ImagePlus im;
    public ImageProcessor ip;
    public ResultsTable rt;

    DirectionalityAnalysis(ImagePlus im){
        this.im = im.duplicate();
        ip = im.getProcessor();
        ip.autoThreshold();
        ip.setRoi(new OvalRoi(Xmid - 0.45 * width, Ymid, 0.9 * width, 0.9 * depth));
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

        params[0] = (float) (angle / tot);
        rs.reset();

        im.close();
    }

}
