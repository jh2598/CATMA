package Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

import Data.UserDefinedType.EnrichedGeneOntology;
import Data.UserDefinedType.RelationToEdge;

public class Session implements Serializable {
	private static final long serialVersionUID = -4961998355515742346L;
	public String name;
	public String filePath;
	public String celFilePath;
	public String sampleInfoPath;

	public DataProcess dataProcess;

	public GOGraph allGo;
	private boolean existGoData = false;
	public static final String GoGraphFileName = GOGraph.allGoDataFileName;

	public DatabaseHelper db;

	public EnrichedGeneOntology[] ego = null;
	public EGOGraph egoGraph;
	public Session(String name, String celPath, String samplePath){
		System.out.println("Session Created. :: "+this);
		this.name = name;
		this.celFilePath = celPath;
		this.sampleInfoPath = samplePath;	
	}
	public void save(){
		//자신을 직렬화해서 filePath에 저장
		try{
			FileOutputStream fos = new FileOutputStream(filePath+"/"+ name+".sav");
			ObjectOutput oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.flush(); 
			fos.close();
			oos.close();
			System.out.println("All GO ::::"+allGo);
			if(allGo != null && !checkGoGraphDataExist()){
				allGo.save();
			}
		}catch(IOException e){
			System.err.println(e);
			e.printStackTrace();
		}
	}
	public boolean checkGoGraphDataExist(){
		String dir = DataProcess.getUserDir(GoGraphFileName);
		File goData = new File(dir);
		System.out.println("ALL_GO_GRAPH PATH ::::"+dir);
		if(goData.exists()){
			existGoData = true;
		}else{
			existGoData = false;
		}
		return existGoData;
	}
	public void setName(String name) {this.name = name;}
	public void setFilePath(String filePath) {this.filePath = filePath;}
	public String loaded() {

		System.out.println("::"+name);
		return "Successfully opened.";
	}
	public void setDB(DatabaseHelper db){
		this.db = db;
	}
	public void setDataProcess(DataProcess process){
		this.dataProcess = process;
	}
	public DatabaseHelper getDB(){
		if(db != null){
			return db;
		}
		System.err.println("Session->db is null");
		return null;
	}
	public void setAllGo(GOGraph allGo){this.allGo = allGo;}
}
