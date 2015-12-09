package view;

import java.awt.BorderLayout;
import java.awt.Color;
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
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEADING);
		JPanel top = new JPanel(layout);
		JoystickController jCtrl = new JoystickController(new XboxController(), b);
		SettingsPanel s = new SettingsPanel(b, jCtrl );
		top.add(s);
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
