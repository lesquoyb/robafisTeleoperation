package view.graphs;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import ChartDirector.AngularMeter;
import ChartDirector.Chart;
import ChartDirector.ChartViewer;

public class GyroGraph extends ChartViewer{

	
	public GyroGraph(){
		super();		
		update( 60);
		
	}
	

    public void update( int currentDegree){
        // Create an AngularMeter object of size 300 x 300 pixels with transparent background
        AngularMeter m = new AngularMeter(300, 300, Chart.Transparent);

        // Set the default text and line colors to white (0xffffff)
        m.setColor(Chart.TextColor, 0xffffff);
        m.setColor(Chart.LineColor, 0xffffff);

        // Center at (150, 150), scale radius = 128 pixels, scale angle 0 to 360 degrees
        m.setMeter(150, 150, 128, 0, 360);

        // Add a black (0x000000) circle with radius 148 pixels as background
        m.addRing(0, 148, 0x000000);

        // Add a ring between radii 139 and 147 pixels using the silver color with a light grey
        // (0xcccccc) edge as border
        m.addRing(139, 147, Chart.silverColor(), 0xcccccc);

        // Meter scale is 0 - 100, with major/minor/micro ticks every 10/5/1 units
        m.setScale(0, 360, 90, 50, 10);

        // Set the scale label style to 16pt Arial Italic. Set the major/minor/micro tick lengths to
        // 13/10/7 pixels pointing inwards, and their widths to 2/1/1 pixels.
        m.setLabelStyle("Arial Italic", 16);
        m.setTickLength(-13, -10, -7);
        m.setLineWidth(0, 2, 1, 1);


        // Add a semi-transparent red (0x7fff6666) pointer using the arrow shape
        m.addPointer(currentDegree, 0x7fff6666, 0xff6666).setShape(Chart.ArrowPointer2);

        this.setChart(m);
    }

	
	
}
