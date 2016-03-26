package Data.UserDefinedType;

import java.io.Serializable;
import java.sql.SQLException;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

import Data.GOdb;

public class Ontology implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int BP = GOdb.BP;
	public static final int CC = GOdb.CC;
	public static final int MF = GOdb.MF;
	
	protected int id = -1;
	protected String goId;
	protected int ontologyType = -1; //GOdb.BP/CC/MF를 받아서 저장.
	protected String ontologyTypeString;
	protected int[] childrenId;
	protected String[] childrenRelation;
	protected int[] parentId;
	protected String[] parentRelation;
	protected String definition;
	//Ontology를 상속받은 그래프의 각 서브그래프가 됨.
	DirectedAcyclicGraph<? extends Ontology, RelationToEdge> children;
	DirectedAcyclicGraph<? extends Ontology, RelationToEdge> parents;
	public Ontology(){
	}
	public Ontology(String goId){
//		this.goId = goId;
//		GOdb godb = GOdb.getInstance();
//		try {
//			ontologyType = godb.getOntologyTypeOf(goId);//GO ID에서 Ontology Type을 알아냄.
//			childrenId = godb.retrieveChildrenOf(ontologyType, godb.getIdFromGoId(goId));
//			childrenRelation = godb.retrieveRelationshipOf(ontologyType, godb.getIdFromGoId(goId));
//			this.id = godb.getIdFromGoId(goId);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}
	public DirectedAcyclicGraph<? extends Ontology, RelationToEdge> getChildren(){
		if(children == null){
			System.out.println(this+" has no children.");
			return null;
		}
		return children;
	}
	public int[] getChildrenId(){
		return childrenId;
	}
	public String[] getChildrenRelation(){
		return childrenRelation;
	}
	public void setGoId(String GoId){
		this.goId = GoId;
	}
	public String getGoId(){return goId;}
	public int getId(){if(id==-1){} return id;}
	public int getOntologyType(){return ontologyType;}
	public void setOntologyType(int ontologyType) {
		this.ontologyType = ontologyType;
		switch(ontologyType){
		case BP:ontologyTypeString = "BP";break;
		case CC:ontologyTypeString = "CC";break;
		case MF:ontologyTypeString = "MF";break;
		default:break;
		}
	}
	public String getOntologyTypeString(){
		return ontologyTypeString;
	}
	public void setOntologyType(String ontologyType){
		this.ontologyTypeString = ontologyType;
		this.ontologyType = GOdb.getOntologyTypeInteger(ontologyType);
	}
	public void setChildrenId(int[] childrenId) {
		this.childrenId = childrenId;
	}
	public void setChildrenRelation(String[] childrenRelation) {
		this.childrenRelation = childrenRelation;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int[] getParentId() {
		return parentId;
	}
	public void setParentId(int[] parentId) {
		this.parentId = parentId;
	}
	public String[] getParentRelation() {
		return parentRelation;
	}
	public void setParentRelation(String[] parentRelation) {
		this.parentRelation = parentRelation;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public String getDefinition() {
		return definition;
	}
}
