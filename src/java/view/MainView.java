package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ch.aplu.xboxcontroller.XboxController;
import controller.BluetoothController;
import controller.GraphController;
import controller.JoystickController;
import model.BluetoothManager;

public class MainView extends JFrame {

	
	public MainView(){
		super();
		setTitle("Lego Rangers");
		setLayout(new BorderLayout());
		JPanel middle = new JPanel();
		middle.setLayout(new GridLayout(3,1));
		BluetoothManager bluetoothManager = new BluetoothManager();
		BluetoothController b = new BluetoothController(bluetoothManager);
		middle.add(new BluetoothPanel(b)) ;
		middle.add(new JoystickPanel(new JoystickController(new XboxController(), b) ));
		middle.add(new RobotPanel());
		add(middle, BorderLayout.CENTER);
		

		JPanel graphs = new JPanel();
		graphs.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		ColourGraph colourG = new ColourGraph();
		GyroPanel gyro = new GyroPanel();
		new GraphController(colourG, bluetoothManager);
		graphs.add(colourG);
		graphs.add(gyro);
		add(graphs, BorderLayout.SOUTH);
		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
}
