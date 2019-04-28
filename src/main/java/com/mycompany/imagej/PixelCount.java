    package com.mycompany.imagej;

    import ij.IJ;
    import ij.ImagePlus;
    import ij.gui.OvalRoi;
    import ij.gui.Overlay;
    import ij.gui.Roi;
    import ij.process.ImageProcessor;

    import java.util.List;

    public class PixelCount {

        class Point {
            int w;
            int h;

            Point(int w0, int h0) {
                w = w0;
                h = h0;
            }
        }

        List<Point> tip;
        ImagePlus im;
        ImageProcessor ip;
        ImagePlus ori;

        PixelCount(ImagePlus im0, ImagePlus ori0) {

            im = im0.duplicate();
            ori = ori0.duplicate();
            ip = im.getProcessor();
            ip.autoThreshold();
            double[] params = new params[1];
            //ip.invert();

            tip = new List<>();
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
            params[0] = nTips;
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
        }

    }