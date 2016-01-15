package Data;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class DataProcess {
	private RConnection c;
	public Session session;
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
	//1
	public void setLibrary() throws RserveException, REXPMismatchException {
		// Setting Library - affy, limma
		c.eval("library(affy)");
		c.eval("library(limma)");
	}
	//2
	public void setPath() throws RserveException, REXPMismatchException {
		//Path 설정.
		c.eval("setwd(\""+session.celFilePath+"\")");
		System.out.println("Cel files at :: "+session.celFilePath);
		
		//Cel File 경로 설정 및 불러오기
//		c.eval("fns <- list.celfiles(path=\"./\",full.names=TRUE)");
		c.eval("fns <- list.celfiles(path=\""+session.celFilePath+"\",full.names=TRUE)");
		x = c.eval("fns");
		for(String str : x.asStrings()){
			System.out.println(str);}
		//Sample.csv 불러오기
		c.eval("pheno = read.csv(\""+ session.sampleInfoPath +"\", sep=\",\", header=TRUE)");
	}
	//3
	public void readData() throws RserveException{
		String userDir = System.getProperty("user.dir");
		userDir = userDir.replaceAll("\\\\", "/");
		userDir += "/R_Code.txt";
		System.out.println("R code is at :: "+ userDir);
		c.eval("source(\"" + userDir + "\")");
	}
	public Deg findDEG(double pValue, double foldChange, int ranking) {
		return null;
	}
}
