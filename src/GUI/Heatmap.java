package GUI;


import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.*;

import javax.swing.JFileChooser;

import org.rosuda.REngine.Rserve.RserveException;

import Data.*;
import g4p_controls.*;
import processing.core.*;
import processing.event.*;
import processing.opengl.*;
import gluegen.*;
import jogl.*;



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
		margin = new Point(20,20);
		buffer = new Point(0,0);
		temp = new Point(0,0);
		axis = new Point(0,0);
		localP = new Point(0,0);
		worldP = new Point(0,0);
		scaleLevel = 1;
		
		//maxValue = input (FOR FILL COLOR)
		
		table = new String[114][5];		//LATER, DO NOT DO LIKE THIS, read the database, input it.
		try {
			readDEGData(table);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cLength = (width-2*margin.x)/table[1].length;
		rLength = (height-2*margin.y)/table.length;
		
		frameCount = 0;
		

	}
	
	public void draw(){
		
		background(0);
		noStroke();
		
		//Drawing Heatmap
		
		//translate(margin.x,margin.y);
		pushMatrix();
		transform();
		drawHeatmap(this.table);
		
			
		popMatrix();
		
		pushMatrix();
		drawGeneInfo();
		popMatrix();
		pushMatrix();
		drawCurrentCursor();
		popMatrix();
		
		
		//Update frameCount
		buffer.x = 0;
		buffer.y = 0;
		frameCount ++;
		frameCount = frameCount%256;
		
		System.out.println("Drawing in... [axis:"+axis.x+","+axis.y+"] [scale level:"+scaleLevel+"]");
	}
	
	public void mouseWheel(MouseEvent event) {
		
		//Saving old axis for new point calculation
		int oldaxis = axis.x;
		int oldyAxis = axis.y;
		
		//Update Scale Axis
		axis.x = mouseX;
		axis.y = mouseY;
		
		//Calculate new Point's wolrd coordinate point
//		worldPx = (int)((axis.x-oldaxis.x)/scaleLevel + oldaxis.x);
//		worldPy = (int)((yAxis-oldaxis.x)/scaleLevel + oldyAxis);
		
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

	
	public void mouseDragged(MouseEvent event){
		
		buffer.x = mouseX - pmouseX;
		buffer.y = mouseY - pmouseY;
		
		axis.x += buffer.x;	
		axis.y += buffer.y;
		
		System.out.println("Heatmap>> x-buffer:"+buffer.x+" / y-buffer:"+buffer.y);
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
	
	private void drawGeneInfo(){
		
		translate(mouseX+5,mouseY-35);
		
		fill(0,60);
		rect(0,0,90,40);
		fill(255);
		textSize(9);
		text("probe ID: ",10,10);
		text("Value: ",10,20);
	}
	
	private void drawCurrentCursor(){
		if(updateLocalP()){
			int x = (int)localP.x/cLength;
			int y = (int)localP.y/rLength;
			
			stroke(255);
			strokeWeight(2);
			rect(x,y,x+cLength,y+rLength);
			noStroke();
			
			System.out.println(x+","+y);
		}
	}
	
	private boolean updateLocalP(){
		
		localP.x = (int)((worldP.x-axis.x)/scaleLevel+axis.x);
		localP.y = (int)((worldP.y-axis.y)/scaleLevel+axis.y);
		
		if((localP.x<width-margin.x)&&(localP.x>margin.x)&&(localP.y>margin.y)&&(localP.y<height-margin.y));
			return true;
	}

	private void transform(){
		
		//Scale method
		translate(-1*axis.x*scaleLevel,-1*axis.y*scaleLevel);
		scale(scaleLevel);
		translate(axis.x*scaleLevel,axis.y*scaleLevel);
		
		//Translate method
		//translate(axis.x,yAxis);
	}
	
	/*******************************
	 * 		Instance Variables
	 *******************************/
	Point margin;
	Point buffer;
	Point temp;
	Point axis;
	Point localP;
	Point worldP;
	int cLength;
	int rLength;
	int maxValue;
	
	float scaleLevel;
	
	//Static Constants
	static final float WHEEL_UP = -1.f;
	static final float WHEEL_DOWN = 1.f;
	

	String[][] table;
	
	//Running Method
	public static void main(String[] args) {
        PApplet.main(new String[] { GUI.Heatmap.class.getName() });
    }
	
}

