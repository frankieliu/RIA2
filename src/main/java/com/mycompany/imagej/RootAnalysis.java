package com.mycompany.imagej;

import ij.ImagePlus;

public class RootAnalysis {
	RootAnalysis(ImagePlus im){
	    boolean blackRoots = true;
		Preprocess pp = new Preprocess(im, blackRoots);
        Diameter dia = new Diameter(pp.im, pp.skel);
        Tissue ts = new Tissue(pp.im, pp.skel);
        Rotate.getVolume(pp.im);
        Geometry gy = new Geometry(pp.im, pp.skel);
        DensityEllipses de = new DensityEllipses(pp.im, gy.geo);
        DensityRectangles dr = new DensityRectangles(pp.im, gy.geo);
        DirectionalityAnalysis da = new DirectionalityAnalysis(pp.im, gy.geo);
        PixelCount pc = new PixelCount(pp.skel, pp.im);
        PixelProfile px = new PixelProfile(pp.skel, gy.geo);
        ConvexHull ch = new ConvexHull(pp.im);
        Coordinates co = new Coordinates(pp.im, gy.geo);
        DepthProfile dp = new DepthProfile(pp.im, pp.skel);
	}
}
