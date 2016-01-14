package Data;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MenuBar {
	private static final long serialVersionUID = -4961998355515742346L;
	Session currentSession;
	public MenuBar() {
		System.out.println("MenuBar Called.");
	}
	
	public Session CreateSession(String name, String file, String cel, String sample){
		System.out.println("MenuBar is creating Session.");
		Session session = new Session(name, file, cel, sample);
		System.out.println("MenuBar just has created Session.");
		return session;
	}
	public Session OpenSession(String path){
		//경로에서 직렬화 되어있는 세션을 불러옴.
		System.out.println("MenuBar is Opening Session from '"+path+"'");
		try{
		FileInputStream fis = new FileInputStream(path);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Session session = (Session) ois.readObject();
		System.out.println("MenuBar just has opened Session.");
		return session;//불러온 세션 반환
		}catch(Exception e){
			System.out.println("MenuBar has failed to opening session.");
			e.printStackTrace();
			return null;//불러온 세션 반환
		}
		
		
	}
	public Session LoadSession(Session s){
		currentSession = s;
		return null;//로드한 세션 반환
	}
	public void SaveSession(){
		//현재 세션을 직렬화해서 파일 경로에 세션 네임으로 저장.
		System.out.println("MenuBar is saving Session.");
		currentSession.Save();
		System.out.println("MenuBar just has saved Session.");
	}
	public void SaveAs(String name, String path){
		currentSession.setName(name);
		currentSession.setFilePath(path);
		SaveSession();
	}
}
