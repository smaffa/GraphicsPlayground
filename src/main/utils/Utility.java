package main.utils;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import main.shapes.CubicBezierCurve;

/**
 * A class defining general purpose functions that are used throughout GraphicsPlayground programs.
 * @author smaffa
 *
 */
public class Utility {
	
	/**
	 * Performs linear interpolation between 2 values at a given proportion between the values.
	 * @param a		the first double value
	 * @param b		the second double value
	 * @param t		the double proportion of the distance between a and b
	 * @return a double value interpolated between a and b
	 */
    public static double lerp(double a, double b, double t) {
        return a + (t * (b - a));
    }

    /**
     * Performs linear interpolation between 2 points in 2D space at a given proportion between the points.
     * @param p1	the first {@link Point2D} object
     * @param p2	the second {@link Point2D} object
     * @param t		the double proportion of the distance between p1 and p2
     * @return
     */
    public static Point2D.Double lerp(Point2D p1, Point2D p2, double t) {
        double xLerp = lerp(p1.getX(), p2.getX(), t);
        double yLerp = lerp(p1.getY(), p2.getY(), t);
        return new Point2D.Double(xLerp, yLerp);
    }
    
    /**
     * Subdivides a range into discrete units.
     * @param min	the double minimum value
     * @param max	the double maximum value
     * @param n		the number of discrete points along the range
     * @return	an array of double values, spaced uniformly between the input range
     */
    public static double[] linspace(double min, double max, int n) {
    	double[] values = new double[n];
    	for (int i = 0; i < n; i++) {
    		values[i] = lerp(min, max, i / (double) (n-1));
    	}
    	return values;
    }
    
    /**
     * Inverts the y coordinate of the input point in reference to the specified maximum height.
     * @param pt	the {@link Point2D} object to invert
     * @param figHeight		the maximum height
     * @return a {@link Point2D} object where the y coordinate is replaced with figHeight - y 
     */
    public static Point2D invertYCoordinates(Point2D pt, int figHeight) {
		return new Point2D.Double(pt.getX(), figHeight - pt.getY());
	}
    
    /**
     * Inverts the y coordinates of the input curve in reference to the specified maximum height.
     * @param curve		the {@link CubicBezierCurve} object to invert
     * @param figHeight		the maximum height
     * @return a {@link CubicBezierCurve} object where the y coordinates are replaced with figHeight - y
     */
    public static CubicBezierCurve invertYCoordinates(CubicBezierCurve curve, int figHeight) {
		CubicBezierCurve invertedCurve = new CubicBezierCurve(curve);
		invertedCurve.setP1(invertYCoordinates(invertedCurve.getP1(), figHeight));
		invertedCurve.setC1(invertYCoordinates(invertedCurve.getC1(), figHeight));
		invertedCurve.setC2(invertYCoordinates(invertedCurve.getC2(), figHeight));
		invertedCurve.setP2(invertYCoordinates(invertedCurve.getP2(), figHeight));
		return invertedCurve;
	}
	
    /**
     * Inverts the y coordinates of the input array of {@link Point2D} objects in reference to the 
     * specified maximum height.
     * @param trace		the array of {@link Point2D} objects to invert
     * @param figHeight		the maximum height
     * @return an array of {@link Point2D} objects where the y coordinates are replaced with figHeight - y
     */
	public static Point2D[] invertYCoordinates(Point2D[] trace, int figHeight) {
		Point2D[] invertedTrace = new Point2D[trace.length];
		for (int i = 0; i < trace.length; i++) {
			invertedTrace[i] = invertYCoordinates(trace[i], figHeight);
		}
		return invertedTrace;
	}
	
	/**
     * Inverts the y coordinates of the input @{link ArrayList} of {@link Point2D} objects in reference 
     * to the specified maximum height.
     * @param trace		the @{link ArrayList} of {@link Point2D} objects to invert
     * @param figHeight		the maximum height
     * @return an @{link ArrayList} of {@link Point2D} objects where the y coordinates are replaced with 
     * figHeight - y
     */
	public static ArrayList<Point2D> invertYCoordinates(ArrayList<Point2D> trace, int figHeight) {
		ArrayList<Point2D> invertedTrace = new ArrayList<Point2D>();
		for (int i = 0; i < trace.size(); i++) {
			invertedTrace.add(invertYCoordinates(trace.get(i), figHeight));
		}
		return invertedTrace;
	}
	
	/** 
	 * Converts a proportion to the nearest index it represents in a collection of specified length.
	 * @param t		the double proportion
	 * @param length	the length of the collection
	 * @return the nearest integer index represented by the input proportion
	 */
	public static int imputeTIndex(double t, int length) {
		return (int) Math.round(t * (length - 1));
	}
	
	public static Point2D scaleVector(Point2D vector, double scale) {
		return new Point2D.Double(scale * vector.getX(), scale * vector.getY());
	}
	
	public static Point2D scaleVector(double xDelta, double yDelta, double scale) {
		return new Point2D.Double(scale * xDelta, scale * yDelta);
	}
	
	public static double computeVectorNorm(Point2D vector) {
		return Math.sqrt(Math.pow(vector.getX(), 2) + Math.pow(vector.getY(), 2));
	}
	
	public static double computeVectorNorm(double xDelta, double yDelta) {
		return Math.sqrt(Math.pow(xDelta, 2) + Math.pow(yDelta, 2));
	}
	
	public static Point2D normalizeVector(Point2D vector) {
		double norm = computeVectorNorm(vector.getX(), vector.getY());
		return new Point2D.Double(vector.getX() / norm, vector.getY() / norm);
	}
	
	public static Point2D normalizeVector(double xDelta, double yDelta) {
		double norm = computeVectorNorm(xDelta, yDelta);
		return new Point2D.Double(xDelta / norm, yDelta / norm);
	}
	
}
