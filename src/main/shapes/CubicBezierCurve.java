package main.shapes;

import main.utils.Constants;
import main.utils.Utility;

import java.awt.geom.Point2D;
import java.util.Objects;

import org.ejml.simple.SimpleMatrix;

/**
 * A class representing cubic Bezier curves. Cubic Bezier curves are defined by 4 points.
 * @author smaffa
 *
 */
public class CubicBezierCurve extends BezierCurve {
	private Point2D p1;
    private Point2D c1;
    private Point2D c2;
    private Point2D p2;
    
    /**
     * Basic constructor for a CubicBezierCurve based on all four control points.
     * @param p1	a {@link Point2D} object representing the first endpoint of the curve
     * @param c1	a {@link Point2D} object representing the first control point of the curve
     * @param c2	a {@link Point2D} object representing the second control point of the curve
     * @param p2	a {@link Point2D} object representing the second endpoint of the curve
     */
    public CubicBezierCurve(Point2D p1, Point2D c1, Point2D c2, Point2D p2) {    	
        this.p1 = p1;
        this.c1 = c1;
        this.c2 = c2;
        this.p2 = p2;
    }
    
    /**
     * Constructor for a CubicBezierCurve based on all four control points and the fineness of the curve.
     * @param p1	a {@link Point2D} object representing the first endpoint of the curve
     * @param c1	a {@link Point2D} object representing the first control point of the curve
     * @param c2	a {@link Point2D} object representing the second control point of the curve
     * @param p2	a {@link Point2D} object representing the second endpoint of the curve
     * @param bezierFineness	an integer specifying the number of points to interpolate along the curve
     */
    public CubicBezierCurve(Point2D p1, Point2D c1, Point2D c2, Point2D p2, int bezierFineness) {    	
        this.p1 = p1;
        this.c1 = c1;
        this.c2 = c2;
        this.p2 = p2;
    	super.setBezierFineness(bezierFineness);
    }
    
    /**
     * Copy constructor
     * @param other		another CubicBezierCurve
     */
    public CubicBezierCurve(CubicBezierCurve other) {
    	this.p1 = new Point2D.Double(other.getP1().getX(), other.getP1().getY());
    	this.c1 = new Point2D.Double(other.getC1().getX(), other.getC1().getY());
    	this.c2 = new Point2D.Double(other.getC2().getX(), other.getC2().getY());
    	this.p2 = new Point2D.Double(other.getP2().getX(), other.getP2().getY());
    	this.setBezierFineness(other.getBezierFineness());
    }
    
    public Point2D getP1() {
		return p1;
	}

	public void setP1(Point2D p1) {
		this.p1 = p1;
	}

	public Point2D getC1() {
		return c1;
	}

	public void setC1(Point2D c1) {
		this.c1 = c1;
	}

	public Point2D getC2() {
		return c2;
	}

	public void setC2(Point2D c2) {
		this.c2 = c2;
	}

	public Point2D getP2() {
		return p2;
	}

	public void setP2(Point2D p2) {
		this.p2 = p2;
	}
	
	@Override
	public Point2D[] getControlPoints() {
        return new Point2D[]{p1, c1, c2, p2};
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(c1, c2, p1, p2);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CubicBezierCurve other = (CubicBezierCurve) obj;
		return Objects.equals(c1, other.c1) && Objects.equals(c2, other.c2) && Objects.equals(p1, other.p1)
				&& Objects.equals(p2, other.p2);
	}

	@Override
	public String toString() {
		return "CubicBezierCurve [p1=" + p1 + ", c1=" + c1 + ", c2=" + c2 + ", p2=" + p2 + "]";
	}

	/**
	 * Computes the parameter matrix for efficient calculation of the full curve's position.
	 * @param bezierFineness	the number of points to interpolate along the curve
	 * @return A {@link SimpleMatrix} of row vectors [1, t, t^2, t^3] for all values t between 0 and 1, inclusive,
	 * discretized into bezierFineness intervals
	 */
	private SimpleMatrix computeTMatrixPosition(int bezierFineness) {
    	double[] tVals = Utility.linspace(0, 1, bezierFineness + 1);
    	
    	SimpleMatrix tCoefficientMatrix = new SimpleMatrix(bezierFineness + 1, 4);
    	
        for (int i = 0; i <= bezierFineness; i++) {
            tCoefficientMatrix.set(i, 0, 1);
            tCoefficientMatrix.set(i, 1, tVals[i]);
            tCoefficientMatrix.set(i, 2, Math.pow(tVals[i], 2));
            tCoefficientMatrix.set(i, 3, Math.pow(tVals[i], 3));
        }
        
        return tCoefficientMatrix;
    }
        
	/**
	 * Computes the parameter matrix for efficient calculation of the full curve's velocity.
	 * @param bezierFineness	the number of points to interpolate along the curve
	 * @return A {@link SimpleMatrix} of row vectors [0, 1, 2t, 3t^2] for all values t between 0 and 1, inclusive,
	 * discretized into bezierFineness intervals
	 */
    private SimpleMatrix computeTMatrixVelocity(int bezierFineness) {
    	double[] tVals = Utility.linspace(0, 1, bezierFineness + 1);
    	
    	SimpleMatrix tCoefficientMatrix = new SimpleMatrix(bezierFineness + 1, 4);
    	
        for (int i = 0; i <= bezierFineness; i++) {
            tCoefficientMatrix.set(i, 1, 1);
            tCoefficientMatrix.set(i, 2, 2 * tVals[i]);
            tCoefficientMatrix.set(i, 3, 3 * Math.pow(tVals[i], 2));
        }
        
        return tCoefficientMatrix;
    }
       
    /**
	 * Computes the parameter matrix for efficient calculation of the full curve's acceleration.
	 * @param bezierFineness	the number of points to interpolate along the curve
	 * @return A {@link SimpleMatrix} of row vectors [0, 0, 2, 6t] for all values t between 0 and 1, inclusive,
	 * discretized into bezierFineness intervals
	 */
    private SimpleMatrix computeTMatrixAcceleration(int bezierFineness) {
    	double[] tVals = Utility.linspace(0, 1, bezierFineness + 1);
    	
    	SimpleMatrix tCoefficientMatrix = new SimpleMatrix(bezierFineness + 1, 4);
    	
        for (int i = 0; i <= bezierFineness; i++) {
            tCoefficientMatrix.set(i, 2, 2);
            tCoefficientMatrix.set(i, 3, 6 * tVals[i]);
        }
        
        return tCoefficientMatrix;
    }
    
    /**
	 * Converts the control points into a matrix for efficient calculation.
	 * @return A {@link SimpleMatrix} where each row is a control point.
	 */
    private SimpleMatrix getControlCoordinateMatrix() {
    	return new SimpleMatrix(new double[][] {
    		{p1.getX(), p1.getY()}, 
			{c1.getX(), c1.getY()}, 
			{c2.getX(), c2.getY()},
			{p2.getX(), p2.getY()}
		});
    }
   
    /**
     * Provides an array of points representing the position of the curve. The curve is discretely quantized by 
     * bezierFineness regular intervals on the curve parameter t, bounded between 0 and 1, inclusive
     * @param bezierFineness	the number of points to interpolate along the curve
     * @return An array of {@link Point2D} objects of length bezierFineness + 1 representing (x,y) coordinates
     * on the curve
     */
    public Point2D[] computePosition(int bezierFineness) {
    	SimpleMatrix controlCoordinateMatrix = getControlCoordinateMatrix();
        SimpleMatrix positionMatrix = computeTMatrixPosition(bezierFineness).mult(Constants.CUBIC_POINT_COEFFICIENT_MATRIX).mult(controlCoordinateMatrix);
        
        Point2D[] positionArray = new Point2D[bezierFineness + 1];
        for (int i = 0; i <= bezierFineness; i++) { 
        	positionArray[i] = new Point2D.Double(positionMatrix.get(i, 0), positionMatrix.get(i, 1));
        }
        return positionArray;
    }
    
    @Override
    public Point2D[] computePosition() {
    	return computePosition(this.getBezierFineness());
    }
    
    @Override
    public Point2D computePositionAtT(double t) {
    	SimpleMatrix controlCoordinateMatrix = getControlCoordinateMatrix();
    	SimpleMatrix tPoint = new SimpleMatrix(new double[][] {
    		{1, t, Math.pow(t, 2), Math.pow(t, 3)}
    	});
        SimpleMatrix positionMatrix = tPoint.mult(Constants.CUBIC_POINT_COEFFICIENT_MATRIX).mult(controlCoordinateMatrix);
        
        return new Point2D.Double(positionMatrix.get(0, 0), positionMatrix.get(0, 1));
    }
    
    /**
     * Provides an array of points representing the velocity along the curve. The curve is discretely quantized by 
     * bezierFineness regular intervals on the curve parameter t, bounded between 0 and 1, inclusive
     * @param bezierFineness	the number of points to interpolate along the curve
     * @return An array of {@link Point2D} objects of length bezierFineness + 1 representing velocities along
     * the curve
     */
    public Point2D[] computeVelocity(int bezierFineness) {
    	SimpleMatrix controlCoordinateMatrix = getControlCoordinateMatrix();
        SimpleMatrix velocityMatrix = computeTMatrixVelocity(bezierFineness).mult(Constants.CUBIC_POINT_COEFFICIENT_MATRIX).mult(controlCoordinateMatrix);
        
        Point2D[] velocityArray = new Point2D[bezierFineness + 1];
        for (int i = 0; i <= this.getBezierFineness(); i++) { 
        	velocityArray[i] = new Point2D.Double(velocityMatrix.get(i, 0), velocityMatrix.get(i, 1));
        }
        
        return velocityArray;
    }

    /**
     * Provides an array of points representing the velocity along the curve. The curve is discretely quantized by 
     * bezierFineness regular intervals on the curve parameter t, bounded between 0 and 1, inclusive
     * @return An array of {@link Point2D} objects of length bezierFineness + 1 representing velocities along
     * the curve
     */
    public Point2D[] computeVelocity() {
    	return computeVelocity(this.getBezierFineness());
    }
    
    public Point2D computeVelocityAtT(double t) {
    	SimpleMatrix controlCoordinateMatrix = getControlCoordinateMatrix();
    	SimpleMatrix tPoint = new SimpleMatrix(new double[][] {
    		{0, 1, 2 * t, 3 * Math.pow(t, 2)}
    	});
        SimpleMatrix velocityMatrix = tPoint.mult(Constants.CUBIC_POINT_COEFFICIENT_MATRIX).mult(controlCoordinateMatrix);
        
        return new Point2D.Double(velocityMatrix.get(0, 0), velocityMatrix.get(0, 1));
    }
    
    /**
     * Provides an array of points representing the acceleration along the curve. The curve is discretely quantized by 
     * bezierFineness regular intervals on the curve parameter t, bounded between 0 and 1, inclusive
     * @param bezierFineness	the number of points to interpolate along the curve
     * @return An array of {@link Point2D} objects of length bezierFineness + 1 representing acceleration along
     * the curve
     */
    public Point2D[] computeAcceleration(int bezierFineness) {
    	SimpleMatrix controlCoordinateMatrix = getControlCoordinateMatrix();
        SimpleMatrix accelerationMatrix = computeTMatrixAcceleration(bezierFineness).mult(Constants.CUBIC_POINT_COEFFICIENT_MATRIX).mult(controlCoordinateMatrix);
        
        Point2D[] accelerationArray = new Point2D[bezierFineness + 1];
        for (int i = 0; i <= this.getBezierFineness(); i++) { 
        	accelerationArray[i] = new Point2D.Double(accelerationMatrix.get(i, 0), accelerationMatrix.get(i, 1));
        }
        
        return accelerationArray;
    }
    
    /**
     * Provides an array of points representing the acceleration along the curve. The curve is discretely quantized by 
     * bezierFineness regular intervals on the curve parameter t, bounded between 0 and 1, inclusive
     * @return An array of {@link Point2D} objects of length bezierFineness + 1 representing acceleration along
     * the curve
     */
    public Point2D[] computeAcceleration() {
    	return computeAcceleration(this.getBezierFineness());
    }
    
    public Point2D computeAccelerationAtT(double t) {
    	SimpleMatrix controlCoordinateMatrix = getControlCoordinateMatrix();
    	SimpleMatrix tPoint = new SimpleMatrix(new double[][] {
    		{0, 0, 2, 6 * t}
    	});
        SimpleMatrix accelerationMatrix = tPoint.mult(Constants.CUBIC_POINT_COEFFICIENT_MATRIX).mult(controlCoordinateMatrix);
        
        return new Point2D.Double(accelerationMatrix.get(0, 0), accelerationMatrix.get(0, 1));
    }
    
    public double[] computeCurvature(int bezierFineness) {
    	Point2D[] velocityArray = computeVelocity(bezierFineness);
    	Point2D[] accelerationArray = computeAcceleration(bezierFineness);
    	
    	double[] curvatureArray = new double[bezierFineness + 1];
    	for (int i = 0; i <= this.getBezierFineness(); i++) { 
    		double determinant = (velocityArray[i].getX() * accelerationArray[i].getY()) - 
        			(velocityArray[i].getY() * accelerationArray[i].getX());
        	double vectorNorm = Utility.computeVectorNorm(velocityArray[i]);
        	curvatureArray[i] = determinant / Math.pow(vectorNorm, 3);
        }
    	
    	return curvatureArray;
    }
    
    public double[] computeCurvature() {
    	return computeCurvature(this.getBezierFineness());
    }
    
    public double computeCurvatureAtT(double t) {
    	Point2D velocityVector = computeVelocityAtT(t);
    	Point2D accelerationVector = computeAccelerationAtT(t);
    	
    	double determinant = (velocityVector.getX() * accelerationVector.getY()) - 
    			(velocityVector.getY() * accelerationVector.getX());
    	double vectorNorm = Utility.computeVectorNorm(velocityVector);
    	
    	return determinant / Math.pow(vectorNorm, 3);
    }
    
    @Override
    public Point2D[] computeLerpsAtT(double t) {
    	Point2D[] lerpsAtT = new Point2D[6];
    	
    	lerpsAtT[3] = Utility.lerp(p1, c1, t);
    	lerpsAtT[4] = Utility.lerp(c1, c2, t);
    	lerpsAtT[5] = Utility.lerp(c2, p2, t);
    	
    	lerpsAtT[1] = Utility.lerp(lerpsAtT[3], lerpsAtT[4], t);
    	lerpsAtT[2] = Utility.lerp(lerpsAtT[4], lerpsAtT[5], t);
    	
    	lerpsAtT[0] = Utility.lerp(lerpsAtT[1], lerpsAtT[2], t);

    	return lerpsAtT;
    }
    
    @Override
    public int getOrder() {
    	return 3;
    }
    
	@Override
	public CubicBezierCurve translate(double xDelta, double yDelta) {
		this.p1.setLocation(p1.getX() + xDelta, p1.getY() + yDelta);
		this.c1.setLocation(c1.getX() + xDelta, c1.getY() + yDelta);
		this.c2.setLocation(c2.getX() + xDelta, c2.getY() + yDelta);
		this.p2.setLocation(p2.getX() + xDelta, p2.getY() + yDelta);
		return this;
	}
	
	/**
	 * Translates the coordinates of the curve such that the first endpoint (P1) is located at (0,0)
	 * @return the translated CubicBezierCurve
	 */
	public CubicBezierCurve centerAtP1() {
    	double xDelta = p1.getX();
    	double yDelta = p1.getY();
    	translate(-xDelta, -yDelta);
    	return this;
    }

	@Override
	public CubicBezierCurve reflect(double axisVectorX, double axisVectorY) {
		double vectorNorm = Math.sqrt(Math.pow(axisVectorX, 2) + Math.pow(axisVectorY, 2));
		double xComponent = axisVectorX / vectorNorm;
		double yComponent = axisVectorY / vectorNorm;
		double xComponentSquared = Math.pow(xComponent, 2);
		double yComponentSquared = Math.pow(yComponent, 2);
		double transformCoeff00 = xComponentSquared - yComponentSquared;
		double transformCoeff01 = 2 * xComponent * yComponent;
		double transformCoeff11 = yComponentSquared - xComponentSquared;
		
		this.p1.setLocation(transformCoeff00 * p1.getX() + transformCoeff01 * p1.getY(), 
				transformCoeff01 * p1.getX() + transformCoeff11 * p1.getY());
		this.c1.setLocation(transformCoeff00 * c1.getX() + transformCoeff01 * c1.getY(), 
				transformCoeff01 * c1.getX() + transformCoeff11 * c1.getY());
		this.c2.setLocation(transformCoeff00 * c2.getX() + transformCoeff01 * c2.getY(), 
				transformCoeff01 * c2.getX() + transformCoeff11 * c2.getY());
		this.p2.setLocation(transformCoeff00 * p2.getX() + transformCoeff01 * p2.getY(), 
				transformCoeff01 * p2.getX() + transformCoeff11 * p2.getY());
		return this;
	}

	@Override
	public CubicBezierCurve scale(double xScale, double yScale) {
		// TODO Auto-generated method stub
		this.p1.setLocation(p1.getX() * xScale, p1.getY() * yScale);
		this.c1.setLocation(c1.getX() * xScale, c1.getY() * yScale);
		this.c2.setLocation(c2.getX() * xScale, c2.getY() * yScale);
		this.p2.setLocation(p2.getX() * xScale, p2.getY() * yScale);
		return this;
	}

	@Override
	public CubicBezierCurve scale(double factor) {
		// TODO Auto-generated method stub
		return scale(factor, factor);
	}

	@Override
	public CubicBezierCurve rotate(double radians) {
		// TODO Auto-generated method stub
		double cosCoeff;
		double sinCoeff;
		if (radians == Math.PI / 2) {
			cosCoeff = 0;
			sinCoeff = 1;
		} else if (radians == Math.PI) {
			cosCoeff = -1;
			sinCoeff = 0;
		} else if (radians == 3 * Math.PI / 2) {
			cosCoeff = 0;
			sinCoeff = -1;
		} else {
			cosCoeff = Math.cos(radians);
			sinCoeff = Math.sin(radians);
		}
		this.p1.setLocation(cosCoeff * p1.getX() - sinCoeff * p1.getY(),
				sinCoeff * p1.getX() + cosCoeff * p1.getY());
		this.c1.setLocation(cosCoeff * c1.getX() - sinCoeff * c1.getY(),
				sinCoeff * c1.getX() + cosCoeff * c1.getY());
		this.c2.setLocation(cosCoeff * c2.getX() - sinCoeff * c2.getY(),
				sinCoeff * c2.getX() + cosCoeff * c2.getY());
		this.p2.setLocation(cosCoeff * p2.getX() - sinCoeff * p2.getY(),
				sinCoeff * p2.getX() + cosCoeff * p2.getY());
		return this;
	}

	@Override
	public CubicBezierCurve shearX(double factor) {
		this.p1.setLocation(p1.getX() + factor * p1.getY(), p1.getY());
		this.c1.setLocation(c1.getX() + factor * c1.getY(), c1.getY());
		this.c2.setLocation(c2.getX() + factor * c2.getY(), c2.getY());
		this.p2.setLocation(p2.getX() + factor * p2.getY(), p2.getY());
		return this;
	}
	
	@Override
	public CubicBezierCurve shearY(double factor) {
		this.p1.setLocation(p1.getX(), factor * p1.getX() + p1.getY());
		this.c1.setLocation(c1.getX(), factor * c1.getX() + c1.getY());
		this.c2.setLocation(c2.getX(), factor * c2.getX() + c2.getY());
		this.p2.setLocation(p2.getX(), factor * p2.getX() + p2.getY());
		return this;
	}
}