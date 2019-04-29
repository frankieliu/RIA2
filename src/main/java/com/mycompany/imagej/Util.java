package com.mycompany.imagej;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ColorProcessor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;


public final class Util {

	/**
	 * Return the sum of an array
	 * @param vect Vector<> to be summed
	 * @return the sum
	 */
	public static float sum(Vector<Float> vect){
		float sum = 0;
		for(float el: vect) sum = sum + el;
		return 	sum;
	}
	
	/**
	 * Return the sum of an array
	 * @param vect Vector<> to be summed
	 * @return the sum
	 */
	public static float sum(float[] vect){
		float sum = 0;
		for(float el: vect) sum = sum + el;
		return 	sum;
	}
	
	/**
	 * Return the mean values of an array
	 * @param vect Vector<> to be averaged
	 * @return the average
	 */
	public static float avg(Vector<Float> vect){
		return 	sum(vect)/vect.size();
	}
	
	/**
	 * Return the mean values of an array
	 * @param vect Vector<> to be averaged
	 * @return the average
	 */
	public static float avg(float[] vect){
		return 	sum(vect)/vect.length;
	}

	/**
	 * return the maximum of an array
	 * @param vect Vector<> to be operated on
	 * @return the max
	 */
	public static float max(Vector<Float> vect){
		float max = 0;
		for(float el: vect) if(el > max) max = el;
		return max;
	}
	
	/**
	 * return the minimum of an array
	 * @param vect Vector<> to be operated on
	 * @return the min
	 */
	public static float min(Vector<Float> vect){
		float min = 10e9f;
		for(float el: vect) if(el < min) min = el;
		return min;
	}
	
	
	/**
	 * return the standart deviation of an array
	 * @param vect Vector<> to be operated on
	 * @return the std
	 */
	public static float std(Vector<Float> vect){
		float sum = 0;
		float avg = avg(vect);
		for(float el: vect) sum = sum + ((el - avg)*(el - avg));
		return (float) Math.sqrt(sum/vect.size());
	}
	
	/**
	 * return the standart deviation of an array
	 * @param vect Vector<> to be operated on
	 * @return the std
	 */
	public static float std(float[] vect){
		float sum = 0;
		float avg = avg(vect);
		for(float el: vect) sum = sum + ((el - avg)*(el - avg));
		return (float) Math.sqrt(sum/vect.length);
	}
	
	   
	/**
	 * Return the plant age based on the experiment starting date 
	 * and the current date (contained in the QR code)
	 * @param dateStart = starting date
	 * @param dateCurent = current date
	 * @return the date different, in days.
	 */
	public static long getDAS(String dateStart, String dateCurent){
		Date start, current;
		try{ 
			DateFormat formatter ; 
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			start = formatter.parse(dateStart);
			current = formatter.parse(dateCurent);
		} 
		catch(Exception e){
			System.out.println("getPlantAge failed"+e );
			return 0;
		}
		
		Calendar calStart = Calendar.getInstance();
		Calendar calToday = Calendar.getInstance();
		calStart.setTime(start);
		calToday.setTime(current);	    
	    		
		return (calToday.getTimeInMillis() - calStart.getTimeInMillis()) / (1000 * 86400);
	} 
	
	
	
	/**
	 * Return the HSB stack of a color image
	 * @param imp the ImagePlus
	 */
    public static ImageStack getHSBStack(ImagePlus imp) {
        int w = imp.getWidth();
        int h = imp.getHeight();
        byte[] hue,s,b;
        ImageStack hsbStack = imp.getStack();
        hue = new byte[w*h];
        s = new byte[w*h];
        b = new byte[w*h];        
        ColorProcessor cp = (ColorProcessor) imp.getProcessor();
        cp.getHSB(hue,s,b);
        hsbStack.addSlice("hue",hue);
        hsbStack.addSlice("saturation",s);
        hsbStack.addSlice("brightness",b);
        
        ImagePlus im = new ImagePlus();
        im.setStack(hsbStack);
        im.show();
        
        return hsbStack;      
   }
    
 
	/**
	 * Retrieve the information contained between two given Strings
	 * @param name: the string to process
	 * @param begin: the openning string
	 * @param end: the closing string. Must be different from "."
	 * @return the String in between
	 */
	public static String processName(String name, String begin, String end){
				
		// Remove the extension
		int cut = name.indexOf(".");
		name = name.substring(0, cut);
				
		String subName, subSubName;	
		int l = name.length();
		
		cut = name.indexOf(begin);
		if(cut != -1){
			subName = name.substring(cut+begin.length(), l);
			cut = subName.contains(end) ? subName.indexOf(end) : subName.length();
			subSubName = subName.substring(0, cut);
			return (subSubName.length() > 0) ? subSubName : "0000";
		}	
		
		return "0000";
	}	
	
	/**
	 * Retrieve the information contained between an openniong string and "_"
	 * @param name = the string to process
	 * @param code = openning string
	 * @return the information in between
	 */
	public static String processName(String name, String code){		
		return processName(name, code, "_");
	}
	
	
	/**
	 * Initialize the CSV connection
	 */
	public static PrintWriter initializeCSV(String folder){	
		
		// Create the connection
		PrintWriter pw;
		try{ pw = new PrintWriter(new FileWriter(folder)); }
		catch(IOException e){
			IJ.log("Could not save file "+folder);
			return null;
		}
		return pw;
	}
	
	
	/**
	 * Create the image folder structure
	 * The structure is:
	 * EXPERIENCE_NAME | GENOTYPE_NAME | TREATMENT_NAME | PLANT_ID
	 */
	public static File createFolderStructure(String folder, boolean global, boolean local, boolean coord, boolean param){
				
		File d0 = new File(folder);
		//File dirOriginal = new File(d0.getAbsolutePath()+"/raws/");
		//if(!dirOriginal.exists()) try{dirOriginal.mkdir();} catch(Exception e){IJ.log("Could not create folder "+dirOriginal);}
		
		if(global){
			File dirMask = new File(d0.getAbsolutePath()+"/global/");
			//File dirConvex = new File(d0.getAbsolutePath()+"/convexhulls/");
			//if(!dirConvex.exists()) try{dirConvex.mkdir();} catch(Exception e){IJ.log("Could not create folder "+dirConvex);}
			if(!dirMask.exists()) try{dirMask.mkdir();} catch(Exception e){IJ.log("Could not create folder "+dirMask);}
		}		
		if(local){
			File dirDiff = new File(d0.getAbsolutePath()+"/local/");
			if(!dirDiff.exists()) try{dirDiff.mkdir();} catch(Exception e){IJ.log("Could not create folder "+dirDiff);}
		}
		if(coord){File dirCoord = new File(d0.getAbsolutePath()+"/shape/");
		if(!dirCoord.exists()) try{dirCoord.mkdir();} catch(Exception e){IJ.log("Could not create folder "+dirCoord);}
		}
		if(param){File dirParam = new File(d0.getAbsolutePath()+"/param/");
		if(!dirParam.exists()) try{dirParam.mkdir();} catch(Exception e){IJ.log("Could not create folder "+dirParam);}
		}
		
		return d0;
	}
	
	/**
	 * Get an array of String from a String with the form "1,2,3,4"
	 * @param s a string
	 * @return a string array
	 */
	public static ArrayList<String> getArrayFromString(String s, String sep, boolean last){
		ArrayList<String> al = new ArrayList<>();
		
		int index = s.indexOf(sep);
		while(index >= 0){
			al.add(s.substring(0, index));
			s = s.substring(index+1, s.length());
			index = s.indexOf(sep);
		} 
		if(last) al.add(s);
		
		return al;
	}    
	
	/**
	 * Get an array of String from a String with the form "1,2,3,4"
	 * @param s a string array
	 * @return a single string
	 */
	public static String getStringFromArray(ArrayList<String> s, String sep){		
		String st = "";
		for(String el: s){
			st = st.concat(el);
			st = st.concat(sep);
		}
		return st;
	} 
	
	
}
