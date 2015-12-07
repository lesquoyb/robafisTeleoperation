package view.graphs;

import ChartDirector.Chart;
import ChartDirector.ChartViewer;
import ChartDirector.LinearMeter;
public class UltrasonicGraph extends ChartViewer {
	
    public UltrasonicGraph(){
    	super();
        update(15);
    }

    public void update(int value){
        // The value to display on the meter

        // Create an LinearMeter object of size 250 x 65 pixels with a very light grey (0xeeeeee)
        // background, and a rounded 3-pixel thick light grey (0xcccccc) border
        LinearMeter m = new LinearMeter(250, 65, 0xeeeeee, 0xcccccc);
        m.setRoundedFrame(Chart.Transparent);
        m.setThickFrame(3);

        // Set the scale region top-left corner at (14, 23), with size of 218 x 20 pixels. The scale
        // labels are located on the top (implies horizontal meter)
        m.setMeter(14, 23, 218, 20, Chart.Top);
        
        m.setScale(1, 250, 20);

        double[] smoothColorScale = {0, 0xff0000, 60, 0xffff00, 160, 0x00ff00, 200, 0x00bbbb, 250, 0x6666ff};
        m.addColorScale(smoothColorScale);

        // Add a blue (0x0000cc) pointer at the specified value
        m.addPointer(value, 0x0000cc);

        // Output the chart
        this.setChart(m);
    }
    

}