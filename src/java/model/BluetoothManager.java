package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import controller.XBoxCtrlListener.Movement;

public class BluetoothManager {
	
	
	private Socket socket;
	private BufferedReader bufferedReader;
	int PORT = 5000;
	public BluetoothManager(){
		socket = null;
	}

	public boolean connect(String ip){
		try {
			socket = new Socket(ip, PORT);
			Thread t = new Thread() {
				
				@Override
				public void run() {
					listen();
				}
			};
			t.start();
			
		} catch (Exception e){
			return false;
		}
		return true;
	
	}
	
	
	public void listen(){
		String fromclient;

		
		try {
			bufferedReader = new BufferedReader(new InputStreamReader (socket.getInputStream()));

			while( ! socket.isClosed()) {
					fromclient = bufferedReader.readLine();
					System.out.println(fromclient);
			}
		} catch (IOException e) {
			
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	
	public void sendMovement(Movement m) {
		if (socket != null && socket.isConnected() && !socket.isClosed()){
			try{
				double radian = Math.toRadians(m.direction);
				String toSend = "move:"
								+Math.cos(radian)
								+","
								+Math.sin(radian)
								+"\n";
				
				double trig = m.RTrigger - m.LTrigger;
				if(trig != 0){
					String trigger = "trigger";
					if (trig > 0){
						trigger += "R";
					}
					else{
						trigger += "L";
					}
					toSend += trigger + ":" + trig + "\n";
				}
				System.out.println(toSend);
				socket.getOutputStream().write(toSend.getBytes());
			}
			catch(Exception e ){
				System.out.println("erreur");
			}
		}
	}
	
	
}
