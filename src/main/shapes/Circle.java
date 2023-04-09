package main.shapes;

import java.awt.geom.Point2D;

import main.utils.Utility;

public class Circle extends AnnotationShape implements Transformable2D<Circle> {
	
	private double x; // the x coordinate of the center
	private double y; // the y coordinate of the center
	private double radius = 5; // the radius of the circle
	private boolean fill = false;
	private int discretization = 100;
	
	public Circle(double x, double y, double radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public Circle(Point2D center, double radius) {
		this.x = center.getX();
		this.y = center.getY();
		this.radius = radius;
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

	public boolean isFill() {
		return fill;
	}

	public void setFill(boolean fill) {
		this.fill = fill;
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

	@Override
	public Point2D[] getTrace() {
		// TODO Auto-generated method stub
		double[] thetaVals = Utility.linspace(0, 2*Math.PI, discretization + 1);
		Point2D[] trace = new Point2D[discretization + 1];
		for (int i = 0; i <= discretization; i++) {
			double traceX = this.x + this.radius * Math.cos(thetaVals[i]);
			double traceY = this.y + this.radius * Math.sin(thetaVals[i]);
            trace[i] = new Point2D.Double(traceX, traceY);
        }
		return trace;
	}

}
