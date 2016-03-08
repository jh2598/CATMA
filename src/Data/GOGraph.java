package Data;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.jgrapht.*;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;
import org.jgrapht.graph.DefaultEdge;

import Data.UserDefinedType.GeneOntology;
import sun.font.CreatedFontTracker;
public class GOGraph {
	private GeneOntology[] allTerm;
	GOdb db;
	HashMap<Integer, GeneOntology> bpMap;
	HashMap<Integer, GeneOntology> ccMap;
	HashMap<Integer, GeneOntology> mfMap;
	HashMap<String, GeneOntology> go_map;
	private DirectedAcyclicGraph<GeneOntology, DefaultEdge> bp;
	private DirectedAcyclicGraph<GeneOntology, DefaultEdge> cc;
	private DirectedAcyclicGraph<GeneOntology, DefaultEdge> mf;
	public GOGraph(GOdb db) {
		// TODO Auto-generated constructor stub
		EdgeFactory<GeneOntology, DefaultEdge> ef = new EdgeFactory<GeneOntology, DefaultEdge>() {
			@Override
			public DefaultEdge createEdge(GeneOntology arg0, GeneOntology arg1) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		bp = new DirectedAcyclicGraph<GeneOntology, DefaultEdge>(DefaultEdge.class);
		cc = new DirectedAcyclicGraph<GeneOntology, DefaultEdge>(DefaultEdge.class);
		mf = new DirectedAcyclicGraph<GeneOntology, DefaultEdge>(DefaultEdge.class);
		go_map = new HashMap<String, GeneOntology>();
		this.db = db;
		try {
			allTerm = db.getAllTerm();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		termClassification();
		try {
			makeAllEdge();
		} catch (SQLException | CycleFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void termClassification(){
		//GO Term을 확인해서 해당하는 Ontology의 Graph에 집어넣음
		bpMap = new HashMap<Integer,GeneOntology>();
		ccMap = new HashMap<Integer,GeneOntology>();
		mfMap = new HashMap<Integer,GeneOntology>();
		System.out.println("GO Term Classficating...");
		for(int i=0;i<allTerm.length;i++){
			if(allTerm[i].getOntology().compareTo("BP")==0){
				bp.addVertex(allTerm[i]);
				bpMap.put(allTerm[i].getId(), allTerm[i]);
				go_map.put(allTerm[i].getGo_id(), allTerm[i]);
			}else if(allTerm[i].getOntology().compareTo("CC")==0){
				cc.addVertex(allTerm[i]);
				ccMap.put(allTerm[i].getId(), allTerm[i]);
				go_map.put(allTerm[i].getGo_id(), allTerm[i]);
			}else if(allTerm[i].getOntology().compareTo("MF")==0){
				mf.addVertex(allTerm[i]);
				mfMap.put(allTerm[i].getId(), allTerm[i]);
				go_map.put(allTerm[i].getGo_id(), allTerm[i]);
			}
		}
		System.out.println("Classfication done.");
		System.out.println("BP Map Size : "+bpMap.size());
		System.out.println("CC Map Size : "+ccMap.size());
		System.out.println("MF Map Size : "+mfMap.size());
	}
	public void makeEdge(DirectedAcyclicGraph<GeneOntology, DefaultEdge> go_graph, HashMap<Integer,GeneOntology> ontologyMap) throws SQLException{
		Iterator<Integer> iter = ontologyMap.keySet().iterator();
		System.out.println("Linking "+ go_graph.hashCode() +" children relation...");
		while(iter.hasNext()){
			int parent = iter.next();
			if(currentOntology == -1){
				System.err.println("Current ontology is not setted.");;
			}
			int[] children = db.retrieveChildrenOf(currentOntology, parent);
//			System.out.println("Parent:"+bpMap.get(tmp));
			for(int j=0;j<children.length;j++){
				go_graph.addEdge(ontologyMap.get(parent), ontologyMap.get(children[j]));
			}
		}		
		System.out.println("MF done.");
		System.out.println("BP Offspring size:"+bp.edgeSet().size());
		System.out.println("CC Offspring size:"+cc.edgeSet().size());
		System.out.println("MF Offspring size:"+mf.edgeSet().size());
		System.out.println("Linking children of "+go_graph.hashCode()+" done.");
	}
	//TODO : GO.db에서 Relationship To 부분을 읽어와야 할 것 같다.
	private static int currentOntology=-1; // 현재 어떤 ontology가 작업중인지 makeEdge에 알려주는 용도의 변수
	public void makeAllEdge() throws SQLException, CycleFoundException{
		//GO Term끼리의 children 관계를 확인해서 Graph의 Edge를 만들어서 연결 
		currentOntology = GOdb.BP;
		makeEdge(bp,bpMap);
		currentOntology = GOdb.CC;
		makeEdge(cc,ccMap);
		currentOntology = GOdb.MF;
		makeEdge(mf,mfMap);
		System.out.println("BP children size:"+bp.edgeSet().size());
		System.out.println("CC children size:"+cc.edgeSet().size());
		System.out.println("MF children size:"+mf.edgeSet().size());
	}
	
	public DirectedAcyclicGraph<GeneOntology, DefaultEdge> getBp() {
		return bp;
	}
	public DirectedAcyclicGraph<GeneOntology, DefaultEdge> getCc() {
		return cc;
	}
	public DirectedAcyclicGraph<GeneOntology, DefaultEdge> getMf() {
		return mf;
	}
	public HashMap<String, GeneOntology> getGoMap() {
		return go_map;
	}
}
