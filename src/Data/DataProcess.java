package Data;

import java.sql.SQLException;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class DataProcess {
	private RConnection c;
	public Session session;
	public String GOdbPath;
	public Database db;
	REXP x;

	public DataProcess(Session session) {
		try {
			this.session = session;
			c = new RConnection();
			x = c.eval("R.version.string");
			System.out.println("R Connected successfully :: " + x.asString());
		} catch (RserveException | REXPMismatchException e) {
			System.out.println("RConnection not Constructed. Please check Rserve running.");
			e.printStackTrace();
		}
	}
	public void init(){
		try {
			setLibrary();
			setPath();
		} catch (RserveException | REXPMismatchException e) {
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
		
		//GO db 경로 체크
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
			e.printStackTrace();
		}
	}
	//4
	public void findDEG(){
		System.out.println("Find DEG from cel files. and Save it as table.");
		String userDir = System.getProperty("user.dir");
		userDir = userDir.replaceAll("\\\\", "/");
		userDir += "/R_Finding_DEG.R";
		System.out.println("R code is at :: "+ userDir);
		System.out.println("R :: Source code is executing...");
		try {
			c.eval("source(\"" + userDir + "\")");
			System.out.println("R Source code is executed successfully.");
		} catch (RserveException e) {
			e.printStackTrace();
		}
	}
	public Database findDEG(double pValue, double foldChange, int ranking) {
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
		userDir += "/R_Mapping_ID.R";
		System.out.println("R code is at :: "+ userDir);
		System.out.println("R :: Source code is executing...");
		try {
			c.eval("source(\"" + userDir + "\")");
			System.out.println("R Source code is executed successfully.");
		} catch (RserveException e) {
			e.printStackTrace();
		}
		
		db = new Database(this);
		session.setDB(db);
		db.saveSample();
		
		//Mapping Entrez id to GO id
		try {
			db.createEntrezToGoTable();
			db.insertEntrezToGoTable(entrezToGoMapping());
//			db.GoToEntrez("GO:0000122");
//			db.entrezToGo("7490");
		} catch (RserveException | REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String[][] entrezToGoMapping() throws RserveException, REXPMismatchException{
		c.eval("eg = bitr(genes.total, fromType=\"ENTREZID\", toType=\"GO\", annoDb=\"org.Hs.eg.db\")");
		x = c.eval("nrow(eg)");
		String[][] eg = new String[4][x.asInteger()];
		x = c.eval("eg[,1]");
		eg[0] = x.asStrings(); //ENTREZ
		x = c.eval("eg[,2]");
		eg[1] = x.asStrings(); //GO
		x = c.eval("eg[,3]");
		eg[2] = x.asStrings(); //EVIDENCE
		x = c.eval("eg[,4]");
		eg[3] = x.asStrings(); //ONTOLOGY
//		for(int i=0;i<4;i++){
//			for(int j=0;j<10;j++){
//				System.out.print(eg[i][j]+" / ");
//			}
//			System.out.println();
//		}
		return eg;
	}
	public String[][] getSampleId(){ // Mapped ID 0:PROBES, 1:ENTREZ, 2:SYMBOL
		String[][] str = new String[3][getSampleLength()];
		try {
			str[0] = getSampleProbes();
			str[1] = getSampleEntrez();
			str[2] = getSampleSymbol();
		} catch (RserveException | REXPMismatchException e) {
			e.printStackTrace();
		}
		return str;
	}
	public String[][] getSampleExp(){ // Expression set
		int num = getSampleNumber();// # of Samples
		int len = getSampleLength();// # of Selected Probes
		String[][] str = new String[num][len];
		for(int i=0;i<num;i++){
			for(int j=0;j<len;j++){
				try {
					x = c.eval("esetSel["+(j+1)+","+(i+1)+"]");
					str[i][j] = x.asString();
				} catch (RserveException | REXPMismatchException e) {
					e.printStackTrace();
				}
			}
		}
		return str;
	}
	public String[] getSampleNames(){
		try {
			x = c.eval("colnames(esetSel)");
			return x.asStrings();
		} catch (RserveException | REXPMismatchException e) {
			e.printStackTrace();
			return null;
		}
	}
	public int getSampleNumber(){//# of Samples
		try {
			x = c.eval("ncol(esetSel)");
			return x.asInteger();
		} catch (RserveException | REXPMismatchException e) {
			e.printStackTrace();
			return -1;
		}
		
	}
	public int getSampleLength(){// # of Selected Probes
		try {
			x = c.eval("length(PROBES)");
			return x.asInteger();
		} catch (RserveException | REXPMismatchException e) {
			e.printStackTrace();
			return -1;
		}
	}
	private String[] getSampleProbes() throws RserveException, REXPMismatchException{
		x = c.eval("PROBES");
		return x.asStrings();
	}
	private String[] getSampleEntrez() throws RserveException, REXPMismatchException {
		x = c.eval("ENTREZID");
		return x.asStrings();
	}
	private String[] getSampleSymbol() throws RserveException, REXPMismatchException {
		x = c.eval("SYMBOLID");
		return x.asStrings();
	}
}
