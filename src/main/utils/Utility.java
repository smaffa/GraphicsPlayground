package main.utils;

import java.awt.geom.Point2D;

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
}
