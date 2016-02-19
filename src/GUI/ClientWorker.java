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
		System.out.println("Client Worker>> New Client Opened");
		clientSocket = socket;
		
		try {
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			ois = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			System.out.println("Client Worker>> Waiting for message....");
			int message = ois.readInt();
			System.out.println("Client Worker>> Message Received : "+ message);
			
			switch(message){
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
	protected ObjectInputStream ois;
	protected ObjectOutputStream oos;
	
}
