package main.shapes;

import java.awt.geom.Point2D;

public class PolyLine extends AnnotationShape implements Transformable2D<PolyLine> {

	private Point2D[] trace;
	
	public PolyLine(Point2D[] trace) {
		this.trace = trace;
	}
	
	@Override
	public Point2D[] getTrace() {
		// TODO Auto-generated method stub
		return trace;
	}

	@Override
	public PolyLine translate(double xDelta, double yDelta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolyLine reflect(double axisVectorX, double axisVectorY) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolyLine scale(double xScale, double yScale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolyLine scale(double factor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolyLine rotate(double radians) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolyLine shearX(double factor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolyLine shearY(double factor) {
		// TODO Auto-generated method stub
		return null;
	}

}
