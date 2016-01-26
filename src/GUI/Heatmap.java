package GUI;


import java.io.*;

import javax.swing.JFileChooser;

import org.rosuda.REngine.Rserve.RserveException;

import Data.*;
import g4p_controls.*;
import processing.core.*;
import processing.event.*;
import processing.opengl.*;



public class Heatmap extends PApplet{


	/****************************************
	 * 
	 *		Initial Processing Setting
	 *  
	 ****************************************/
	
	public void settings(){
		//From Processing 3.x, method size()&smooth() must be inside settings 
		size(700,800,JAVA2D);
		noSmooth();
	}
	
	public void setup(){
		//GUI Setting
		background(0);

		//Init. variables
		xMargin = 20;
		yMargin = 20;
		scaleLevel = 1;
		xScaleAxis = 0;
		yScaleAxis = 0;
		worldPx = 0;
		worldPy = 0;
		
		//maxValue = input (FOR FILL COLOR)
		
		table = new String[114][5];		//LATER, DO NOT DO LIKE THIS, read the database, input it.
		try {
			readDEGData(table);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cLength = (width-2*xMargin)/table[1].length;
		rLength = (height-2*yMargin)/table.length;
		
		frameCount = 0;
		

	}
	
	public void draw(){
		
		background(0);
		noStroke();
		
		//Drawing Heatmap
		
		//translate(xMargin,yMargin);
		pushMatrix();
		scaleTable();
		drawHeatmap(this.table);
		popMatrix();
		
		//Update frameCount
		frameCount ++;
		frameCount = frameCount%256;
	}
	
	public void mouseWheel(MouseEvent event) {
		
		//Saving old axis for new point calculation
		int oldxScaleAxis = xScaleAxis;
		int oldyScaleAxis = yScaleAxis;
		
		//Update Scale Axis
		xScaleAxis = mouseX;
		yScaleAxis = mouseY;
		
		//Calculate new Point's wolrd coordinate point
		worldPx = (int)((xScaleAxis-oldxScaleAxis)/scaleLevel + oldxScaleAxis);
		worldPy = (int)((yScaleAxis-oldxScaleAxis)/scaleLevel + oldyScaleAxis);
		
		//This method controls scaleLevel variable
		if(event.getCount()==WHEEL_UP)
			scaleLevel += 0.2;
		else if(event.getCount()==WHEEL_DOWN)
			if(scaleLevel>=1.2)
				scaleLevel -= 0.2;
			else
				scaleLevel = 1;
				
		System.out.println("Heatmap>> Scale Level: "+scaleLevel);
	}

	
	/*******************************
	 * 		Custom Methods
	 * @throws IOException 
	 *******************************/
	
	private void readDEGData(String[][] table) throws IOException{
		
		InputStream inputStream;
		Reader streamReader;
		BufferedReader br = null;
		
		inputStream = new FileInputStream("c:/Data/testDEGData.txt");
		streamReader = new InputStreamReader(inputStream);
		br = new BufferedReader(new InputStreamReader(inputStream));
		
		String line;
		for (int i=0; (line = br.readLine()) != null; i++) {
			table[i][0]=line; 
			table[i] = table[i][0].split(",");
		}
		
	}	//Saves DEG Data into table[][]
	
	private void drawHeatmap(String[][] table){
		
		fill(255,255,0);
		textSize(9);
		
		//First Line : CEL Information.
		for(int i=0; i<table[0].length; i++)
			text(table[0][i],cLength*(i-1),0);
		
		for(int i=1; i<table.length; i++){
			for(int j=0; j<table[i].length;j++){
				if(j==0){
					fill(255);
					text(table[i][0],cLength*4,rLength*(i+1));
				}
				else{
					fill(Float.parseFloat(table[i][j])/13*255,Float.parseFloat(table[i][j])/13*255/2,255-Float.parseFloat(table[i][j])/13*255);
					rect(cLength*(j-1),rLength*i,cLength,rLength);
				}
			}
		}
	}
	

	private void scaleTable(){
		translate(-1*xScaleAxis,-1*yScaleAxis);
		scale(scaleLevel);
		//translate(xScaleAxis,yScaleAxis);	
	}
	
	/*******************************
	 * 		Instance Variables
	 *******************************/
	int xMargin;
	int yMargin;
	int cLength;
	int rLength;
	int maxValue;
	int xScaleAxis;
	int yScaleAxis;
	int worldPx;
	int worldPy;
	
	float scaleLevel;
	
	//Static Constants
	static final float WHEEL_UP = -1.f;
	static final float WHEEL_DOWN = 1.f;
	static final int XAXIS = 1;
	static final int YAXIS = 1;
	

	String[][] table;
	
	//Running Method
	public static void main(String[] args) {
        PApplet.main(new String[] { GUI.Heatmap.class.getName() });
    }
	
}

