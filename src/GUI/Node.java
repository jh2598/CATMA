package GUI;

import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import Data.GO;
import processing.core.*;

public class Node extends VerletParticle2D{

	public Node(Vec2D pos, PApplet parent){
		this(pos, parent, null);
	}
	
	public Node(Vec2D pos, PApplet parent, GO g) {
		super(pos);
		p = parent;
		go = g;
	}

	//public methods
	public void display(boolean goName){
		p.fill(255,255,0);	
		p.noStroke();
		p.ellipse(x,y,3,3);
		
		if(goName)
			p.text(go.getGo_id(),x,y);
		
	}

	public GO getGO(){
		return go;
	}
	
	//Instance Variables
	private PApplet p;
	private GO go;
}