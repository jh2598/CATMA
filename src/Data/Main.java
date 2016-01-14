package Data;

public class Main {

	public static void main(String[] args) {
		MenuBar menu = new MenuBar();
		//Save용 테스트 코드
		//Session session = menu.CreateSession("NAME_TEST", "C:/Users/JH/Desktop/CATMA/path", "C:/Users/JH/Desktop/CATMA/path", "C:/Users/JH/Desktop/CATMA/path");
		//menu.LoadSession(session);
		//menu.SaveSession();

		//Open용 테스트 코드
		Session session = menu.OpenSession("C:/Users/JH/Desktop/CATMA/path/NAME_TEST.ser");
		menu.LoadSession(session);
		System.out.println(session.Loaded());

	}
}
