package Data.UserDefinedType;

import java.sql.SQLException;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

import Data.GOdb;

public class Ontology {
	String goId;
	int id;
	protected int ontologyType = -1; //GOdb.BP/CC/MF를 받아서 저장.
	private int[] childrenId;
	private String[] childrenRelation;
	//Ontology를 상속받은 그래프의 각 서브그래프가 됨. 자식에 대한 정보만 저장
	DirectedAcyclicGraph<? extends Ontology, RelationToEdge> children;
	public Ontology(String goId){
		this.goId = goId;
		GOdb godb = GOdb.getInstance();
		try {
			ontologyType = godb.getOntologyTypeOf(goId);//GO ID에서 Ontology Type을 알아냄.
			childrenId = godb.retrieveChildrenOf(ontologyType, godb.getIdFromGoId(goId));
			childrenRelation = godb.retrieveRelationshipOf(ontologyType, godb.getIdFromGoId(goId));
			this.id = godb.getIdFromGoId(goId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
}
