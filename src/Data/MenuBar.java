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
	
	public Session createSession(String name, String cel, String sample){
		System.out.println("MenuBar is creating Session.");
		Session session = new Session(name, cel, sample);
		System.out.println("MenuBar just has created Session.");
		return session;
	}
	public Session openSession(String path){
		//경로에서 직렬화 되어있는 세션을 불러옴.
		System.out.println("MenuBar is Opening Session from '"+path+"'");
		//path = path.replaceAll("\\\\", "/");
		try{
		FileInputStream fis = new FileInputStream(path);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Session session = (Session) ois.readObject();
		System.out.println("MenuBar just has opened Session.");
		fis.close();
		ois.close();
		return session;//불러온 세션 반환
		}catch(Exception e){
			System.out.println("MenuBar has failed to opening session.");
			e.printStackTrace();
			return null;//불러온 세션 반환
		}
	}
	public Session loadSession(Session s){
		currentSession = s;
		return currentSession;//로드한 세션 반환
	}
	public void saveSession(){
		//현재 세션을 직렬화해서 파일 경로에 세션 네임으로 저장.
		System.out.println("MenuBar is saving Session.");
		currentSession.save();
		System.out.println("MenuBar just has saved Session.");
	}
	public void saveAs(String name, String path){
		//세션의 경로만 바꿔서 저장
		currentSession.setName(name);
		currentSession.setFilePath(path);
		saveSession();
	}
	
	public Session getSession(){
		return currentSession;
	}
}
