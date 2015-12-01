import ChartDirector.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

public class trackbox implements DemoModule
{
    //
    // The main method to allow this demo to run as a standalone program.
    //
    public static void main(String args[]) 
    {
        trackbox demo = new trackbox();
        demo.display();
        System.exit(0);
    }

    //
    // Display the window
    //
    private void display() 
    {   
        // Use a JDialog as the window
       	JDialog d = new JDialog((java.awt.Frame)null, toString(), true);
        d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        d.setResizable(false); 	
   
        // Create the ChartViewer and add it to the JDialog
        ChartViewer viewer = new ChartViewer();
        d.getContentPane().add(viewer);

        // Add a movedMovedPlotArea event listener to draw the track cursor
        viewer.addTrackCursorListener(new TrackCursorAdapter() {
            public void mouseMovedPlotArea(MouseEvent e) {
                chartViewer1_MouseMovedPlotArea(e);
            }
        });
        
        // Draw the chart
        drawChart(viewer);

        // Layout and display the JDialog
        d.pack();
		d.setVisible(true);
    }
  
    //
    // Main code for creating charts
    //
    public void drawChart(ChartViewer viewer)
    {
        // The data for the bar chart
        double[] data0 = {100, 125, 245, 147, 67};
        double[] data1 = {85, 156, 179, 211, 123};
        double[] data2 = {97, 87, 56, 267, 157};
        String[] labels = {"Mon", "Tue", "Wed", "Thur", "Fri"};

        // Create a XYChart object of size 540 x 375 pixels
        XYChart c = new XYChart(540, 375);

        // Add a title to the chart using 18pt Times Bold Italic font
        c.addTitle("Average Weekly Network Load", "Times New Roman Bold Italic", 18);

        // Set the plotarea at (50, 55) and of 440 x 280 pixels in size. Use a vertical gradient color from
        // light blue (f9f9ff) to blue (6666ff) as background. Set border and grid lines to white (ffffff).
        c.setPlotArea(50, 55, 440, 280, c.linearGradientColor(0, 55, 0, 335, 0xf9f9ff, 0x6666ff), -1,
            0xffffff, 0xffffff);

        // Add a legend box at (50, 28) using horizontal layout. Use 10pt Arial Bold as font, with transparent
        // background.
        c.addLegend(50, 28, false, "Arial Bold", 10).setBackground(Chart.Transparent);

        // Set the x axis labels
        c.xAxis().setLabels(labels);

        // Draw the ticks between label positions (instead of at label positions)
        c.xAxis().setTickOffset(0.5);

        // Set axis label style to 8pt Arial Bold
        c.xAxis().setLabelStyle("Arial Bold", 8);
        c.yAxis().setLabelStyle("Arial Bold", 8);

        // Set axis line width to 2 pixels
        c.xAxis().setWidth(2);
        c.yAxis().setWidth(2);

        // Add axis title
        c.yAxis().setTitle("Throughput (MBytes Per Hour)");

        // Add a multi-bar layer with 3 data sets
        BarLayer layer = c.addBarLayer2(Chart.Side);
        layer.addDataSet(data0, 0xff0000, "Server #1");
        layer.addDataSet(data1, 0x00ff00, "Server #2");
        layer.addDataSet(data2, 0xff8800, "Server #3");

        // Set bar border to transparent. Use glass lighting effect with light direction from left.
        layer.setBorderColor(Chart.Transparent, Chart.glassEffect(Chart.NormalGlare, Chart.Left));

        // Configure the bars within a group to touch each others (no gap)
        layer.setBarGap(0.2, Chart.TouchBar);

        // Assign the chart to the ChartViewer
        viewer.setChart(c);
    }

    //
    // Draw track cursor when mouse is moving over plotarea
    //
    private void chartViewer1_MouseMovedPlotArea(MouseEvent e)
    {
        ChartViewer viewer = (ChartViewer)e.getSource();
        trackBoxLegend((XYChart)viewer.getChart(), viewer.getPlotAreaMouseX(), viewer.getPlotAreaMouseY());
        viewer.updateDisplay();

        // Hide the track cursor when the mouse leaves the plot area
        viewer.removeDynamicLayer("MouseExitedPlotArea");
    }

    //
    // Draw the track box with legend
    //
    private void trackBoxLegend(XYChart c, int mouseX, int mouseY)
    {
        // Clear the current dynamic layer and get the DrawArea object to draw on it.
        DrawArea d = c.initDynamicLayer();

        // The plot area object
        PlotArea plotArea = c.getPlotArea();

        // Get the data x-value that is nearest to the mouse
        double xValue = c.getNearestXValue(mouseX);

        // Compute the position of the box. This example assumes a label based x-axis, in which the labeling
        // spacing is one x-axis unit. So the left and right sides of the box is 0.5 unit from the central
        // x-value.
        int boxLeft = c.getXCoor(xValue - 0.5);
        int boxRight = c.getXCoor(xValue + 0.5);
        int boxTop = plotArea.getTopY();
        int boxBottom = plotArea.getBottomY();

        // Draw the track box
        d.rect(boxLeft, boxTop, boxRight, boxBottom, 0x000000, Chart.Transparent);

        // Container to hold the legend entries
        ArrayList legendEntries = new ArrayList();

        // Iterate through all layers to build the legend array
        for (int i = 0; i < c.getLayerCount(); ++i) {
            Layer layer = c.getLayerByZ(i);

            // The data array index of the x-value
            int xIndex = layer.getXIndexOf(xValue);

            // Iterate through all the data sets in the layer
            for (int j = 0; j < layer.getDataSetCount(); ++j) {
                ChartDirector.DataSet dataSet = layer.getDataSetByZ(j);

                // Build the legend entry, consist of the legend icon, the name and the data value.
                double dataValue = dataSet.getValue(xIndex);
                if ((dataValue != Chart.NoValue) && (dataSet.getDataColor() != Chart.Transparent)) {
                    legendEntries.add(dataSet.getLegendIcon() + " " + dataSet.getDataName() + ": " +
                        c.formatValue(dataValue, "{value|P4}"));
                }
            }
        }

        // Create the legend by joining the legend entries
        if (legendEntries.size() > 0) {
            Collections.reverse(legendEntries);
            String legend = "<*block,bgColor=FFFFCC,edgeColor=000000,margin=5*><*font,underline=1*>" +
                c.xAxis().getFormattedLabel(xValue) + "<*/font*><*br*>" + Chart.stringJoin(legendEntries,
                "<*br*>") + "<*/*>";

            // Display the legend at the bottom-right side of the mouse cursor, and make sure the legend will
            // not go outside the chart image.
            TTFText t = d.text(legend, "Arial Bold", 8);
            t.draw(Math.min(mouseX + 12, c.getWidth() - t.getWidth()), Math.min(mouseY + 18, c.getHeight() -
                t.getHeight()), 0x000000, Chart.TopLeft);
        }
    }
    
    //
    // Implementation of the DemoModule interface to allow this demo to run inside the 
    // ChartDirectorDemo browser
    //
    
    // Name of demo program
    public String toString() 
    { 
        return "Track Box with Floating Legend"; 
    }

    // Number of charts produced in this demo
    public int getNoOfCharts() 
    { 
        // This demo open its own dialog instead of using the right pane of the ChartDirectorDemo 
        // for display. We just display the dialog, then return 0.
        display();
        return 0; 
    }

    // Main code for creating charts
    public void createChart(ChartViewer viewer, int chartIndex)
    {
        // do nothing, as the ChartDirectorDemo browser right pane is not used
    }
}
