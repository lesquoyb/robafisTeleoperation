package controller;

import java.util.Timer;
import java.util.TimerTask;

import ch.aplu.xboxcontroller.XboxController;
import view.JoystickView;
import view.SettingsPanel;

public class JoystickController {

	public XboxController model;
	private SettingsPanel view;
	public BluetoothController bluetoothCtrl;
	public XBoxCtrlListener listener;
	
	public JoystickController(XboxController m, BluetoothController b) {
		model = m;
		bluetoothCtrl = b;
		listener = new XBoxCtrlListener();
		
			m.addXboxControllerListener(listener);
			listener.connected = m.isConnected();
			bluetoothCtrl.model.bController = this;
		
	}
	
	JoystickView joyView;
	public void addJoystickView(JoystickView v){
		joyView = v;
	}
	
	public void setView(SettingsPanel j){
		view = j;
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			
			@Override
			public void run() {
				refreshControllerView(listener.connected);
				if(listener.connected && joyView != null){
					joyView.armsSpeed.setText(Integer.toString((int)(10*listener.movement.vitesseP)));
					joyView.motorSpeed.setText(Integer.toString((int)(10*listener.movement.vitesse)));
				}
			}
		}, 0, 10);
	}
	
	public void start(){
		Timer t = new Timer();
		listener.movement.end = false;
		t.schedule( new TimerTask() {
			
			@Override
			public void run() {
				if(listener.connected){
					bluetoothCtrl.sendAll(listener);
				}
			}
		},0, 50 ) ;	

	}

	
	public void refreshControllerView(boolean connected){
		view.refreshControllerConnected(connected);
	}

}
