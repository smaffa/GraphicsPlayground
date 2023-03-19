package main.shapes;

import main.utils.Constants;

import java.awt.geom.Point2D;
import java.util.Objects;

public abstract class BezierCurve implements Transformable2D<BezierCurve> {
    private int bezierFineness = Constants.BEZIER_FINENESS;

    public void drawPoint(double t) {}

    public abstract Point2D[] computePosition();
    
    public abstract Point2D computePositionAtT(double t);

    public abstract Point2D[] getControlPoints();
    
    public abstract Point2D[] computeLerpsAtT(double t);
    
    public abstract int getOrder();

	public int getBezierFineness() {
		return bezierFineness;
	}

	public void setBezierFineness(int bezierFineness) {
		this.bezierFineness = bezierFineness;
	}

	@Override
	public int hashCode() {
		return Objects.hash(bezierFineness);
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
		return bezierFineness == other.bezierFineness;
	}
    
    
}
