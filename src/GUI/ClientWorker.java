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
	
	}
	
	public void run() {
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
			System.out.println("Client Worker>> Waiting Response....");
			int message = ois.readInt();
			
			switch(message){
			case GUIServer.OPEN_HEATMAP_WINDOW:
				System.out.println("Client Worker>> Message Received : Open Heatmap Window");
				Heatmap.run();
				break;
			case GUIServer.OPEN_GO_WINDOW:
				System.out.println("Client Worker>> Message Received : Open GOVisualize Window");
				//GOVisualize.run();
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
	}

	protected Socket clientSocket;
	
}
