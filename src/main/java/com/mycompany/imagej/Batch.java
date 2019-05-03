package com.mycompany.imagej;

import com.google.devtools.common.options.OptionsParser;
import ij.IJ;
import ij.ImagePlus;

import java.io.File;
import java.util.Collections;

public class Batch {
    public static BatchOptions options;
    public static void main(String... args) {
        OptionsParser parser = OptionsParser.newOptionsParser(BatchOptions.class);
        parser.parseAndExitUponError(args);
        options = parser.getOptions(BatchOptions.class);
        if (options.inputFile.isEmpty() || options.anInt < 0 || options.aListStrings.isEmpty()) {
            printUsage(parser);
            return;
        }
        File fi = new File(options.inputFile);
        ImagePlus im = IJ.openImage(fi.getAbsolutePath());
        RootAnalysis ra = new RootAnalysis(im);

    }

    private static void printUsage(OptionsParser parser) {
        System.out.println("Usage: java -jar Batch.jar OPTIONS");
        System.out.println(parser.describeOptions(Collections.<String, String>emptyMap(),
                OptionsParser.HelpVerbosity.LONG));
    }

}
