package GUI;

import processing.core.*;
import gluegen.*;
import jogl.util.*;

public class GOVisualize extends PApplet{
	
	/* *************************************
	 * 
	 * 		Basic Processing Setting
	 * 
	 * *************************************/
	
	
	public void settings(){
		size(1280,800,OPENGL);
		noSmooth();
	}
	
	public void setup(){
		
		background(0);
		noStroke();
		
	}
	
	public void draw(){
		background(0);
		
	}
	
	//Running method
	public static void main(String args[]) {
        PApplet.main(new String[] { GUI.GOVisualize.class.getName() });
    }
	

}
