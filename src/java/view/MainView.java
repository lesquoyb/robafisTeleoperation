package view;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;

import ch.aplu.xboxcontroller.XboxController;
import controller.BluetoothController;
import controller.JoystickController;
import javafx.scene.layout.VBox;
import model.BluetoothManager;

public class MainView extends JFrame {

	
	public MainView(){
		super();
		setTitle("Lego Rangers");
		setLocationRelativeTo(null);
		setLayout(new GridLayout(3,1));
		add(new BluetoothPanel(new BluetoothController(new BluetoothManager()))) ;
		add(new JoystickPanel(new JoystickController(new XboxController())));
		add(new RobotPanel());
		pack();
		setVisible(true);
	}
	
	
}
