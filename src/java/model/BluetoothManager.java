package model;

import java.io.IOException;
import java.net.Socket;

import controller.XBoxCtrlListener.Movement;

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
