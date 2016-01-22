package Data;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class DataProcess {
	private RConnection c;
	public Session session;
	public String GOdbPath;
	REXP x;

	public DataProcess(Session session) {
		try {
			this.session = session;
			c = new RConnection();
			x = c.eval("R.version.string");
			System.out.println("R Connected successfully :: " + x.asString());
		} catch (RserveException | REXPMismatchException e) {
			// TODO Auto-generated catch block
			System.out.println("RConnection not Constructed.");
			e.printStackTrace();
		}
	}
	public void init(){
		try {
			setLibrary();
			setPath();
		} catch (RserveException | REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//1
	public void setLibrary() throws RserveException, REXPMismatchException {
		// Setting Library - affy, limma, GO.db
		System.out.println("R :: Library setting... : affy");
		c.eval("library(affy)");
		System.out.println("R :: Library setting... : limma");
		c.eval("library(limma)");
		System.out.println("R :: Library setting... : GO.db");
		c.eval("library(GO.db)");
		System.out.println("R :: Library setting... : clusterProfiler");
		c.eval("library(clusterProfiler)");
		System.out.println("R :: Library setting... : hgu133a.db");
		c.eval("library(hgu133a.db)");
		System.out.println("R :: Library setting is done.");
	}
	//2
	public void setPath() throws RserveException, REXPMismatchException {
		//Path 설정.
		System.out.println("R :: Path Setting...");
		c.eval("setwd(\""+session.celFilePath+"\")");
		System.out.println("R :: Cel files :: "+session.celFilePath);
		
		//Cel File 경로 설정 및 불러오기
//		c.eval("fns <- list.celfiles(path=\"./\",full.names=TRUE)");
		c.eval("fns <- list.celfiles(path=\""+session.celFilePath+"\",full.names=TRUE)");
		x = c.eval("fns");
//		for(String str : x.asStrings()){
//			System.out.println(str);}
		
		//Sample.csv 불러오기
		c.eval("pheno = read.csv(\""+ session.sampleInfoPath +"\", sep=\",\", header=TRUE)");
		
		x = c.eval("GO_dbfile()");
		GOdbPath = x.asString();
		System.out.println("R :: GO db file : "+GOdbPath);
		
		System.out.println("R :: Sample info path is setted.");
	}
	//3
	public void setSearchValue(double pValue, double foldChange) throws RserveException{
		double[] value = new double[2];
		value[0] = pValue;
		value[1] = foldChange;
		System.out.println(" P Value="+pValue +", FoldChange="+foldChange);
		System.out.println("R :: Search Value Setting...");
		try {
			c.assign("searchValue", value);
			
		} catch (REngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//4
	public void findDEG(){
		System.out.println("Find DEG from cel files. and Save it as table.");
		String userDir = System.getProperty("user.dir");
		userDir = userDir.replaceAll("\\\\", "/");
		userDir += "/R_Finding_DEG.txt";
		System.out.println("R code is at :: "+ userDir);
		System.out.println("R :: Source code is executing...");
		try {
			c.eval("source(\"" + userDir + "\")");
			System.out.println("R Source code is executed successfully.");
		} catch (RserveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Deg findDEG(double pValue, double foldChange, int ranking) {
		return null;
	}
	public String getGOdb(){
		return GOdbPath;
	}
//	public void saveData() throws RserveException {
//		//Save data to .csv file
//		c.eval("write.csv(tab,\""+session.filePath+"/"+session.name+".csv\")");
//		System.out.println("DEG information is saved at "+session.filePath+"/"+session.name+".csv");
//	}
	//5
	public void mapId(){
		System.out.println("Mapping Probe ID to ENTREZ, SYMBOL ID.");
		String userDir = System.getProperty("user.dir");
		userDir = userDir.replaceAll("\\\\", "/");
		userDir += "/R_Mapping_ID.txt";
		System.out.println("R code is at :: "+ userDir);
		System.out.println("R :: Source code is executing...");
		try {
			c.eval("source(\"" + userDir + "\")");
			System.out.println("R Source code is executed successfully.");
		} catch (RserveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
