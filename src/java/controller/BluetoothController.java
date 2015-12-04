package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import model.BluetoothManager;
import view.BluetoothPanel;

public class BluetoothController implements ActionListener{

	public BluetoothPanel view;
	public BluetoothManager model;
	
	
	public BluetoothController(BluetoothManager m) {
		model = m;
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (model.connect(view.ip.getText())){
			view.connectionSucceeded();
		}
		else{
			view.connectionFailed();
		}
	}
	
	public void addView(BluetoothPanel v){
		view = v;
	}
	
	public void sendAll(XBoxCtrlListener x){
		model.sendMovement(x.movement);
	}
	

	
}
