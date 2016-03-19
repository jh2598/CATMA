package Data;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

import Data.UserDefinedType.EnrichedGeneOntology;
import Data.UserDefinedType.Ontology;
import Data.UserDefinedType.RelationToEdge;

public class EGOGraph implements Serializable{
	private static final long serialVersionUID = 5385450819385802517L;
	private DirectedAcyclicGraph<EnrichedGeneOntology, RelationToEdge> bp;
	private DirectedAcyclicGraph<EnrichedGeneOntology, RelationToEdge> cc;
	private DirectedAcyclicGraph<EnrichedGeneOntology, RelationToEdge> mf;
	HashMap<String, EnrichedGeneOntology> egoMap;
	
	public EGOGraph(EnrichedGeneOntology[] egoArray){
		System.out.println("EGO Array Size : "+ egoArray.length);
		egoMap = new HashMap<>();
		bp = new DirectedAcyclicGraph<EnrichedGeneOntology, RelationToEdge>(RelationToEdge.class);
		cc = new DirectedAcyclicGraph<EnrichedGeneOntology, RelationToEdge>(RelationToEdge.class);
		mf = new DirectedAcyclicGraph<EnrichedGeneOntology, RelationToEdge>(RelationToEdge.class);
		for(int i=0;i<egoArray.length;i++){
			egoMap.put(egoArray[i].getGoId(), egoArray[i]);
			GOdb godb = GOdb.getInstance();
			try {
				egoArray[i].setOntologyType(godb.getOntologyTypeOf(egoArray[i].getGoId()));//GO ID에서 Ontology Type을 알아냄.
				egoArray[i].setChildrenId(godb.retrieveChildrenOf(egoArray[i].getOntologyType(), godb.getIdFromGoId(egoArray[i].getGoId())));
				egoArray[i].setChildrenRelation(godb.retrieveRelationshipOf(egoArray[i].getOntologyType(), godb.getIdFromGoId(egoArray[i].getGoId())));
				egoArray[i].setId(godb.getIdFromGoId(egoArray[i].getGoId()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(egoArray[i].getOntologyType() == GOdb.BP){
				bp.addVertex(egoArray[i]);
			}else if(egoArray[i].getOntologyType() == GOdb.CC){
				cc.addVertex(egoArray[i]);
			}else if(egoArray[i].getOntologyType() == GOdb.MF){
				mf.addVertex(egoArray[i]);
			}
		}
		for(int i=0;i<egoArray.length;i++){
			if(egoArray[i].getOntologyType() == GOdb.BP){
				addChildrenIntoGraphFromArray(egoArray[i], egoArray, bp);
			}else if(egoArray[i].getOntologyType() == GOdb.CC){
				addChildrenIntoGraphFromArray(egoArray[i], egoArray, cc);
			}else if(egoArray[i].getOntologyType() == GOdb.MF){
				addChildrenIntoGraphFromArray(egoArray[i], egoArray, mf);
			}
		}
		System.out.println("ego bp graph v size :: "+bp.vertexSet().size());
		System.out.println("ego bp graph e size :: "+bp.edgeSet().size());
		System.out.println("ego bp graph v size :: "+cc.vertexSet().size());
		System.out.println("ego bp graph e size :: "+cc.edgeSet().size());
		System.out.println("ego bp graph v size :: "+mf.vertexSet().size());
		System.out.println("ego bp graph e size :: "+mf.edgeSet().size());
	}
	//해당 ego의 children을 살펴봐서 egoArray에 들어있다면 bp에 추가.
	//추가하면서 edge에 relation type을 명시해줌.
	private void addChildrenIntoGraphFromArray(EnrichedGeneOntology ego, EnrichedGeneOntology[] egoArray, DirectedAcyclicGraph<EnrichedGeneOntology, RelationToEdge> graphByOntology){
		int[] childrenId = ego.getChildrenId();
		for(int idIdx=0;idIdx<childrenId.length;idIdx++){
			for(int arrIdx=0;arrIdx<egoArray.length;arrIdx++){
				if(egoArray[arrIdx].getId() == childrenId[idIdx]){
					RelationToEdge edge = graphByOntology.addEdge(ego, egoArray[arrIdx]);
					edge.setType(ego.getChildrenRelation()[idIdx]);
				}
			}
		}
	}
	
	public DirectedAcyclicGraph<EnrichedGeneOntology, RelationToEdge> getBpGraph(){
		return bp;
	}
	public DirectedAcyclicGraph<EnrichedGeneOntology, RelationToEdge> getCcGraph(){
		return cc;
	}
	public DirectedAcyclicGraph<EnrichedGeneOntology, RelationToEdge> getMfGraph(){
		return mf;
	}	
	//GO id로 ego 객체를 찾아냄.
	public EnrichedGeneOntology getEgoObjectFromOther(Ontology other){
		return egoMap.get(other.getGoId());
	}
}
