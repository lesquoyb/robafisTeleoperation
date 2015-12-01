import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import ChartDirector.*;

public class stackedbar implements DemoModule
{
    //Name of demo program
    public String toString() { return "Stacked Bar Chart"; }

    //Number of charts produced in this demo
    public int getNoOfCharts() { return 1; }

    //Main code for creating charts
    public void createChart(ChartViewer viewer, int chartIndex)
    {
        // The data for the bar chart
        double[] data0 = {100, 115, 165, 107, 67};
        double[] data1 = {85, 106, 129, 161, 123};
        double[] data2 = {67, 87, 86, 167, 157};

        // The labels for the bar chart
        String[] labels = {"Mon", "Tue", "Wed", "Thu", "Fri"};

        // Create a XYChart object of size 600 x 360 pixels
        XYChart c = new XYChart(600, 360);

        // Set the plotarea at (70, 20) and of size 400 x 300 pixels, with transparent background
        // and border and light grey (0xcccccc) horizontal grid lines
        c.setPlotArea(70, 20, 400, 300, Chart.Transparent, -1, Chart.Transparent, 0xcccccc);

        // Add a legend box at (480, 20) using vertical layout and 12pt Arial font. Set background
        // and border to transparent and key icon border to the same as the fill color.
        LegendBox b = c.addLegend(480, 20, true, "Arial", 12);
        b.setBackground(Chart.Transparent, Chart.Transparent);
        b.setKeyBorder(Chart.SameAsMainColor);

        // Set the x and y axis stems to transparent and the label font to 12pt Arial
        c.xAxis().setColors(Chart.Transparent);
        c.yAxis().setColors(Chart.Transparent);
        c.xAxis().setLabelStyle("Arial", 12);
        c.yAxis().setLabelStyle("Arial", 12);

        // Add a stacked bar layer
        BarLayer layer = c.addBarLayer2(Chart.Stack);

        // Add the three data sets to the bar layer
        layer.addDataSet(data0, 0xaaccee, "Server # 1");
        layer.addDataSet(data1, 0xbbdd88, "Server # 2");
        layer.addDataSet(data2, 0xeeaa66, "Server # 3");

        // Set the bar border to transparent
        layer.setBorderColor(Chart.Transparent);

        // Enable labelling for the entire bar and use 12pt Arial font
        layer.setAggregateLabelStyle("Arial", 12);

        // Enable labelling for the bar segments and use 12pt Arial font with center alignment
        layer.setDataLabelStyle("Arial", 10).setAlignment(Chart.Center);

        // For a vertical stacked bar with positive data, the first data set is at the bottom. For
        // the legend box, by default, the first entry at the top. We can reverse the legend order
        // to make the legend box consistent with the stacked bar.
        layer.setLegendOrder(Chart.ReverseLegend);

        // Set the labels on the x axis.
        c.xAxis().setLabels(labels);

        // For the automatic y-axis labels, set the minimum spacing to 40 pixels.
        c.yAxis().setTickDensity(40);

        // Add a title to the y axis using dark grey (0x555555) 14pt Arial Bold font
        c.yAxis().setTitle("Y-Axis Title Placeholder", "Arial Bold", 14, 0x555555);

        // Output the chart
        viewer.setChart(c);

        //include tool tip for the chart
        viewer.setImageMap(c.getHTMLImageMap("clickable", "",
            "title='{dataSetName} on {xLabel}: {value} MBytes/hour'"));
    }

    //Allow this module to run as standalone program for easy testing
    public static void main(String[] args)
    {
        //Instantiate an instance of this demo module
        DemoModule demo = new stackedbar();

        //Create and set up the main window
        JFrame frame = new JFrame(demo.toString());
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);} });
        frame.getContentPane().setBackground(Color.white);

        // Create the chart and put them in the content pane
        ChartViewer viewer = new ChartViewer();
        demo.createChart(viewer, 0);
        frame.getContentPane().add(viewer);

        // Display the window
        frame.pack();
        frame.setVisible(true);
    }
}

