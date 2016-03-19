package Data;

import java.io.Serializable;
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
	HashMap<String, EnrichedGeneOntology> ego_map;
	
	public EGOGraph(EnrichedGeneOntology[] egoArray){
		bp = new DirectedAcyclicGraph<EnrichedGeneOntology, RelationToEdge>(RelationToEdge.class);
		cc = new DirectedAcyclicGraph<EnrichedGeneOntology, RelationToEdge>(RelationToEdge.class);
		mf = new DirectedAcyclicGraph<EnrichedGeneOntology, RelationToEdge>(RelationToEdge.class);
		for(int i=0;i<egoArray.length;i++){
			ego_map.put(egoArray[i].getGoId(), egoArray[i]);
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
		System.out.println("ego bp graph size :: "+bp.vertexSet().size());
		System.out.println("ego cc graph size :: "+cc.vertexSet().size());
		System.out.println("ego mf graph size :: "+mf.vertexSet().size());
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
		return ego_map.get(other.getGoId());
	}
}
