package main.shapes;

import main.utils.Constants;

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
