package view;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.JoystickController;

public class JoystickView extends JPanel {

	public JLabel armsSpeed;
	public JLabel motorSpeed;
	
	public JoystickView(JoystickController c){
		
		setLayout(new GridLayout(2, 2));
		add(new JLabel("vitesse pince: "));
		armsSpeed = new JLabel("1");
		add(armsSpeed);
		add(new JLabel("vitesse moteur: "));
		motorSpeed = new JLabel("1");
		add(motorSpeed);
		c.addJoystickView(this);
	}
}
