package main.shapes;

import java.awt.geom.Point2D;

import main.utils.Utility;

public class Circle extends AnnotationShape implements Transformable2D<Circle> {

	private double x;
	private double y;
	private double radius = 5;
	private int traceFineness = 24;
	
	public Circle(double x, double y, double radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public Circle(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Circle(Point2D pt, double radius) {
		this.x = pt.getX();
		this.y = pt.getY();
		this.radius = radius;
	}
	
	public Circle(Point2D pt) {
		this.x = pt.getX();
		this.y = pt.getY();
	}
	
	@Override
	public Point2D[] getTrace() {
		// TODO Auto-generated method stub
		double[] thetaVals = Utility.linspace(0, 2*Math.PI, traceFineness + 1);
		Point2D[] trace = new Point2D[traceFineness + 1];
		for (int i = 0; i <= traceFineness; i++) {
			double traceX = this.x + this.radius * Math.cos(thetaVals[i]);
			double traceY = this.y + this.radius * Math.sin(thetaVals[i]);
            trace[i] = new Point2D.Double(traceX, traceY);
        }
		return trace;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	@Override
	public Circle translate(double xDelta, double yDelta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Circle reflect(double axisVectorX, double axisVectorY) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Circle scale(double xScale, double yScale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Circle scale(double factor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Circle rotate(double radians) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Circle shearX(double factor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Circle shearY(double factor) {
		// TODO Auto-generated method stub
		return null;
	}

}
