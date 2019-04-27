package com.mycompany.imagej;
import java.util.*;

public class PrintParam {
    public PrintParam() {
    }

    private String baseName;
    public String getBaseName() { return baseName; }
    public void setBaseName(String baseName) { this.baseName = baseName; }

    private Map<String, Double> h;
    public Map<String, Double> getH() { return h; }
    public void setH(Map<String, Double> h) { this.h = h; }
    public void setH() { this.h = new LinkedHashMap<>(); }
    public void putAllH(Map<String, Double> m) { this.h.putAll(m); }

    /**
     * Send parameters data to an CSV file
     */

    private void sendParametersToCSV(){
        String toPrint = this.baseName.replaceAll(",", "-");
        /*      for(int i = 0; i < geom.length; i++){
            toPrint = toPrint.concat(","+geom[i]);
        }
        pwParam.println(toPrint);
        pwParam.flush();
        */
    }

}
