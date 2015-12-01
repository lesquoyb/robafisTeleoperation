package controller;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import model.BluetoothManager;
import view.ColourGraph;

public class GraphController {

	ColourGraph view;
	BluetoothManager model;
	Timer timer;
	
	
	public GraphController(ColourGraph g, BluetoothManager m){
		view = g;
		model = m;
		timer = new Timer(20, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				update(model.colours);				
			}
		});
		timer.start();
	}
	
	public void update(List<Double> colours){
        double[] colors = new double[colours.size()];
        int i = 0;
        for(Object o : colours){
        	colors[i] = (double) o;
        	i++;
        }
		view.update(colors);
	}
	
	
	
	
}
