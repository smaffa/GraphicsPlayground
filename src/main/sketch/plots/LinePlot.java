package main.sketch.plots;

import java.util.ArrayList;
import java.util.Collection;

import main.shapes.AnnotationShape;
import main.shapes.PlotArrow;
import main.shapes.PlotLine;
import main.utils.Constants;
import main.utils.Utility;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.*;

/**
 * Class that stores the data for a line plot and arranges the drawing layout appropriately
 * @author smaffa
 *
 */
public class LinePlot extends GraphicsPlot {

	private ArrayList<PlotLine> lines = new ArrayList<PlotLine>();
	private ArrayList<PlotLine> projections = new ArrayList<PlotLine>();
	private ArrayList<AnnotationShape> annotations = new ArrayList<AnnotationShape>();
	private ArrayList<PlotLine> annotationProjections = new ArrayList<PlotLine>();
	
	private int figWidth = 500;
	private int figHeight = 500;
	private double borderProportion = 0.05;
	
	private double axisX = this.figHeight / 2; // y coordinate of x-axis
	private double axisY = this.figWidth / 2; // 
	
	private boolean showT = false;
	private double t;
	
	private boolean showAnnotations = false;
	
	public LinePlot() {}
	
	public LinePlot(int width, int height) {
		this.figWidth = width;
		this.figHeight = height;
		this.axisY = width / 2;
		this.axisX = height / 2;
	}
	
	public LinePlot(int width, int height, double axisY, double axisX, double border) {
		this.figWidth = width;
		this.figHeight = height;
		if (0 <= border & border < 0.5) {
			this.borderProportion = border;
		}
		if (this.borderProportion * width < axisY & axisY < (1 - this.borderProportion) * width) {
			this.axisY = axisY;
		} else {
			this.axisY = width / 2;
		}
		if (this.borderProportion * height < axisX & axisX < (1 - this.borderProportion) * height) {
			this.axisX = axisX;
		} else {
			this.axisX = height / 2;
		}
	}
	
	public int addLine(PlotLine line) {
		lines.add(line);
		return lines.size();
	}
	
	public int addLine(Collection<Point2D> trace, Color c) {
		lines.add(new PlotLine(trace, c));
		return lines.size();
	}
	
	public void setLine(int idx, PlotLine line) {
		lines.set(idx, line);
	}
	
	public int addAnnotation(AnnotationShape annShape) {
		this.annotations.add(annShape);
		return this.annotations.size();
	}
	
	public void setAnnotation(int idx, AnnotationShape annShape) {
		annotations.set(idx, annShape);
	}
	
	@Override
	public void computePlotProjection() {
		
		// choose a scale so that all lines fit in the border
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double maxY = Double.MIN_VALUE;
		
		for (PlotLine line : lines) {	
			if (line.getMinX() < minX) {
				minX = line.getMinX();
			}
			if (line.getMaxX() > maxX) {
				maxX = line.getMaxX();
			}
			if (line.getMinY() < minY) {
				minY = line.getMinY();
			}
			if (line.getMaxY() > maxY) {
				maxY = line.getMaxY();
			}
		}
		
		// the negative range is the axis shift
		// the positive range is the remaining space
		double posBorderX = (this.figWidth - this.axisY) - (this.borderProportion * this.figWidth);
		double negBorderX = (this.borderProportion * this.figWidth) - this.axisY;
		double posBorderY = (this.figHeight - this.axisX) - (this.borderProportion * this.figHeight);
		double negBorderY = (this.borderProportion * this.figHeight) - this.axisX;
		
		double minBorderRatio = Double.MAX_VALUE;
		if (minX != 0 & negBorderX / minX > 0) {
			minBorderRatio = negBorderX / minX;
		}
		if (maxX != 0 & posBorderX / maxX > 0 & posBorderX / maxX < minBorderRatio) {
			minBorderRatio = posBorderX / maxX;
		}
		if (minY != 0 & negBorderY / minY > 0 & negBorderY / minY < minBorderRatio) {
			minBorderRatio = negBorderY / minY;
		}
		if (maxY != 0 & posBorderY / maxY > 0 & posBorderY / maxY < minBorderRatio) {
			minBorderRatio = posBorderY / maxY;
		}
		
		
		projections.clear();
		for (PlotLine line : lines) {
			PlotLine projectedLine = new PlotLine(line);
			projectedLine.scale(minBorderRatio);
			projectedLine.translate(this.axisY, this.axisX);
			projections.add(projectedLine);
		}
		
		annotationProjections.clear();
		for (AnnotationShape ann : annotations) {
			if (ann instanceof PlotArrow) {
				PlotArrow projectedArrow = new PlotArrow((PlotArrow) ann);
				projectedArrow.scale(minBorderRatio);
				projectedArrow.translate(this.axisY, this.axisX);
				annotationProjections.add(new PlotLine(projectedArrow.getTrace(), Color.RED));
			} else {
				PlotLine projectedAnnotation = new PlotLine(ann.getTrace(), Color.RED);
				projectedAnnotation.scale(minBorderRatio);
				projectedAnnotation.translate(this.axisY,  this.axisX);
				annotationProjections.add(projectedAnnotation);
			}
		}
	}
	
	public void drawAxes(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(2.0f));
		
		g2d.draw(new Line2D.Double(0, this.axisY,
                    this.figWidth, this.axisY));
		g2d.draw(new Line2D.Double(this.figHeight - this.axisX, 0,
				this.figHeight - this.axisX, this.figHeight));
	}
	
	public void drawPlotLine(Graphics2D g2d, PlotLine line) {
    	g2d.setColor(line.getColor());
    	g2d.setStroke(new BasicStroke(1.0f));
    	
    	ArrayList<Point2D> tracePoints = line.getTrace();
    	
    	for (int i = 0; i < tracePoints.size() - 1; i++) {
    		// flip the y components
            g2d.draw(new Line2D.Double(tracePoints.get(i).getX(), (this.figHeight - tracePoints.get(i).getY()),
                    tracePoints.get(i+1).getX(), (this.figHeight - tracePoints.get(i+1).getY())));
        }
    	
    	g2d.draw(getBounds());
    }
	
	public void drawPoint(Graphics2D g2d, Point2D pt, Color color, boolean fill) {
    	double offset = Constants.POINT_RADIUS;
    	if (fill) {
    		g2d.setPaint(color);
    		g2d.fill(new Ellipse2D.Double(pt.getX() - offset, pt.getY() - offset,
                    2 * Constants.POINT_RADIUS, 2 * Constants.POINT_RADIUS));
    	} else {
    		g2d.setColor(color);
    		g2d.draw(new Ellipse2D.Double(pt.getX() - offset, pt.getY() - offset,
                2 * Constants.POINT_RADIUS, 2 * Constants.POINT_RADIUS));
    	}
    }
	
	@Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);
        
        computePlotProjection();
        
        drawAxes(g2d);
        for (PlotLine line : projections) {
        	drawPlotLine(g2d, line);
        }
        
        if (this.showT) {
        	for (PlotLine line : projections) {
        		drawPoint(g2d, 
        				Utility.invertYCoordinates(line.getTrace().get(Utility.imputeTIndex(t, line.getTrace().size())), this.figHeight), 
        				Color.MAGENTA, false);
            }
        }
        if (this.showAnnotations) {
        	for (PlotLine ann : annotationProjections) {
        		drawPlotLine(g2d, new PlotLine(ann.getTrace(), ann.getColor()));
            }
        }
        g2d.setColor(Color.BLACK);
    }
	
	public void setShowT(boolean showT) {
		this.showT = showT;
		this.repaint();
	}
	
	public void setT(double t) {
		this.t = t;
	}
	
	public void setShowAnnotations(boolean showAnnotations) {
		this.showAnnotations = showAnnotations;
		this.repaint();
	}
	
	public ArrayList<PlotLine> getLines() {
		return this.lines;
	}
	
	public ArrayList<PlotLine> getProjections() {
		return this.projections;
	}
	
	public int getFigureWidth() {
		return this.figWidth;
	}
	
	public int getFigureHeight() {
		return this.figHeight;
	}
	
	public double getBorderProportion() {
		return this.borderProportion;
	}
	
	public double getAxisX() {
		return axisX;
	}

	public double getAxisY() {
		return axisY;
	}

}
