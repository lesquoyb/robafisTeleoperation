package view;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.BluetoothController;

public class BluetoothPanel extends JPanel{

	public JTextField ip;
	String DEFAULT_IP = "10.0.1.1";
	private JLabel connectionState;
	
	public BluetoothPanel(BluetoothController listener){
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
	}
	
	
	public void connectionFailed(){
		connectionState.setText("échec de la connexion");
	}
	
	
	public void connectionSucceeded(){
		connectionState.setText("connexion établie");
	}
	
	
	
}
