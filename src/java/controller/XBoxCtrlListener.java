package controller;

import ch.aplu.xboxcontroller.XboxControllerListener;

public class XBoxCtrlListener implements XboxControllerListener{


		@Override
		public void buttonA(boolean pressed) {
			System.out.println("a pressed");
			
		}

		@Override
		public void buttonB(boolean pressed) {
			System.out.println("b pressed");
			
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
			System.out.println("start pressed");
			
		}

		@Override
		public void leftShoulder(boolean pressed) {
			System.out.println("left shoulder");
			
		}

		@Override
		public void rightShoulder(boolean pressed) {
			System.out.println("right shouler");
			
		}

		@Override
		public void leftThumb(boolean pressed) {
			System.out.println("l thumb");
			
		}

		@Override
		public void rightThumb(boolean pressed) {
			System.out.println("r thumb");
			
		}

		@Override
		public void dpad(int direction, boolean pressed) {
			System.out.println("qpad " + direction + " " + pressed);
			
		}

		@Override
		public void leftTrigger(double value) {
			System.out.println("l trigger "+value);
			
		}

		@Override
		public void rightTrigger(double value) {
			System.out.println("r trigger "+value);
			
		}

		@Override
		public void leftThumbMagnitude(double magnitude) {
			System.out.println("l thumb magn "+magnitude);
			
		}

		@Override
		public void leftThumbDirection(double direction) {
			System.out.println("l thumb dir "+direction);
			
		}

		@Override
		public void rightThumbMagnitude(double magnitude) {
			System.out.println("r thum magn "+magnitude);
			
		}

		@Override
		public void rightThumbDirection(double direction) {
			System.out.println("r thumb dir " + direction);
			
		}

		@Override
		public void isConnected(boolean connected) {
			System.out.println("is connected");
			
		}
		
	

	
}
