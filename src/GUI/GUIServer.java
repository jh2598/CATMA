package GUI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/*********************************************
 * 
 * 		Singleton pattern Server Class
 * 
 * *******************************************/

public class GUIServer implements Runnable{

	
	public GUIServer() {
		running = true;
	}
	
	@Override
	public void run() {
		
		synchronized(this){
            this.runningThread = Thread.currentThread();
        }
		
		try {
			//Opening Server Socket
			socketListener = new ServerSocket(GUIServer.PORT_NUMBER);
			System.out.println("Server>> Start Running Server...");
			//*********Start Running Server!
			this.runServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Main method

	
	private void runServer(){
		
		//Multi-thread listening loop
		//When new client accepts, 
		while(isRunning()){
			try {
				clientSocket = socketListener.accept();
			} catch (IOException e) {
				System.err.println("Server>> Client Connection Failled!!");
				e.printStackTrace();
			}
			//Running new thread for client
			new Thread(new ClientWorker(clientSocket)).start();
		}
	}
	
	public static GUIServer getServer(){
		if(instance != null)
			instance = new GUIServer();
		
		return instance;
	}
		
	public synchronized void stopServer(){
		this.running = false;
		try {
			this.socketListener.close();
		} catch (IOException e) {
			System.err.println("Server>> Failed to stop server");
			e.printStackTrace();
		}
	}
	
	public boolean isRunning(){
		return this.running;		
	}
	
	//Instance Variables
	
	private static GUIServer instance;
	private boolean running;
	protected Thread runningThread;
	
	//server instance variables
	private ServerSocket socketListener;
	private Socket clientSocket;
	private DataOutputStream dos;
	private DataInputStream dis;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	//Message rule constants
	//Server message rules
	public static final int PORT_NUMBER = 9999;
	public static final int GO_PORT = 6666;
	public static final int OPEN_WINDOWCONTROLLER = 0;
	public static final int OPEN_HEATMAP_WINDOW = 1;
	public static final int OPEN_GO_WINDOW = 2;

}
