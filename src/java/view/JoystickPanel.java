package view;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.JoystickController;

public class JoystickPanel extends JPanel{

	
	public JComboBox<String> combo;
	public JButton refresh;
	public JButton connect;
	
	public JoystickPanel(JoystickController controller){
		super();
		add(new JLabel("sélectionnez un joystick: "));
		combo = new JComboBox<>();
		combo.setPreferredSize(new Dimension(100, 30));
		add(combo);
		add(new JLabel("état du joystick"));
		refresh = new JButton("rafraichir la liste des joysticks");
		refresh.setActionCommand("refresh");
		connect = new JButton("connecter le joystick");
		connect.setActionCommand("connect");
		add(refresh);
		add(connect);
		refresh.addActionListener(controller);
		connect.addActionListener(controller);
		controller.setView(this);
		
	}
}
