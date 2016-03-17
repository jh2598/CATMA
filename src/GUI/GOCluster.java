package GUI;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

import Data.*;
import Data.UserDefinedType.EnrichedGeneOntology;
import Data.UserDefinedType.GeneOntology;
import Data.UserDefinedType.RelationToEdge;
import processing.core.PApplet;
import toxi.geom.Vec2D;
import toxi.physics.VerletParticle;
import toxi.physics.behaviors.ParticleBehavior;
import toxi.physics2d.VerletConstrainedSpring2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.VerletSpring2D;
import toxi.physics2d.behaviors.AttractionBehavior;
import toxi.physics2d.behaviors.ParticleBehavior2D;
import toxi.physics2d.constraints.CircularConstraint;

public class GOCluster {

	public GOCluster(GOGraph graph, EnrichedGeneOntology[] gl, float diameter, Vec2D center, VerletPhysics2D physics, PApplet pApplet) {
		
		//init. variables
		p = pApplet;
		this.diameter = diameter;
		this.center = center;
		this.graph = graph;
		this.goList = gl;
		this.physics = physics;
		this.repulsionRadius = 100;
		this.repulsionStrength = -0.1f;
		removedSprings = new ArrayList<VerletSpring2D>();
	}
	
	private void clusterBp2(){
		System.out.println("\nGOCluster>> Processing : linking nodes...");
		
		nodes = new ArrayList<Node>();
		egoGraph = new DirectedAcyclicGraph<Node, RelationToEdge>(RelationToEdge.class);
		
		//Adding Root Node-[GO:0008150] and its children
		GeneOntology rootGO = graph.getGoMap().get("GO:0008150");
		Node rootNode = new Node(new Vec2D(p.width/2,30),rootGO,Node.RED,p);
		nodes.add(rootNode);
		nodes.get(0).lock();
		nodes.get(0).setHierarchy(0);
		//nodes.get(0).addBehavior(new AttractionBehavior(nodes.get(0),1280,-1));
		physics.addParticle(rootNode);
		egoGraph.addVertex(rootNode);

		
		//Creating new graph with ego
		for(EnrichedGeneOntology ego : goList){
		
			GeneOntology go = graph.getGoMap().get(ego.getGoId());
			Node nodeEGO = new Node(center.add(Vec2D.randomVector()),go,Node.BLUE,p);
					
			//Creates new graph with ego by finding up its parents
			nodes = linkParent(nodeEGO,nodes,egoGraph);
		}
		
		System.out.println("List of GO:");
		Iterator iterator = egoGraph.iterator();
		while(iterator.hasNext()){
			Node n = (Node) iterator.next();
			System.out.println(n.getGO().getGo_id());
		}
		

		System.out.println("\nList of GO marking:");
		//marking graph hierarchy to nodes 
		markHierarchy(rootNode, 0);

		for(Node n : nodes)
			System.out.println(n.getGO().getGo_id()+","+n.getHierarchy());
		
		System.out.println("graph vertex:"+egoGraph.vertexSet().size()+" / edge:"+egoGraph.edgeSet().size());
		System.out.println("GOCluster>> Clustering Finished. Number of node: "+nodes.size()+" | Number of edges: "+physics.springs.size());
	}
	
	
	/*
	* 		Method for marking hierarchy and removing spring of node with multiple parents.
	*		Removed springs are saved in variable 'removedSpring', later to draw it.
	*
	*		This way, as it searches down its children,
	*		it will ALWAYS mark the lower hierarchy,
	*		removing spring of higher parent
	*/
	private void markHierarchy(Node n, int currentH){
		Node parent = n;
		
		//Now marking child's hierarchy
		parent.setHierarchy(currentH);
		
		//adding weak repulse
		AttractionBehavior repulse = new AttractionBehavior(parent, 30, -0.5f);
		parent.addBehavior(repulse);
		
		//Place position and link child
		Set<RelationToEdge> childEdgeSet = egoGraph.outgoingEdgesOf(parent);
		Object[] childEdges = childEdgeSet.toArray();
		
		System.out.println("Outgoing Edges of "+parent.getGO().getGo_id()+":");
		for(int i=0; i<childEdges.length; i++){
			//Getting child node of parent
			RelationToEdge e = (RelationToEdge)childEdges[i];
			Node child = egoGraph.getEdgeTarget(e);
			System.out.println(child.getGO().getGo_id());
			
		}
		
		for(int i=0; i<childEdges.length; i++){
			
			//Getting child node of parent
			RelationToEdge e = (RelationToEdge)childEdges[i];
			Node child = egoGraph.getEdgeTarget(e);
			child.set(new Vec2D(p.width/2,currentH*30+30));
			if(child.getHierarchy()<currentH+1)
				markHierarchy(child, currentH+1);
		}
	}
	
	//Recursive-Function linking nodes to parent
	private ArrayList<Node> linkParent(Node child, ArrayList<Node> n, DirectedAcyclicGraph<Node, RelationToEdge> egoGraph2){
		
		//Add child to node[]
		n.add(child);
		physics.addParticle(child);
		if(!egoGraph2.containsVertex(child))
			egoGraph2.addVertex(child);

		//Finding Parents of child
		Set<RelationToEdge> parentEdgesSet = graph.getBp().incomingEdgesOf(child.getGO());
		Object[] parentEdges = parentEdgesSet.toArray();

		//For every parent
		for(Object e : parentEdges){
			GeneOntology parent = graph.getBp().getEdgeSource((RelationToEdge) e);
			Node parentNode = isGOExist(parent.getGo_id(), n);
			
			//If parent doesn't exist in node[]
			if(parentNode==null){
				
				//Link(spring) with child
				parentNode = new Node(child.add(Vec2D.randomVector().normalize()),parent,p);
				physics.addSpring(new VerletSpring2D(parentNode,child,diameter,0.1f));
				egoGraph2.addVertex(parentNode);
				egoGraph2.addEdge(parentNode,child);
				
				//***Recall Function*****
				linkParent(parentNode,n,egoGraph2);
			}
			//If parent already exist in Node, just add spring
			else{
				physics.addSpring(new VerletSpring2D(parentNode,child,diameter,0.0001f));
				egoGraph2.addEdge(parentNode, child);
			}
		}
		return n;
	}
	
	private Node isGOExist(String goID, ArrayList<Node> n){
		for(int i=0; i<n.size(); i++){
			if(n.get(i).getGO().getGo_id().matches(goID))
				return n.get(i);
		}
		return null;
	}
	
	private void clusterGraph(int goTerm){
		
		nodes = new ArrayList<Node>();
		DirectedAcyclicGraph<GeneOntology, RelationToEdge> term = null;
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
		BreadthFirstIterator<GeneOntology, RelationToEdge> bfs = new BreadthFirstIterator<GeneOntology,RelationToEdge>(term,rootNode);
		
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
			Set<RelationToEdge> offspringTemp = term.edgesOf(parent);
			Object[] offspring = offspringTemp.toArray();
			
			System.out.println("GOCluster>> Parent node:"+parentIndex +"/Number of offspring: "+offspring.length);

			if((offspring != null) && (offspring.length!=0)){
				for(int i=0; i<offspring.length;i++){
					
					RelationToEdge e = (RelationToEdge)offspring[i];
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
							physics.addSpring(new VerletConstrainedSpring2D(nodes.get(parentIndex),nodes.get(currentNodeIndex),diameter,0.0001f));
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
	
	public void display(boolean node, boolean edge, boolean goID){
		
		if(nodes == null){
			//clusterGraph(goTerm);
			clusterBp2();
		}
		
		//Display line
		if(edge){
			for (int i = 0; i < physics.springs.size(); i++) {
				VerletParticle2D pi = physics.springs.get(i).a;
				VerletParticle2D pj = physics.springs.get(i).b;
			
		    	p.stroke(255,50);
		    	p.line(pi.x,pi.y,pj.x,pj.y);
			}
		}
		
		//Display Node
		if(node){
			for (Node n : nodes)
			 	n.display(goID);
		}
	}
	
	//Getter
	public ArrayList<Node> getNodes(){
		return nodes;
	}
	
	public DirectedAcyclicGraph<Node, RelationToEdge> getEgoGraph(){
		return egoGraph;
	};
	
	
	//Instance Variables
	private PApplet p;
	private GOGraph graph;
	private EnrichedGeneOntology[] goList;
	private DirectedAcyclicGraph<Node, RelationToEdge> egoGraph;
	private ArrayList<VerletSpring2D> removedSprings;
	private Vec2D center;

	private ArrayList<Node> nodes;
	private VerletPhysics2D physics;
	float diameter;
	float repulsionRadius;
	float repulsionStrength;
	boolean clustered;
	
	public static int BP = 0;
	public static int CC = 1;
	public static int MF = 2;
}
