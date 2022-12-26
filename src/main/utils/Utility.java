package main.utils;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import main.shapes.CubicBezierCurve;

public class Utility {
    public static double lerp(double a, double b, double t) {
        return a + (t * (b - a));
    }

    public static Point2D.Double lerp(Point2D p1, Point2D p2, double t) {
        double xLerp = lerp(p1.getX(), p2.getX(), t);
        double yLerp = lerp(p1.getY(), p2.getY(), t);
        return new Point2D.Double(xLerp, yLerp);
    }
    
    public static double[] linspace(double min, double max, int n) {
    	double[] values = new double[n];
    	for (int i = 0; i < n; i++) {
    		values[i] = lerp(min, max, i / (double) (n-1));
    	}
    	return values;
    }
    
    public static CubicBezierCurve invertYCoordinates(CubicBezierCurve curve, int figHeight) {
		CubicBezierCurve invertedCurve = new CubicBezierCurve(curve);
		invertedCurve.setP1(invertYCoordinates(invertedCurve.getP1(), figHeight));
		invertedCurve.setC1(invertYCoordinates(invertedCurve.getC1(), figHeight));
		invertedCurve.setC2(invertYCoordinates(invertedCurve.getC2(), figHeight));
		invertedCurve.setP2(invertYCoordinates(invertedCurve.getP2(), figHeight));
		return invertedCurve;
	}
	
	public static Point2D invertYCoordinates(Point2D pt, int figHeight) {
		return new Point2D.Double(pt.getX(), figHeight - pt.getY());
	}
	
	public static Point2D[] invertYCoordinates(Point2D[] trace, int figHeight) {
		Point2D[] invertedTrace = new Point2D[trace.length];
		for (int i = 0; i < trace.length; i++) {
			invertedTrace[i] = invertYCoordinates(trace[i], figHeight);
		}
		return invertedTrace;
	}
	
	public static ArrayList<Point2D> invertYCoordinates(ArrayList<Point2D> trace, int figHeight) {
		ArrayList<Point2D> invertedTrace = new ArrayList<Point2D>();
		for (int i = 0; i < trace.size(); i++) {
			invertedTrace.add(invertYCoordinates(trace.get(i), figHeight));
		}
		return invertedTrace;
	}
	
	public static int imputeTIndex(double t, int length) {
		return (int) Math.round(t * (length - 1));
	}
}
