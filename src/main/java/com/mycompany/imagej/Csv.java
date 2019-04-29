package com.mycompany.imagej;

import java.io.PrintWriter;

public class Csv {
    public PrintWriter pwParam, pwTPS, pwEFD, pwAnalysis;
    public String baseName;
    public int nCoord;
    public int nSlices;

    Csv(String baseName, int nCoord, int nSlices) {
        this.baseName = baseName;
        this.nCoord = nCoord;
        this.nSlices = nSlices;
    }
    /**
     * Print Parameters CSV header
     */
    public void printAnalysisCSVHeader(){
        String toPrint = "image";
        toPrint = toPrint.concat(",width,height,time_start, time_end");
        pwAnalysis.println(toPrint);
        pwAnalysis.flush();
    }

    /**
     * Print Parameters CSV header
     */
    private void sendAnalysisToCSV(String image, int width, int height, long time1, long time2){
        String toPrint = image+","+width+","+height+","+time1+","+time2;
        pwAnalysis.println(toPrint);
        pwAnalysis.flush();
    }

    /**
     * Print Parameters CSV header
     */
    private void printParamCSVHeader(){
        String toPrint = "image";
        toPrint = toPrint.concat(",diam_max,diam_mean,diam_mode");
        toPrint = toPrint.concat(",length,area,width,depth,width_depth_ratio,com_x,com_y");
        toPrint = toPrint.concat(",ellips_025,ellips_050,ellips_075,ellips_100");
        toPrint = toPrint.concat(",rect_020,rect_040,rect_060,rect_080");
        toPrint = toPrint.concat(",directionality");
        toPrint = toPrint.concat(",tip_count");

        for(int i = 0; i < nSlices; i++) toPrint = toPrint.concat(",cross_hori_"+i+"_mean,cross_hori_"+i+"_max");
        toPrint = toPrint.concat(",cross_vert_mean,cross_vert_max,convexhull");
        for(int i = 0; i < nCoord * 2; i++) toPrint = toPrint.concat(",coord_x"+i);
        for(int i = 0; i < nCoord; i++) toPrint = toPrint.concat(",diff_x"+i);
        for(int i = 0; i < nCoord; i++) toPrint = toPrint.concat(",cumul_x"+i);

        pwParam.println(toPrint);
        pwParam.flush();
    }

    /**
     * send Shape Data To CSV
     */
    private void sendShapeDataToTPS(float[] coordX, float[] coordY){

        pwTPS.println("ID="+baseName);
        pwTPS.println("LM="+(nCoord*2));

        for(int i = 0; i < coordX.length; i++) pwTPS.println(coordX[i]+" "+coordY[i]);
        pwTPS.flush();
    }

    /**
     * Print EFD CSV header
     */
    private void printEFDCSVHeader(){
        pwEFD.println("image, index, ax, ay, bx, by, efd");
        pwEFD.flush();
    }
    /**
     * Send EFD data to an CSV file
     */
    private void sendEFDDataToCSV(int i, double ax, double ay, double bx, double by, double efd){

        pwEFD.println(baseName +","+ i +","+ ax +","+ ay +","+ bx+","+ by+","+ efd);

        pwEFD.flush();
    }

}
