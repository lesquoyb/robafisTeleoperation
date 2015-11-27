package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.BluetoothManager;
import view.BluetoothPanel;

public class BluetoothController implements ActionListener{

	private BluetoothPanel view;
	private BluetoothManager model;
	
	
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

	
	
}
