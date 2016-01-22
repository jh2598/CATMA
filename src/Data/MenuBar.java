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
		//��ο��� ����ȭ �Ǿ��ִ� ������ �ҷ���.
		System.out.println("MenuBar is Opening Session from '"+path+"'");
		//path = path.replaceAll("\\\\", "/");
		try{
		FileInputStream fis = new FileInputStream(path);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Session session = (Session) ois.readObject();
		System.out.println("MenuBar just has opened Session.");
		fis.close();
		ois.close();
		return session;//�ҷ��� ���� ��ȯ
		}catch(Exception e){
			System.out.println("MenuBar has failed to opening session.");
			e.printStackTrace();
			return null;//�ҷ��� ���� ��ȯ
		}
	}
	public Session loadSession(Session s){
		currentSession = s;
		return currentSession;//�ε��� ���� ��ȯ
	}
	public void saveSession(){
		//���� ������ ����ȭ�ؼ� ���� ��ο� ���� �������� ����.
		System.out.println("MenuBar is saving Session.");
		currentSession.save();
		System.out.println("MenuBar just has saved Session.");
	}
	public void saveAs(String name, String path){
		//������ ��θ� �ٲ㼭 ����
		currentSession.setName(name);
		currentSession.setFilePath(path);
		saveSession();
	}
	
	public Session getSession(){
		return currentSession;
	}
}
