import ChartDirector.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class zoomscrolltrack implements DemoModule
{
	//
	// The main method to allow this demo to run as a standalone program.
	//
	public static void main(String args[]) 
	{
		new zoomscrolltrackDialog().setVisible(true);
		System.exit(0); 
	} 
    
	//
	// Implementation of the DemoModule interface to allow this demo to run inside the 
	// ChartDirectorDemo browser
	//
    
	// Name of demo program
	public String toString() 
	{ 
		return "Zoom/Scroll with Track Line"; 
	}

	// Number of charts produced in this demo
	public int getNoOfCharts() 
	{ 
		// This demo open its own dialog instead of using the right pane of the ChartDirectorDemo 
		// browser for display, so we just display the dialog, and return 0.
		new zoomscrolltrackDialog().setVisible(true);
		return 0; 
	}

	// Main code for creating charts
	public void createChart(ChartViewer viewer, int index)
	{
		// do nothing, as the ChartDirectorDemo browser right pane is not used
	}
}


class zoomscrolltrackDialog extends JDialog
{
	// Data arrays for the scrollable / zoomable chart.
	private Date[] timeStamps;
	private double[] dataSeriesA;
	private double[] dataSeriesB;
	private double[] dataSeriesC;
		
	// The earliest date and the duration in seconds for horizontal scrolling
	private Date minDate;
	private double dateRange;
		
	// The vertical range of the chart for vertical scrolling
	private double maxValue;
	private double minValue;
		
	// The current visible duration of the view port in seconds
	private double currentDuration = 360 * 86400;
	// In this demo, the maximum zoom-in is set to 10 days
	private double minDuration = 10 * 86400;
	
	// This flag is used to suppress event handlers before complete initialization
	private boolean hasFinishedInitialization;
	
	//
	// Controls
	//
	private JButton pointerPB;
	private JButton zoomInPB;
	private JButton zoomOutPB;
	private ChartViewer chartViewer1;
	private JScrollBar hScrollBar1;

	//
	// Constructor
	//
	zoomscrolltrackDialog() 
	{
		// Use DISPOSE_ON_CLOSE to avoid mmeory leak, and set dialog to modal and non-resizable
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		
		// Set title to name of this demo program
		setTitle("Zoom/Scroll with Track Line");

		// Top label bar
		JLabel topLabel = new JLabel("Advanced Software Engineering");
		topLabel.setForeground(new java.awt.Color(255, 255, 51));
		topLabel.setBackground(new java.awt.Color(0, 0, 128));
		topLabel.setBorder(new javax.swing.border.EmptyBorder(2, 0, 2, 5));
		topLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		topLabel.setOpaque(true);
		getContentPane().add(topLabel, java.awt.BorderLayout.NORTH);

		// Left panel
 		JPanel leftPanel = new JPanel(null);
		leftPanel.setBorder(javax.swing.BorderFactory.createRaisedBevelBorder());
		
		// Pointer push button
		pointerPB = new JButton("Pointer", loadImageIcon("pointer.gif"));
		pointerPB.setHorizontalAlignment(SwingConstants.LEFT);
		pointerPB.setMargin(new Insets(5, 5, 5, 5));
		pointerPB.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt)	{
				pointerPB_Clicked();
			}});
		leftPanel.add(pointerPB).setBounds(1, 0, 148, 24);
        
        // Zoom In push button
		zoomInPB = new JButton("Zoom In", loadImageIcon("zoomin.gif"));
		zoomInPB.setHorizontalAlignment(SwingConstants.LEFT);
		zoomInPB.setMargin(new Insets(5, 5, 5, 5));
		zoomInPB.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt)	{
				zoomInPB_Clicked();
			}});		
		leftPanel.add(zoomInPB).setBounds(1, 24, 148, 24);

		// Zoom out push button
		zoomOutPB = new JButton("Zoom Out", loadImageIcon("zoomout.gif"));
		zoomOutPB.setHorizontalAlignment(SwingConstants.LEFT);
		zoomOutPB.setMargin(new Insets(5, 5, 5, 5));
		zoomOutPB.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt)	{
				zoomOutPB_Clicked();
			}});		
		leftPanel.add(zoomOutPB).setBounds(1, 48, 148, 24);

		// Total expected panel size
		leftPanel.setPreferredSize(new Dimension(150, 264));
		
		// Chart Viewer
		chartViewer1 = new ChartViewer();
		chartViewer1.setBackground(new java.awt.Color(255, 255, 255));
		chartViewer1.setOpaque(true);
		chartViewer1.setPreferredSize(new Dimension(656, 366));
		chartViewer1.setHorizontalAlignment(SwingConstants.CENTER);
		chartViewer1.setHotSpotCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		chartViewer1.addViewPortListener(new ViewPortAdapter() {
			public void viewPortChanged(ViewPortChangedEvent e) {
				chartViewer1_ViewPortChanged(e);
			}
		});
		chartViewer1.addTrackCursorListener(new TrackCursorAdapter() {
			public void mouseMovedPlotArea(MouseEvent e) {
				chartViewer1_MouseMovedPlotArea(e);
			}
		});
		
		// Horizontal Scroll bar
		hScrollBar1 = new JScrollBar(JScrollBar.HORIZONTAL, 0, 100000000, 0, 1000000000);
		hScrollBar1.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				hScrollBar1_ValueChanged();		 
			}
		});

		// Put the ChartViewer and the scroll bars in the right panel
		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.add(chartViewer1, java.awt.BorderLayout.CENTER);
		rightPanel.add(hScrollBar1, java.awt.BorderLayout.SOUTH);
		
		// Put the leftPanel and rightPanel on the content pane
		getContentPane().add(leftPanel, java.awt.BorderLayout.WEST);
		getContentPane().add(rightPanel, java.awt.BorderLayout.CENTER);
		
		// Set all UI fonts (except labels)
		Font uiFont = new Font("Dialog", Font.PLAIN, 11);
		for (int i = 0; i < leftPanel.getComponentCount(); ++i)
		{
			Component c = leftPanel.getComponent(i);
			if (!(c instanceof JLabel))
				c.setFont(uiFont);
		}

		// Layout the window
		pack();
		
		//
		// At this point, the user interface layout has been completed. 
		// Can load data and plot chart now.
		//

		// Load the data
		loadData();

		// Initialize the ChartViewer
		initChartViewer(chartViewer1);
		
		// It is safe to handle events now.
		hasFinishedInitialization = true;

		// Trigger a view port update to draw chart.
		chartViewer1.updateViewPort(true, true);
	}

	//
	// A utility to load an image icon from the Java class path
	//
	private ImageIcon loadImageIcon(String path)
	{
		try { return new ImageIcon(getClass().getClassLoader().getResource(path)); }
		catch (Exception e) { return null; }
	}
	
	//
	// Load the data
	//
	private void loadData()
	{
		// In this example, we just use random numbers as data.
		RanSeries r = new RanSeries(127);
		timeStamps = r.getDateSeries(1827, new GregorianCalendar(2007, 0, 1).getTime(), 86400);
		dataSeriesA = r.getSeries2(1827, 150, -10, 10);
		dataSeriesB = r.getSeries2(1827, 200, -10, 10);
		dataSeriesC = r.getSeries2(1827, 250, -8, 8);
	}
	

	//
	// Initialize the WinChartViewer
	//
	private void initChartViewer(ChartViewer viewer)
	{
		// Set the full x range to be the duration of the data
		viewer.setFullRange("x", timeStamps[0], timeStamps[timeStamps.length - 1]);

		// Initialize the view port to show the latest 20% of the time range
		viewer.setViewPortWidth(0.2);
		viewer.setViewPortLeft(1 - viewer.getViewPortWidth());
	
		// Set the maximum zoom to 10 points
		viewer.setZoomInWidthLimit(10.0 / timeStamps.length);
		
		// Enable mouse wheel zooming by setting the zoom ratio to 1.1 per wheel event
		viewer.setMouseWheelZoomRatio(1.1);
		
		// Initially set the mouse usage to "Pointer" mode (Drag to Scroll mode)
		pointerPB.doClick();
	}
	
	//
	// The ViewPortChanged event handler. This event occurs if the user scrolls or zooms in
	// or out the chart by dragging or clicking on the chart. It can also be triggered by
	// calling WinChartViewer.updateViewPort.
	//
	private void chartViewer1_ViewPortChanged(ViewPortChangedEvent e)
	{
		// In addition to updating the chart, we may also need to update other controls that
		// changes based on the view port.
		updateControls(chartViewer1);
     
		// Update the chart if necessary
		if (e.needUpdateChart())
			drawChart(chartViewer1);
	}

	//
	// Update controls when the view port changed
	//
	private void updateControls(ChartViewer viewer)
	{
		// In this demo, we need to update the scroll bar to reflect the view port position and
		// width of the view port.
		hScrollBar1.setEnabled(chartViewer1.getViewPortWidth() < 1);
		hScrollBar1.setVisibleAmount((int)Math.ceil(chartViewer1.getViewPortWidth() * 
			(hScrollBar1.getMaximum() - hScrollBar1.getMinimum())));
		hScrollBar1.setBlockIncrement(hScrollBar1.getVisibleAmount());
		hScrollBar1.setUnitIncrement((int)Math.ceil(hScrollBar1.getVisibleAmount() * 0.1));
		hScrollBar1.setValue((int)Math.round(chartViewer1.getViewPortLeft() * 
			(hScrollBar1.getMaximum() - hScrollBar1.getMinimum())) + hScrollBar1.getMinimum());
	}
	
	//
	// Draw the chart.
	//
	private void drawChart(ChartViewer viewer)
	{ 
		// Get the start date and end date that are visible on the chart.
		Date viewPortStartDate = Chart.NTime(viewer.getValueAtViewPort("x", viewer.getViewPortLeft()));
		Date viewPortEndDate = Chart.NTime(viewer.getValueAtViewPort("x", viewer.getViewPortLeft() +
			viewer.getViewPortWidth()));

		// Get the array indexes that corresponds to the visible start and end dates
		int startIndex = (int)Math.floor(Chart.bSearch(timeStamps, viewPortStartDate));
		int endIndex = (int)Math.ceil(Chart.bSearch(timeStamps, viewPortEndDate));
		int noOfPoints = endIndex - startIndex + 1;

		// Extract the part of the data array that are visible.
		Date[] viewPortTimeStamps = (Date[])Chart.arraySlice(timeStamps, startIndex, noOfPoints);
		double[] viewPortDataSeriesA = (double[])Chart.arraySlice(dataSeriesA, startIndex, noOfPoints);
		double[] viewPortDataSeriesB = (double[])Chart.arraySlice(dataSeriesB, startIndex, noOfPoints);
		double[] viewPortDataSeriesC = (double[])Chart.arraySlice(dataSeriesC, startIndex, noOfPoints);

		//
		// At this stage, we have extracted the visible data. We can use those data to plot the chart.
		//

		//================================================================================
		// Configure overall chart appearance.
		//================================================================================

		// Create an XYChart object of size 640 x 350 pixels
		XYChart c = new XYChart(640, 350);
		
		// Re-cycle the resources of the existing chart, if any. This can improve performance 
		// by reducing the frequency of garbage collections. 		
		c.recycle(chartViewer1.getChart());
									
		// Set the plotarea at (55, 55) with width 80 pixels less than chart width, and height 90 pixels
		// less than chart height. Use a vertical gradient from light blue (f0f6ff) to sky blue (a0c0ff)
		// as background. Set border to transparent and grid lines to white (ffffff).
		c.setPlotArea(55, 55, c.getWidth() - 80, c.getHeight() - 90, c.linearGradientColor(0, 55, 0,
			c.getHeight() - 35, 0xf0f6ff, 0xa0c0ff), -1, Chart.Transparent, 0xffffff, 0xffffff);

		// As the data can lie outside the plotarea in a zoomed chart, we need enable clipping.
		c.setClipping();

		// Add a title to the chart using 18 pts Times New Roman Bold Italic font
		c.addTitle("   Zooming and Scrolling with Track Line (1)", "Times New Roman Bold Italic", 18);

		// Set legend icon style to use line style icon, sized for 8pt font
		c.getLegend().setLineStyleKey();
		c.getLegend().setFontSize(8);

		// Set the axis stem to transparent
		c.xAxis().setColors(Chart.Transparent);
		c.yAxis().setColors(Chart.Transparent);

		// Add axis title using 10pts Arial Bold Italic font
		c.yAxis().setTitle("Ionic Temperature (C)", "Arial Bold Italic", 10);

		//================================================================================
		// Add data to chart
		//================================================================================

		//
		// In this example, we represent the data by lines. You may modify the code below to use other
		// representations (areas, scatter plot, etc).
		//

		// Add a line layer for the lines, using a line width of 2 pixels
		LineLayer layer = c.addLineLayer2();
		layer.setLineWidth(2);

		// In this demo, we do not have too many data points. In real code, the chart may contain a lot
		// of data points when fully zoomed out - much more than the number of horizontal pixels in this
		// plot area. So it is a good idea to use fast line mode.
		layer.setFastLineMode();

		// Now we add the 3 data series to a line layer, using the color red (ff33333), green (008800)
		// and blue (3333cc)
		layer.setXData(viewPortTimeStamps);
		layer.addDataSet(viewPortDataSeriesA, 0xff3333, "Alpha");
		layer.addDataSet(viewPortDataSeriesB, 0x008800, "Beta");
		layer.addDataSet(viewPortDataSeriesC, 0x3333cc, "Gamma");

		//================================================================================
		// Configure axis scale and labelling
		//================================================================================

		// Set the x-axis as a date/time axis with the scale according to the view port x range.
		viewer.syncDateAxisWithViewPort("x", c.xAxis());

		//
		// In this demo, the time range can be from a few years to a few days. We demonstrate how to set
		// up different date/time format based on the time range.
		//

		// If all ticks are yearly aligned, then we use "yyyy" as the label format.
		c.xAxis().setFormatCondition("align", 360 * 86400);
		c.xAxis().setLabelFormat("{value|yyyy}");

		// If all ticks are monthly aligned, then we use "mmm yyyy" in bold font as the first label of a
		// year, and "mmm" for other labels.
		c.xAxis().setFormatCondition("align", 30 * 86400);
		c.xAxis().setMultiFormat(Chart.StartOfYearFilter(), "<*font=bold*>{value|mmm yyyy}",
			Chart.AllPassFilter(), "{value|mmm}");

		// If all ticks are daily algined, then we use "mmm dd<*br*>yyyy" in bold font as the first
		// label of a year, and "mmm dd" in bold font as the first label of a month, and "dd" for other
		// labels.
		c.xAxis().setFormatCondition("align", 86400);
		c.xAxis().setMultiFormat(Chart.StartOfYearFilter(),
			"<*block,halign=left*><*font=bold*>{value|mmm dd<*br*>yyyy}", Chart.StartOfMonthFilter(),
			"<*font=bold*>{value|mmm dd}");
		c.xAxis().setMultiFormat2(Chart.AllPassFilter(), "{value|dd}");

		// For all other cases (sub-daily ticks), use "hh:nn<*br*>mmm dd" for the first label of a day,
		// and "hh:nn" for other labels.
		c.xAxis().setFormatCondition("else");
		c.xAxis().setMultiFormat(Chart.StartOfDayFilter(), "<*font=bold*>{value|hh:nn<*br*>mmm dd}",
			Chart.AllPassFilter(), "{value|hh:nn}");

		//================================================================================
		// Output the chart
		//================================================================================

		// We need to update the track line too. If the mouse is moving on the chart (eg. if 
		// the user drags the mouse on the chart to scroll it), the track line will be updated
		// in the MouseMovePlotArea event. Otherwise, we need to update the track line here.
		if (!viewer.isInMouseMoveEvent())
		{
			trackLineLegend(c, (null == viewer.getChart()) ? c.getPlotArea().getRightX() : 
				viewer.getPlotAreaMouseX());
		}

		viewer.setChart(c);
	}
	
	//
	// Click event for the pointerPB.
	//
	private void pointerPB_Clicked()
	{
		pointerPB.setBackground(new Color(0x80, 0xff, 0x80));
		zoomInPB.setBackground(null);
		zoomOutPB.setBackground(null);
		chartViewer1.setMouseUsage(Chart.MouseUsageScrollOnDrag);
	}

	//
	// Click event for the zoomInPB.
	//
	private void zoomInPB_Clicked()
	{
		pointerPB.setBackground(null);
		zoomInPB.setBackground(new Color(0x80, 0xff, 0x80));
		zoomOutPB.setBackground(null);
		chartViewer1.setMouseUsage(Chart.MouseUsageZoomIn);
	}

	//
	// Click event for the zoomOutPB.
	//
	private void zoomOutPB_Clicked()
	{
		pointerPB.setBackground(null);
		zoomInPB.setBackground(null);
		zoomOutPB.setBackground(new Color(0x80, 0xff, 0x80));
		chartViewer1.setMouseUsage(Chart.MouseUsageZoomOut);
	}
	
	//
	// Horizontal ScrollBar ValueChanged event handler
	//
	private void hScrollBar1_ValueChanged()
	{
		if (hasFinishedInitialization && !chartViewer1.isInViewPortChangedEvent())
		{
			// Get the view port left as according to the scroll bar
			double newViewPortLeft = ((double)(hScrollBar1.getValue() - hScrollBar1.getMinimum())) 
				/ (hScrollBar1.getMaximum() - hScrollBar1.getMinimum());

			// Check if view port has really changed - sometimes the scroll bar may issue redundant
			// value changed events when value has not actually changed.
			if (Math.abs(chartViewer1.getViewPortLeft() - newViewPortLeft) > 
				0.00001 * chartViewer1.getViewPortWidth())
			{
				// Set the view port based on the scroll bar
				chartViewer1.setViewPortLeft(newViewPortLeft);
	
				// Update the chart display without updating the image maps. We delay updating
				// the image map because the chart may still be unstable (still scrolling).
				chartViewer1.updateViewPort(true, false);
			}
		}
	}

	//
	// Draw track cursor when mouse is moving over plotarea
	//
	private void chartViewer1_MouseMovedPlotArea(MouseEvent e)
	{
		ChartViewer viewer = (ChartViewer)e.getSource();
		trackLineLegend((XYChart)viewer.getChart(), viewer.getPlotAreaMouseX());
		viewer.updateDisplay();
	}

	//
	// Draw the track line with legend
	//
	private void trackLineLegend(XYChart c, int mouseX)
	{
		// Clear the current dynamic layer and get the DrawArea object to draw on it.
		DrawArea d = c.initDynamicLayer();

		// The plot area object
		PlotArea plotArea = c.getPlotArea();

		// Get the data x-value that is nearest to the mouse, and find its pixel coordinate.
		double xValue = c.getNearestXValue(mouseX);
		int xCoor = c.getXCoor(xValue);

		// Draw a vertical track line at the x-position
		d.vline(plotArea.getTopY(), plotArea.getBottomY(), xCoor, d.dashLineColor(0x000000, 0x0101));

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

				// We are only interested in visible data sets with names
				String dataName = dataSet.getDataName();
				int color = dataSet.getDataColor();
				if ((!(dataName == null || dataName.length() == 0)) && (color != Chart.Transparent)) {
					// Build the legend entry, consist of the legend icon, name and data value.
					double dataValue = dataSet.getValue(xIndex);
					legendEntries.add("<*block*>" + dataSet.getLegendIcon() + " " + dataName + ": " + ((
						dataValue == Chart.NoValue) ? "N/A" : c.formatValue(dataValue, "{value|P4}")) +
						"<*/*>");

					// Draw a track dot for data points within the plot area
					int yCoor = c.getYCoor(dataSet.getPosition(xIndex), dataSet.getUseYAxis());
					if ((yCoor >= plotArea.getTopY()) && (yCoor <= plotArea.getBottomY())) {
						d.circle(xCoor, yCoor, 4, 4, color, color);
					}
				}
			}
		}

		// Create the legend by joining the legend entries
		Collections.reverse(legendEntries);
		String legendText = "<*block,maxWidth=" + plotArea.getWidth() + "*><*block*><*font=Arial Bold*>[" +
			c.xAxis().getFormattedLabel(xValue, "mmm dd, yyyy") + "]<*/*>        " + Chart.stringJoin(
			legendEntries, "        ") + "<*/*>";

		// Display the legend on the top of the plot area
		TTFText t = d.text(legendText, "Arial", 8);
		t.draw(plotArea.getLeftX() + 5, plotArea.getTopY() - 3, 0x000000, Chart.BottomLeft);
	}
}
