package GUI;

import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import processing.core.*;

public class Node extends VerletParticle2D{

	public Node(Vec2D pos, PApplet parent) {
		super(pos);
		p = parent;
	}

	//public methods
	public void display(){
		p.fill(255);	
		p.noStroke();
		p.ellipse(x,y,3,3);
	}

	//Instance Variables
	PApplet p;
}