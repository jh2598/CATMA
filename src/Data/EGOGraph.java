package Data;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;

import Data.UserDefinedType.EnrichedGeneOntology;
import Data.UserDefinedType.Ontology;
import Data.UserDefinedType.RelationToEdge;

//EGO �迭�� �޾Ƽ� �ش� EGO�������� Root���� ���� GO �׷����� ����
public class EGOGraph implements Serializable{
	private static final long serialVersionUID = 5385450819385802517L;
	private DirectedAcyclicGraph<Ontology, RelationToEdge> bp;
	private DirectedAcyclicGraph<Ontology, RelationToEdge> cc;
	private DirectedAcyclicGraph<Ontology, RelationToEdge> mf;
	public HashMap<String, Ontology> goMap; //go id�� Ű��, ontology ��ü�� ����� ��� �ؽø�, ���� ��ü�� ���� ã�� ���� ��.
	public EGOGraph(Session session) throws SQLException{
		bp = new DirectedAcyclicGraph<Ontology, RelationToEdge>(RelationToEdge.class);
		cc = new DirectedAcyclicGraph<Ontology, RelationToEdge>(RelationToEdge.class);
		mf = new DirectedAcyclicGraph<Ontology, RelationToEdge>(RelationToEdge.class);
		goMap = new HashMap<>();
		GOdb godb = GOdb.getInstance();
		EnrichedGeneOntology[] ego = session.ego;
		GOGraph goGraph = session.allGo;
		System.out.println(goGraph);
		System.out.println("EGO Graph Construct.");
		//ego���� Ontology ��ü �ν��Ͻ� �踮��� �Ҵ�. 
		//XXX: �ӽ÷� ���⼭ ó���ϴ� ��. Ontology ������ �κп��� ó�� �� �����ͺ��̽� ���� �߻�. 
		//������ ��ü�� godb�� instance�� ResultSet�� ���ο� ������� ��������鼭 ������ �߻���. ���� ���� �ʿ�.
		for(int i=0;i<ego.length;i++){
			try {
				ego[i].setId(godb.getIdFromGoId(ego[i].getGoId()));
				ego[i].setOntologyType(godb.getOntologyTypeOf(ego[i].getGoId()));//GO ID���� Ontology Type�� �˾Ƴ�.
				ego[i].setChildrenId(godb.retrieveChildrenOf(ego[i].getOntologyType(), ego[i].getId()));
				ego[i].setChildrenRelation(godb.retrieveRelationshipOf(ego[i].getOntologyType(), ego[i].getId()));
				ego[i].setParentId(godb.retrieveParentOf(ego[i].getOntologyType(), ego[i].getId()));
				ego[i].setParentRelation(godb.retrieveRelationshipOfParent(ego[i].getOntologyType(), ego[i].getId(), ego[i].getParentId()));
				goMap.put(ego[i].getGoId(), ego[i]);
				addVertexToGraph(ego[i]);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println("EGO Member variable setted.");

		//GO graph���� ego�� �θ� �ش��ϴ� ��ü���� ���ͼ� �׳���� �θ� �� ã�Ƴ�.
		Queue<Ontology> queue = new LinkedList<Ontology>();

		System.out.println("EGO parents into Queue(VERTEX)");
		//�켱 ego�� parents�� ���� ť�� ����. �׷��鼭 �׷����� vertex���� ���� ����.
		for(int i=0;i<ego.length;i++){
			for(int parentIdx=0;parentIdx<ego[i].getParentId().length;parentIdx++){
				String goId = null;
				goId = godb.getGoIdFromId(ego[i].getParentId()[parentIdx]);
				Ontology go = goGraph.getGo(goId);
				queue.add(go);
				goMap.put(go.getGoId(), go);
				addVertexToGraph(go);
			}
		}
		System.out.println("EGO parents are entered queue.");

		System.out.println("Ancesters are adding to Graph.");
		while(true){
			Ontology go = queue.remove();
			//GO�� Parent�� ���������.
			int[] parents = godb.retrieveParentOf(go.getOntologyType(), go.getId());
			if(parents != null){
				go.setParentId(parents);
			}else{
				int[] rootParent = new int[1];
				rootParent[0] = -1;
				go.setParentId(rootParent);
			}
			if(parents.length<1){
				for(int i=0;i<parents.length;i++){
					String goId = godb.getGoIdFromId(go.getParentId()[i]);
					Ontology parentGo = goGraph.getGo(goId);
					if(parentGo == null){
						System.err.println("parentGo is NULL");
						continue;
					}
					queue.add(parentGo);
					goMap.put(parentGo.getGoId(), parentGo);
					addVertexToGraph(parentGo);
				}
			}
			if(queue.isEmpty()){break;}

		}
		System.out.println("Ancesters are added to Graph.");
		//Vertex���� ���� �����Ƿ� Edge�� ������ְ� relation�� ������.
		System.out.println("Making edge...");
		makeEdge(bp);
		makeEdge(cc);
		makeEdge(mf);
		System.out.println("Edge maked.");
		System.out.println("GO Map size :: "+goMap.size());
		System.out.println("BP Graph Vertex size ::"+bp.vertexSet().size());
		System.out.println("BP Graph Edge size ::"+bp.edgeSet().size());
	}

	private void makeEdge(DirectedAcyclicGraph<Ontology, RelationToEdge> graph) throws SQLException{
		Set<Ontology> set = graph.vertexSet();
		GOdb godb = GOdb.getInstance();
		Iterator<Ontology> iter = set.iterator();
		while(iter.hasNext()){
			Ontology go = iter.next();
			int[] parents = go.getParentId();
			String[] relations = godb.retrieveRelationshipOfParent(go.getOntologyType(), go.getId(), go.getParentId());
			if(parents.length == 0){
				continue;
			}
			for(int i=0;i<parents.length;i++){
				String goId = godb.getGoIdFromId(parents[i]);
				Ontology parent = goMap.get(goId);
				if(parent == null){
					continue;
				}
					try {
						RelationToEdge edge = bp.addDagEdge(parent, go);
						edge.setType(relations[i]);
					} catch (CycleFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
	}

	public void addVertexToGraph(Ontology go){
		if(go.getOntologyType()==Ontology.BP){
			bp.addVertex(go);
		}else if(go.getOntologyType()== Ontology.CC){
			cc.addVertex(go);
		}else if(go.getOntologyType()== Ontology.MF){
			mf.addVertex(go);
		}else{
			System.err.println("Ontolgoy Type Error.");
			return;
		}	
	}
	public DirectedAcyclicGraph<Ontology, RelationToEdge> getBpGraph() {
		return bp;
	}
	public DirectedAcyclicGraph<Ontology, RelationToEdge> getCcGraph() {
		return cc;
	}
	public DirectedAcyclicGraph<Ontology, RelationToEdge> getMfGraph() {
		return mf;
	}
	public HashMap<String, Ontology> getGoMap() {
		return goMap;
	}

	/*
	private DirectedAcyclicGraph<EnrichedGeneOntology, RelationToEdge> bp;
	private DirectedAcyclicGraph<EnrichedGeneOntology, RelationToEdge> cc;
	private DirectedAcyclicGraph<EnrichedGeneOntology, RelationToEdge> mf;
	public HashMap<String, EnrichedGeneOntology> egoMap;

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
				//Enriched Gene Ontology ������ �ν��Ͻ� �踮��� ���� �Ҵ�.
				egoArray[i].setOntologyType(godb.getOntologyTypeOf(egoArray[i].getGoId()));//GO ID���� Ontology Type�� �˾Ƴ�.
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
	//�ش� ego�� children�� ������� egoArray�� ����ִٸ� bp�� �߰�.
	//�߰��ϸ鼭 edge�� relation type�� �������.
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
	//GO id�� ego ��ü�� ã�Ƴ�.
	public EnrichedGeneOntology getEgoObjectFromOther(Ontology other){
		return egoMap.get(other.getGoId());
	}
	 */
}