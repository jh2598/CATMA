package GUI;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

public class Main {
	
	public static void main(String args[]){
		//Starting Server
		GUIServer server = new GUIServer();
		new Thread(server).start();
		
		//Starting Main Window
		MainWindow.run();
	}		
}