package GUI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

import Data.*;
import Data.UserDefinedType.GeneOntology;
import processing.core.PApplet;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletConstrainedSpring2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.VerletSpring2D;

public class GOCluster {

	public GOCluster(GOGraph graph, float diameter, Vec2D center, VerletPhysics2D physics, PApplet pApplet) {
		
		//init. variables
		p = pApplet;
		this.diameter = diameter;
		this.center = center;
		this.graph = graph;
		this.physics = physics;
		clustered = false;
	}
	
	private void cluster(int goTerm){
		
		nodes = new ArrayList<Node>();
		DirectedAcyclicGraph<GeneOntology, DefaultEdge> term = null;
		GeneOntology rootNode = null;
		
		//Receiving graph
		switch(goTerm){
		case 0:
			term = graph.getBp();
			rootNode = graph.getGoMap().get("GO:0008150");
			break;
		case 1:
			term = graph.getCc();
			rootNode = graph.getGoMap().get("GO:0005575");
			break;
		case 2:
			term = graph.getMf();
			rootNode = graph.getGoMap().get("GO:0003674");
			break;
		}
		
		//Cluster Using breadth-first-search
		BreadthFirstIterator<GeneOntology, DefaultEdge> bfs = new BreadthFirstIterator<GeneOntology,DefaultEdge>(term,rootNode);
		
		int edgeNumber = 0;
		int parentIndex = -1;
		int currentNodeIndex = 0;
		
		while(bfs.hasNext()){
			
			//Adding Parent Node
			GeneOntology parent = bfs.next();
			parentIndex++;
			
			//Adding Root Node
			if(nodes.isEmpty()){
				nodes.add(new Node(new Vec2D(p.width/2,p.height/2),p,parent));
				nodes.get(0).lock();
			}
			
			//Getting offspring of that vertex, Make Connection
			Set<DefaultEdge> offspringTemp = term.edgesOf(parent);
			Object[] offspring = offspringTemp.toArray();
			
			System.out.println("GOCluster>> Parent node:"+parentIndex +"/Number of offspring: "+offspring.length);

			if((offspring != null) && (offspring.length!=0)){
				for(int i=0; i<offspring.length;i++){
					
					DefaultEdge e = (DefaultEdge)offspring[i];
					//Connecting child with parent
					GeneOntology child = term.getEdgeTarget(e);
					
					if(!parent.equals(child)){
						
						if(!child.visited){
							child.visited = true;
							nodes.add(new Node(center.add(Vec2D.randomVector()),p,child));
							currentNodeIndex++;
						}
						
						VerletSpring2D spring = physics.getSpring(nodes.get(parentIndex),nodes.get(currentNodeIndex));
						if(spring==null){
							physics.addSpring(new VerletConstrainedSpring2D(nodes.get(parentIndex),nodes.get(currentNodeIndex),diameter,0.99f,diameter));
							edgeNumber++;
						}
						//else
							//spring.setRestLength((float)(spring.getRestLength()*1.5));
						
						System.out.println("GOCluster>> Adding node ["+currentNodeIndex+","+child.getGo_id()+"]"+", Connecting with parent ["+parentIndex+","+parent.getGo_id()+"]");
						
					}
				}
			}
		}
		System.out.println("Clustering Finished. Number of node: "+nodes.size()+" | Number of edges: "+physics.springs.size());
	}
	
	public void display(int goTerm, boolean goName){
		
		if(!clustered){
			cluster(goTerm);
			clustered = true;
		}
		
		//Display line
		for (int i = 0; i < physics.springs.size(); i++) {
			VerletParticle2D pi = physics.springs.get(i).a;
			VerletParticle2D pj = physics.springs.get(i).b;

		    p.stroke(255,10);
		    p.line(pi.x,pi.y,pj.x,pj.y);
		}
		
		//Display Node
		for (int i = 0; i < nodes.size(); i++) {
			 nodes.get(i).display(goName);
			 
			//Finding particle under mouse
			Vec2D mousePos = new Vec2D(p.mouseX,p.mouseY);
			VerletParticle2D p = nodes.get(i);
			if(mousePos.distanceToSquared(p)<SNAP_DIST){
				drawGOInfo(nodes.get(i).getGO());
				break;
			}
		}
	}
	
	public void drawGOInfo(GeneOntology go){
				
		int lineSpace = 20;
		int xMargin=20;		//Left Upper Margin space-x
		int yMargin=25;		//Left Upper Margin space-y
		
		String[] strTerm = go.getTerm().split("(?<=\\G.{30})");
		String[] strDef = go.getDefinition().split("(?<=\\G.{25})");
		
		p.pushMatrix();
		//Background Box
		p.fill(50);
		p.rect(0, 0, 250, (int)(lineSpace*1.2*(2+strTerm.length+strDef.length)));
		//Text Area
		p.fill(255);
		p.textSize(11);
		p.text("GO ID: "+go.getGo_id(), xMargin, yMargin + lineSpace*0);
		p.text("GO ID: "+go.getOntology(), xMargin, yMargin + lineSpace*1);
		p.text("Term: ", xMargin, yMargin + lineSpace*2);
		for(int i=0; i<strTerm.length; i++)
			p.text(strTerm[i], 35 + xMargin, yMargin + lineSpace*(2+i));
		p.text("Definition: ", xMargin, yMargin + lineSpace*(2+strTerm.length));
		for(int i=0; i<strDef.length; i++)
			p.text(strDef[i], 65 + xMargin, yMargin + lineSpace*(2+strTerm.length+i));
		p.popMatrix();
	}
	
	//Instance Variables
	PApplet p;
	GOGraph graph;
	Vec2D center;

	ArrayList<Node> nodes;
	VerletPhysics2D physics;
	float diameter;
	boolean clustered;
	
	public static int BP = 0;
	public static int CC = 1;
	public static int MF = 2;
	private static int SNAP_DIST = 10;
}
