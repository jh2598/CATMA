package Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Session implements Serializable {
	private static final long serialVersionUID = -4961998355515742346L;
	public String name;
	public String filePath;
	public String celFilePath;
	public String sampleInfoPath;
	Database db;

	public Session(String name, String celPath, String samplePath){
		System.out.println("Session Created. :: "+this);
		this.name = name;
		this.celFilePath = celPath;
		this.sampleInfoPath = samplePath;
//		db = new Database();	
	}
	public void save(){
		//자신을 직렬화해서 filePath에 저장
		try{
			FileOutputStream fos = new FileOutputStream(filePath+"/"+ name+".ser");
			ObjectOutput oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.flush(); 
			fos.close();
			oos.close();
		}catch(IOException e){
			System.err.println(e);
		}
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String loaded() {
		// TODO Auto-generated method stub
		System.out.println("::"+name);
		return "Successfully opened.";
	}
}
