package Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import Data.UserDefinedType.EnrichedGeneOntology;

public class Session implements Serializable {
	private static final long serialVersionUID = -4961998355515742346L;
	public String name;
	public String filePath;
	public String celFilePath;
	public String sampleInfoPath;
	
	public DataProcess dataProcess;
	
	public GOGraph allGo;
	private boolean existGoData = false;
	public static final String GoGraphFileName = "ALL_GO_GRAPH.dat";
	
	public DatabaseHelper db;
	
	public EnrichedGeneOntology[] ego = null;
	
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
		}catch(IOException e){
			System.err.println(e);
		}
	}
	public boolean checkGoGraphDataExist(){
		String dir = DataProcess.getUserDir(GoGraphFileName);
		File goData = new File(dir);
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
		// TODO Auto-generated method stub
		System.out.println("::"+name);
		return "Successfully opened.";
	}
	public void setDB(DatabaseHelper db){
		this.db = db;
	}
	public void setDataProcess(DataProcess process){
		this.dataProcess = process;
	}
	public DatabaseHelper getDB(){return db;}
	public void setAllGo(GOGraph allGo){this.allGo = allGo;}
}
