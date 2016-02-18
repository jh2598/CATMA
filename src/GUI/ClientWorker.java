package GUI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientWorker implements Runnable {
	


	//Constructor, Receive Socket from main server
	public ClientWorker(Socket socket) {
		clientSocket = socket;
		try {
			dos = (DataOutputStream) clientSocket.getOutputStream();
			dis = (DataInputStream) clientSocket.getInputStream();
			ois = (ObjectInputStream) clientSocket.getInputStream();
			oos = (ObjectOutputStream) clientSocket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		
		try {
			int message = dis.readInt();
			
			switch(message){
			case GUIServer.OPEN_WINDOWCONTROLLER:
				break;
			case GUIServer.OPEN_HEATMAP_WINDOW:
				break;
			case GUIServer.OPEN_GO_WINDOW:
				break;
			}
			
		} catch (IOException e) {
			System.out.println("Client Worker>> Wrong Message Communication Rule!");
			e.printStackTrace();
		}
		

		// TODO Auto-generated method stub

	}

	protected Socket clientSocket;
	private DataOutputStream dos;
	private DataInputStream dis;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
}
