package main.shapes;

import main.utils.Constants;
import main.utils.Utility;

import java.awt.geom.Point2D;
import java.util.Arrays;

/**
 * Parent class for all objects representing a Bezier Curve in 2D space
 * @author smaffa
 *
 */
public abstract class BezierCurve implements Transformable2D<BezierCurve> {
    private int bezierFineness = Constants.BEZIER_FINENESS; // number of points to interpolate along the curve

    /**
     * Provides an array of points representing the position of the curve. The curve is discretely quantized by 
     * bezierFineness regular intervals on the curve parameter t, bounded between 0 and 1, inclusive.
     * @return An array of {@link Point2D} objects of length bezierFineness + 1 representing (x,y) coordinates
     * on the curve
     */
    public abstract Point2D[] computePosition();
    
    /**
     * Provides the point identified by parameter value t along the curve.
     * @param t		a double between 0 and 1, inclusive
     * @return A {@link Point2D} object providing the (x,y) coordinates of the specified point
     */
    public abstract Point2D computePositionAtT(double t);

    /**
     * Provides an array of points that control the shape of the curve.
     * @return An array of {@link Point2D} objects of length order + 1 providing the (x,y) coordinates of all 
     * control points for the Bezier curve. The points are ordered such that the first and last points are
     * endpoints of the curve
     */
    public abstract Point2D[] getControlPoints();
    
    /**
     * Provides an array of points that represent a sequence of linear interpolations at proportion t between
     * consecutive control points and subsequent reference points.
     * @param t		a double between 0 and 1, inclusive
     * @return An array of {@link Point2D} objects of length (order) + (order - 1) + ... + 1, where the last
     * elements are the linear interpolations between control points, preceded by the linear interpolations
     * between those reference points, and so on... until the starting element, which is the position at t
     */
    public abstract Point2D[] computeLerpsAtT(double t);
    
    /**
     * Provides the order of the curve
     * @return An integer value representing the order of the curve's polynomial
     */
    public abstract int getOrder();
    
    /**
     * Provides an array representing the cumulative arc length distance from the first endpoint to the point
     * represented by the curve parameter t for bezierFineness evenly spaced values of t.
     * @return an array of double values representing distance from the first endpoint of the curve
     */
    public double[] computeCumulativeArcLengthDistance() {
    	double[] cumulativeDistance = new double[this.bezierFineness + 1];
    	Point2D[] position = this.computePosition();
 		cumulativeDistance[0] = 0;
 		for (int i = 1; i < position.length; i++) {
 			cumulativeDistance[i] = Math.sqrt(Math.pow(position[i].getX() - position[i-1].getX(), 2) + 
 					Math.pow(position[i].getY() - position[i-1].getY(), 2)) + cumulativeDistance[i-1];
 		}
 		for (int i = 1; i < position.length; i++) {
 			cumulativeDistance[i] = cumulativeDistance[i] / cumulativeDistance[cumulativeDistance.length-1];
 		}
 		return cumulativeDistance;
    }
    
    /**
     * Estimates the value of the curve parameter t which produces the point s proportion of the distance from 
     * the first endpoint of the curve.
     * @param cumulativeDistance	the array of values representing the distance from the endpoint 
     * @param s		a double value between 0 and 1, inclusive, representing the proportion of the maximum
     * distance along the curve
     * @return a double approximation for the curve parameter t
     */
    public double arcLengthApproximateT(double[] cumulativeDistance, double s) {
    	int maxIdxLessThan = 0;
 		int minIdxGreaterThan = cumulativeDistance.length - 1;
 		double sPartial = 0;
 		for (int i = 0; i < cumulativeDistance.length - 1; i++) {
 			if (cumulativeDistance[i] <= s & s <= cumulativeDistance[i+1]) {
 				maxIdxLessThan = i;
 				minIdxGreaterThan = i + 1;
 				sPartial = (s - cumulativeDistance[i]) / (cumulativeDistance[i+1] - cumulativeDistance[i]);
 			}
 		}
 		double tLow = (double) maxIdxLessThan / this.getBezierFineness();
 		double tHigh = (double) minIdxGreaterThan / this.getBezierFineness();
 		return Utility.lerp(tLow, tHigh, sPartial);
    }

	public int getBezierFineness() {
		return bezierFineness;
	}

	public void setBezierFineness(int bezierFineness) {
		this.bezierFineness = bezierFineness;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BezierCurve other = (BezierCurve) obj;
		return Arrays.equals(this.computePosition(), other.computePosition());
	}
    
    
}
