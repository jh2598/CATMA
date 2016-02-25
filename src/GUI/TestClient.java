package GUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestClient {

	
	public static void main(String args[]){
		try {
			
			Socket client = new Socket(InetAddress.getLocalHost(),GUIServer.PORT_NUMBER);
			ObjectInputStream input = new ObjectInputStream(client.getInputStream());
			ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
			
			output.writeInt(GUIServer.OPEN_HEATMAP_WINDOW);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
