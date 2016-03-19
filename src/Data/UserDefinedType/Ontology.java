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
	String goId;
	private int id;
	private int ontologyType = -1; //GOdb.BP/CC/MF�� �޾Ƽ� ����.
	private int[] childrenId;
	private String[] childrenRelation;
	//Ontology�� ��ӹ��� �׷����� �� ����׷����� ��. �ڽĿ� ���� ������ ����
	DirectedAcyclicGraph<? extends Ontology, RelationToEdge> children;
	public Ontology(String goId){
//		this.goId = goId;
//		GOdb godb = GOdb.getInstance();
//		try {
//			ontologyType = godb.getOntologyTypeOf(goId);//GO ID���� Ontology Type�� �˾Ƴ�.
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
	public String getGoId(){return goId;}
	public int getId(){return id;}
	public int getOntologyType(){return ontologyType;}
	public void setOntologyType(int ontologyType) {
		this.ontologyType = ontologyType;
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
}
