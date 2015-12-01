import ChartDirector.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;


public class xyzoomscroll implements DemoModule
{
	//
	// The main method to allow this demo to run as a standalone program.
	//
	public static void main(String args[]) 
	{
		new xyzoomscrollDialog().setVisible(true);
		System.exit(0); 
	} 
    
	//
	// Implementation of the DemoModule interface to allow this demo to run inside the 
	// ChartDirectorDemo browser
	//
    
	// Name of demo program
	public String toString() 
	{ 
		return "XY Zooming and Scrolling"; 
	}

	// Number of charts produced in this demo
	public int getNoOfCharts() 
	{ 
		// This demo open its own dialog instead of using the right pane of the ChartDirectorDemo 
		// browser for display, so we just display the dialog, and return 0.
		new xyzoomscrollDialog().setVisible(true);
		return 0; 
	}

	// Main code for creating charts
	public void createChart(ChartViewer viewer, int index)
	{
		// do nothing, as the ChartDirectorDemo browser right pane is not used
	}
}


class xyzoomscrollDialog extends JDialog
{
    // XY data points for the chart
    private double[] dataX0;
    private double[] dataY0;
    private double[] dataX1;
    private double[] dataY1;
    private double[] dataX2;
    private double[] dataY2;
   
    //
    // Controls
    //
    private ChartViewer chartViewer1;
	private JButton pointerPB;
	private JButton zoomInPB;
	private JButton zoomOutPB;
	private JSlider zoomBar;
	private JFileChooser saveDialog;
   
	//
	// Constructor
	//	   
	xyzoomscrollDialog()
	{	
		// Use DISPOSE_ON_CLOSE to avoid mmeory leak, and set dialog to modal and non-resizable
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);

		// Set title to name of this demo program
		setTitle("XY Zooming and Scrolling");

		// Font to use for user interface elements
		Font uiFont = new Font("Dialog", Font.PLAIN, 11);

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
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				pointerPB_Clicked();
			}});
		leftPanel.add(pointerPB).setBounds(1, 0, 118, 24);
        
		// Zoom In push button
		zoomInPB = new JButton("Zoom In", loadImageIcon("zoomin.gif"));
		zoomInPB.setHorizontalAlignment(SwingConstants.LEFT);
		zoomInPB.setMargin(new Insets(5, 5, 5, 5));
		zoomInPB.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				zoomInPB_Clicked();
			}});        
		leftPanel.add(zoomInPB).setBounds(1, 24, 118, 24);

		// Zoom out push button
		zoomOutPB = new JButton("Zoom Out", loadImageIcon("zoomout.gif"));
		zoomOutPB.setHorizontalAlignment(SwingConstants.LEFT);
		zoomOutPB.setMargin(new Insets(5, 5, 5, 5));
		zoomOutPB.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				zoomOutPB_Clicked();
			}});        
		leftPanel.add(zoomOutPB).setBounds(1, 48, 118, 24);

		// Save push button
		JButton savePB = new JButton("Save", loadImageIcon("save.gif"));
		savePB.setHorizontalAlignment(SwingConstants.LEFT);
		savePB.setMargin(new Insets(5, 5, 5, 5));
		savePB.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				savePB_Clicked();
			}});        
		leftPanel.add(savePB).setBounds(1, 96, 118, 24);

		// Zoom level label
		JLabel zoomLabel = new JLabel("Zoom Level");
		zoomLabel.setHorizontalAlignment(SwingConstants.CENTER);
		leftPanel.add(zoomLabel).setBounds(5, 220, 108, 20);
        
		// Zoom level bar
		zoomBar = new JSlider(1, 100, 50);
		zoomBar.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent e) {
				zoomBar_ValueChanged(e);
			}
		});
		leftPanel.add(zoomBar).setBounds(5, 240, 110, 20);        

		// ViewPortControl - use transparent black for region outside the viewport, that is, 
		// to darken the outside region.
		ViewPortControl vpControl = new ViewPortControl();
		vpControl.setViewPortExternalColor(new Color(0, 0, 0, 127));
		vpControl.setViewPortBorderColor(new Color(255, 255, 255, 127));
		vpControl.setSelectionBorderColor(new Color(255, 255, 255, 127)); 
		leftPanel.add(vpControl).setBounds(4, 310, 110, 110);

		 // Total expected panel size
		leftPanel.setPreferredSize(new java.awt.Dimension(120, 440));
            
		// Chart Viewer - enabled 2D zooming and scrolling
		chartViewer1 = new ChartViewer();
		chartViewer1.setBackground(new java.awt.Color(0xc0, 0xc0, 0xff));
		chartViewer1.setOpaque(true);
		chartViewer1.setHotSpotCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		chartViewer1.setScrollDirection(Chart.DirectionHorizontalVertical);
		chartViewer1.setZoomDirection(Chart.DirectionHorizontalVertical);
		chartViewer1.setMouseWheelZoomRatio(1.1);
		chartViewer1.setPreferredSize(new Dimension(500, 480));      
		chartViewer1.addViewPortListener(new ViewPortAdapter() {
			public void viewPortChanged(ViewPortChangedEvent e) {
				chartViewer1_ViewPortChanged(e);
			}
		});
		chartViewer1.addHotSpotListener(new HotSpotAdapter() {
			public void hotSpotClicked(HotSpotEvent e) {
				chartViewer1_HotSpotClicked(e);
		}});
		chartViewer1.addTrackCursorListener(new TrackCursorAdapter() {
			public void mouseMovedPlotArea(MouseEvent e) {
				chartViewer1_MouseMovedPlotArea(e);
			}
		});
		 
	    // Put the leftPanel and rightPanel on the content pane
		getContentPane().add(leftPanel, java.awt.BorderLayout.WEST);
		getContentPane().add(chartViewer1, java.awt.BorderLayout.CENTER);

		// Set all UI fonts (except labels)
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

		// Initially set the mouse usage to "Pointer" mode (Drag to Scroll mode)
		pointerPB.doClick();
		
		// Load the data
		loadData();
		
		// Trigger a view port update to draw chart.
		chartViewer1.updateViewPort(true, true);

		// Draw and display the full chart in the ViewPortControl
		drawFullChart(vpControl, chartViewer1);

		// Bind the ChartViewer to the ViewPortControl
		vpControl.setViewer(chartViewer1);
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
		//
		// For simplicity, in this demo, we just use hard coded data.
		//
        dataX0 = new double[] {10, 15, 6, -12, 14, -8, 13, -3, 16, 12, 10.5, -7, 3, -10, -5, 2, 5};
        dataY0 = new double[] {130, 150, 80, 110, -110, -105, -130, -15, -170, 125,  125, 60, 25, 
            150, 150, 15, 120};
        dataX1 = new double[] {6, 7, -4, 3.5, 7, 8, -9, -10, -12, 11, 8, -3, -2, 8, 4, -15, 15};
        dataY1 = new double[] {65, -40, -40, 45, -70, -80, 80, 10, -100, 105, 60, 50, 20, 170, -25,
            50, 75};
        dataX2 = new double[] {-10, -12, 11, 8, 6, 12, -4, 3.5, 7, 8, -9, 3, -13, 16, -7.5, -10,
            -15};
        dataY2 = new double[] {65, -80, -40, 45, -70, -80, 80, 90, -100, 105, 60, -75, -150, -40, 
            120, -50, -30};
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
			
		// Update the image map if necessary
		if (e.needUpdateImageMap())
			updateImageMap(chartViewer1);
	}

	//
	// Update controls when the view port changed
	//
	private void updateControls(ChartViewer viewer)
	{
		// Synchronize the zoom bar value with the view port width/height
		zoomBar.setValue((int)Math.round(Math.min(chartViewer1.getViewPortWidth(), 
			chartViewer1.getViewPortHeight()) * zoomBar.getMaximum()));
	}

	//
	// Draw the chart.
	//
	private void drawChart(ChartViewer viewer)
	{
		// Create an XYChart object 500 x 480 pixels in size, with light blue (c0c0ff) as 
		// background color
		XYChart c = new XYChart(500, 480, 0xc0c0ff);
        
		// Re-cycle the resources of the existing chart, if any. This can improve performance 
		// by reducing the frequency of garbage collections.         
		c.recycle(chartViewer1.getChart());

		// Set the plotarea at (50, 40) and of size 400 x 400 pixels. Use light grey (c0c0c0)
		// horizontal and vertical grid lines. Set 4 quadrant coloring, where the colors of 
		// the quadrants alternate between lighter and deeper grey (dddddd/eeeeee). 
		c.setPlotArea(50, 40, 400, 400, -1, -1, -1, 0xc0c0c0, 0xc0c0c0
			).set4QBgColor(0xdddddd, 0xeeeeee, 0xdddddd, 0xeeeeee, 0x000000);

		// Enable clipping mode to clip the part of the data that is outside the plot area.
		c.setClipping();

		// Set 4 quadrant mode, with both x and y axes symetrical around the origin
		c.setAxisAtOrigin(Chart.XYAxisAtOrigin, Chart.XAxisSymmetric + Chart.YAxisSymmetric);

		// Add a legend box at (450, 40) (top right corner of the chart) with vertical layout
		// and 8 pts Arial Bold font. Set the background color to semi-transparent grey.
		LegendBox legendBox = c.addLegend(450, 40, true, "arialbd.ttf", 8);
		legendBox.setAlignment(Chart.TopRight);
		legendBox.setBackground(0x40dddddd);

		// Add a titles to axes
		c.xAxis().setTitle("Alpha Index");
		c.yAxis().setTitle("Beta Index");

		// Set axes width to 2 pixels
		c.xAxis().setWidth(2);
		c.yAxis().setWidth(2);

		// The default ChartDirector settings has a denser y-axis grid spacing and less-dense
		// x-axis grid spacing. In this demo, we want the tick spacing to be symmetrical.
		// We use around 50 pixels between major ticks and 25 pixels between minor ticks.
		c.xAxis().setTickDensity(50, 25);
		c.yAxis().setTickDensity(50, 25);

		//
		// In this example, we represent the data by scatter points. If you want to represent
		// the data by somethings else (lines, bars, areas, floating boxes, etc), just modify
		// the code below to use the layer type of your choice. 
		//

		// Add scatter layer, using 11 pixels red (ff33333) X shape symbols
		c.addScatterLayer(dataX0, dataY0, "Group A", Chart.Cross2Shape(), 11, 0xff3333);

		// Add scatter layer, using 11 pixels green (33ff33) circle symbols
		c.addScatterLayer(dataX1, dataY1, "Group B", Chart.CircleShape, 11,    0x33ff33);

		// Add scatter layer, using 11 pixels blue (3333ff) triangle symbols
		c.addScatterLayer(dataX2, dataY2, "Group C", Chart.TriangleSymbol, 11, 0x3333ff);

		//
		// In this example, we have not explicitly configured the full x and y range. In this case, the
		// first time syncLinearAxisWithViewPort is called, ChartDirector will auto-scale the axis and
		// assume the resulting range is the full range. In subsequent calls, ChartDirector will set the
		// axis range based on the view port and the full range.
		//
		viewer.syncLinearAxisWithViewPort("x", c.xAxis());
		viewer.syncLinearAxisWithViewPort("y", c.yAxis());
		
		// We need to update the track line too. If the mouse is moving on the chart (eg. if 
		// the user drags the mouse on the chart to scroll it), the track line will be updated
		// in the MouseMovePlotArea event. Otherwise, we need to update the track line here.
		if ((!viewer.isInMouseMoveEvent()) && viewer.isMouseOnPlotArea())
			crossHair(c, viewer.getPlotAreaMouseX(), viewer.getPlotAreaMouseY());

		// Set the chart image to the ChartViewer
		chartViewer1.setChart(c);
	}

	//
	// Draw the full thumbnail chart and display it in the given ViewPortControl
	//
	private void drawFullChart(ViewPortControl vpc, ChartViewer viewer)
	{
		// Create an XYChart object of the same size as the Viewport Control
		XYChart c = new XYChart(vpc.getWidth(), vpc.getHeight());

		// Set the plotarea to cover the entire chart. Disable grid lines by setting their colors
		// to transparent. Set 4 quadrant coloring, where the colors of the quadrants alternate 
		// between lighter and deeper grey (dddddd/eeeeee). 
		c.setPlotArea(0, 0, c.getWidth() - 1, c.getHeight() - 1, -1, -1, 0xff0000, Chart.Transparent, 
			Chart.Transparent).set4QBgColor(0xdddddd, 0xeeeeee, 0xdddddd, 0xeeeeee, 0x000000);

		// Set 4 quadrant mode, with both x and y axes symetrical around the origin
		c.setAxisAtOrigin(Chart.XYAxisAtOrigin, Chart.XAxisSymmetric + Chart.YAxisSymmetric);

		// The x and y axis scales reflect the full range of the view port
		c.xAxis().setLinearScale(viewer.getValueAtViewPort("x", 0), viewer.getValueAtViewPort("x", 1),
			Chart.NoValue);
		c.yAxis().setLinearScale(viewer.getValueAtViewPort("y", 0), viewer.getValueAtViewPort("y", 1),
			Chart.NoValue);

		// Add scatter layer, using 3 pixels red (ff33333) X shape symbols
		c.addScatterLayer(dataX0, dataY0, "Group A", Chart.Cross2Shape(), 3, 0xff3333, 0xff3333);

		// Add scatter layer, using 3 pixels green (33ff33) circle symbols
		c.addScatterLayer(dataX1, dataY1, "Group B", Chart.CircleShape, 3, 0x33ff33, 0x33ff33);

		// Add scatter layer, using 3 pixels blue (3333ff) triangle symbols
		c.addScatterLayer(dataX2, dataY2, "Group C", Chart.TriangleSymbol, 3, 0x3333ff, 0x3333ff);

		// Set the chart image to the WinChartViewer
		vpc.setChart(c);
	}
    
    //
	// Update the image map used on the chart.
	//
	private void updateImageMap(ChartViewer viewer)
	{
		// Include tool tip for the chart
		if (viewer.getImageMap() == null)
		{
			viewer.setImageMap(viewer.getChart().getHTMLImageMap("clickable", "",
				"title='[{dataSetName}] Alpha = {x}, Beta = {value}'"));
		}
	}
    
	//
	// Handler when a hot spot is clicked. In this demo, we just list out the hot spot parameters
	// in a pop up dialog.
	//
	private void chartViewer1_HotSpotClicked(HotSpotEvent e)
	{
		// We show the pop up dialog only when the mouse action is not in zoom-in or zoom-out mode.
		if ((chartViewer1.getMouseUsage() != Chart.MouseUsageZoomIn) && 
			(chartViewer1.getMouseUsage() != Chart.MouseUsageZoomOut))
			showHotSpotParam(e);
	}
    
	//
	// Utility to list out all hot spot parameters on a pop-up dialog
	//
	private void showHotSpotParam(HotSpotEvent e)
	{
		// Get all parameters and sort them by key
		Hashtable parameters = e.getAttrValues();
		Object[] attributes = parameters.keySet().toArray();
		Arrays.sort(attributes);
    
		// Create a JTable to show the hot spot attribute/value pairs
		Object[][] rows = new Object[parameters.size()][2];
		for (int i = 0; i < attributes.length; ++i)
		{
			rows[i][0] = attributes[i];
			rows[i][1] = parameters.get(attributes[i]);
		}
		JTable table = new JTable(rows, new Object[] {"Parameter", "Value"});

		// Show the table in a dialog
		JDialog d = new JDialog(this, "Hot Spot Parameters", true);
		d.setSize(300, 300);
		Container container = d.getContentPane();

		// Just add some descriptive text to the dialg
		JTextArea t = new JTextArea ("This dialog is for demonstration only." +
			" In this demo, we simply list out all hot spot parameters.");
		t.setLineWrap(true);
		t.setWrapStyleWord(true);
		t.setEditable(false);
		t.setOpaque(false);
		t.setMargin(new Insets(5, 5, 5, 5));
		container.add(t, BorderLayout.NORTH);
    
		// Create the scroll pane on the dialog and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);
		container.add(scrollPane);

		// Show the dialog on where the mouse click occur    
		Point topLeft = ((ChartViewer)e.getSource()).getLocationOnScreen();
		d.setLocation(topLeft.x + e.getX(), topLeft.y + e.getY());
		d.setVisible(true);
	}
        
	//
	// Pointer (Drag to Scroll) button event handler
	//
    private void pointerPB_Clicked()
    {
        pointerPB.setBackground(new Color(0x80, 0xff, 0x80));
        zoomInPB.setBackground(null);
        zoomOutPB.setBackground(null);
        chartViewer1.setMouseUsage(Chart.MouseUsageScrollOnDrag);
    }

	//
	// Zoom In button event handler
	//
    private void zoomInPB_Clicked()
    {
        pointerPB.setBackground(null);
        zoomInPB.setBackground(new Color(0x80, 0xff, 0x80));
        zoomOutPB.setBackground(null);
        chartViewer1.setMouseUsage(Chart.MouseUsageZoomIn);
    }

	//
	// Zoom Out button event handler
	//
    private void zoomOutPB_Clicked()
    {
        pointerPB.setBackground(null);
        zoomInPB.setBackground(null);
        zoomOutPB.setBackground(new Color(0x80, 0xff, 0x80));
        chartViewer1.setMouseUsage(Chart.MouseUsageZoomOut);
    }
    
    //
    // A utility class to be used with JFileChooser to filter files with certain extensions.
    // This is to maintain compatibility with older versions of Java that does not built-in
    // extension filtering class.
    //
	private static class SimpleExtensionFilter extends FileFilter 
	{
		public String ext;
		public SimpleExtensionFilter(String extension) { this.ext = "." + extension; }
		public String getDescription() { return ext.substring(1);	}
		public boolean accept(java.io.File file) 
		{ return file.isDirectory() || file.getName().endsWith(ext); }
	}

	//
	// Save button event handler
	//
	private void savePB_Clicked()
	{
		String[] extensions = { "png", "jpg", "gif", "bmp", "svg", "pdf" };

		// The File Save dialog
		if (null == saveDialog)
		{
			saveDialog = new JFileChooser();
			for (int i = 0; i < extensions.length; ++i)
				saveDialog.addChoosableFileFilter(new SimpleExtensionFilter(extensions[i]));		
			saveDialog.setAcceptAllFileFilterUsed(false);
			saveDialog.setFileFilter(saveDialog.getChoosableFileFilters()[0]);
			saveDialog.setSelectedFile(new java.io.File("chartdirector_demo"));
		}
		
		int status = saveDialog.showSaveDialog(null);
		if ((status == JFileChooser.APPROVE_OPTION) && (null != chartViewer1.getChart()))
		{
			// Add extension if the pathName does not already have one
			String pathName = saveDialog.getSelectedFile().getAbsolutePath();
			boolean hasExtension = false;
			for (int i = 0; i < extensions.length; ++i)
				if (hasExtension = pathName.endsWith("." + extensions[i]))
					break;
			if ((!hasExtension) && (saveDialog.getFileFilter() instanceof SimpleExtensionFilter))
				pathName += ((SimpleExtensionFilter)saveDialog.getFileFilter()).ext;
			
			// Issue an overwrite confirmation dialog if the file already exists
			if (new java.io.File(pathName).exists())
			{
				if (JOptionPane.YES_OPTION != JOptionPane.showOptionDialog(this, 
					"File \"" + pathName + "\" already exists, confirm overwrite?", 
					"Existing File - Confirm Overwrite", 
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, 
					null, new String[] { "Yes", "No" }, "No"))
					return;	
			}

			chartViewer1.getChart().makeChart(pathName);					
		}
	}
    
	//
	// ValueChanged event handler for zoomBar. Zoom in around the center point and try to 
	// maintain the aspect ratio
	//
    private void zoomBar_ValueChanged(javax.swing.event.ChangeEvent e)
    {
    	if (!chartViewer1.isInViewPortChangedEvent())
    	{
            // Remember the center point
            double centerX = chartViewer1.getViewPortLeft() + chartViewer1.getViewPortWidth() / 2;
            double centerY = chartViewer1.getViewPortTop() + chartViewer1.getViewPortHeight() / 2;

            // Aspect ratio and zoom factor
            double aspectRatio = chartViewer1.getViewPortWidth() / chartViewer1.getViewPortHeight();
            double zoomTo = ((double)zoomBar.getValue()) / zoomBar.getMaximum();

            // Zoom while respecting the aspect ratio
            chartViewer1.setViewPortWidth(zoomTo * Math.max(1, aspectRatio));
            chartViewer1.setViewPortHeight(zoomTo * Math.max(1, 1 / aspectRatio));
            
            // Adjust ViewPortLeft and ViewPortTop to keep center point unchanged
            chartViewer1.setViewPortLeft(centerX - chartViewer1.getViewPortWidth() / 2);
            chartViewer1.setViewPortTop(centerY - chartViewer1.getViewPortHeight() / 2);
                        
            // Update the chart, but no need to update the image map yet, as the chart is still 
            // zooming and is unstable
            chartViewer1.updateViewPort(true, false);
    	}
    }
    
	//
	// Draw track cursor when mouse is moving over plotarea
	//
	private void chartViewer1_MouseMovedPlotArea(MouseEvent e)
	{
		ChartViewer viewer = (ChartViewer)e.getSource();

		// Draw crosshair track cursor
		crossHair((XYChart)viewer.getChart(), viewer.getPlotAreaMouseX(), viewer.getPlotAreaMouseY());
		viewer.updateDisplay();

		// Hide the track cursor when the mouse leaves the plot area
		viewer.removeDynamicLayer("MouseExitedPlotArea");
		
		// Update image map if necessary. If the mouse is still dragging, the chart is still 
		// updating and not confirmed, so there is no need to set up the image map.
		if (viewer.isMouseDragging())
			updateImageMap(viewer);
	}

	//
	// Draw cross hair cursor with axis labels
	//
	private void crossHair(XYChart c, int mouseX, int mouseY)
	{
		// Clear the current dynamic layer and get the DrawArea object to draw on it.
		DrawArea d = c.initDynamicLayer();

		// The plot area object
		PlotArea plotArea = c.getPlotArea();

		// Draw a vertical line and a horizontal line as the cross hair
		d.vline(plotArea.getTopY(), plotArea.getBottomY(), mouseX, d.dashLineColor(0x000000, 0x0101));
		d.hline(plotArea.getLeftX(), plotArea.getRightX(), mouseY, d.dashLineColor(0x000000, 0x0101));

		// Draw y-axis label
		String label = "<*block,bgColor=FFFFDD,margin=3,edgeColor=000000*>" + c.formatValue(c.getYValue(
			mouseY, c.yAxis()), "{value|P4}") + "<*/*>";
		TTFText t = d.text(label, "Arial Bold", 8);
		t.draw(plotArea.getLeftX() - 5, mouseY, 0x000000, Chart.Right);

		// Draw x-axis label
		label = "<*block,bgColor=FFFFDD,margin=3,edgeColor=000000*>" + c.formatValue(c.getXValue(mouseX),
			"{value|P4}") + "<*/*>";
		t = d.text(label, "Arial Bold", 8);
		t.draw(mouseX, plotArea.getBottomY() + 5, 0x000000, Chart.Top);
	}
}
