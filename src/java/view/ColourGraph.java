package view;

import javax.swing.Timer;

import ChartDirector.Chart;
import ChartDirector.ChartViewer;
import ChartDirector.XYChart;

public class ColourGraph extends ChartViewer {
	
	
	
	Timer timer;
	int delay = 20;
	int chartWidth = 800, chartHeight = 420;
	
	public ColourGraph(){
		super();
		setChart(newChart());
		double[] t = new double[0];
		update(t);
  	
	
	}
	
	public XYChart newChart(){
		XYChart chart = new XYChart(chartWidth, chartHeight);
		 chart.setPlotArea(55, 65, 730, 300, -1, -1, 0xc0c0c0, 0xc0c0c0, -1);

	     chart.addLegend(50, 30, false, "Times New Roman Bold Italic", 12).setBackground(Chart.Transparent);

        chart.addTitle("couleurs lues dans le temps", "Times New Roman Bold Italic", 18);

        chart.yAxis().setTitle("Valeur moyenne de gris mesurée", "Arial Bold Italic", 12);

        chart.xAxis().setTitle("temps (en dizaines de millisecondes)", "Arial Bold Italic", 12);
        chart.yAxis().setLinearScale(0, 255, 30);
        chart.yAxis().addLabel(255, "blanc");
        chart.yAxis().addLabel(0, "noir");
        chart.xAxis().setWidth(3);
        chart.yAxis().setWidth(3);
        return chart;

	}
	public void update(double[] colours){

			
        double[] dataX = new double[colours.length];
		for (int i = 0; i < colours.length ; i++){
			dataX[i] = i*10;
		}
		
		XYChart chart = newChart();
		
		
        // Add an orange (0xff9933) scatter chart layer, using 13 pixel diamonds as
        // symbols     
        chart.addLineLayer(colours, 200, "Couleurs");
        chart.xAxis().setLinearScale(0, colours.length-1,500);
		setChart(chart);


	}

	
	
}
