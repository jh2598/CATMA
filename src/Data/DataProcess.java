package Data;

import java.io.Serializable;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import Data.UserDefinedType.*;

public class DataProcess implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5763457894404886383L;
	
	static private RConnection c;
	public Session session;
	public String GOdbPath;
	public DatabaseHelper db;
	public EnrichedGeneOntology[] ego = null;
	static REXP x;

	
	public DataProcess(Session session) {
		try {
			this.session = session;
			session.setDataProcess(this);
			c = new RConnection();
			x = c.eval("R.version.string");
			System.out.println("R Connected successfully :: " + x.asString());
		} catch (RserveException | REXPMismatchException e) {
			System.err.println("RConnection not Constructed. Please check Rserve running.");
			//e.printStackTrace();
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
	public String getDBPath(){
		try {
			x = c.eval("library(GO.db)");
			x = c.eval("GO_dbfile()");
		} catch (RserveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			GOdbPath = x.asString();
		} catch (REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("R :: GO db file : "+GOdbPath);
		return GOdbPath;
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
		String userDir = getUserDir("/R_Finding_DEG.R");
		System.out.println("R code is at :: "+ userDir);
		System.out.println("R :: Source code is executing...");
		try {
			c.eval("source(\"" + userDir + "\")");
			System.out.println("R Source code is executed successfully.");
		} catch (RserveException e) {
			e.printStackTrace();
		}
	}
	//UserDirectory를 R에서 사용가능하도록 \를 치환 후 R코드 파일을 받은 경로를 덧붙임
	public static String getUserDir(String fileName){
		String userDir = System.getProperty("user.dir");
		userDir = userDir.replaceAll("\\\\", "/");
		userDir += "/"+fileName;
		return userDir;
	}
	public static String getUserDir(){
		String userDir = System.getProperty("user.dir");
		userDir = userDir.replaceAll("\\\\", "/");
		return userDir;
	}
	public String getGOdb(){
		return GOdbPath;
	}
	//5
	public void mapId(){
		System.out.println("Mapping Probe ID to ENTREZ, SYMBOL ID.");
		String userDir = getUserDir("/R_Mapping_ID.R");
		System.out.println("R code is at :: "+ userDir);
		System.out.println("R :: Source code(Mapping ID) is executing...");
		try {
			c.eval("source(\"" + userDir + "\")");
			System.out.println("R Source code(Mapping ID) is executed successfully.");
		} catch (RserveException e) {
			e.printStackTrace();
		}
		
		db = new DatabaseHelper(this);
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
	//Entrez id를 GO로 변환한다.
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
	//6
	//assign ego
	public void overRepresentation(){
		String userDir = getUserDir("GO_OverRepresentation.R");
		try {		
			System.out.println("R :: Source code(GO OverRepresentation) is executing...");
			c.eval("source(\"" + userDir + "\")");
			System.out.println("R Source code(GO OverRepresentation) is executed successfully.");
			this.ego = selectEnrichedGeneOntolgy();
			session.ego = this.ego;
			session.egoGraph = new EGOGraph(ego);
		} catch (RserveException | REXPMismatchException e) {
			e.printStackTrace();
		}
	}
	
	//R에서 생성된 ego를 ExpressedGeneOntology 배열로 정리해서 반환
	public EnrichedGeneOntology[] selectEnrichedGeneOntolgy() throws REXPMismatchException, RserveException{
		x = c.eval("nrow(summary(ego.up.bp))");
		int egoLength = x.asInteger();
		EnrichedGeneOntology ego[] = new EnrichedGeneOntology[egoLength];
		
		String[] egoAttr = new String[9];
		for(int row = 1; row <= egoLength;row++){
			for(int col = 1; col <= 9; col++){
				x = c.eval("summary(ego.up.bp)["+ row + ","+ col +"]");
				egoAttr[col-1] = x.asString(); 
			}
			ego[row-1] = new EnrichedGeneOntology(egoAttr[0], egoAttr[1], egoAttr[2], egoAttr[3], egoAttr[4], egoAttr[5], egoAttr[6], egoAttr[7], egoAttr[8]);
		}
		return ego;
	}
	
	//ego list에서 인자로 받은 go를 찾아서 그에 해당하는 gene list를 반환
	public String[] getGeneListOf(GeneOntology go){
		for(int i=0;i<ego.length;i++){
			if(ego[i].getGoId() == go.getGo_id()){
				return ego[i].getGeneList();
			}
		}
		System.err.println("There is no such Gene Ontology");
		return null;
	}
	public String[] getGeneListOf(String GoID){
		for(int i=0;i<ego.length;i++){
			if(ego[i].getGoId() == GoID){
				return ego[i].getGeneList();
			}
		}
		System.err.println("There is no such Gene Ontology");
		return null;
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
	public String[] getFromSample(String col){
		try {
			x = c.eval(col);
			return x.asStrings();
		} catch (RserveException | REXPMismatchException e) {
			e.printStackTrace();
			return null;
		}
	}
	public String[] getCelFileNames(){
		return getFromSample("colnames(esetSel)");
	}
	public String[] getSampleNames(){
		return getFromSample("pheno[,2]");
	}
	public String[] getGroup(){
		return getFromSample("pheno[,3]");
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
