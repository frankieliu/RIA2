package com.mycompany.imagej;

import ij.IJ;
import ij.ImagePlus;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.awt.*;

public class PixelProfile {
    public String name;
    public int nSlices = 30;
    public ImagePlus im;
    public JsonObject jobj = new JsonObject();

    PixelProfile(ImagePlus im0,Geom geo){
        name = "Pixel Profile";
        im = im0.duplicate();

        double sum, count, max, tot;
        int inc = (int) geo.height / nSlices;

        // The threshold sets the area fraction
        im.getProcessor().autoThreshold();

        ResultsTable rt = new ResultsTable();
        Analyzer an;

        JsonArray ja = new JsonArray();
        for(int j = 0; j < nSlices; j++){

            sum = 0;
            count = 0;
            max = 0;

            for(int i = ((j+1) * inc)+(int) geo.bY; i > j * inc; i = i-2){
                // height one slices
                Rectangle roi = new Rectangle((int) geo.bX, i, (int) geo.width, 1);
                im.setRoi(roi);
                Analyzer.setResultsTable(rt);
                rt.reset();
                an = new Analyzer(im, Measurements.AREA | Measurements.AREA_FRACTION, rt);
                an.measure();

                // tot: area that has been selected
                tot = (rt.getValue("%Area", 0)/100) * rt.getValue("Area", 0);
                if(tot >= 0){
                    sum += tot;
                    count++;
                }
                if(tot > max) max = tot;
            }

            // sum/count, measure the average area fraction within the slice
            // max, measures the max fraction within the slice
            JsonObject jo = new JsonObject();
            jo.addProperty("average", sum/count);
            jo.addProperty("max", max);
            ja.add(jo);
        }
        jobj.add("horizontal", ja);

        sum = 0;
        count = 0;
        max = 0;

        // this measures the average fraction in vertical slices
        for(int i = (int) geo.width ; i > 0; i = i-2){
            Rectangle Rect2 = new Rectangle(i, 0, 1, (int) geo.height);
            im.setRoi(Rect2);
            Analyzer.setResultsTable(rt);
            rt.reset();
            an = new Analyzer(im, Measurements.AREA | Measurements.AREA_FRACTION, rt);
            an.measure();
            tot = (rt.getValue("%Area", 0)/100) * rt.getValue("Area", 0);
            if(tot > 0){
                sum += tot;
                count++;
            }
            if(tot > max) max = tot;
        }

        JsonObject jo = new JsonObject();
        jo.addProperty("average", sum/count);
        jo.addProperty("max", max);
        ja.add(jo);
        jobj.add("vertical", jo);
    }
}
