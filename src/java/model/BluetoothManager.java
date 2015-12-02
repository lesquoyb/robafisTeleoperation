package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import controller.XBoxCtrlListener.Movement;

public class BluetoothManager {


	private Socket socket;
	private BufferedReader bufferedReader;
	int PORT = 5000;
	public List<Double> colours;
	public List<Double> gyro;
	public List<Double> dist;
	public BluetoothManager(){
		socket = null;
		colours = new ArrayList<>();
		gyro = new ArrayList<>();
		dist = new ArrayList<>();


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
				if(fromclient.startsWith("colors: ")){
					setColors(fromclient.split("colors: ")[1]);
				}
				else if(fromclient.startsWith("gyro: ")){
					setGyro(fromclient.split("gyro: ")[1]);
				}
				else if(fromclient.startsWith("dist: ")){
					//setDist(fromclient.split("dist: ")[1]);
				}
			}
		} catch (IOException e) {

			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	private void setColors(String colors){
		String[] tmp = colors.split(" ");
		colours.clear();
		for(String col : tmp){
			colours.add(Double.parseDouble(col));
		}
	}


	private void setGyro(String s){
		String[] g = s.split(" ");
		gyro.clear();
		for(String val : g){
			gyro.add(Double.parseDouble(val));
		}
	}

	private void setDist(String s){
		String[] g = s.split(" ");
		dist.clear();
		for(String val : g){
			dist.add(Double.parseDouble(val));
		}
	}


	public void sendMovement(Movement m) {
		if (socket != null && socket.isConnected() && !socket.isClosed()){
			try{
				double radian = Math.toRadians(m.direction);
				String toSend = "move:"
						+(Math.cos(radian) * m.magnitude)
						+","
						+(Math.sin(radian) * m.magnitude)
						+"\n";
				
				System.out.println(toSend);

				double trig = m.RTrigger - m.LTrigger;
				if( Math.abs(trig) < 0.2) trig = 0;

				String trigger = "trigger";
				if (trig > 0){
					trigger += "R";
				}
				else{
					trigger += "L";
				}
				toSend += trigger + ":" + trig + "\n";

				socket.getOutputStream().write(toSend.getBytes());

			}
			catch(Exception e ){
				System.out.println("erreur d'envoi bluetooth: " + e.getMessage());
			}
		}
	}


}
