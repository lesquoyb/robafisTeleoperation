package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

	public static final Lock l = new ReentrantLock();
	public synchronized boolean connect(String ip){
		try {
			socket = new Socket(ip, PORT);
			l.lock();

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


	public synchronized void listen(){
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
				else if(fromclient.startsWith("ready")){
					l.unlock();
					System.out.println("fuck");
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

	int 	YMAXSPEED = 500,
			XMAXSPEED = 200;
	static final double dead_zone = 0.1;

	public String movement(Movement m){
		double speedR, speedL;
		double y, x;
		double radians = Math.toRadians(m.direction);
		y = apply_dead_zone(Math.cos(radians) * m.magnitude);
		x = apply_dead_zone(Math.sin(radians) * m.magnitude);
		
		speedR = speedL = YMAXSPEED * y;


		if (-0.8 < x && x < 0.8){
			speedR -= x * XMAXSPEED;
			speedL += x * XMAXSPEED;
		}
		else{
			speedR += x * XMAXSPEED;
			speedL -= x * XMAXSPEED;
			
		}
				
		
		if ( y == 0 ){
			if (x > 0.3){
				speedR = x * YMAXSPEED/2;
				speedL = x * YMAXSPEED/2;
			}
			if (-0.3 > x){
				speedR = x * YMAXSPEED/2;
				speedL = x * YMAXSPEED/2;
			}
		}

		return "move:"
				+ (int) speedL
				+","
				+ (int) speedR
				+"\n";

	}

	public double apply_dead_zone(double i ){
		if( Math.abs(i) < dead_zone){
			return 0;
		}
		return i;
	}
	
	synchronized public void sendMovement(Movement m) {
		l.lock();
		if ( socket != null && socket.isConnected() && !socket.isClosed() ){
			try{
				
				String toSend = movement(m);
				
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
		l.unlock();
	}


}
