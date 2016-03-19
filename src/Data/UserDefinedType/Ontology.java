package Data.UserDefinedType;

import java.sql.SQLException;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

import Data.GOdb;

public class Ontology {
	String goId;
	int id;
	protected int ontologyType = -1; //GOdb.BP/CC/MF�� �޾Ƽ� ����.
	private int[] childrenId;
	private String[] childrenRelation;
	//Ontology�� ��ӹ��� �׷����� �� ����׷����� ��. �ڽĿ� ���� ������ ����
	DirectedAcyclicGraph<? extends Ontology, RelationToEdge> children;
	public Ontology(String goId){
		this.goId = goId;
		GOdb godb = GOdb.getInstance();
		try {
			ontologyType = godb.getOntologyTypeOf(goId);//GO ID���� Ontology Type�� �˾Ƴ�.
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
