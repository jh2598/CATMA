package GUI;

import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import Data.UserDefinedType.GeneOntology;
import processing.core.*;

public class Node extends VerletParticle2D{

	public Node(Vec2D pos, PApplet parent){
		this(pos, null, 3, YELLOW, parent);
	}
	
	public Node(Vec2D pos, GeneOntology g, PApplet parent){
		this(pos, g, 3, YELLOW, parent);
	}
	
	public Node(Vec2D pos, GeneOntology g, int color, PApplet parent){
		this(pos, g, 3, color, parent);
	}
	
	public Node(Vec2D pos, GeneOntology g, int size, int color, PApplet parent) {
		super(pos);
		p = parent;
		go = g;
		this.size = size;
		this.color = color;
		hierarchy = -1;
		count = 0;
	}

	//public methods
	public void display(boolean goName){
		
		switch(color){
		case 0:	//Red
			p.fill(255,0,0);
			break;
		case 1:	//Green
			p.fill(0,255,0);
			break;
		case 2:	//Blue
			p.fill(0,0,255);
			break;
		case 3: //Yellow
			p.fill(255,255,0);
			break;
		}
		p.noStroke();
		p.ellipse(x,y,size,size);
		
		//Printing GO_ID
		if(goName){
			p.pushMatrix();
			p.textAlign(p.CENTER);
			p.text(go.getGo_id(),x,y-5);
			p.popMatrix();
		}
		
	}
	
	public void highLight(){
		count = (count+1)%30;
		p.pushMatrix();
		p.stroke(255,255-count*9);
		p.ellipse(x,y,size+count,size+count);
		p.strokeWeight(2);
		
		p.popMatrix();
	}
	
	public void setNodeSize(int size){
		this.size = size;
	}
	
	public void setColor(int color){
		this.color = color;
	}

	public void setHierarchy(int h){
		hierarchy = h;
	}
	
	public int getHierarchy(){
		return hierarchy;
	}
	
	public GeneOntology getGO(){
		return go;
	}
	
	//Instance Variables
	private PApplet p;
	private GeneOntology go;
	private int size;
	private int color;
	private int hierarchy;
	private int count;
	
	//ColorConstant
	public static int RED = 0;
	public static int GREEN = 1;
	public static int BLUE = 2;
	public static int YELLOW = 3;
}