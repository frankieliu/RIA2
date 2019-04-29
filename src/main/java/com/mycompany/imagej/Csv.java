package com.mycompany.imagej;

import java.io.File;
import java.io.PrintWriter;

public class Csv {
    public String csvParamFolder, csvParamAnalysis;
    public String tpsFolder, efdFolder;
    public String depthAFolder, depthLFolder, depthDFolder;
    public PrintWriter pwParam, pwTPS, pwEFD, pwAnalysis;
    public PrintWriter pwDA, pwDL, pwDD;
    public String baseName;
    public int nCoord;
    public int nSlices;

    Csv(String file, String baseName, int nCoord, int nSlices) {
        csvParamFolder = file;
        csvParamAnalysis = file.substring(0, file.length()-4)+"-analysis.csv";
        tpsFolder = file.substring(0, file.length()-4)+"-shape.tps";
        efdFolder = file.substring(0, file.length()-4)+"-efd.csv";
        depthAFolder = file.substring(0, file.length()-4)+"-depthA.txt";
        depthLFolder = file.substring(0, file.length()-4)+"-depthL.txt";
        depthDFolder = file.substring(0, file.length()-4)+"-depthD.txt";
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


    public void setPrintWriters() {
        // Initialize CSV
        pwParam = Util.initializeCSV(csvParamFolder);
        pwAnalysis = Util.initializeCSV(csvParamAnalysis);
        printParamCSVHeader();
        printAnalysisCSVHeader();

        pwTPS = Util.initializeCSV(tpsFolder);
        pwEFD = Util.initializeCSV(efdFolder);
        printEFDCSVHeader();

        pwDA = Util.initializeCSV(depthAFolder);
        pwDL = Util.initializeCSV(depthLFolder);
        pwDD = Util.initializeCSV(depthDFolder);
    }

    public File dirParam(File dirAll) {
        // Create the folder structure to store the images
        File dirSave;
        dirSave = Util.createFolderStructure(dirAll.getAbsolutePath(), false, false, false, true);
        return new File(dirSave.getAbsolutePath() + "/param/");
    }

}
