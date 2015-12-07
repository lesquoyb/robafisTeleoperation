package controller;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import model.BluetoothManager;
import view.graphs.ColourGraph;
import view.graphs.GyroGraph;
import view.graphs.UltrasonicGraph;

public class GraphsController {

	ColourGraph viewCol;
	GyroGraph viewGyr;
	UltrasonicGraph viewUlt;
	BluetoothManager model;
	Timer timer;
	
	
	public GraphsController(ColourGraph colG,GyroGraph gyrG, UltrasonicGraph ultG, BluetoothManager m){
		viewCol = colG;
		viewGyr = gyrG;
		viewUlt = ultG;
		model = m;
		timer = new Timer(20, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				update((ArrayList<Integer>) model.colours.clone(), (ArrayList<Integer>) model.gyro.clone(), (ArrayList<Integer>) model.dist.clone());				
			}
		});
		timer.start();
	}
	
	public void update(ArrayList<Integer> colours, ArrayList<Integer> gyro, ArrayList<Integer> dist){
        double[] colors = new double[colours.size()];
        int i = 0;
        for(Object o : colours){
        	colors[i] = (int) o;
        	i++;
        }
		viewCol.update(colors);
		if(dist.size() > 0){
			viewUlt.update(dist.get(0) );
		}
		if(gyro.size() > 0) 
		viewGyr.update( gyro.get(0) );
	}
	
	
	
	
}
