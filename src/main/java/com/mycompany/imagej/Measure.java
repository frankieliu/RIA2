package com.mycompany.imagej;

import ij.ImagePlus;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;

public class Measure {

    // https://imagej.nih.gov/ij/developer/source/ij/measure/Measurements.java.html

    class Measurements {
        public static final int AREA = 1, MEAN = 2, STD_DEV = 4, MODE = 8, MIN_MAX = 16,
                CENTROID = 32, CENTER_OF_MASS = 64, PERIMETER = 128, LIMIT = 256, RECT = 512,
                LABELS = 1024, ELLIPSE = 2048, INVERT_Y = 4096, CIRCULARITY = 8192,
                SHAPE_DESCRIPTORS = 8192, FERET = 16384, INTEGRATED_DENSITY = 0x8000,
                MEDIAN = 0x10000, SKEWNESS = 0x20000, KURTOSIS = 0x40000, AREA_FRACTION = 0x80000,
                SLICE = 0x100000, STACK_POSITION = 0x100000, SCIENTIFIC_NOTATION = 0x200000,
                ADD_TO_OVERLAY = 0x400000, NaN_EMPTY_CELLS = 0x800000;
    }

    class Results {
        public static final int AREA = 0, MEAN = 1, STD_DEV = 2, MODE = 3, MIN = 4, MAX = 5,
                X_CENTROID = 6, Y_CENTROID = 7, X_CENTER_OF_MASS = 8, Y_CENTER_OF_MASS = 9,
                PERIMETER = 10, ROI_X = 11, ROI_Y = 12, ROI_WIDTH = 13, ROI_HEIGHT = 14,
                MAJOR = 15, MINOR = 16, ANGLE = 17, CIRCULARITY = 18, FERET = 19,
                INTEGRATED_DENSITY = 20, MEDIAN = 21, SKEWNESS = 22, KURTOSIS = 23,
                AREA_FRACTION = 24, RAW_INTEGRATED_DENSITY = 25, CHANNEL = 26, SLICE = 27, FRAME = 28,
                FERET_X = 29, FERET_Y = 30, FERET_ANGLE = 31, MIN_FERET = 32, ASPECT_RATIO = 33,
                ROUNDNESS = 34, SOLIDITY = 35, MIN_THRESHOLD = 36, MAX_THRESHOLD = 37, LAST_HEADING = 37;

        private static final String[] defaultHeadings = {"Area", "Mean", "StdDev", "Mode", "Min", "Max",
                "X", "Y", "XM", "YM", "Perim.", "BX", "BY", "Width", "Height", "Major", "Minor", "Angle",
                "Circ.", "Feret", "IntDen", "Median", "Skew", "Kurt", "%Area", "RawIntDen", "Ch", "Slice", "Frame",
                "FeretX", "FeretY", "FeretAngle", "MinFeret", "AR", "Round", "Solidity", "MinThr", "MaxThr"};
    }

    static {
        AREA = 1, MEAN = 2, STD_DEV = 4, MODE = 8, MIN_MAX = 16,
                CENTROID = 32, CENTER_OF_MASS = 64, PERIMETER = 128, LIMIT = 256, RECT = 512,
                LABELS = 1024, ELLIPSE = 2048, INVERT_Y = 4096, CIRCULARITY = 8192,
                SHAPE_DESCRIPTORS = 8192, FERET = 16384, INTEGRATED_DENSITY = 0x8000,
                MEDIAN = 0x10000, SKEWNESS = 0x20000, KURTOSIS = 0x40000, AREA_FRACTION = 0x80000,
                SLICE = 0x100000, STACK_POSITION = 0x100000, SCIENTIFIC_NOTATION = 0x200000,
                ADD_TO_OVERLAY = 0x400000, NaN_EMPTY_CELLS = 0x800000;

        public static final int AREA = 0, MEAN = 1, STD_DEV = 2, MODE = 3, MIN = 4, MAX = 5,
                X_CENTROID = 6, Y_CENTROID = 7, X_CENTER_OF_MASS = 8, Y_CENTER_OF_MASS = 9,
                PERIMETER = 10, ROI_X = 11, ROI_Y = 12, ROI_WIDTH = 13, ROI_HEIGHT = 14,
                MAJOR = 15, MINOR = 16, ANGLE = 17, CIRCULARITY = 18, FERET = 19,
                INTEGRATED_DENSITY = 20, MEDIAN = 21, SKEWNESS = 22, KURTOSIS = 23,
                AREA_FRACTION = 24, RAW_INTEGRATED_DENSITY = 25, CHANNEL = 26, SLICE = 27, FRAME = 28,
                FERET_X = 29, FERET_Y = 30, FERET_ANGLE = 31, MIN_FERET = 32, ASPECT_RATIO = 33,
                ROUNDNESS = 34, SOLIDITY = 35, MIN_THRESHOLD = 36, MAX_THRESHOLD = 37, LAST_HEADING = 37;

    }


    public static void measure(ImagePlus im) {
        ResultsTable rt = new ResultsTable();
        Analyzer an = new Analyzer(im, Measurements.MODE | Measurements.MEAN | Measurements.MIN_MAX, rt);
        Analyzer.setResultsTable(rt);
        rt.reset();
        an.measure();
    }

    public static void main(String[] args) {
        int[] a = {
                Measurements.ADD_TO_OVERLAY,
                Measurements.ALL_STATS,
                Measurements.AREA,
                Measurements.AREA_FRACTION,
                Measurements.CENTER_OF_MASS,
                Measurements.CENTROID,
                Measurements.CIRCULARITY,
                Measurements.ELLIPSE,
                Measurements.FERET,
                Measurements.INTEGRATED_DENSITY,
                Measurements.INVERT_Y,
                Measurements.KURTOSIS,
                Measurements.LABELS,
                Measurements.LIMIT,
                Measurements.MAX_STANDARDS,
                Measurements.MEAN,
                Measurements.MEDIAN,
                Measurements.MIN_MAX,
                Measurements.MODE,
                Measurements.NaN_EMPTY_CELLS,
                Measurements.PERIMETER,
                Measurements.RECT,
                Measurements.SCIENTIFIC_NOTATION,
                Measurements.SHAPE_DESCRIPTORS,
                Measurements.SKEWNESS,
                Measurements.SLICE,
                Measurements.STACK_POSITION,
                Measurements.STD_DEV,
        };
        String[] as = {
            "Measurements.ADD_TO_OVERLAY",
                "Measurements.ALL_STATS",
                "Measurements.AREA",
                "Measurements.AREA_FRACTION",
                "Measurements.CENTER_OF_MASS",
                "Measurements.CENTROID",
                "Measurements.CIRCULARITY",
                "Measurements.ELLIPSE",
                "Measurements.FERET",
                "Measurements.INTEGRATED_DENSITY",
                "Measurements.INVERT_Y",
                "Measurements.KURTOSIS",
                "Measurements.LABELS",
                "Measurements.LIMIT",
                "Measurements.MAX_STANDARDS",
                "Measurements.MEAN",
                "Measurements.MEDIAN",
                "Measurements.MIN_MAX",
                "Measurements.MODE",
                "Measurements.NaN_EMPTY_CELLS",
                "Measurements.PERIMETER",
                "Measurements.RECT",
                "Measurements.SCIENTIFIC_NOTATION",
                "Measurements.SHAPE_DESCRIPTORS",
                "Measurements.SKEWNESS",
                "Measurements.SLICE",
                "Measurements.STACK_POSITION",
                "Measurements.STD_DEV",
        };
        int[] b = {
                ResultsTable.ANGLE,                   
                ResultsTable.AREA,
                ResultsTable.AREA_FRACTION,
                ResultsTable.ASPECT_RATIO,
                ResultsTable.AUTO_FORMAT,
                ResultsTable.CHANNEL,
                ResultsTable.CIRCULARITY,
                ResultsTable.COLUMN_IN_USE,
                ResultsTable.COLUMN_NOT_FOUND,
                ResultsTable.FERET,
                ResultsTable.FERET_ANGLE,
                ResultsTable.FERET_X,
                ResultsTable.FERET_Y,
                ResultsTable.FRAME,
                ResultsTable.INTEGRATED_DENSITY,
                ResultsTable.KURTOSIS,
                ResultsTable.LAST_HEADING,
                ResultsTable.MAJOR,
                ResultsTable.MAX,
                ResultsTable.MAX_COLUMNS,
                ResultsTable.MAX_THRESHOLD,
                ResultsTable.MEAN,
                ResultsTable.MEDIAN,
                ResultsTable.MIN,
                ResultsTable.MIN_FERET,
                ResultsTable.MIN_THRESHOLD,
                ResultsTable.MINOR,
                ResultsTable.MODE,
                ResultsTable.PERIMETER,
                ResultsTable.RAW_INTEGRATED_DENSITY,
                ResultsTable.ROI_HEIGHT,
                ResultsTable.ROI_WIDTH,
                ResultsTable.ROI_X,
                ResultsTable.ROI_Y,
                ResultsTable.ROUNDNESS,
                ResultsTable.SKEWNESS,
                ResultsTable.SLICE,
                ResultsTable.SOLIDITY,
                ResultsTable.STD_DEV,
                ResultsTable.TABLE_FULL,
                ResultsTable.X_CENTER_OF_MASS,
                ResultsTable.X_CENTROID,
                ResultsTable.Y_CENTER_OF_MASS,
                ResultsTable.Y_CENTROID
        };
        String[] bs = {
            "ResultsTable.ANGLE",
            "ResultsTable.AREA",
            "ResultsTable.AREA_FRACTION",
            "ResultsTable.ASPECT_RATIO",
            "ResultsTable.AUTO_FORMAT",
            "ResultsTable.CHANNEL",
            "ResultsTable.CIRCULARITY",
            "ResultsTable.COLUMN_IN_USE",
            "ResultsTable.COLUMN_NOT_FOUND",
            "ResultsTable.FERET",
            "ResultsTable.FERET_ANGLE",
            "ResultsTable.FERET_X",
            "ResultsTable.FERET_Y",
            "ResultsTable.FRAME",
            "ResultsTable.INTEGRATED_DENSITY",
            "ResultsTable.KURTOSIS",
            "ResultsTable.LAST_HEADING",
            "ResultsTable.MAJOR",
            "ResultsTable.MAX",
            "ResultsTable.MAX_COLUMNS",
            "ResultsTable.MAX_THRESHOLD",
            "ResultsTable.MEAN",
            "ResultsTable.MEDIAN",
            "ResultsTable.MIN",
            "ResultsTable.MIN_FERET",
            "ResultsTable.MIN_THRESHOLD",
            "ResultsTable.MINOR",
            "ResultsTable.MODE",
            "ResultsTable.PERIMETER",
            "ResultsTable.RAW_INTEGRATED_DENSITY",
            "ResultsTable.ROI_HEIGHT",
            "ResultsTable.ROI_WIDTH",
            "ResultsTable.ROI_X",
            "ResultsTable.ROI_Y",
            "ResultsTable.ROUNDNESS",
            "ResultsTable.SKEWNESS",
            "ResultsTable.SLICE",
            "ResultsTable.SOLIDITY",
            "ResultsTable.STD_DEV",
            "ResultsTable.TABLE_FULL",
            "ResultsTable.X_CENTER_OF_MASS",
            "ResultsTable.X_CENTROID",
            "ResultsTable.Y_CENTER_OF_MASS",
            "ResultsTable.Y_CENTROID"
        };

        for (int i = 0; i < a.length; i++) {
            System.out.println(as[i] + " " + a[i]);
        }
        System.out.println("--------------------------------------");
        for (int i = 0; i < b.length; i++) {
            System.out.println(bs[i] + " " + b[i]);
        }
    }
}
