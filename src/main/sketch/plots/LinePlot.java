package main.sketch.plots;

import java.util.ArrayList;
import java.util.Collection;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.*;

/**
 * Class that stores the data for a line plot and arranges the drawing layout appropriately
 * @author smaffa
 *
 */
public class LinePlot extends GraphicsPlot {

	private ArrayList<PlotLine> lines = new ArrayList<PlotLine>();
	private ArrayList<PlotLine> projections = new ArrayList<PlotLine>();
	
	private int figWidth = 500;
	private int figHeight = 500;
	private final double border = 0.05;
	
	private double axisX = this.figHeight / 2; // y coordinate of x-axis
	private double axisY = this.figWidth / 2; // 
	
	private double[] transformValues = new double[3];
	
	public LinePlot() {	}
	
	public LinePlot(int width, int height) {
		this.figWidth = width;
		this.figHeight = height;
	}
	
	public void addLine(Collection<Point2D> trace, Color c) {
		lines.add(new PlotLine(trace, c));
	}
	
	@Override
	public void computePlotProjection() {
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double maxY = Double.MIN_VALUE;
		
		for (PlotLine line : lines) {
			Point2D maxPoint = line.getMaxPoint();
			Point2D minPoint = line.getMinPoint();
			
			if (minPoint.getX() < minX) {
				minX = minPoint.getX();
			}
			if (maxPoint.getX() > maxX) {
				maxX = maxPoint.getX();
			}
			if (minPoint.getY() < minY) {
				minY = minPoint.getY();
			}
			if (maxPoint.getY() > maxY) {
				maxY = maxPoint.getY();
			}
		}
		
		// use the long axis to fit the scale
		// check both axes' scale factor and use the more extreme scaling
		double borderXRange = (1 - (2 * border)) * figWidth;
		double borderYRange = (1 - (2 * border)) * figHeight;
		double xRange = maxX - minX;
		double yRange = maxY - minY;
		
		double xScale = borderXRange / xRange;
		double yScale = borderYRange / yRange;
		
		double overallScale;
		if (xScale < yScale) {
			overallScale = xScale;
		} else {
			overallScale = yScale;
		}
		
		projections.clear();
		for (PlotLine line : lines) {
			PlotLine projectedLine = new PlotLine(line);
			projectedLine.scaleCoordinates(overallScale);
			projectedLine.shiftCoordinates(this.axisY, this.axisX);
			projections.add(projectedLine);
		}
	}
	
	public void drawAxes(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(2.0f));
		
		g2d.draw(new Line2D.Double(0, this.axisY,
                    this.figWidth, this.axisY));
		g2d.draw(new Line2D.Double(this.axisX, 0,
                this.axisY, this.figHeight));
	}
	
	public void drawPlotLine(Graphics2D g2d, PlotLine line) {
    	g2d.setColor(line.getColor());
    	g2d.setStroke(new BasicStroke(1.0f));
    	
    	ArrayList<Point2D> tracePoints = line.getTrace();
    	
    	for (int i = 0; i < tracePoints.size() - 1; i++) {
    		// flip the y components
            g2d.draw(new Line2D.Double(tracePoints.get(i).getX(), (this.figHeight - tracePoints.get(i).getY()),
                    tracePoints.get(i+1).getX(), (this.figHeight - tracePoints.get(i+1).getY())));
        }
    }
	
	@Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);
        
        computePlotProjection();
        
        drawAxes(g2d);
        for (PlotLine line : projections) {
        	drawPlotLine(g2d, line);
        }
    }
	
	public ArrayList<PlotLine> getLines() {
		return this.lines;
	}
	
	public ArrayList<PlotLine> getProjections() {
		return this.projections;
	}
	
	public int getFigureWidth() {
		return this.figWidth;
	}
	
	public int getFigureHeight() {
		return this.figHeight;
	}

}
