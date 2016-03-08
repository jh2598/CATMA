package GUI;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import Data.Database;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class Heatmap extends PApplet{

	/****************************************
	 * 
	 *		Initial Processing Setting
	 *  
	 ****************************************/
	
	public void settings(){
		//From Processing 3.x, method size()&smooth() must be inside settings 
		size(550,800,JAVA2D);
		noSmooth();
	}
	
	public void setup(){
		//GUI Setting
		background(0);

		//Init. variables
		margin = new Point(30,30);
		buffer = new Point(0,0);
		axis = new Point(0,0);
		localP = new Point(0,0);
		worldP = new Point(0,0);
		scaleLevel = 1;

		//Reading DEG data
		try {
			readDEGData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cLength = (width-2*margin.x)/(table[0].length-3);
		rLength = (int)((height-2*margin.y)/table.length);
		
		frameCount = 0;
		
		System.out.println("Window Height:"+height+"/ rLength:"+rLength+"/ tableLength:"+table.length);
	}
	
	public void draw(){
		
		background(0);
		noStroke();
		
		//Drawing Heatmap
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

		//This method controls scaleLevel variable
		if(event.getCount()==WHEEL_UP){
			if(scaleLevel<2)
				scaleLevel += 0.2;
			else
				scaleLevel = 2;
		}
		else if(event.getCount()==WHEEL_DOWN){
			if(scaleLevel>1.2)
				scaleLevel -= 0.2;
			else
				scaleLevel = 1;
		}
				
		System.out.println("Heatmap>> Scale Level: "+scaleLevel);
	}
	
	public void mouseDragged(MouseEvent event){
		
		buffer.y = mouseY - pmouseY;
		
		//Limits axis value
		if(axis.y<=0)
			axis.y += buffer.y;
		else
			axis.y = 0;
		
		System.out.println("axis.y ="+axis.y+" / Limit value: "+(-1)*(int)(table.length*scaleLevel+height));
		//System.out.println("Heatmap>> x-buffer:"+buffer.x+" / y-buffer:"+buffer.y);
	}

	
	
	/*******************************
	 * 		Custom Methods
	 * @throws IOException 
	 *******************************/
	
	private void readDEGData() throws IOException{
		
		String s = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader("temp.txt"));
			s = in.readLine();
			in.close();
		} catch (IOException e) {
			System.err.println(e); 
			System.exit(1);
		}
		
		Database db = new Database(s);
		table = db.retrieveSampleTable();
		
	}	//Saves DEG Data into table[][]
	
	private void drawHeatmap(String[][] table){
		
		fill(255,255,0);
		textSize(9);
		
		if(scaleLevel>2.5){
			stroke(0);
			textSize(11);
		}
		
		for(int i=0; i<table.length; i++){
			for(int j=PROBE_ID; j<table[i].length;j++){
				if(j==PROBE_ID){
					fill(255);
					text(table[i][PROBE_ID],cLength*4,rLength*((i+1)*scaleLevel-scaleLevel/2));
				}
				else if(j>=SAMPLE){
					fill(Float.parseFloat(table[i][j])/13*255,255-Float.parseFloat(table[i][j])/13*255,0);
					rect(cLength*(j-4),rLength*i*scaleLevel,cLength,rLength*scaleLevel);
				}
			}
		}
		noStroke();
	}
	
	private void drawGeneInfo(){
		
		translate(mouseX+5,mouseY-35);
		
		fill(0,90);
		rect(0,0,150,40);
		textSize(11);
		//Probe ID
		fill(255);
		text(table[(int)(localP.y/(float)rLength)][PROBE_ID],3,15);
		//Value
		fill(255);
		text("Value: ",3,30);
		fill(255,255,0);
		text(table[(int)(localP.y/(float)rLength)][(int)(localP.x/(float)cLength)+SAMPLE],35,30);
	}
	
	private void drawCurrentCursor(){
		if(updateLocalP()){
			int x = (int)(localP.x/(float)cLength);
			int y = (int)(localP.y/(float)rLength);

			pushMatrix();
			fill(255,255,0,125);
			rect(margin.x,y*rLength*scaleLevel+axis.y+margin.y,cLength*(table[0].length-3),rLength*scaleLevel);
	
			fill(255);
			rect(x*cLength+margin.x,y*rLength*scaleLevel+axis.y+margin.y,cLength,rLength*scaleLevel);
			popMatrix();
			
			drawGeneInfo();
		}
	}
	
	private boolean updateLocalP(){
		
		if(mouseX>margin.x && mouseY>margin.y+axis.y){
			localP.x = mouseX-margin.x;
			localP.y = (int)((mouseY-axis.y)/scaleLevel)-(int)(margin.y/scaleLevel);
		}
		else
			return false;

		if((localP.x<cLength*(table[0].length-4))&&(localP.y<rLength*table.length))
			return true;
		return false;
	}

	private void transform(){
		translate(margin.x,margin.y);
		translate(0,axis.y);
	}
	
	private void retrieve(){
		String s = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader("temp.txt"));
			s = in.readLine();
			in.close();
		} catch (IOException e) {
			System.err.println(e); 
			System.exit(1);
		}
		Database db = new Database(s);
		db.retrieveSampleTable();
		db.getSampleNames();
	}
	
	//Running Method
	public static void run(Communicator c) {
		communicator = c;
        PApplet.main(new String[] { GUI.Heatmap.class.getName() });
    }
	
	
	/*******************************
	 * 		Instance Variables
	 *******************************/
	static Communicator communicator;
	Point margin;
	Point buffer;
	Point axis;		//x-axis : margin, y-axis : custom
	Point localP;	//Local map position (axis : 0,0)
	Point worldP;	//World map position (custom y-axis)
	int cLength;
	int rLength;
	int maxValue;
	
	float scaleLevel;
	
	//Static Constants
	static final float WHEEL_UP = -1.f;
	static final float WHEEL_DOWN = 1.f;
	static final int INDEX = 0;
	static final int PROBE_ID = 1;
	static final int ENTIZ_ID = 2;
	static final int NAME =3;
	static final int SAMPLE =4;

	String[][] table;	//[Index][ProbeID][Entrez][Symbol][SAMPLE....]
	
}

