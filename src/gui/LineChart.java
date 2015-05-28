package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;

import mini_pack.Process;

public class LineChart extends ApplicationFrame
{
	//variables
	private XYSeriesCollection drawingSet;
	private String[] labels;
	
	public String algoname;
	private XYSeries line_series;
	private XYSeries context_series;
	private int noprocess;
	private float lastfinish=0;
	
	public LineChart(String title)
	{
		super(title);
		
		//Initialize variables
		drawingSet = new XYSeriesCollection();
		line_series = new XYSeries("Algorithm");
		context_series = new XYSeries("Context Switching");
	}
	
	public void drawnShow()
	{
		drawingSet.addSeries(line_series);
		//drawingSet.addSeries(context_series);
		
		JFreeChart chart = createChart((XYDataset)drawingSet);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(750, 400));
		
		setContentPane(chartPanel);
	}
	
	public void setLabels(Vector<Process> processlist)
	{
		noprocess = processlist.size()+1;
		labels = new String[noprocess];
		labels[0]="";
		for(int i=0;i<processlist.size();i++)
		{
			labels[i+1] = Integer.toString(processlist.elementAt(i).ID);
		}
	}
	
	private int convertToLabels(int pid)
	{
		for(int i=1;i<noprocess;i++)
		{
			if(Integer.parseInt(labels[i]) == pid)
				return i;
		}
		return 0;
	}
	
	public void addLineStep(int pid,float start,float finish)
	{
		if(start != lastfinish)
		{
			line_series.add(lastfinish, 0);
			line_series.add(start, 0);
		}
		
		pid = convertToLabels(pid);
		
		line_series.add(start, pid);
		line_series.add(finish, pid);
		
		lastfinish = finish;
	}
	
	@Override
	public void windowClosing(final WindowEvent evt) {
		if (evt.getWindow() == this) {
		    dispose();
		   }
		}
	
	private JFreeChart createChart(XYDataset dataset)
	{
		// create the chart...
		JFreeChart chart = ChartFactory.createXYLineChart("PSS Simulator ("+algoname+")",
				"Time",
				"Process ID",
				dataset, // data
				PlotOrientation.VERTICAL, 
				true, // include legend
				true, // tooltips
				false // urls
				);
		
		
		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.lightGray);
		// get a reference to the plot for further customisation...
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		// step render
		XYStepRenderer renderer = new XYStepRenderer();
		renderer.setBaseShapesVisible(true);
		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		//renderer.setSeriesStroke(1, new BasicStroke(5.0f));
		renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
		renderer.setDefaultEntityRadius(6);
		plot.setRenderer(renderer);

		SymbolAxis rangeAxis = new SymbolAxis("", labels);
		rangeAxis.setTickUnit(new NumberTickUnit(1));
		rangeAxis.setRange(0, noprocess);
		plot.setRangeAxis(rangeAxis);

		//NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// OPTIONAL CUSTOMISATION COMPLETED.
		return chart;
	}

	
}