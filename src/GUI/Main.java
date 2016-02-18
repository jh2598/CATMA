package GUI;


import java.net.*;

public class Main {
	
	public static void main(String args[]){
		GUIServer server = new GUIServer();
		new Thread(server).start();
	}
	
}
