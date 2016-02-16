package GUI;

import java.io.*;
import java.net.*;

public class WinController {

	//window running methods
	public static void runMainWindow(){
		MainWindow.run();
	}
	
	public static void runHeatmap(){
		Heatmap.run();
	}
	
	public static void main(String[] args) {
		//Starting server
		Server server = new Server();
		server.runWindowServer();
	}
}

final class Server{
	
	//Server Constructor
	public Server(){
		
		//Opening server	
		try {
			mainWindowServer = new ServerSocket(Server.MAINWINDOW_PORT);	//port number must be over 1023
			heatmapServer = new ServerSocket(Server.HEATMAP_PORT);
			goServer = new ServerSocket(Server.GO_PORT);
			System.out.println("Server>> Opening Port...");
			System.out.println("Server>> Port Number - Main Window:9999 | Heatmap:8888 | GO:7777");
			WinController.runMainWindow();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//start server method
	public void runWindowServer(){
		//Linking with client
		try {
			//Linked with main window!
			mainWindowSocket = mainWindowServer.accept();
			System.out.println("Server>> Sever Connected : MainWindow");
			is = new DataInputStream(mainWindowSocket.getInputStream());
	        os = new PrintStream(mainWindowSocket.getOutputStream());
	        
	     // As long as we receive data, echo that data back to the client.
	        while (true) {
	          message = is.readInt();

	          switch(message){
	          case CALL_HEATMAP_WINDOW:
	        	  WinController.runHeatmap();
	        	  runHeatmapServer();
	        	  break;
	          case CALL_GO_WINDOW:
	        	  break;
	          }
	        }
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void runHeatmapServer(){
		//Linking with Heatmap
		try {
			heatmapSocket = heatmapServer.accept();
			System.out.println("Server>> server connected with Heatmap");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void runGoServer(){
		//Linking with Heatmap
		try {
			goSocket = goServer.accept();
			System.out.println("Server>> server connected with GO");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//server instance variables
	ServerSocket mainWindowServer;
	ServerSocket heatmapServer;
	ServerSocket goServer;
	Socket mainWindowSocket;
	Socket heatmapSocket;
	Socket goSocket;
	DataInputStream is;
	PrintStream os;
	int message;
	//Server message rules
	public static final int MAINWINDOW_PORT = 9999;
	public static final int HEATMAP_PORT = 8888;
	public static final int GO_PORT = 7777;
	public static final int CALL_HEATMAP_WINDOW = 0;
	public static final int CALL_GO_WINDOW = 1;
	
}