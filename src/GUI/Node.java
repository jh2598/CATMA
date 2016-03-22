package GUI;

import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import Data.UserDefinedType.EnrichedGeneOntology;
import Data.UserDefinedType.GeneOntology;
import Data.UserDefinedType.Ontology;
import processing.core.*;

public class Node extends VerletParticle2D{
	
	public Node(Vec2D pos, EnrichedGeneOntology g, int color, PApplet parent){
		super(pos);
		p = parent;
		go = g;
		size = 10;
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
			p.text(go.getDescription(),x,y-5);
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
	
	public EnrichedGeneOntology getGO(){
		return go;
	}
	
	//Instance Variables
	private PApplet p;
	private EnrichedGeneOntology go;
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