package com.mycompany.imagej;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import ij.ImagePlus;
import ij.io.FileSaver;

public class RootAnalysis {

    public JsonObject jobj = new JsonObject();

    public Preprocess preprocess;
    public Diameter diameter;
    public Tissue tissue;
    public Rotate rotate;
    public Geometry geometry;
    public DensityEllipses densityEllipses;
    public DensityRectangles densityRectangles;
    public DirectionalityAnalysis directionalityAnalysis;
    public PixelCount pixelCount;
    public PixelProfile pixelProfile;
    public ConvexHull convexHull;
    public Coordinates coordinates;
    public DepthProfile depthProfile;

	RootAnalysis(ImagePlus im){
	           boolean blackRoots = true;
        preprocess = new Preprocess(im, blackRoots);
        // preprocess.im.show(); preprocess.skel.show();

        diameter = new Diameter(preprocess.im, preprocess.skel);
        jobj.add(diameter.name, diameter.jobj);
        // diameter.overlay().show();

        tissue = new Tissue(preprocess.im, preprocess.skel);
        jobj.add(tissue.name, tissue.jobj);

        rotate = new Rotate(preprocess.im, preprocess.skel);
        jobj.add(rotate.name, rotate.jobj);
        // rotate.im.show(); rotate.skel.show();

        geometry = new Geometry(preprocess.im, preprocess.skel);
        jobj.add(geometry.name, geometry.jobj);
        // geometry.overlay().show();

        densityEllipses = new DensityEllipses(preprocess.im, geometry.geo);
        jobj.add(densityEllipses.name, densityEllipses.jobj);
        // densityEllipses.overlay().show();

        densityRectangles = new DensityRectangles(preprocess.im, geometry.geo);
        jobj.add(densityRectangles.name, densityRectangles.jobj);
        // densityRectangles.overlay().show();

        directionalityAnalysis = new DirectionalityAnalysis(preprocess.im, geometry.geo);
        jobj.add(directionalityAnalysis.name, directionalityAnalysis.jobj);

        pixelCount = new PixelCount(preprocess.skel, preprocess.im);
        jobj.add(pixelCount.name, pixelCount.jobj);
        // pixelCount.overlay().show();

        pixelProfile = new PixelProfile(preprocess.skel, geometry.geo);
        jobj.add(pixelProfile.name, pixelProfile.jobj);

        convexHull = new ConvexHull(preprocess.im);
        jobj.add(convexHull.name, convexHull.jobj);
        // convexHull.overlay().show();

        coordinates = new Coordinates(preprocess.im, geometry.geo);
        jobj.add(coordinates.name, coordinates.jobj);
        // coordinates.overlay().show();

        depthProfile = new DepthProfile(preprocess.im, preprocess.skel);
        jobj.add(depthProfile.name, depthProfile.jobj);

    }
}
