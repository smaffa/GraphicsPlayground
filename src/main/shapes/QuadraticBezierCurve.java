package main.shapes;

import main.utils.Utility;

import java.awt.geom.Point2D;

public class QuadraticBezierCurve extends BezierCurve {
    public Point2D p1;
    public Point2D p2;
    public Point2D p3;

    public Point2D[] p1p2Endpoints = new Point2D[this.bezierFineness + 1];
    public Point2D[] p2p3Endpoints = new Point2D[this.bezierFineness + 1];
    public Point2D[] traceArray = new Point2D[this.bezierFineness + 1];

    public QuadraticBezierCurve(Point2D p1, Point2D p2, Point2D p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p2 = p3;

        computeCurve();
    }

    public Point2D[] computeCurve() {
        for (int i = 0; i <= this.bezierFineness; i++) {
            double t = (double) i / this.bezierFineness;
            p1p2Endpoints[i] = Utility.lerp(p1, p2, t);
            p2p3Endpoints[i] = Utility.lerp(p2, p3, t);

            traceArray[i] = Utility.lerp(p1p2Endpoints[i], p2p3Endpoints[i], t);
        }
        return this.traceArray;
    }

    public Point2D[] getTraceArray() {
        return this.traceArray;
    }

    public Point2D[] getControlPoints() {
        return new Point2D[]{p1, p2, p3};
    }
    
    public Point2D[] getAllPointsAtT(double t) {
    	int i = (int) Math.round(bezierFineness * t);
    	return new Point2D[]{traceArray[i], 
    			p1p2Endpoints[i], p2p3Endpoints[i]};
    }
    
    public int getOrder() {
    	return 2;
    }
}
