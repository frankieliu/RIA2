package com.mycompany.imagej;

import ij.IJ;
import ij.ImagePlus;
import ij.measure.Calibration;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.ParticleAnalyzer;
import ij.process.BinaryProcessor;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

public class Preprocess {

    // Default from RIAInterface
    public double scalePix = 2020f;
    public double scaleCm = 23.5f;
    public double rootMinSize = 50;
    public boolean blackRoots;
    // Resize the image to speed up the analysis (resize to width = 800)
    public double scale = scalePix/scaleCm;

    public ImagePlus im;
    public ImagePlus skel;

    Preprocess(ImagePlus im0, boolean blackRoots) {
        im = im0.duplicate();
        ImageProcessor ip = im.getProcessor();
        ResultsTable rt = new ResultsTable();

        // Convert to 8bit image if needed
        if (ip.getBitDepth() != 8) ip = ip.convertToByte(true);

        // If the root is white on black, then invert the image
        this.blackRoots = blackRoots;
        if (!blackRoots) {
            ip.invert();
            System.out.println("root inverted");
        }

        // Threshold the image (used to be Otsu
        ip.setAutoThreshold("Default");
        im.setProcessor(ip);

        // Remove small particles in the image
        ParticleAnalyzer pa;
        pa = new ParticleAnalyzer(ParticleAnalyzer.SHOW_MASKS, Measurements.AREA, rt, rootMinSize, 10e9, 0, 1);
        pa.analyze(im);

        // Get the mask from the ParticuleAnalyser
        im = IJ.getImage();
        im.hide(); // Hide the mask, we do not want to display it.
        ip = im.getProcessor();

        // Reset calibration
        // This is needed in case the image was previously calibrated using ImageJ.
        // TIFF images store the calibration...
        Calibration calDefault = new Calibration();
        calDefault.setUnit("px");
        calDefault.pixelHeight = 1;
        calDefault.pixelWidth = 1;
        im.setCalibration(calDefault);

        // Create skeleton
        skel = new ImagePlus();
        BinaryProcessor bp = new BinaryProcessor(new ByteProcessor(ip, true));
        //for(int i = 0; i < 5; i++) bp.smooth(); // Smooth the image for a better skeletonisation
        bp.autoThreshold();
        bp.skeletonize();
        //bp.invert();
        skel.setProcessor(bp);

        // if(saveImages) IJ.save(skel, dirParam.getAbsolutePath()+"/"+baseName+"_skeleton.tiff");

        skel.setRoi(0, 0, ip.getWidth(), ip.getHeight());
        im.setRoi(0, 0, ip.getWidth(), ip.getHeight());

        // skel.show(); im.show();

    }

    public boolean isBlackRoots() { return blackRoots; }
    public void setBlackRoots(boolean blackRoots) { this.blackRoots = blackRoots; }
}
