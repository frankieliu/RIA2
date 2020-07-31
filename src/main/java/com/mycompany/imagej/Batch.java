package com.mycompany.imagej;

import com.google.devtools.common.options.OptionsParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ij.IJ;
import ij.ImagePlus;
import ij.io.FileSaver;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

public class Batch {
    public static BatchOptions options;
    public static RootAnalysis ra;
    
    public static void main(String... args) {
        OptionsParser parser = OptionsParser.newOptionsParser(BatchOptions.class);
        parser.parseAndExitUponError(args);
        options = parser.getOptions(BatchOptions.class);
        if (options.inputFile.isEmpty() || options.anInt < 0 || options.aListStrings.isEmpty()) {
            printUsage(parser);
            return;
        }
        File fi = new File(options.inputFile);

        Path p = FileSystems.getDefault().getPath(options.inputFile);
        System.out.println("Directory: " + p.getParent());

        ImagePlus im = IJ.openImage(fi.getAbsolutePath());
        ra = new RootAnalysis(im);

        // save the overlays
        ArrayList<String> image_names = new ArrayList<>();
        ArrayList<ImagePlus> images = new ArrayList<>();
        image_names.add(ra.preprocess.name); images.add(ra.preprocess.im);
        image_names.add(ra.preprocess.name + " Skeleton"); images.add(ra.preprocess.skel);
        image_names.add(ra.diameter.name); images.add(ra.diameter.overlay());
        image_names.add(ra.rotate.name); images.add(ra.rotate.im);
        image_names.add(ra.rotate.name + " Skeleton"); images.add(ra.rotate.skel);
        image_names.add(ra.geometry.name); images.add(ra.geometry.overlay());
        image_names.add(ra.densityEllipses.name); images.add(ra.densityEllipses.overlay());
        image_names.add(ra.densityRectangles.name); images.add(ra.densityRectangles.overlay());
        image_names.add(ra.pixelCount.name); images.add(ra.pixelCount.overlay());
        image_names.add(ra.convexHull.name); images.add(ra.convexHull.overlay());
        image_names.add(ra.coordinates.name); images.add(ra.coordinates.overlay());

        for(int i = 0; i < image_names.size(); i++){
            new FileSaver(images.get(i)).saveAsPng(p.getParent() + "/" + image_names.get(i) + ".png");
            System.out.println("Saving " + image_names.get(i));
        }

        // save the json data
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            Writer writer = new FileWriter(p.getParent() + "/" + "jobj.json");
            gson.toJson(ra.jobj, writer);
            writer.flush();
        } catch(Exception e) {
        }
        System.out.println(gson.toJson(ra.jobj));

    }

    private static void printUsage(OptionsParser parser) {
        System.out.println("Usage: java -jar Batch.jar OPTIONS");
        System.out.println(parser.describeOptions(Collections.<String, String>emptyMap(),
                OptionsParser.HelpVerbosity.LONG));
    }

}
