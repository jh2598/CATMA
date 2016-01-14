package Data;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;

public class Main {
	
	public static void main(String[] args) {
		MenuBar menu = new MenuBar();
		//Save용 테스트 코드
//		Session session = menu.createSession("NAME_TEST", "C:/Users/JH/Desktop/CATMA/path", "C:/Users/JH/Desktop/CATMA/data/celfile", "C:/Users/JH/Desktop/CATMA/data");
		//menu.LoadSession(session);
		//menu.SaveSession();

		//Open용 테스트 코드
//		Session session = menu.OpenSession("C:/Users/JH/Desktop/CATMA/path/NAME_TEST.ser");
//		menu.LoadSession(session);
//		System.out.println(session.Loaded());
		
		//Session created.
		Session session = menu.createSession("NAME_TEST", "C:/Users/JH/Desktop/CATMA/path", "C:/Users/JH/Desktop/CATMA/data/celfiles", "C:/Users/JH/Desktop/CATMA/data");
		menu.loadSession(session); // the session is loaded on program.
		DataProcess process = new DataProcess(session); // session을 넘겨줘서 해당 세션의 path에 따라 R에서 데이터를 처리하도록 한다.
		try {
			process.setLibrary();//Library setting
			process.setPath();
		} catch (RserveException | REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
