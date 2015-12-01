import ChartDirector.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class simplezoomscroll implements DemoModule
{
	//
	// The main method to allow this demo to run as a standalone program.
	//
	public static void main(String args[]) 
	{
		new simplezoomscrollDialog().setVisible(true);
		System.exit(0); 
	} 
    
	//
	// Implementation of the DemoModule interface to allow this demo to run inside the 
	// ChartDirectorDemo browser
	//
    
	// Name of demo program
	public String toString() 
	{ 
		return "Simple Zooming and Scrolling"; 
	}

	// Number of charts produced in this demo
	public int getNoOfCharts() 
	{ 
		// This demo open its own dialog instead of using the right pane of the ChartDirectorDemo 
		// browser for display, so we just display the dialog, and return 0.
		new simplezoomscrollDialog().setVisible(true);
		return 0; 
	}

	// Main code for creating charts
	public void createChart(ChartViewer viewer, int index)
	{
		// do nothing, as the ChartDirectorDemo browser right pane is not used
	}
}


class simplezoomscrollDialog extends JDialog
{
    // Data arrays for the scrollable / zoomable chart.
    private Date[] timeStamps;
    private double[] dataSeriesA;
    private double[] dataSeriesB;
    private double[] dataSeriesC;
           
    //
    // Controls
    //
    private ChartViewer chartViewer1;
    private JButton pointerPB;
    private JButton zoomInPB;
    private JButton zoomOutPB;

    //
    // Constructor
    //
	simplezoomscrollDialog() 
    {
		// Use DISPOSE_ON_CLOSE to avoid mmeory leak, and set dialog to modal and non-resizable
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		
		// Set title to name of this demo program
        setTitle("Simple Zooming and Scrolling");
               
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
            public void actionPerformed(java.awt.event.ActionEvent evt)    {
                pointerPB_Clicked();
            }});
        leftPanel.add(pointerPB).setBounds(1, 0, 148, 24);
        
        // Zoom In push button
        zoomInPB = new JButton("Zoom In", loadImageIcon("zoomin.gif"));
        zoomInPB.setHorizontalAlignment(SwingConstants.LEFT);
        zoomInPB.setMargin(new Insets(5, 5, 5, 5));
        zoomInPB.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt)    {
                zoomInPB_Clicked();
            }});        
        leftPanel.add(zoomInPB).setBounds(1, 24, 148, 24);

        // Zoom out push button
        zoomOutPB = new JButton("Zoom Out", loadImageIcon("zoomout.gif"));
        zoomOutPB.setHorizontalAlignment(SwingConstants.LEFT);
        zoomOutPB.setMargin(new Insets(5, 5, 5, 5));
        zoomOutPB.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt)    {
                zoomOutPB_Clicked();
            }});        
        leftPanel.add(zoomOutPB).setBounds(1, 48, 148, 24);

        // Total expected panel size
        leftPanel.setPreferredSize(new Dimension(150, 264));
        
        // Chart Viewer
        chartViewer1 = new ChartViewer();
        chartViewer1.setBackground(new java.awt.Color(255, 255, 255));
        chartViewer1.setOpaque(true);
        chartViewer1.setPreferredSize(new Dimension(616, 316));
        chartViewer1.setHorizontalAlignment(SwingConstants.CENTER);
        chartViewer1.addViewPortListener(new ViewPortAdapter() {
            public void viewPortChanged(ViewPortChangedEvent e) {
                chartViewer1_ViewPortChanged(e);
            }
        });
 
		// Put the ChartViewer in the right panel
		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.add(chartViewer1, java.awt.BorderLayout.CENTER);

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
	
		// Initially set the mouse usage to "Pointer" mode (Drag to Scroll mode)
		pointerPB.doClick();
    }
    
	//
	// The ViewPortChanged event handler. This event occurs if the user scrolls or zooms in
	// or out the chart by dragging or clicking on the chart. It can also be triggered by
	// calling ChartViewer.updateViewPort.
	//
	private void chartViewer1_ViewPortChanged(ViewPortChangedEvent e)
	{
        if (e.needUpdateChart())
			drawChart(chartViewer1);
		if (e.needUpdateImageMap())
			updateImageMap(chartViewer1);    
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

		// Create an XYChart object 600 x 300 pixels in size, with pale blue (0xf0f0ff) background,
		// black (000000) rounded border, 1 pixel raised effect.
		XYChart c = new XYChart(600, 300, 0xf0f0ff, 0, 1);
		c.setRoundedFrame();

		// Re-cycle the resources of the existing chart, if any. This can improve performance 
		// by reducing the frequency of garbage collections.         
		c.recycle(viewer.getChart());

		// Set the plotarea at (52, 60) and of size 520 x 205 pixels. Use white (ffffff) background.
		// Enable both horizontal and vertical grids by setting their colors to grey (cccccc). Set
		// clipping mode to clip the data lines to the plot area.
		c.setPlotArea(52, 60, 520, 205, 0xffffff, -1, -1, 0xcccccc, 0xcccccc);
            
		// As the data can lie outside the plotarea in a zoomed chart, we need to enable clipping.
		c.setClipping();

		// Add a top title to the chart using 15 pts Times New Roman Bold Italic font, with a light blue
		// (ccccff) background, black (000000) border, and a glass like raised effect.
		c.addTitle("Simple Zooming and Scrolling", "Times New Roman Bold Italic", 15
			).setBackground(0xccccff, 0x0, Chart.glassEffect());

		// Add a legend box at the top of the plot area with 9pts Arial Bold font with flow layout.
		c.addLegend(50, 33, false, "Arial Bold", 9).setBackground(Chart.Transparent, Chart.Transparent);

		// Set axes width to 2 pixels
		c.yAxis().setWidth(2);
		c.xAxis().setWidth(2);

		// Add a title to the y-axis
		c.yAxis().setTitle("Price (USD)", "Arial Bold", 9);

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

		// Now we add the 3 data series to a line layer, using the color red (ff0000), green (00cc00)
		// and blue (0000ff)
		layer.setXData(viewPortTimeStamps);
		layer.addDataSet(viewPortDataSeriesA, 0xff0000, "Product Alpha");
		layer.addDataSet(viewPortDataSeriesB, 0x00cc00, "Product Beta");
		layer.addDataSet(viewPortDataSeriesC, 0x0000ff, "Product Gamma");

		//================================================================================
		// Configure axis scale and labelling
		//================================================================================

		// Set the x-axis as a date/time axis with the scale according to the view port x range.
		viewer.syncDateAxisWithViewPort("x", c.xAxis());

		// In this demo, we rely on ChartDirector to auto-label the axis. We ask ChartDirector to ensure
		// the x-axis labels are at least 75 pixels apart to avoid too many labels.
		c.xAxis().setTickDensity(75);

		//================================================================================
		// Output the chart
		//================================================================================

		viewer.setChart(c);
	}
    
	//
	// Update the image map
	//
    private void updateImageMap(ChartViewer viewer)
    {
	    // Include tool tip for the chart
	    if (viewer.getImageMap() == null)
	    {
		    viewer.setImageMap(viewer.getChart().getHTMLImageMap("clickable", "",
			    "title='[{dataSetName}] {x|mmm dd, yyyy}: USD {value|2}'"));
	    }
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
}
