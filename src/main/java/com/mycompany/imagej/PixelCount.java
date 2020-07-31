    package com.mycompany.imagej;

    import ij.ImagePlus;
    import ij.gui.OvalRoi;
    import ij.gui.Overlay;
    import ij.gui.Roi;
    import ij.process.ImageProcessor;

    import com.google.gson.JsonObject;
    import java.awt.*;
    import java.util.*;

    public class PixelCount {

        class Point {
            int w;
            int h;

            Point(int w0, int h0) {
                w = w0;
                h = h0;
            }
        }

        public String name;
        public ImagePlus im;
        public ImageProcessor ip;
        public ImagePlus ori;
        public java.util.List<Point> tip;

        JsonObject jobj = new JsonObject();

        PixelCount(ImagePlus im0, ImagePlus ori0) {
            name = "Pixel Count";
            im = im0.duplicate();
            ori = ori0.duplicate();

            ip = im.getProcessor();
            ip.autoThreshold();
            //ip.invert();

            tip = new ArrayList<>();
            int nTips = 0;
            for (int w = 0; w < ip.getWidth(); w++) {
                for (int h = 0; h < ip.getHeight(); h++) {
                    if (ip.get(w, h) > 125) {
                        int n = Line.nNeighbours(ip, w, h);
                        if (n == 1) {
                            nTips += 1;
                            tip.add(new Point(w, h));

                        }
                    }
                }
            }
            jobj.addProperty("nTips", nTips);
        }

        public ImagePlus overlay() {
            for (Point el : tip) {
                Roi apex = new OvalRoi(el.w, el.h, 10, 10);
                apex.setStrokeColor(Color.blue);
                apex.setStrokeWidth(5);
                Overlay over = new Overlay(apex);
                ori.setOverlay(over);
                ori = ori.flatten();
            }
            ori.setTitle("Pixel Count");
            return ori;
        }

    }