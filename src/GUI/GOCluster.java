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

import com.sun.org.apache.xerces.internal.dom.ParentNode;

import Data.*;
import Data.UserDefinedType.EnrichedGeneOntology;
import Data.UserDefinedType.GeneOntology;
import Data.UserDefinedType.Ontology;
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

	public GOCluster(EGOGraph egoGraph, VerletPhysics2D physics, PApplet pApplet) {
		
		//init. variables
		p = pApplet;
		this.egoGraph = egoGraph;
		this.physics = physics;
		nodes = new ArrayList<Node>();

		//Root node of Bp
		Ontology rootBP = egoGraph.getGoMap().get("GO:0008150");

		Iterator it = egoGraph.getBpGraph().vertexSet().iterator();
		while(it.hasNext())
			System.out.println(((EnrichedGeneOntology)it.next()).getGoId());
		//Mark Hierarchy of all ego
//		markHierarchy(rootBP, 0);
	}
	
	/*
	 * 	Method for marking hierarchy of egoGraph
	 * 	A recursive function.
	 */
	public void markHierarchy(EnrichedGeneOntology ego, int currentH){
		
		System.out.println("\nGOCluster>> Marking Hierarchy");
		
		//Saving parent and MARK HIERARCHY
		EnrichedGeneOntology parent = ego;
		Node parentNode = new Node(Vec2D.randomVector(),parent,Node.YELLOW,p);
		nodes.add(parentNode);
		parentNode.setHierarchy(currentH);
		
		Iterator iterator = egoGraph.getBpGraph().outgoingEdgesOf(parent).iterator();
		while(iterator.hasNext()){
			RelationToEdge edge = (RelationToEdge) iterator.next();
			EnrichedGeneOntology child = (EnrichedGeneOntology) egoGraph.getBpGraph().getEdgeSource(edge);
			Node childNode = new Node(Vec2D.randomVector(),child,Node.YELLOW,p);
			System.out.println("Marking... ["+parent.getGoId()+"-"+child.getGoId()+"]");
			if(childNode.getHierarchy()<currentH)
				markHierarchy(child, currentH+1);
		}
	}
	
	private Node isGOExist(String goID, ArrayList<Node> n){
		for(int i=0; i<n.size(); i++){
			if(n.get(i).getGO().getGoId().matches(goID))
				return n.get(i);
		}
		return null;
	}
	
	public void display(boolean node, boolean edge, boolean goID){
		
		//Display line
/*		if(edge){
			for (int i = 0; i < physics.springs.size(); i++) {
				VerletParticle2D pi = physics.springs.get(i).a;
				VerletParticle2D pj = physics.springs.get(i).b;
			
		    	p.stroke(255,50);
		    	p.line(pi.x,pi.y,pj.x,pj.y);
			}
		}*/
		
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
	
	//Instance Variables
	private PApplet p;
	private EGOGraph egoGraph;
	private ArrayList<Node> nodes;
	private VerletPhysics2D physics;
	
	public static int BP = 0;
	public static int CC = 1;
	public static int MF = 2;
}
