package Data;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;

public class Main {
	/*
	public static void main(String[] args) {
		//MenuBar menu = new MenuBar();
		//Save�� �׽�Ʈ �ڵ�
		//Session session = menu.createSession("NAME_TEST", "C:/Users/JH/Desktop/CATMA/path", "C:/Users/JH/Desktop/CATMA/data/celfile", "C:/Users/JH/Desktop/CATMA/data");
		//menu.LoadSession(session);
		//menu.SaveSession();

		//Open�� �׽�Ʈ �ڵ�
		//Session session = menu.OpenSession("C:/Users/JH/Desktop/CATMA/path/NAME_TEST.ser");
		//menu.LoadSession(session);
		//System.out.println(session.Loaded());

		
		//Session created.
		Session session = menu.createSession("SessionName", "C:/data/celfiles", "C:/data/sample.csv");
		menu.loadSession(session); // the session is loaded on program.
		
		DataProcess process = new DataProcess(session); // session�� �Ѱ��༭ �ش� ������ path�� ���� R���� �����͸� ó���ϵ��� �Ѵ�.
		GOdb go;
		//DEG Finding
		try {
			process.setLibrary();//Library setting
			process.setPath();//Path setting
			process.setSearchValue(0.05, 1.5);//Search Value setting
			process.findDEG();//data processing with R
			//process.saveData();
			//session.deg.setDEG(process);//Set DEG to a database of session
			System.out.println("R Process is done.");
		} catch (RserveException | REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//GO Finding
		process.mapId();	

//		//GOdb read
//		try{
//			go = new GOdb(process.getGOdb());
//			//go.selectOntology(); //Test
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}
*/

}
