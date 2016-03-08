package GUI;

import processing.core.*;
import processing.event.KeyEvent;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.VerletSpring2D;
import Data.GOGraph;
import gluegen.*;
import jogl.util.*;

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
		
		//init. variables
		loop = 0;
		edgeLength = 300;
		physics = new VerletPhysics2D();
		scaleLevel = 0;
		//Begin Clustering
		cluster = new GOCluster(goGraph,edgeLength,new Vec2D(width/2,height/2),physics,this);
		
	}
	
	public void draw(){
		
		loop++;
		background(0);
		stroke(255);
		noFill();
		//Loop method
		physics.update();
		cluster.display(GOCluster.MF,false);	// Display flag(Go Term Name)
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
	
	//Running method
	public static void run(GOGraph g, Communicator c) {
        PApplet.main(new String[] { GUI.GOVisualize.class.getName() });
        goGraph =g;
        communicator = c;
    }
	
	//Instance Variables
	private int loop;
	float scaleLevel;
	static GOGraph goGraph;
	static Communicator communicator;
	private VerletPhysics2D physics;
	private VerletParticle2D selectedParticle;
	GOCluster cluster;
	float edgeLength;		//length of edge
	float edgeLimit;

}
