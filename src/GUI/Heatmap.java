package GUI;


import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.*;

import javax.swing.JFileChooser;

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
		drawCurrentCursor();
		popMatrix();
		
		
		//Update frameCount
		buffer.x = 0;
		buffer.y = 0;
		frameCount ++;
		frameCount = frameCount%256;

	}
	
	public void mouseWheel(MouseEvent event) {
		
		axis.y = mouseY;
		
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
		
		//buffer.x = mouseX - pmouseX;
		buffer.y = mouseY - pmouseY;
		
		//axis.x += buffer.x;	
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
					text(table[i][0],cLength*4,rLength*(i+1)+scaleLevel/2);
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
		
		fill(0,90);
		rect(0,0,150,40);
		textSize(9);
		//Organism
		fill(255);
		text("Organ. : ",2,10);
		String org = table[0][(int)(localP.x/(float)cLength)+1].substring(0, 15) + "...";		//Limit String length to 15
		fill(255,255,0);
		text(org,45,10);
		//Probe ID
		fill(255);
		text("Probe ID: ",2,20);
		fill(255,255,0);
		text(table[(int)(localP.y/(float)rLength)][0],50,20);
		//Value
		fill(255);
		text("Value: ",2,30);
		fill(255,255,0);
		text(table[(int)(localP.y/(float)rLength)][(int)(localP.x/(float)cLength)+1],35,30);
	}
	
	private void drawCurrentCursor(){
		if(updateLocalP()){
			int x = (int)(localP.x/(float)cLength);
			int y = (int)(localP.y/(float)rLength);
			
			stroke(255);
			strokeWeight(2);
			fill(0,0);
			rect(x*cLength,y*rLength*scaleLevel,cLength,rLength*scaleLevel);
			noStroke();
			
			drawGeneInfo();
		}
	}
	
	private boolean updateLocalP(){
		
		localP.x = (int)((mouseX-axis.x)/scaleLevel+axis.x);
		localP.y = (int)((mouseY-axis.y)/scaleLevel+axis.y);
		
		if((localP.x<cLength*(table[0].length-1))&&(localP.y<rLength*table.length))
			return true;
		return false;
				
	}

	private void transform(){
		
		//Scale method
		translate(0,-axis.y);
		scale(1,scaleLevel);
		//translate(0,axis.y);
		
		
		//Translate method
		//translate(axis.x,axis.y);
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

