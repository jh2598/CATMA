package GUI;

import processing.core.*;
import processing.data.Table;
import processing.event.KeyEvent;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.VerletSpring2D;
import toxi.physics2d.behaviors.AttractionBehavior;
import toxi.physics2d.behaviors.ParticleBehavior2D;
import toxi.physics2d.constraints.CircularConstraint;

import java.util.ArrayList;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.graph.DefaultEdge;

import Data.EGOGraph;
import Data.GOGraph;
import Data.UserDefinedType.EnrichedGeneOntology;
import Data.UserDefinedType.GeneOntology;
import gluegen.*;
import jogl.util.*;
import controlP5.*;

public class GOVisualize extends PApplet{
	
	/* *************************************
	 * 
	 * 		Basic Processing Setting
	 * 
	 * *************************************/
	
	public void settings(){
		size(1280,800,P2D);
		smooth();
	}
	
	public void setup(){
		
		background(0);
		noStroke();
		initControlP5();
		
/*		Table table = loadTable("D:/David/Ajou Undergraduate/2015-2/CATMA/모임자료/DEG_bY-R/GO.list.csv");
		
		String[] list = new String[table.getRowCount()];
		for(int i=0; i<list.length; i++){
			list[i] = table.getString(i, 0);
		}
		EnrichedGeneOntology[] temp = new EnrichedGeneOntology[list.length];
		int count = 0;
		for(int i=0; i<goList.length; i++){
			EnrichedGeneOntology ego = goList[i];
			for(int j=0; j<list.length; j++){
				if(ego.getGoId().matches(list[j])){
					temp[count] = ego;
					count++;
					System.out.println(ego.getGoId());
				}
			}
		}
		
		goList = temp;*/
		
		//init. variables
		loop = 0;
		edgeLength = 100;
		physics = new VerletPhysics2D();
		scaleLevel = 0;
		displayNode = true;
		displayEdge = true;
		displayGOID = true;
		communicator = Communicator.getCommunicator();
		//Begin Clustering
		cluster = new GOCluster(egoGraph,physics,this);
		
		//for(int i=0; i<goList.length; i++)
			//System.out.println(goList[i].getGoId());
	}
	
	public void draw(){
		
		loop++;
		background(0);
		stroke(255);
		noFill();
		//Loop method
		
	}
	
	//Mouse Event Handlers
/*	public void mousePressed(){
		int nodeSize = (int) cp5.getController("Node size").getValue();
		
		Vec2D mousePos=new Vec2D(mouseX,mouseY);
		for(int i=1; i<physics.particles.size(); i++) {
		  VerletParticle2D p=physics.particles.get(i);
		  if (mousePos.distanceToSquared(p)<nodeSize*nodeSize/2) {
		    selectedParticle=p.lock();
		    break;
		  }
		}
	}
	
	public void mouseClicked(){
		int nodeSize = (int) cp5.getController("Node size").getValue();
		
		Vec2D mousePos=new Vec2D(mouseX,mouseY);
	}
	
	public void keyPressed(KeyEvent event){
		
		if(event.getKey() == 's')
			scaleLevel += 0.1;
		if(event.getKey() == 'a')
			scaleLevel -= 0.1;
	}
	
	public void mouseDragged() {
		if (selectedParticle!=null) {
		  selectedParticle.set(mouseX,mouseY);
		}
	}

	public void mouseReleased() {
		// unlock the selected particle
		if (selectedParticle!=null) {
		    selectedParticle.unlock();
		    selectedParticle=null;
		}
	}*/

	private void initControlP5(){
		
		//Init controller
		cp5 = new ControlP5(this);
		
		//Creating slider group
		Group graphGroup = cp5.addGroup("Graph Control")
				.setBackgroundColor(color(0,64))
				.setBackgroundHeight(200);
		
		cp5.addSlider("Node size")
	     .setPosition(20,20)
	     .setSize(100,20)
	     .setRange(1,15)
	     .setValue(3)
	     .moveTo(graphGroup)
	     ;
		
		cp5.addSlider("Edge length")
	     .setPosition(20,50)
	     .setSize(100,20)
	     .setRange(1,500)
	     .setValue(100)
	     .moveTo(graphGroup)
	     ;
		
		cp5.addSlider("Edge strength")
	     .setPosition(20,80)
	     .setSize(100,20)
	     .setRange(1,100)
	     .setValue(10)
	     .moveTo(graphGroup)
	     ;
		
		cp5.addSlider("Radius")
	     .setPosition(20,110)
	     .setSize(100,20)
	     .setRange(1,100)
	     .setValue(50)
	     .moveTo(graphGroup)
	     ;
		
		cp5.addSlider("Repulse Strength")
	     .setPosition(20,140)
	     .setSize(100,20)
	     .setRange(1,100)
	     .setValue(10)
	     .moveTo(graphGroup)
	     ;
		
		Group displayGroup = cp5.addGroup("Display Control")
				.setBackgroundColor(color(0,64))
				.setBackgroundHeight(150);
		
		
		cp5.addToggle("Node")
	     .setPosition(20,20)
	     .setSize(20,20)
	     .setValue(true)
	     .moveTo(displayGroup)
	     ;
		
		cp5.addToggle("Edge")
	     .setPosition(50,20)
	     .setSize(20,20)
	     .setValue(true)
	     .moveTo(displayGroup)
	     ;
		
		cp5.addToggle("GO ID")
	     .setPosition(80,20)
	     .setSize(20,20)
	     .setValue(true)
	     .moveTo(displayGroup)
	     ;
		
		cp5.addToggle("Lock")
	     .setPosition(110,20)
	     .setSize(70,20)
	     .setValue(false)
	     .setColorBackground(color(100,0,0))
	     .setColorActive(color(255,0,0))
	     .moveTo(displayGroup)
	     ;
		
		//Creating Accordion
		accordion = cp5.addAccordion("accordion")
				.setPosition(width-220,20)
				.setWidth(200)
				.addItem(graphGroup)
				.addItem(displayGroup)
				;
		accordion.open();
		accordion.setCollapseMode(Accordion.MULTI);
		
		//GO info text
		Textlabel label_goid = cp5.addTextlabel("goID")
                .setText("GO ID:")
                .setPosition(width-290+15,height-190+15)
                .setColorValue(0xffffffff)
                .setFont(createFont("Georgia",10,false))
                ;
		
		Textlabel label_goidDisplay = cp5.addTextlabel("goIDDisplay")
                .setText("")
                .setPosition(width-290+53,height-190+15)
                .setColorValue(0xffffff00)
                .setFont(createFont("Georgia",10,false))
                ;
		
		Textlabel label_goName = cp5.addTextlabel("goName")
                .setText("Name:")
                .setPosition(width-290+15,height-190+30)
                .setColorValue(0xffffffff)
                .setFont(createFont("Georgia",10,false))
                ;
		
		Textlabel label_goNameDisplay = cp5.addTextlabel("goNameDisplay")
                .setText("")
                .setPosition(width-290+53,height-190+30)
                .setColorValue(0xffffff00)
                .setFont(createFont("Georgia",10,false))
                ;
		
		Textlabel label_def = cp5.addTextlabel("goDef")
                .setText("Definition:")
                .setPosition(width-290+15,height-190+45)
                .setColorValue(0xffffffff)
                .setFont(createFont("Georgia",10,false))
                ;
		textareaGODef = cp5.addTextarea("goDefDisplay")
                .setPosition(width-290+15,height-190+60)
                .setSize(240,100)
                .setFont(createFont("Georgia",10,false))
                .setLineHeight(12)
                .setColor(color(255))
                .setColorBackground(color(0,100))
                .setColorForeground(color(255,100));
                ;
		
	}
	
	/*******************************
	 * 
	 * 		Custom Methods
	 * 
	 *******************************/
	
	private void updateGraphDisplay(){
		
		//Receiving graph control values
		int nodeSize = (int) cp5.getController("Node size").getValue();
		int edgeLength = (int) cp5.getController("Edge length").getValue();
		float edgeStrength = cp5.getController("Edge strength").getValue()/100;
		int radius = (int) cp5.getController("Radius").getValue();
		float repulseStrength = cp5.getController("Repulse Strength").getValue()/100*(-1);
		//Receiving display control values
		displayNode = flagCheck((int) cp5.getController("Node").getValue());
		displayEdge = flagCheck((int) cp5.getController("Edge").getValue());
		displayGOID = flagCheck((int) cp5.getController("GO ID").getValue());
		boolean lockNodes = flagCheck((int)cp5.getController("Lock").getValue());
		
		Vec2D center = new Vec2D(width/2,height/2);
		
		Object[] springs = physics.springs.toArray();
		
		//Spring modification
		for(Object spring : springs){
			((VerletSpring2D) spring).setRestLength(edgeLength);
			((VerletSpring2D) spring).setStrength(edgeStrength);
		}
		
		//Node modification
		for(int i=1; i<cluster.getNodes().size(); i++){
			Node n = cluster.getNodes().get(i);
			n.setNodeSize(nodeSize);
			n.normalizeTo(n.getHierarchy()*edgeLength/2);
			n.addSelf(center);
		
			if(lockNodes)
				n.lock();
			else
				n.unlock();
		}
		
		//spacing repulsion
		for(int i=0; i<physics.behaviors.size(); i++){
			AttractionBehavior behavior = (AttractionBehavior) physics.behaviors.get(i);
			if(i==0)
				behavior.setStrength(repulseStrength*10);
			else
				behavior.setStrength(repulseStrength*2);
		}
		
	}
	
	/*
	 * 
	 * 		Method for drawing background circle
	 * 		for easy to distinguish hierarchy
	 * 
	 */

	private boolean flagCheck(int value){
		if(value>0)
			return true;
		return false;
	}
	
/*	private void displayGOInfo(){
		
		int startX = width-320;
		int starrY = height-270;
		
		int nodeSize = (int) cp5.getController("Node size").getValue();
		
	 	//Finding particle under mouse
	 	Vec2D mousePos = new Vec2D(mouseX,mouseY);
	 	for(int i=0; i<cluster.getNodes().size();i++){
	 		Node node = cluster.getNodes().get(i);
	 		if(mousePos.distanceToSquared(node)<nodeSize*nodeSize/2){
	 			
	 			GeneOntology go = node.getGO();
	 			Textlabel goID = (Textlabel) cp5.getController("goIDDisplay");
	 			Textlabel goName = (Textlabel) cp5.getController("goNameDisplay");

	 			goID.setText(go.getGo_id());
	 			goName.setText(go.getTerm());
	 			textareaGODef.setText(go.getDefinition());
			}
		}
	}*/
	
	/*
	 * 		Method finding certain EGO
	 */

	//Running method
	public static void run(GOGraph g, EGOGraph eg) {
        PApplet.main(new String[] { GUI.GOVisualize.class.getName() });
        goGraph =g;
        egoGraph = eg;
    }
	
	//Instance Variables
	private ControlP5 cp5;
	private Accordion accordion;
	private int loop;
	float scaleLevel;
	private static GOGraph goGraph;
	private static EGOGraph egoGraph;
	private Communicator communicator;
	private VerletPhysics2D physics;
	private VerletParticle2D selectedParticle;
	private Textarea textareaGODef;
	GOCluster cluster;
	float edgeLength;		//length of edge
	float edgeLimit;
	
	//flag
	boolean displayNode;
	boolean displayEdge;
	boolean displayGOID;

}
