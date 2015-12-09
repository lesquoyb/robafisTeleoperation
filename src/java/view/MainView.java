package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ch.aplu.xboxcontroller.XboxController;
import controller.BluetoothController;
import controller.JoystickController;
import model.BluetoothManager;
import view.graphs.GraphsPanel;

public class MainView extends JFrame {

	
	public MainView(){
		super();
		setTitle("Lego Rangers");
		setLayout(new BorderLayout());
		
		BluetoothManager bluetoothManager = new BluetoothManager();
		BluetoothController b = new BluetoothController(bluetoothManager);
		
		
		JPanel top = new JPanel(new GridLayout(2, 1));
		JoystickController jCtrl = new JoystickController(new XboxController(), b);
		top.add(new SettingsPanel(b, jCtrl ));
		JPanel tmp = new JPanel(new FlowLayout());
		tmp.add(new JoystickView(jCtrl));
		tmp.add(new PhasePanel(bluetoothManager));
		top.add(tmp);
		add(top, BorderLayout.NORTH);

		add(new GraphsPanel(bluetoothManager), BorderLayout.SOUTH);
		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
}
