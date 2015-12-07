package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.BluetoothController;
import controller.JoystickController;

public class SettingsPanel extends JPanel{

	public JTextField ip;
	String DEFAULT_IP = "10.0.1.1";
	private JLabel connectionState;
	public JLabel connectionLbl;

	
	public SettingsPanel(BluetoothController listener, JoystickController controller){
		super();
		listener.addView(this);
		setLayout(new FlowLayout());
		add(new JLabel("ip du robot: "));
		ip = new JTextField(DEFAULT_IP);
		ip.setPreferredSize(new Dimension(100, 30));
		add(ip);
		connectionState = new JLabel("état de la connexion");
		add(connectionState);
		JButton connection = new JButton("connexion");
		connection.addActionListener(listener);
		add(connection);
		
		connectionLbl = new JLabel("veuillez connecter un joystick");
		connectionLbl.setBackground(Color.red);
		add(connectionLbl);
		controller.setView(this);
		
	}
	
	
	public void refreshControllerConnected(boolean connected){
		if(connected){
			connectionLbl.setText("Joystick connecté");
			connectionLbl.setForeground(Color.green);
		}
		else{
			connectionLbl.setText("veuillez connecter un joystick");
			connectionLbl.setForeground(Color.red);	
		}
	}
	
	
	public void connectionFailed(){
		connectionState.setText("échec de la connexion");
	}
	
	
	public void connectionSucceeded(){
		connectionState.setText("connexion établie");
	}
	
	
	
}
