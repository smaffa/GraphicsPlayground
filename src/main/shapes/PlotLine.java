package main.shapes;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;

import main.utils.Constants;

public class PlotLine implements Transformable2D<PlotLine> {
	
	private ArrayList<Point2D> trace = new ArrayList<Point2D>(Constants.BEZIER_FINENESS);
	private double minX = Double.MAX_VALUE;
	private double maxX = Double.MIN_VALUE;
	private double minY = Double.MAX_VALUE;
	private double maxY = Double.MIN_VALUE;
	private Color color = Color.BLACK;
	
	public PlotLine(Collection<Point2D> trace) {		
		for (Point2D pt : trace) {
			if (pt.getX() < this.minX) {
				this.minX = pt.getX();
			}
			if (pt.getX() > this.maxX) {
				this.maxX = pt.getX();
			}
			if (pt.getY() < this.minY) {
				this.minY = pt.getY();
			}
			if (pt.getY() > this.maxY) {
				this.maxY = pt.getY();
			}
			this.trace.add(pt);
		}
	}
	
	public PlotLine(Collection<Point2D> trace, Color c) {
		this.color = c;
		
		for (Point2D pt : trace) {
			if (pt.getX() < this.minX) {
				this.minX = pt.getX();
			}
			if (pt.getX() > this.maxX) {
				this.maxX = pt.getX();
			}
			if (pt.getY() < this.minY) {
				this.minY = pt.getY();
			}
			if (pt.getY() > this.maxY) {
				this.maxY = pt.getY();
			}
			this.trace.add(pt);
		}
	}
	
	public PlotLine(Point2D[] trace) {		
		for (Point2D pt : trace) {
			if (pt.getX() < this.minX) {
				this.minX = pt.getX();
			}
			if (pt.getX() > this.maxX) {
				this.maxX = pt.getX();
			}
			if (pt.getY() < this.minY) {
				this.minY = pt.getY();
			}
			if (pt.getY() > this.maxY) {
				this.maxY = pt.getY();
			}
			this.trace.add(pt);
		}
	}
	
	public PlotLine(Point2D[] trace, Color c) {
		this.color = c;
		
		for (Point2D pt : trace) {
			if (pt.getX() < this.minX) {
				this.minX = pt.getX();
			}
			if (pt.getX() > this.maxX) {
				this.maxX = pt.getX();
			}
			if (pt.getY() < this.minY) {
				this.minY = pt.getY();
			}
			if (pt.getY() > this.maxY) {
				this.maxY = pt.getY();
			}
			this.trace.add(pt);
		}
	}
	
	public PlotLine(PlotLine line) {
		for (Point2D pt : line.getTrace()) {
			this.trace.add(new Point2D.Double(pt.getX(), pt.getY()));
		}
		this.maxX = line.getMaxX();
		this.maxY = line.getMaxY();
		this.minX = line.getMinX();
		this.minY = line.getMinY();
		this.color = line.getColor();
	}

	public ArrayList<Point2D> getTrace() {
		return this.trace;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public double getMaxX() {
		return this.maxX;
	}
	
	public double getMaxY() {
		return this.maxY;
	}
	
	public double getMinX() {
		return this.minX;
	}
	
	public double getMinY() {
		return this.minY;
	}
	
	public void recomputeMaxima() {
		this.minX = Double.MAX_VALUE;
		this.minY = Double.MAX_VALUE;
		this.maxX = Double.MIN_VALUE;
		this.maxY = Double.MIN_VALUE;
		
		for (Point2D pt : this.trace) {
			if (pt.getX() < this.minX) {
				this.minX = pt.getX();
			}
			if (pt.getX() > this.maxX) {
				this.maxX = pt.getX();
			}
			if (pt.getY() < this.minY) {
				this.minY = pt.getY();
			}
			if (pt.getY() > this.maxY) {
				this.maxY = pt.getY();
			}
		}
	}
	
	public String toString() {
		return this.trace.toString();
	}

	@Override
	public PlotLine translate(double xDelta, double yDelta) {
		// TODO Auto-generated method stub
		for (Point2D pt : trace) {
			pt.setLocation(new Point2D.Double(pt.getX() + xDelta, pt.getY() + yDelta));
		}
		this.maxX = this.maxX + xDelta;
		this.maxY = this.maxY + yDelta;
		this.minX = this.minX + xDelta;
		this.minY = this.minY + yDelta;
		return this;
	}

	@Override
	public PlotLine reflect(double axisVectorX, double axisVectorY) {
		// TODO Auto-generated method stub
		double vectorNorm = Math.sqrt(Math.pow(axisVectorX, 2) + Math.pow(axisVectorY, 2));
		double xComponent = axisVectorX / vectorNorm;
		double yComponent = axisVectorY / vectorNorm;
		double xComponentSquared = Math.pow(xComponent, 2);
		double yComponentSquared = Math.pow(yComponent, 2);
		double transformCoeff00 = xComponentSquared - yComponentSquared;
		double transformCoeff01 = 2 * xComponent * yComponent;
		double transformCoeff11 = yComponentSquared - xComponentSquared;
		
		for (Point2D pt : trace) {
			pt.setLocation(new Point2D.Double(transformCoeff00 * pt.getX() + transformCoeff01 * pt.getY(),
					transformCoeff01 * pt.getX() + transformCoeff11 * pt.getY()));
		}
		
		recomputeMaxima();
		return this;
	}

	@Override
	public PlotLine scale(double xScale, double yScale) {
		// TODO Auto-generated method stub
		for (Point2D pt : trace) {
			pt.setLocation(new Point2D.Double(xScale * pt.getX(), yScale * pt.getY()));
		}
		if (xScale < 0) {
			this.maxX = xScale * this.minX;
			this.minX = xScale * this.maxX;
		} else {
			this.maxX = xScale * this.maxX;
			this.minX = xScale * this.minX;
		}
		if (yScale < 0) {
			this.maxY = yScale * this.minY;
			this.minY = yScale * this.maxY;
		} else {
			this.maxY = yScale * this.maxY;
			this.minY = yScale * this.minY;
		}
		return this;
	}

	@Override
	public PlotLine scale(double factor) {
		// TODO Auto-generated method stub
		return scale(factor, factor);
	}

	@Override
	public PlotLine rotate(double radians) {
		// TODO Auto-generated method stub
		double cosCoeff;
		double sinCoeff;
		if (radians == Math.PI / 2) {
			cosCoeff = 0;
			sinCoeff = 1;
		} else if (radians == Math.PI) {
			cosCoeff = -1;
			sinCoeff = 0;
		} else if (radians == 3 * Math.PI / 2) {
			cosCoeff = 0;
			sinCoeff = -1;
		} else {
			cosCoeff = Math.cos(radians);
			sinCoeff = Math.sin(radians);
		}
		for (Point2D pt : trace) {
			pt.setLocation(cosCoeff * pt.getX() - sinCoeff * pt.getY(),
					sinCoeff * pt.getX() + cosCoeff * pt.getY());
		}
		recomputeMaxima();
		return this;
	}

	@Override
	public PlotLine shearX(double factor) {
		// TODO Auto-generated method stub
		for (Point2D pt : trace) {
			pt.setLocation(pt.getX() + factor * pt.getY(), pt.getY());
		}
		recomputeMaxima();
		return this;
	}

	@Override
	public PlotLine shearY(double factor) {
		// TODO Auto-generated method stub
		for (Point2D pt : trace) {
			pt.setLocation(pt.getX(), factor * pt.getX() + pt.getY());
		}
		recomputeMaxima();
		return this;
	}
}
