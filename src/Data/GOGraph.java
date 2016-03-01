package Data;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.jgrapht.*;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;
import org.jgrapht.graph.DefaultEdge;

import sun.font.CreatedFontTracker;
public class GOGraph {
	private GO[] allTerm;
	GOdb db;
	HashMap<Integer, GO> bpMap;
	HashMap<Integer, GO> ccMap;
	HashMap<Integer, GO> mfMap;
	HashMap<String, GO> go_map;
	private DirectedAcyclicGraph<GO, DefaultEdge> bp;
	private DirectedAcyclicGraph<GO, DefaultEdge> cc;
	private DirectedAcyclicGraph<GO, DefaultEdge> mf;
	public GOGraph(GOdb db) {
		// TODO Auto-generated constructor stub
		EdgeFactory<GO, DefaultEdge> ef = new EdgeFactory<GO, DefaultEdge>() {
			@Override
			public DefaultEdge createEdge(GO arg0, GO arg1) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		bp = new DirectedAcyclicGraph<GO, DefaultEdge>(DefaultEdge.class);
		cc = new DirectedAcyclicGraph<GO, DefaultEdge>(DefaultEdge.class);
		mf = new DirectedAcyclicGraph<GO, DefaultEdge>(DefaultEdge.class);
		go_map = new HashMap<String, GO>();
		this.db = db;
		try {
			allTerm = db.getAllTerm();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		termClassification();
		try {
			makeEdge();
		} catch (SQLException | CycleFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void termClassification(){
		//GO Term을 확인해서 해당하는 Ontology의 Graph에 집어넣음
		bpMap = new HashMap<Integer,GO>();
		ccMap = new HashMap<Integer,GO>();
		mfMap = new HashMap<Integer,GO>();
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
	public void makeEdge() throws SQLException, CycleFoundException{
		//GO Term끼리의 Offspring 관계를 확인해서 Graph의 Edge를 만들어서 연결 
		Iterator<Integer> iter = bpMap.keySet().iterator();
		System.out.println("Linking BP offspring relation...");
		while(iter.hasNext()){
			int tmp = iter.next();
			int[] offsprings = db.getBpOffspring(tmp);
//			System.out.println("Parent:"+bpMap.get(tmp));
			for(int j=0;j<offsprings.length;j++){
				bp.addEdge(bpMap.get(tmp), bpMap.get(offsprings[j]));
			}
		}
		System.out.println("BP done.");
		
		iter = ccMap.keySet().iterator();
		System.out.println("Linking CC offspring relation...");
		while(iter.hasNext()){
			int tmp = iter.next();
			int[] offsprings = db.getCcOffspring(tmp);
//			System.out.println("Parent:"+ccMap.get(tmp));
			for(int j=0;j<offsprings.length;j++){
				cc.addEdge(ccMap.get(tmp), ccMap.get(offsprings[j]));
			}
		}
		System.out.println("CC done.");
		
		iter = mfMap.keySet().iterator();
		System.out.println("Linking MF offspring relation...");
		while(iter.hasNext()){
			int tmp = iter.next();
			int[] offsprings = db.getMfOffspring(tmp);
//			System.out.println("Parent:"+mfMap.get(tmp));
			for(int j=0;j<offsprings.length;j++){
				mf.addEdge(mfMap.get(tmp), mfMap.get(offsprings[j]));
			}
		}
		System.out.println("MF done.");
		System.out.println("BP Offspring size:"+bp.edgeSet().size());
		System.out.println("CC Offspring size:"+cc.edgeSet().size());
		System.out.println("MF Offspring size:"+mf.edgeSet().size());
	}
	
	public DirectedAcyclicGraph<GO, DefaultEdge> getBp() {
		return bp;
	}
	public DirectedAcyclicGraph<GO, DefaultEdge> getCc() {
		return cc;
	}
	public DirectedAcyclicGraph<GO, DefaultEdge> getMf() {
		return mf;
	}
	public HashMap<String, GO> getGoMap() {
		return go_map;
	}
}
