package Data;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;

public class Main {
	
	public static void main(String[] args) {
		MenuBar menu = new MenuBar();
		//Save용 테스트 코드
		//Session session = menu.createSession("NAME_TEST", "C:/Users/JH/Desktop/CATMA/path", "C:/Users/JH/Desktop/CATMA/data/celfile", "C:/Users/JH/Desktop/CATMA/data");
		//menu.LoadSession(session);
		//menu.SaveSession();

		//Open용 테스트 코드
		//Session session = menu.OpenSession("C:/Users/JH/Desktop/CATMA/path/NAME_TEST.ser");
		//menu.LoadSession(session);
		//System.out.println(session.Loaded());
		
		//Session created.
		Session session = menu.createSession(/*Session name*/"NAME_TEST", /*Session file path*/"C:/Data", /*Cel file path*/"C:/data/celfiles", /*Sample info path*/"C:/data/sample.csv");
		menu.loadSession(session); // the session is loaded on program.
		DataProcess process = new DataProcess(session); // session을 넘겨줘서 해당 세션의 path에 따라 R에서 데이터를 처리하도록 한다.
		try {
			process.setLibrary();//Library setting
			process.setPath();//Path setting
			process.setSearchValue(0.05, 1.5);//Search Value setting
			process.readData();//data processing with R
			System.out.println("R Process is done.");
		} catch (RserveException | REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
