import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import ChartDirector.*;

public class roundmeter implements DemoModule
{
    //Name of demo program
    public String toString() { return "Round Meter"; }

    //Number of charts produced in this demo
    public int getNoOfCharts() { return 1; }

    //Main code for creating charts
    public void createChart(ChartViewer viewer, int chartIndex)
    {
        // The value to display on the meter
        double value = 72.3;

        // Create an AngularMeter object of size 250 x 250 pixels with transparent background
        AngularMeter m = new AngularMeter(250, 250, Chart.Transparent);

        // Center at (125, 125), scale radius = 111 pixels, scale angle -145 to +145 degrees
        m.setMeter(125, 125, 111, -145, 145);

        // Add a very light grey (0xeeeeee) circle with radius 123 pixels as background
        m.addRing(0, 123, 0xeeeeee);
        // Add a grey (0xcccccc) ring between radii 116 and 123 pixels as border
        m.addRing(116, 123, 0xcccccc);

        // Meter scale is 0 - 100, with major/minor/micro ticks every 10/5/1 units
        m.setScale(0, 100, 10, 5, 1);

        // Set the scale label style to 15pt Arial Italic. Set the major/minor/micro tick lengths to
        // 12/9/6 pixels pointing inwards, and their widths to 2/1/1 pixels.
        m.setLabelStyle("Arial Italic", 15);
        m.setTickLength(-12, -9, -6);
        m.setLineWidth(0, 2, 1, 1);

        // Add a smooth color scale to the meter
        double[] smoothColorScale = {0, 0x3333ff, 25, 0x0088ff, 50, 0x00ff00, 75, 0xdddd00, 100,
            0xff0000};
        m.addColorScale(smoothColorScale);

        // Add a text label centered at (125, 175) with 15pt Arial Italic font
        m.addText(125, 175, "CPU", "Arial Italic", 15, Chart.TextColor, Chart.Center);

        // Add a red (0xff0000) pointer at the specified value
        m.addPointer2(value, 0xff0000);

        // Output the chart
        viewer.setChart(m);
    }

    //Allow this module to run as standalone program for easy testing
    public static void main(String[] args)
    {
        //Instantiate an instance of this demo module
        DemoModule demo = new roundmeter();

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

