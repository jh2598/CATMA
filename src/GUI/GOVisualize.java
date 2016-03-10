package GUI;

import processing.core.*;
import processing.event.KeyEvent;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.VerletSpring2D;

import java.util.ArrayList;

import Data.GOGraph;
import Data.UserDefinedType.EnrichedGeneOntology;
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
		
		//init. variables
		loop = 0;
		edgeLength = 100;
		physics = new VerletPhysics2D();
		scaleLevel = 0;
		//Begin Clustering
		cluster = new GOCluster(goGraph,goList,edgeLength,new Vec2D(width/2,height/2),physics,this);
		
	}
	
	public void draw(){
		
		loop++;
		background(0);
		stroke(255);
		noFill();
		//Loop method
		physics.update();
		cluster.display(GOCluster.BP,true);	// Display flag(Go Term Name)
		
		updateGraphDisplay();
		
	}
	
	//Mouse Event Handlers
	public void mousePressed(){

	}
	
	public void keyPressed(KeyEvent event){
		
		if(event.getKey() == 's')
			scaleLevel += 0.1;
		if(event.getKey() == 'a')
			scaleLevel -= 0.1;
	}
	
	private void initControlP5(){
		
		//Init controller
		cp5 = new ControlP5(this);
		
		//Creating slider group
		Group graphGroup = cp5.addGroup("Graph Control")
				.setBackgroundColor(color(0,64))
				.setBackgroundHeight(150);
		
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
	     .setRange(10,1000)
	     .setValue(100)
	     .moveTo(graphGroup)
	     ;
		
		cp5.addSlider("Edge strength")
	     .setPosition(20,80)
	     .setSize(100,20)
	     .setRange(1,99)
	     .setValue(99)
	     .moveTo(graphGroup)
	     ;
		
		//Creating Accordion
		accordion = cp5.addAccordion("accordion")
				.setPosition(1000,30)
				.setWidth(200)
				.addItem(graphGroup)
				;
		
		accordion.open();
		accordion.setCollapseMode(Accordion.MULTI);
	}
	
	private void updateGraphDisplay(){
		
		int nodeSize = (int) cp5.getController("Node size").getValue();
		int edgeLength = (int) cp5.getController("Edge length").getValue();
		float edgeStrength = cp5.getController("Edge strength").getValue()/100;
		
		Object[] nodes = physics.particles.toArray();
		Object[] springs = physics.springs.toArray();
		
		for(Object spring : springs){
			((VerletSpring2D) spring).setRestLength(edgeLength);
			((VerletSpring2D) spring).setStrength(edgeStrength);
		}
		
	}
	
	//Running method
	public static void run(GOGraph g, EnrichedGeneOntology[] gl, Communicator c) {
        PApplet.main(new String[] { GUI.GOVisualize.class.getName() });
        goGraph =g;
        goList = gl;
        communicator = c;
    }
	
	//Instance Variables
	private ControlP5 cp5;
	private Accordion accordion;
	private int loop;
	float scaleLevel;
	static GOGraph goGraph;
	static EnrichedGeneOntology[] goList;
	static Communicator communicator;
	private VerletPhysics2D physics;
	private VerletParticle2D selectedParticle;
	GOCluster cluster;
	float edgeLength;		//length of edge
	float edgeLimit;

}
