package main.shapes;

import java.awt.geom.Point2D;

import main.utils.Utility;

/**
 * Class for regular polygons drawn onto a canvas. All transformations operate only on the center of the polygon.
 * @author smaffa
 *
 */
public class RegularPolygon extends AnnotationShape implements Transformable2D<RegularPolygon> {

	private double x; // the x coordinate of the center
	private double y; // the y coordinate of the center
	private int nSides; // the number of sides to the regular polygon
	private double radius = 5; // the radius of the polygon
	private double orientation = 0; // the angle describing the orientation of a vertex, in radians from the x-axis
	
	/**
	 * Full constructor using all fields of the regular polygon
	 * @param x		a double specifying the x coordinate of the center
	 * @param y		a double specifying the y coordinate of the center
	 * @param nSides	an int specifying the number of sides to the polygon
	 * @param radius	a double specifying the radius of the polygon
	 * @param orientation	a double specifying the angle at which the polygon is oriented
	 */
	public RegularPolygon(double x, double y, int nSides, double radius, double orientation) {
		this.x = x;
		this.y = y;
		this.nSides = nSides;
		this.radius = radius;
		this.orientation = orientation;
	}
	
	/**
	 * Basic constructor using the x and y coordinates of the polygon's center and the number of sides
	 * @param x		a double specifying the x coordinate of the center
	 * @param y		a double specifying the y coordinate of the center
	 * @param nSides	an int specifying the number of sides to the polygon
	 */
	public RegularPolygon(double x, double y, int nSides) {
		this.x = x;
		this.y = y;
		this.nSides = nSides;
	}
	
	/**
	 * Basic constructor using a {@link Point2D} object for the polygon's center and the number of sides
	 * @param pt	a {@link Point2D} object representing the (x,y) coordinates of the polygon's center
	 * @param nSides	an int specifying the number of sides to the polygon
	 */
	public RegularPolygon(Point2D pt, int nSides) {
		this.x = pt.getX();
		this.y = pt.getY();
		this.nSides = nSides;
	}
	
	@Override
	public Point2D[] getTrace() {
		// TODO Auto-generated method stub
		double[] thetaVals = Utility.linspace(orientation, orientation + (2*Math.PI), nSides + 1);
		Point2D[] trace = new Point2D[nSides + 1];
		for (int i = 0; i <= nSides; i++) {
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
	
	public int getNSides() {
		return nSides;
	}

	public void setNSides(int nSides) {
		this.nSides = nSides;
	}
	
	public double getOrientation() {
		return orientation;
	}

	public void setOrientation(double orientation) {
		this.orientation = orientation;
	}

	@Override
	public RegularPolygon translate(double xDelta, double yDelta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegularPolygon reflect(double axisVectorX, double axisVectorY) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegularPolygon scale(double xScale, double yScale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegularPolygon scale(double factor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegularPolygon rotate(double radians) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegularPolygon shearX(double factor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegularPolygon shearY(double factor) {
		// TODO Auto-generated method stub
		return null;
	}

}
