package view;

import java.awt.GridLayout;

import javax.swing.JFrame;

import ch.aplu.xboxcontroller.XboxController;
import controller.BluetoothController;
import controller.JoystickController;
import model.BluetoothManager;

public class MainView extends JFrame {

	
	public MainView(){
		super();
		setTitle("Lego Rangers");
		setLocationRelativeTo(null);
		setLayout(new GridLayout(3,1));
		BluetoothController b = new BluetoothController(new BluetoothManager());
		add(new BluetoothPanel(b)) ;
		add(new JoystickPanel(new JoystickController(new XboxController(), b) ));
		add(new RobotPanel());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	
}
