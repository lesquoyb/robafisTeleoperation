package controller;

import ch.aplu.xboxcontroller.XboxControllerListener;


public class XBoxCtrlListener implements XboxControllerListener{


	public class Movement{
		public double direction;
		public double magnitude;
		public double vitesse = 0.5;
		public double vitesseP = 1;
		public double RTrigger;
		public double LTrigger;
		public boolean end = false;
	}
	
	public Movement movement;
	public boolean connected;
	public XBoxCtrlListener() {
		movement = new Movement();
		connected = false;
	}

	@Override
	public void buttonA(boolean pressed) {
		movement.vitesse = Math.max(movement.vitesse/1.3, 0.3);
	}

	@Override
	public void buttonB(boolean pressed) {
		movement.vitesse = Math.min(movement.vitesse * 1.3, 2) ;		
	}

	@Override
	public void buttonX(boolean pressed) {
		System.out.println("x pressed");
		
	}

	@Override
	public void buttonY(boolean pressed) {
		System.out.println("y pressed");
		
	}

	@Override
	public void back(boolean pressed) {
		System.out.println("back pressed");
		
	}

	@Override
	public void start(boolean pressed) {
		movement.end = true;
	}

	@Override
	public void leftShoulder(boolean pressed) {
		movement.vitesseP = Math.max(movement.vitesseP - 0.3, 0.3);
	}

	@Override
	public void rightShoulder(boolean pressed) {
		movement.vitesseP = Math.min(movement.vitesseP + 0.3, 2);
	}

	@Override
	public void leftThumb(boolean pressed) {
	}

	@Override
	public void rightThumb(boolean pressed) {
	}

	@Override
	public void dpad(int direction, boolean pressed) {
		System.out.println("qpad " + direction + " " + pressed);
		
	}

	@Override
	public void leftTrigger(double value) {
		movement.LTrigger = value;
		
	}

	@Override
	public void rightTrigger(double value) {
		movement.RTrigger = value;
	}

	@Override
	public void leftThumbMagnitude(double magnitude) {
		movement.magnitude = magnitude;
	}

	@Override
	public void leftThumbDirection(double direction) {
		movement.direction = direction;
	}

	@Override
	public void rightThumbMagnitude(double magnitude) {
		//System.out.println("r thum magn "+magnitude);
		
	}

	@Override
	public void rightThumbDirection(double direction) {
		//System.out.println("r thumb dir " + direction);
		
	}

	@Override
	public void isConnected(boolean connected) {
		this.connected = connected;
		
	}
	


	
}
