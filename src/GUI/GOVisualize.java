package GUI;

import processing.core.*;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
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
		noSmooth();
	}
	
	public void setup(){
		
		background(0);
		noStroke();
		
		//init. variables
		loop = 0;
		diameter = 300;
		physics = new VerletPhysics2D();
		
		//Begin Clustering
		cluster = new GOCluster(goGraph,diameter,new Vec2D(width/2,height/2),physics,this);
		
	}
	
	public void draw(){
		
		loop++;
		background(0);
		stroke(255);
		noFill();

		
		//Loop method
		physics.update();
		cluster.display(GOCluster.MF,true);	// Display flag(Go Term Name)
	}

	//Running method
	public static void run(GOGraph g) {
        PApplet.main(new String[] { GUI.GOVisualize.class.getName() });
        goGraph =g;
    }
	
	//Instance Variables
	private int loop;
	static GOGraph goGraph;
	private VerletPhysics2D physics;
	private VerletParticle2D selectedParticle;
	GOCluster cluster;
	float diameter;		//length of edge

}
