package main.shapes;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;

import main.utils.Constants;

public class PlotLine {
	
	private ArrayList<Point2D> trace = new ArrayList<Point2D>(Constants.BEZIER_FINENESS);
	private Point2D maxPoint;
	private Point2D minPoint;
	private Color color = Color.BLACK;
	
	public PlotLine(Collection<Point2D> trace) {
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double maxY = Double.MIN_VALUE;
		
		for (Point2D pt : trace) {
			if (pt.getX() < minX) {
				minX = pt.getX();
			}
			if (pt.getX() > maxX) {
				maxX = pt.getX();
			}
			if (pt.getY() < minY) {
				minY = pt.getY();
			}
			if (pt.getY() > maxY) {
				maxY = pt.getY();
			}
			this.trace.add(pt);
		}
		
		this.maxPoint = new Point2D.Double(maxX, maxY);
		this.minPoint = new Point2D.Double(minX, minY);
	}
	
	public PlotLine(Collection<Point2D> trace, Color c) {
		this.color = c;
		
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double maxY = Double.MIN_VALUE;
		
		for (Point2D pt : trace) {
			if (pt.getX() < minX) {
				minX = pt.getX();
			}
			if (pt.getX() > maxX) {
				maxX = pt.getX();
			}
			if (pt.getY() < minY) {
				minY = pt.getY();
			}
			if (pt.getY() > maxY) {
				maxY = pt.getY();
			}
			this.trace.add(pt);
		}
		
		this.maxPoint = new Point2D.Double(maxX, maxY);
		this.minPoint = new Point2D.Double(minX, minY);
	}
	
	public PlotLine(PlotLine line) {
		for (Point2D pt : line.getTrace()) {
			this.trace.add(new Point2D.Double(pt.getX(), pt.getY()));
		}
		this.maxPoint = new Point2D.Double(line.getMaxPoint().getX(), line.getMaxPoint().getY());
		this.minPoint = new Point2D.Double(line.getMinPoint().getX(), line.getMinPoint().getY());
		this.color = line.getColor();
	}
	
	public ArrayList<Point2D> getTrace() {
		return this.trace;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public void shiftCoordinates(double deltaX, double deltaY) {
		for (Point2D pt : trace) {
			pt.setLocation(pt.getX() + deltaX, pt.getY() + deltaY);
		}
		this.maxPoint = new Point2D.Double(maxPoint.getX() + deltaX, maxPoint.getY() + deltaY);
		this.minPoint = new Point2D.Double(minPoint.getX() + deltaX, minPoint.getY() + deltaY);
	}
	
	public void scaleCoordinates(double factor) {
		for (Point2D pt : trace) {
			pt.setLocation(pt.getX() * factor, pt.getY() * factor);
		}
		this.maxPoint = new Point2D.Double(maxPoint.getX() * factor, maxPoint.getY() * factor);
		this.minPoint = new Point2D.Double(minPoint.getX() * factor, minPoint.getY() * factor);
	}
	
	public Point2D getMaxPoint() {
		return this.maxPoint;
	}
	
	public Point2D getMinPoint() {
		return this.minPoint;
	}
	
	public String toString() {
		return this.trace.toString();
	}
}
