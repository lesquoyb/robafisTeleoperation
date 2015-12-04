package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import ch.aplu.xboxcontroller.XboxController;
import view.JoystickPanel;

public class JoystickController implements ActionListener {

	public XboxController model;
	public JoystickPanel view;
	public BluetoothController bluetoothCtrl;
	public XBoxCtrlListener listener;
	
	public JoystickController(XboxController m, BluetoothController b) {
		model = m;
		bluetoothCtrl = b;
		listener = new XBoxCtrlListener();
		m.addXboxControllerListener(listener);
		bluetoothCtrl.model.bController = this;
	}
	
	public void setView(JoystickPanel j){
		view = j;
	}
	
	public void start(){
		Timer t = new Timer();
		t.schedule( new TimerTask() {
			
			@Override
			public void run() {
				bluetoothCtrl.sendAll(listener);
			}
		},0, 50 ) ;	

	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// TODO 
		String cmd = e.getActionCommand();
		switch(cmd){
		case "refresh":
			
			break;
		case "connect":
			
			break;
		default:
			System.out.println("commande non prise en charge");
		}
	}

}
