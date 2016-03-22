package GUI;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Data.DatabaseHelper;
import Data.UserDefinedType.*;
import controlP5.ControlP5;
import controlP5.RadioButton;
import controlP5.Toggle;
import processing.core.PApplet;
import processing.core.PFont;
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
		mouseInitP = new Point(-10,-10);
		mouseFinalP = new Point(-10,-10);
		scaleLevel = 1;
		geneDisplayMode = SYMBOL_NAME;
		communicator = Communicator.getCommunicator();
		cp5 = new ControlP5(this);
		
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
		
		initDEG();
		initControlP5();
	}
	
	public void draw(){
		
		background(0);
		noStroke();
		
		//Drawing methods
		drawBackground();
		drawHeatmap();
		
		pushMatrix();
		drawCurrentCursor();
		popMatrix();		
		
		//Update frameCount
		buffer.x = 0;
		buffer.y = 0;
		frameCount ++;
		frameCount = frameCount%256;

	}
	
	private void initControlP5(){
		RadioButton radioDisplayIDMode;
		
		radioDisplayIDMode = cp5.addRadioButton("radioIDMode")
		         .setPosition(60,height-60)
		         .setSize(20,20)
		         .setColorForeground(color(120))
		         .setColorActive(color(255))
		         .setColorLabel(color(255))
		         .setItemsPerRow(5)
		         .setSpacingColumn(30)
		         .addItem("Symbol Name",3)
		         .addItem("Entriz ID",2)
		         .addItem("Probe ID",1)
		         .activate(0)
		         ;
		
	    for(Toggle t: radioDisplayIDMode.getItems()) {
	        t.getCaptionLabel().getStyle().moveMargin(15,0,0,0);
	        t.getCaptionLabel().alignX(CENTER);
	        //t.getCaptionLabel().getStyle().movePadding(7,0,0,3);
	     }
	}

/*	public void mouseWheel(MouseEvent event) {

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
	}*/
	
	public void mousePressed(MouseEvent event){
		
		mouseInitP.setLocation(-10,-10);
		mouseFinalP.setLocation(-10,-10);
		
		//If mouse pressed, save its position
		if(isHeatmapArea()){
			mouseInitP.setLocation(mouseX, mouseY);
			mouseFinalP.setLocation(mouseX, mouseY);
		}
	}
		
	public void mouseDragged(MouseEvent event){
		
		if(isHeatmapArea())
			mouseFinalP.setLocation(mouseX, mouseY);
		
		/*buffer.y = mouseY - pmouseY;
		
		//Limits axis value
		if(axis.y<=0)
			axis.y += buffer.y;
		else
			axis.y = 0;
		
		System.out.println("axis.y ="+axis.y+" / Limit value: "+(-1)*(int)(table.length*scaleLevel+height));
		*/
	}
	
	public void mouseReleased(MouseEvent e){
		
		if(isHeatmapArea()){
			int selectedCol = (int)(((int)(mouseInitP.y-axis.y/scaleLevel)-(int)(margin.y/scaleLevel))/rLength);
			int selectedColEnd = (int)(((int)(mouseFinalP.y-axis.y/scaleLevel)-(int)(margin.y/scaleLevel))/rLength);
	
			int start;
			
			if(selectedCol>selectedColEnd)
				start = selectedColEnd;
			else
				start = selectedCol;
		
			//clear all selected genes before
			communicator.getSelectedEntrizID().clear();
			
			//Add new selected genes
			for(int i=start; i<=start+abs(selectedColEnd-selectedCol); i++){
				deg[i].setSelected(true);	
			}
			
			//Updating selected genes
			for(DEG g : deg){
				if(g.isSelected())
					communicator.getSelectedEntrizID().add(g.getEntrizID());
			}
		}
	}

	/*******************************
	 * 		Custom Methods
	 * @throws IOException 
	 *******************************/
	
	//Event method of cp5 radio button
	public void radioIDMode(int id){
		geneDisplayMode = id;
	}
	
	private void drawBackground(){
		pushMatrix();
		strokeWeight(1);
		stroke(255);
		fill(0,0,100,30);
		rect(margin.x,height-70,width-2*margin.x,50);		
		popMatrix();
	}
	
	private void drawHeatmap(){
		for(int i=0; i<deg.length; i++){
			deg[i].display(margin.x, margin.y + rLength*i,maxValue,minValue,geneDisplayMode);
		}
	}
	
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
		
		DatabaseHelper db = new DatabaseHelper(s);
		table = db.retrieveSampleTable();
		
	}	//Saves DEG Data into table[][]
	
	private void drawGeneInfo(){
		
		translate(mouseX+5,mouseY-35);
		
		fill(0,90);
		rect(0,0,150,40);
		textSize(11);
		//Probe ID
		fill(255);
		if(checkSelected(table[(int)(localP.y/(float)rLength)][ENTRIZ_ID]))
			fill(255,255,0);
		text(table[(int)(localP.y/(float)rLength)][geneDisplayMode],3,15);
		//Value
		fill(255);
		text("Value: ",3,30);
		fill(255,255,0);
		text(table[(int)(localP.y/(float)rLength)][(int)(localP.x/(float)cLength)+SAMPLE],35,30);
	}
	
	private void drawCurrentCursor(){
		
		if(mousePressed){
			int mouseInitY = (int)(((int)(mouseInitP.y-axis.y/scaleLevel)-(int)(margin.y/scaleLevel))/rLength);
			int mouseFinalY = (int)(((int)(mouseFinalP.y-axis.y/scaleLevel)-(int)(margin.y/scaleLevel))/rLength);
			
			//Drawing Selected Area
			pushMatrix();
			stroke(255);
			strokeWeight(1);
			fill(0,0,255,30);
			rect(margin.x,mouseInitY*rLength*scaleLevel+axis.y+margin.y,cLength*(table[0].length-3),rLength*(mouseFinalY-mouseInitY+1)*scaleLevel);
			popMatrix();
		}
		
		if(updateLocalP()){
			int x = (int)(localP.x/(float)cLength);
			int y = (int)(localP.y/(float)rLength);

			pushMatrix();
			noStroke();
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
	
	/*
	 *	Method for checking mouse position
	 *	whether if it is inside heatmap
	 *
	 */
	private boolean isHeatmapArea(){
		
		if(mouseX>margin.x && mouseY>margin.y && mouseX<(margin.x+cLength*(table[0].length-3)) && mouseY<(margin.y+rLength*table.length))
			return true;
		return false;
	}
	
	private boolean checkSelected(String entrizID){
		
		ArrayList<String> genes = new ArrayList<String>();
		for(EnrichedGeneOntology ego : communicator.getSelectedGO()){
			for(String g : ego.getGeneList())
				genes.add(g);
		}
		for(String s : genes){
			if(s.matches(entrizID))
				return true;
		}
		return false;
	}
	
	/*
	 * 		Create array of DEG 
	 */
	private void initDEG(){
		
		deg = new DEG[table.length];
		maxValue = 1;
		minValue = 10;
		
		//Initialize DEG data
		for(int i=0; i<deg.length; i++){
			
			//Get organism sample value
			double[] sampleValue = new double[table[i].length-4];
			for(int j=0; j<sampleValue.length; j++){
				sampleValue[j] = Double.parseDouble(table[i][table[i].length-4+j]);
				//Save max&minValue
				if(sampleValue[j]>maxValue)
					maxValue = sampleValue[j];
				else if(sampleValue[j]<minValue)
					minValue = sampleValue[j];
			}
			//Construct first step
			//(Index,ProbeID,Entrez,Symbol,Sample number,width,height)	
			deg[i] = new DEG(Integer.parseInt(table[i][0]),table[i][1],table[i][2],table[i][3],sampleValue,
					margin.x, i*rLength+margin.y,width-2*margin.x,(height-2*margin.y)/deg.length,this); 
			
		}
		
		System.out.println("\nHeatmap>> MAX P-Value:"+maxValue+", MIN P-Value:"+minValue);
		
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
		DatabaseHelper db = new DatabaseHelper(s);
		db.retrieveSampleTable();
		db.getCelFileNames();
	}
	
	//Running Method
	public static void run() {
        PApplet.main(new String[] { GUI.Heatmap.class.getName() });
    }
	
	
	/*******************************
	 * 		Instance Variables
	 *******************************/
	ControlP5 cp5;
	private Communicator communicator;
	Point margin;
	Point buffer;
	Point axis;		//x-axis : margin, y-axis : custom
	Point localP;	//Local map position (axis : 0,0)
	Point worldP;	//World map position (custom y-axis)
	Point mouseInitP;	//Initial mouse pressed position
	Point mouseFinalP;
	int geneDisplayMode;
	int cLength;
	int rLength;
	double maxValue;
	double minValue;
	
	float scaleLevel;
	
	//Static Constants
	public static final float WHEEL_UP = -1.f;
	public static final float WHEEL_DOWN = 1.f;
	public static final int INDEX = 0;
	public static final int PROBE_ID = 1;
	public static final int ENTRIZ_ID = 2;
	public static final int SYMBOL_NAME =3;
	public static final int SAMPLE =4;

	String[][] table;	//[Index][ProbeID][Entrez][Symbol][SAMPLE....]
	DEG[] deg;
	
}

