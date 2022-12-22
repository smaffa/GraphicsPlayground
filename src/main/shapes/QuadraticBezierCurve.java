package main.shapes;

import main.utils.Utility;

import java.awt.geom.Point2D;

public class QuadraticBezierCurve extends BezierCurve {
    public Point2D p1;
    public Point2D p2;
    public Point2D p3;

    public Point2D[] p1p2Endpoints = new Point2D[this.getBezierFineness() + 1];
    public Point2D[] p2p3Endpoints = new Point2D[this.getBezierFineness() + 1];
    public Point2D[] traceArray = new Point2D[this.getBezierFineness() + 1];

    public QuadraticBezierCurve(Point2D p1, Point2D p2, Point2D p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p2 = p3;

        computePosition();
    }

    public Point2D[] computePosition() {
        for (int i = 0; i <= this.getBezierFineness(); i++) {
            double t = (double) i / this.getBezierFineness();
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
    
    public Point2D[] computeLerpsAtT(double t) {
    	int i = (int) Math.round(this.getBezierFineness() * t);
    	return new Point2D[]{traceArray[i], 
    			p1p2Endpoints[i], p2p3Endpoints[i]};
    }
    
    public int getOrder() {
    	return 2;
    }

	@Override
	public QuadraticBezierCurve translate(double x, double y) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public QuadraticBezierCurve reflect(double axisVectorX, double axisVectorY) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public QuadraticBezierCurve scale(double xScale, double yScale) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public QuadraticBezierCurve scale(double factor) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public QuadraticBezierCurve rotate(double radians) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public QuadraticBezierCurve shearX(double factor) {
		// TODO Auto-generated method stub
		return this;
	}
	
	@Override
	public QuadraticBezierCurve shearY(double factor) {
		// TODO Auto-generated method stub
		return this;
	}
}
