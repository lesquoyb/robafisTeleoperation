import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import ChartDirector.*;

public class donutshading implements DemoModule
{
    //Name of demo program
    public String toString() { return "2D Donut Shading"; }

    //Number of charts produced in this demo
    public int getNoOfCharts() { return 7; }

    //Main code for creating charts
    public void createChart(ChartViewer viewer, int chartIndex)
    {
        // The data for the pie chart
        double[] data = {18, 30, 20, 15};

        // The labels for the pie chart
        String[] labels = {"Labor", "Licenses", "Facilities", "Production"};

        // The colors to use for the sectors
        int[] colors = {0x66aaee, 0xeebb22, 0xbbbbbb, 0x8844ff};

        // Create a PieChart object of size 200 x 220 pixels. Use a vertical gradient color from
        // blue (0000cc) to deep blue (000044) as background. Use rounded corners of 16 pixels
        // radius.
        PieChart c = new PieChart(200, 220);
        c.setBackground(c.linearGradientColor(0, 0, 0, c.getHeight(), 0x0000cc, 0x000044));
        c.setRoundedFrame(0xffffff, 16);

        // Set donut center at (100, 120), and outer/inner radii as 80/40 pixels
        c.setDonutSize(100, 120, 80, 40);

        // Set the pie data
        c.setData(data, labels);

        // Set the sector colors
        c.setColors2(Chart.DataColor, colors);

        // Demonstrates various shading modes
        if (chartIndex == 0) {
            c.addTitle("Default Shading", "bold", 12, 0xffffff);
        } else if (chartIndex == 1) {
            c.addTitle("Local Gradient", "bold", 12, 0xffffff);
            c.setSectorStyle(Chart.LocalGradientShading);
        } else if (chartIndex == 2) {
            c.addTitle("Global Gradient", "bold", 12, 0xffffff);
            c.setSectorStyle(Chart.GlobalGradientShading);
        } else if (chartIndex == 3) {
            c.addTitle("Concave Shading", "bold", 12, 0xffffff);
            c.setSectorStyle(Chart.ConcaveShading);
        } else if (chartIndex == 4) {
            c.addTitle("Rounded Edge", "bold", 12, 0xffffff);
            c.setSectorStyle(Chart.RoundedEdgeShading);
        } else if (chartIndex == 5) {
            c.addTitle("Radial Gradient", "bold", 12, 0xffffff);
            c.setSectorStyle(Chart.RadialShading);
        } else if (chartIndex == 6) {
            c.addTitle("Ring Shading", "bold", 12, 0xffffff);
            c.setSectorStyle(Chart.RingShading);
        }

        // Disable the sector labels by setting the color to Transparent
        c.setLabelStyle("", 8, Chart.Transparent);

        // Output the chart
        viewer.setChart(c);

        //include tool tip for the chart
        viewer.setImageMap(c.getHTMLImageMap("clickable", "",
            "title='{label}: US${value}K ({percent}%)'"));
    }

    //Allow this module to run as standalone program for easy testing
    public static void main(String[] args)
    {
        //Instantiate an instance of this demo module
        DemoModule demo = new donutshading();

        //Create and set up the main window
        JFrame frame = new JFrame(demo.toString());
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);} });
        frame.getContentPane().setBackground(Color.white);
        frame.getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT));
        frame.setSize(800, 450);

        // Create the charts and put them in the content pane
        for (int i = 0; i < demo.getNoOfCharts(); ++i)
        {
            ChartViewer viewer = new ChartViewer();
            demo.createChart(viewer, i);
            frame.getContentPane().add(viewer);
        }

        // Display the window
        frame.setVisible(true);
    }
}

