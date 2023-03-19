package main.shapes;

import java.awt.geom.Point2D;

/**
 * Class for Arrows drawn onto a canvas. All transformations operate only on the underlying vector.
 * @author smaffa
 *
 */
public class Arrow extends AnnotationShape implements Transformable2D<Arrow> {

	private double xStart; 
	private double yStart; 
	private double xVector; 
	private double yVector; 
	
	/**
	 * Basic constructor for an Arrow based on its tail coordinates and vector.
	 * @param xStart	the x coordinate of the tail
	 * @param yStart	the y coordinate of the tail
	 * @param xVector	the x component of the vector from the tail to the head
	 * @param yVector	the y component of the vector from the tail to the head
	 */
	public Arrow(double xStart, double yStart, double xVector, double yVector) {
		this.xStart = xStart;
		this.yStart = yStart;
		this.xVector = xVector;
		this.yVector = yVector;
	}
	
	/**
	 * Copy constructor
	 * @param other		another Arrow object
	 */
	public Arrow(Arrow other) {
		this.xStart = other.getxStart();
		this.yStart = other.getyStart();
		this.xVector = other.getxVector();
		this.yVector = other.getyVector();
		super.setColor(other.getColor());
		super.setTraceStroke(other.getTraceStroke());
		super.setShowShape(other.getShowShape());
	}
	
	public double getxStart() {
		return xStart;
	}

	public void setxStart(double xStart) {
		this.xStart = xStart;
	}

	public double getyStart() {
		return yStart;
	}

	public void setyStart(double yStart) {
		this.yStart = yStart;
	}

	public double getxVector() {
		return xVector;
	}

	public void setxVector(double xVector) {
		this.xVector = xVector;
	}

	public double getyVector() {
		return yVector;
	}

	public void setyVector(double yVector) {
		this.yVector = yVector;
	}

	@Override
	public Point2D[] getTrace() {
		Point2D[] trace = new Point2D[5];
		trace[0] = new Point2D.Double(this.xStart, this.yStart);
		trace[1] = new Point2D.Double(this.xStart + xVector, this.yStart + this.yVector);
		
		// make the triangle head by projecting 1/4 of the vector length 30 degrees from the tip in both directions backward
		double headLengthX = 0.25 * this.xVector;
		double headLengthY = 0.25 * this.yVector;
		
		double cosCoeff = Math.cos(Math.PI / 6);
		double sinCoeff = Math.sin(Math.PI / 6);
		Point2D headRotation1 = new Point2D.Double(cosCoeff * headLengthX - sinCoeff * headLengthY,
				sinCoeff * headLengthX + cosCoeff * headLengthY);
		trace[2] = new Point2D.Double(trace[1].getX() - headRotation1.getX(), trace[1].getY() - headRotation1.getY());
		
		trace[3] = trace[1];
		
		cosCoeff = Math.cos(11 * Math.PI / 6);
		sinCoeff = Math.sin(11 * Math.PI / 6);
		Point2D headRotation2 = new Point2D.Double(cosCoeff * headLengthX - sinCoeff * headLengthY,
				sinCoeff * headLengthX + cosCoeff * headLengthY);
		trace[4] = new Point2D.Double(trace[1].getX() - headRotation2.getX(), trace[1].getY() - headRotation2.getY());
	
		return trace;
	}

	@Override
	public Arrow translate(double xDelta, double yDelta) {
		// TODO Auto-generated method stub
		this.xStart = this.xStart + xDelta;
		this.yStart = this.yStart + yDelta;
		return this;
	}

	@Override
	public Arrow reflect(double axisVectorX, double axisVectorY) {
		// TODO Auto-generated method stub
		double vectorNorm = Math.sqrt(Math.pow(axisVectorX, 2) + Math.pow(axisVectorY, 2));
		double xComponent = axisVectorX / vectorNorm;
		double yComponent = axisVectorY / vectorNorm;
		double xComponentSquared = Math.pow(xComponent, 2);
		double yComponentSquared = Math.pow(yComponent, 2);
		double transformCoeff00 = xComponentSquared - yComponentSquared;
		double transformCoeff01 = 2 * xComponent * yComponent;
		double transformCoeff11 = yComponentSquared - xComponentSquared;
		
		this.xStart = transformCoeff00 * this.xStart + transformCoeff01 * this.yStart;
		this.yStart = transformCoeff01 * this.xStart + transformCoeff11 * this.yStart;
		
		this.xVector = transformCoeff00 * this.xVector + transformCoeff01 * this.yVector;
		this.yVector = transformCoeff01 * this.xVector + transformCoeff11 * this.yVector;
		
		return this;
	}

	@Override
	public Arrow scale(double xScale, double yScale) {
		// TODO Auto-generated method stub
		this.xStart = this.xStart * xScale;
		this.yStart = this.yStart * yScale;
		this.xVector = this.xVector * xScale;
		this.yVector = this.yVector * yScale;
		return this;
	}

	@Override
	public Arrow scale(double factor) {
		// TODO Auto-generated method stub
		return scale(factor, factor);
	}

	@Override
	public Arrow rotate(double radians) {
		// TODO Auto-generated method stub
		double cosCoeff = Math.cos(radians);
		double sinCoeff = Math.sin(radians);
		this.xStart = cosCoeff * this.xStart - sinCoeff * this.yStart;
		this.yStart = sinCoeff * this.xStart + cosCoeff * this.yStart;
		this.xVector = cosCoeff * this.xVector - sinCoeff * this.yVector;
		this.yVector = sinCoeff * this.xVector + cosCoeff * this.yVector;
		return this;
	}

	@Override
	public Arrow shearX(double factor) {
		// TODO Auto-generated method stub
		this.xStart = this.xStart + factor * this.yStart;
		this.xVector = this.xVector + factor * this.yVector;
		return this;
	}

	@Override
	public Arrow shearY(double factor) {
		// TODO Auto-generated method stub
		this.yStart = this.yStart + factor * this.xStart;
		this.yVector = this.yVector + factor * this.xVector;
		return this;
	}

}
