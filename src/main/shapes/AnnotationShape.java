package main.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Point2D;

/**
 * Abstract class for general shapes to be painted onto a canvas. AnnotationShapes are drawn using a
 * single continuous trace of points.
 * @author smaffa
 *
 */
public abstract class AnnotationShape {
	
	private Color c = Color.BLACK;
	private Stroke traceStroke = new BasicStroke(1.0f); 
	private boolean showShape = true;
	
	/**
	 * Provides the trace of a given annotation shape.
	 * @return An array of {@link Point2D} objects representing the outline of the shape as a continuous trace.
	 */
	public abstract Point2D[] getTrace();

	public Color getColor() {
		return c;
	}

	public void setColor(Color c) {
		this.c = c;
	}

	public Stroke getTraceStroke() {
		return this.traceStroke;
	}

	public void setTraceStroke(Stroke stroke) {
		this.traceStroke = stroke;
	}
	
	public boolean getShowShape() {
		return this.showShape;
	}
	
	public void setShowShape(boolean show) {
		this.showShape = show;
	}
	

}
