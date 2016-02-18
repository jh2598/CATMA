package GUI;

import processing.core.*;
import gluegen.*;
import jogl.util.*;

public class GOVisualize extends PApplet{
	
<<<<<<< HEAD
	public void settings(){
		size(800,800,OPENGL);
=======
	/* *************************************
	 * 
	 * 		Basic Processing Setting
	 * 
	 * *************************************/
	
	
	public void settings(){
		size(1280,800,OPENGL);
>>>>>>> origin
		noSmooth();
	}
	
	public void setup(){
		
		background(0);
		noStroke();
		
	}
	
	public void draw(){
		background(0);
		
	}
<<<<<<< HEAD

	public GOVisualize() {
		// TODO Auto-generated constructor stub
	}
=======
	
	//Running method
	public static void main(String args[]) {
        PApplet.main(new String[] { GUI.GOVisualize.class.getName() });
    }
	
>>>>>>> origin

}
