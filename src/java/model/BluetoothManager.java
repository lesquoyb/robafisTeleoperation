package model;

import java.net.Socket;

public class BluetoothManager {
	
	public Socket socket;
	int PORT = 5000;
	public BluetoothManager(){
		socket = null;
	}

	public boolean connect(String ip){
		try {
			socket = new Socket(ip, PORT);
		} catch (Exception e){
			return false;
		}
		return true;
		
	}
	
	
}
