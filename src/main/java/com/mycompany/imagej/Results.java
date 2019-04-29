package com.mycompany.imagej;

import ij.IJ;
import ij.ImagePlus;

import java.io.File;

public class Results {

    public File dirAll;
    public String csvOut;
    public float scalePix;
    public float scaleCm;
    public boolean blackRoots;
    public float rootMinSize;
    public boolean verbatim;
    public boolean saveImages;
    public boolean saveTips;
    public boolean saveTPS;
    public boolean saveEFD;
    public boolean saveShapes;
    public String shapeFolder;

    /**
     * Constructor
     *
     * @param dirAll   = File containing the different images
     * @param csvOut   = where to save csv file
     * @param scalePix = scale, in pixels
     * @param scaleCm  = scale, in cm
     */
    Results(File dirAll,
            String csvOut,
            float scalePix,
            float scaleCm,
            boolean blackRoots,
            float rootMinSize,
            boolean verbatim,
            boolean saveImages,
            boolean saveTips,
            boolean saveTPS,
            boolean saveEFD,
            boolean saveShapes,
            String shapeFolder
    ) {
        // Set up the different variables
        this.dirAll = dirAll;
        this.csvOut = csvOut;
        this.scalePix = scalePix;
        this.scaleCm = scaleCm;
        this.blackRoots = blackRoots;
        this.rootMinSize = rootMinSize;
        this.verbatim = verbatim;
        this.saveImages = saveImages;
        this.saveTips = saveTips;
        this.saveTPS = saveTPS;
        this.saveEFD = saveEFD;
        this.saveShapes = saveShapes;
        this.shapeFolder = shapeFolder;

        // Analyze the plants
        analyze();
    }

    /**
     * @param dirAll directory
     * @return return File[] of images
     */
    public File[] imageFiles(File dirAll) {
        File[] images;
        // Get all the images files in the directory
        images = dirAll.listFiles((file) -> {
            String name = file.getName();
            return name.toLowerCase().endsWith(".jpg")
                    || name.toLowerCase().endsWith(".tiff")
                    || name.toLowerCase().endsWith(".tif")
                    || name.toLowerCase().endsWith(".jpeg")
                    || name.toLowerCase().endsWith(".png");
        });
        return images;
    }

    /**
     * Perform the analysis of all the images
     */
    public void analyze() {

        ImagePlus nextImage;
        File[] images = imageFiles(dirAll);

        long startD = System.currentTimeMillis();
        int counter = 0;

        // Navigate the different images in the time series
        int percent = 0;
        double progression;
        for (int i = 0; i < images.length; i++) {

            long startD1 = System.currentTimeMillis();

            // Open the image
            nextImage = IJ.openImage(images[i].getAbsolutePath());

            progression = ((double) i / images.length) * 100;
            if (progression > percent) {
                IJ.log(percent + " % of the rsml files converted. " + (images.length - i) + " files remaining.");
                System.out.println(percent + " % of the rsml files converted. " + (images.length - i) + " files remaining.");
                percent = percent + 5;
            }

            // Reset the ROI to the size of the image. This is done to prevent previously drawn ROI (ImageJ keep them in memory) to empede the analysis
            nextImage.setRoi(0, 0, nextImage.getWidth(), nextImage.getHeight());

            // Process the name to retrieve the experiment information
            String baseName = images[i].getName();
            String fullName = images[i].getAbsolutePath();

            // Measure the image
            try {
                RootAnalysis ra = new RootAnalysis(nextImage);
                // sendAnalysisToCSV(nextImage.getTitle(), nextImage.getWidth(), nextImage.getHeight(), startD1, System.currentTimeMillis());
            } catch (Exception e) {
                System.out.println("I am at the exception handling routine");
                e.printStackTrace(System.out);
                System.out.println(e.toString());
            }
            // Close the current image
            nextImage.flush();
            nextImage.close();
            counter++;

            IJ.log("Loading the image " + (i + 1) + "/" + images.length + " in " + (startD1 - System.currentTimeMillis()));

        }

        // Compute the time taken for the analysis
        long endD = System.currentTimeMillis();
        IJ.log("------------------------");
        IJ.log(counter + " images analyzed in " + (endD - startD) + " ms");
    }

}
