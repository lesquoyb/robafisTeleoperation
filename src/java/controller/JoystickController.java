package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ch.aplu.xboxcontroller.XboxController;
import view.JoystickPanel;

public class JoystickController implements ActionListener {

	public XboxController model;
	public JoystickPanel view;
	
	public JoystickController(XboxController m) {
		model = m;
		m.addXboxControllerListener(new XBoxCtrlListener());
	}
	
	public void setView(JoystickPanel j){
		view = j;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// TODO Auto-generated method stub
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
