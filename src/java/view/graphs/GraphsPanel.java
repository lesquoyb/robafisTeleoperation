package view.graphs;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import controller.GraphsController;
import model.BluetoothManager;

public class GraphsPanel extends JPanel{

	
	
	public GraphsPanel(BluetoothManager bluetoothManager){
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		ColourGraph colourG = new ColourGraph();
		GyroGraph gyro = new GyroGraph();
		UltrasonicGraph obstacle = new UltrasonicGraph();

		new GraphsController(colourG, gyro, obstacle, bluetoothManager);
		add(colourG);
		JPanel tmp = new JPanel();
		tmp.setLayout(new GridLayout(2,1));
		tmp.add(gyro);
		tmp.add(obstacle);
		add(tmp);
		
	}

}
