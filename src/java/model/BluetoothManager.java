package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import controller.JoystickController;
import controller.PhaseController;
import controller.XBoxCtrlListener.Movement;

public class BluetoothManager {

	private Socket socket;
	private BufferedReader bufferedReader;
	int PORT = 5000;
	public ArrayList<Integer> colours;
	public ArrayList<Integer> gyro;
	public ArrayList<Integer> dist;

	public PhaseController phaseController;
	public JoystickController bController;

	public BluetoothManager() {
		socket = null;
		colours = new ArrayList<>();
		gyro = new ArrayList<>();
		dist = new ArrayList<>();
	}

	public boolean connect(String ip) {
		try {
			socket = new Socket(ip, PORT);

			Thread t = new Thread() {

				@Override
				public void run() {
					listen();
				}
			};
			t.start();

		} catch (Exception e) {
			return false;
		}
		return true;

	}

	public void listen() {
		String fromRobot;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			phaseController.changePhase(Phases.FollowL);
			while (!socket.isClosed()) {
				fromRobot = bufferedReader.readLine();
				if (fromRobot != null) {
					if (fromRobot.startsWith("colors: ")) {
						setColors(fromRobot.split("colors: ")[1]);
					} else if (fromRobot.startsWith("gyro: ")) {
						setGyro(fromRobot.split("gyro: ")[1]);
					} else if (fromRobot.startsWith("dist: ")) {
						setDist(fromRobot.split("dist: ")[1]);
					} else if (fromRobot.startsWith("ready")) {
						phaseController.changePhase(Phases.Teleop);
						bController.start();
					}
					else if (fromRobot.startsWith("c:")){
						phaseController.changePhase(Phases.Color);
						System.out.println("couleur: "+  fromRobot.substring(2));
					}
				}
			}
		} catch (IOException e) {

			// e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	private void setColors(String colors) {
		String[] tmp = colors.split(" ");
		colours.clear();
		for (String col : tmp) {
			colours.add(Integer.parseInt(col));
		}
	}

	private void setGyro(String s) {
		String[] g = s.split(" ");
		gyro.clear();
		for (String val : g) {
			gyro.add(Integer.parseInt(val));
		}
	}

	private void setDist(String s) {
		String[] g = s.split(" ");
		dist.clear();
		for (String val : g) {
			dist.add(Integer.parseInt(val));
		}
	}

	int YMAXSPEED = 500, XMAXSPEED = 200;
	static final double dead_zone = 0.3;

	public String movement(Movement m) {
		double speedR, speedL;
		double y, x;
		double radians = Math.toRadians(m.direction);
		y = apply_dead_zone(Math.cos(radians) * m.magnitude);
		x = apply_dead_zone(Math.sin(radians) * m.magnitude);

		speedR = speedL = YMAXSPEED * y * m.vitesse;

		speedR += x * XMAXSPEED * m.vitesse;
		speedL -= x * XMAXSPEED * m.vitesse;

		return "move:" + (int) speedL + "," + (int) speedR + "\n";

	}

	public double apply_dead_zone(double i) {
		if (Math.abs(i) < dead_zone) {
			return 0;
		}
		return i - dead_zone;
	}

	public void sendMovement(Movement m) {
		if (socket != null && socket.isConnected() && !socket.isClosed()) {
			try {

				String toSend = movement(m);

				System.out.println(toSend);

				double trig = m.RTrigger - m.LTrigger;
				if (Math.abs(trig) < 0.2){
					trig = 0;
				}
				
				String trigger = "trigger";
				if (trig > 0) {
					trigger += "R";
				} else {
					trigger += "L";
				}
				toSend += trigger + ":" + trig * m.vitesseP + "\n";

				if (m.end) {
					toSend = "end_";
					m.end = false;
					phaseController.changePhase(Phases.End);
				}

				socket.getOutputStream().write(toSend.getBytes());

			} catch (Exception e) {
				System.out.println("erreur d'envoi bluetooth: " + e.getMessage());
			}
		}
	}

}
