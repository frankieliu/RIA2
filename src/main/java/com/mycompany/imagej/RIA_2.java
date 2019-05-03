package com.mycompany.imagej;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.prefs.Preferences;

import ij.ImageJ;
import ij.plugin.frame.PlugInFrame;

public class RIA_2 extends PlugInFrame{
	
	static RIA_2 instance, fop;
	public static Preferences prefs;


//	private static final long serialVersionUID = 42L;

	public RIA_2() {
		super("Root Image Analysis J");
		
		new Gui();
	}
	
	
	public static void main(String args[]) {
		if(args.length > 0){

			if(args.length == 6){
				new Results(new File(args[0]), args[1], Float.valueOf(args[2]), 2.54f,
					true, 50, false, true, true, Boolean.valueOf(args[3]), true, Boolean.valueOf(args[4]), args[5] 
					);
			} else if(args.length > 6){
				new Results(new File(args[0]), args[1], Float.valueOf(args[2]), 2.54f,
					true, 50, false, Boolean.valueOf(args[6]), Boolean.valueOf(args[7]), 
					Boolean.valueOf(args[3]), false, Boolean.valueOf(args[4]), args[5] 
					);
			} else {
			    int count = 0;
                for(String a: args){
                    System.out.println(count + a);
                    count++;
                }
            }
			System.exit(0);
		}
		else{
			prefs = Preferences.userRoot().node("/ImageJ/plugins");
			ImageJ ij = new ImageJ();
			fop = new RIA_2();
			ij.addWindowListener(new WindowAdapter() {
				public void windowClosed(WindowEvent e) {
					fop.dispose();
					System.exit(0);
				}
			});
		}
		//System.exit(0);
	}
}
