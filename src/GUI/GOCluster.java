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
import Data.UserDefinedType.EnrichedGeneOntology;
import Data.UserDefinedType.GeneOntology;
import processing.core.PApplet;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletConstrainedSpring2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.VerletSpring2D;

public class GOCluster {

	public GOCluster(GOGraph graph, EnrichedGeneOntology[] gl, float diameter, Vec2D center, VerletPhysics2D physics, PApplet pApplet) {
		
		//init. variables
		p = pApplet;
		this.diameter = diameter;
		this.center = center;
		this.graph = graph;
		this.goList = gl;
		this.physics = physics;
	}
	
	private void clusterBp(){
		
		System.out.println("\nGOCluster>> Processing : linking nodes...");
		
		nodes = new ArrayList<Node>();
		
		//Adding Root Node-[GO:0008150] and its children
		GeneOntology rootGO = graph.getGoMap().get("GO:0008150");
		Node rootNode = new Node(new Vec2D(p.width/2,p.height/2),rootGO,p);
		nodes.add(rootNode);
		nodes.get(0).lock();
		//Getting children of root node
		Set<DefaultEdge> childrenSet = graph.getBp().outgoingEdgesOf(rootGO);
		Object[] childrenEdges = childrenSet.toArray();
		//Adding children
		for(Object e : childrenEdges){
			GeneOntology child = graph.getBp().getEdgeSource((DefaultEdge) e);
			
			Node childNode = new Node(center.add(Vec2D.randomVector()),child,p);
			VerletConstrainedSpring2D spring = new VerletConstrainedSpring2D(rootNode,childNode,diameter,0.9f,diameter);
			spring.lockB(true);
			physics.addSpring(spring);			
		}
		
		for(EnrichedGeneOntology ego : goList){
			
			GeneOntology go = graph.getGoMap().get(ego.getGoId());
			Node nodeEGO = new Node(center.add(Vec2D.randomVector()),go,p);
					
			//Create node graph by recursive function
			nodes = linkParent(nodeEGO,nodes);
		}
		
		System.out.println("GOCluster>> Clustering Finished. Number of node: "+nodes.size()+" | Number of edges: "+physics.springs.size());
	}
	
	//Recursive-Function linking nodes to parent
	private ArrayList<Node> linkParent(Node child, ArrayList<Node> n){
		
		//Add child to node[]
		n.add(child);
		
		//Finding Parents of child
		Set<DefaultEdge> parentEdgesSet = graph.getBp().incomingEdgesOf(child.getGO());
		Object[] parentEdges = parentEdgesSet.toArray();
		
		//if root, return
		if(parentEdges == null)
			return n;

		//For every parent
		for(Object e : parentEdges){
			GeneOntology parent = graph.getBp().getEdgeSource((DefaultEdge) e);
			
			//Link(spring) with child
			Node parentNode = new Node(center.add(Vec2D.randomVector()),parent,p);
			physics.addSpring(new VerletConstrainedSpring2D(parentNode,child,diameter,0.9f,diameter));
			
			System.out.println("GOCluster>> Linking ["+parentNode.getGO().getGo_id()+" - "+child.getGO().getGo_id()+"]");
			
			//If parent doesn't exist in node[]
			if(!isGOExist(parent.getGo_id(), n)){
				//***Recall Function*****
				linkParent(parentNode,n);
			}
		}
		return n;
	}
	
	private boolean isGOExist(String goID, ArrayList<Node> n){
			for(int i=0; i<n.size(); i++){
				if(n.get(i).getGO().getGo_id().matches(goID))
					return true;
			}
		return false;
	}
	
	private void clusterGraph(int goTerm){
		
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
				nodes.add(new Node(new Vec2D(p.width/2,p.height/2),parent,p));
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
							nodes.add(new Node(center.add(Vec2D.randomVector()),child,p));
							currentNodeIndex++;
						}
						
						VerletSpring2D spring = physics.getSpring(nodes.get(parentIndex),nodes.get(currentNodeIndex));
						if(spring==null){
							physics.addSpring(new VerletConstrainedSpring2D(nodes.get(parentIndex),nodes.get(currentNodeIndex),diameter,0.0001f,diameter));
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
		
		if(nodes == null){
			//clusterGraph(goTerm);
			clusterBp();
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
	EnrichedGeneOntology[] goList;
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
