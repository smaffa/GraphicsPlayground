package main.shapes;

import main.utils.Constants;

import java.awt.geom.Point2D;

public abstract class BezierCurve {
    public int bezierFineness = Constants.BEZIER_FINENESS;

    public void drawPoint(double t) {}

    public abstract Point2D[] computeCurve();

    public abstract Point2D[] getTraceArray();

    public abstract Point2D[] getControlPoints();
    
    public abstract Point2D[] getAllPointsAtT(double t);
    
    public abstract int getOrder();

}
