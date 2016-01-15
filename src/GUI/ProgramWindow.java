package GUI;

import g4p_controls.*;
import processing.core.*;

public class ProgramWindow extends PApplet{
	
	/****************************************
	 * 
	 *		Initial Processing Setting
	 *  
	 ****************************************/
	
	public void settings(){
		//From Processing 3.x, method size()&smooth() must be inside settings 
		size(500,300,JAVA2D);
		smooth();
	}
	
	public void setup(){
		//GUI Setting
		background(0);
		noStroke();
		fill(255);
		textAlign(CENTER);
		
		//Init. variables
		frameCount = 0;
			
		//Using G4P Library
		createGUI();
	}
	
	public void draw(){
		background(0);
		textAlign(CENTER);
		textSize(20);
		fill(255);
		text("Clustering Analysis Toll for Microarray", width/2, height/2-50);
		textSize(15);
		fill(255,255,0,frameCount*2%256);
		text("Program Running...", width/2, height/2);
		text("(Do not close this window!)", width/2, height/2+18);
		textSize(10);
		fill(255);
		textAlign(RIGHT);
		text("2016 Dept. of Media, Ajou University", width-10, height-30);
		text("Bioinformatics Lab, Dankook University", width-10, height-15);
		
		//Update frameCount
		frameCount ++;
		frameCount = frameCount%256;
	}

	public void customGUI(){
		
	}
	
	//Running Method
	public static void run() {
        PApplet.main(new String[] { GUI.ProgramWindow.class.getName() });
    }

	/**************************************
	 * 
	 * 		Main G4P Window Setting
	 * 
	 **************************************/
	
	//GUI method
	public void createGUI(){
		G4P.messagesEnabled(false);
		G4P.setGlobalColorScheme(GCScheme.BLUE_SCHEME);
		G4P.setCursor(ARROW);
		surface.setTitle("Program Window(Do not close this window)");
		
		MainWindow = GWindow.getWindow(this, "Cluster Analysis Tool for Microarray", 0, 0, 800, 600, JAVA2D);
		MainWindow.noLoop();
		MainWindow.setActionOnClose(G4P.CLOSE_WINDOW);
		MainWindow.addDrawHandler(this, "win_draw");
		MainWindow.loop();
	}
	
	//Event Handlers
	synchronized public void win_draw(PApplet appc, GWinData data) {
		  appc.background(230);
	}
	
	
	/**************************************
	 * 	  		Instance Variables
	 **************************************/
	//Processing Variables
	int frameCount;
	String s;
	
	//G4P Variables
	GWindow MainWindow;
}

