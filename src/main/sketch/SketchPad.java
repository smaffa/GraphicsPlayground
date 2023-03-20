package main.sketch;

import main.shapes.AnnotationShape;
import main.shapes.BezierCurve;
import main.shapes.CubicBezierCurve;
import main.utils.Constants;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * A class for creating an interactive canvas on which points can be added, deleted, adjusted freely, and
 * be connected into Bezier curves.
 * @author smaffa
 *
 */
public class SketchPad extends JPanel {

	// point data
    private ArrayList<CanvasPoint> controlPoints = new ArrayList<CanvasPoint>();
    private int selectedIndex = Constants.NULL_INDEX;

    // curve data
    private ArrayList<BezierCurve> bezierCurves = new ArrayList<BezierCurve>();
    private ArrayList<ArrayList<Integer>> bezierControlIndices = new ArrayList<ArrayList<Integer>>();
    
    // annotation data
    private ArrayList<AnnotationShape> annotationShapes = new ArrayList<AnnotationShape>();
    
    // curve creation
    private boolean isBezierCreationModeOn = false;
    private ArrayList<Integer> currentBezierCreationList = new ArrayList<Integer>(4);
    
    // curve parameter
    private double globalT = 0;
    
    // visual display modes
    private boolean showT = false;
    private boolean showReferenceLines = false;

    //visual settings
    private Color pointDefaultColor = Color.BLACK;
    private Color pointSelectedColor = Color.RED;
    private Color tPointColor = Color.MAGENTA;
    private Color[] bezierPalette = {Color.BLACK, Color.BLUE, Color.GREEN, Color.GRAY};
    final static BasicStroke dashed =
            new BasicStroke(1.0f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10.0f, new float[]{10.0f}, 0.0f);
    final static BasicStroke referenceStroke = 
    		new BasicStroke(); 
    final static BasicStroke traceStroke = 
    		new BasicStroke(2.0f); 
    
    // GUI settings
    private int canvasWidth = Constants.CANVAS_WIDTH;
    private int canvasHeight = Constants.CANVAS_HEIGHT;

    /**
     * Default constructor for a SketchPad object
     */
    public SketchPad() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        registerPointSelectionListener();
        registerDragMotionListener();
    }
    
    /**
     * Basic constructor for a SketchPad object with specified canvas height and width.
     * @param width 	the width of the canvas
     * @param height	the height of the canvas
     */
    public SketchPad(int width, int height) {
    	this.canvasWidth = width;
    	this.canvasHeight = height;
    	
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        registerPointSelectionListener();
        registerDragMotionListener();
    }
    
    /**
     * Registers a {@link MouseListener} object in the SketchPad which controls point selection.
     */
    public void registerPointSelectionListener() {
    	addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isBezierCreationModeOn) {
                    int nextSelectionIndex = getBoundingPointIndex(e.getX(), e.getY());
                    if (nextSelectionIndex != Constants.NULL_INDEX) {
                        // if next selection is an already-selected point, remove it from the creation list
                        if (currentBezierCreationList.contains(nextSelectionIndex)) {
                            currentBezierCreationList.remove(Integer.valueOf(nextSelectionIndex));
                        } else { // add it to the list
                            currentBezierCreationList.add(nextSelectionIndex);
                            if (currentBezierCreationList.size() == 4) {
                                // if we reach 4 points, create the curve and reset the creation array
                            	createBezierCurve(currentBezierCreationList);
                                currentBezierCreationList.clear();
                            }
                        }
                        repaint();
                    }
                } else {
                    selectedIndex = getBoundingPointIndex(e.getX(), e.getY());
                    repaint();
                }
            }
        });
    }
    
    /**
     * Registers a {@link MouseListener} object in the SketchPad which allows for dragging points around
     * the canvas.
     */
    public void registerDragMotionListener() {
    	addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedIndex != Constants.NULL_INDEX & selectedIndex < controlPoints.size()) {
                    movePoint(e.getX(), e.getY());
                }
            }
        });
    }
    
    /**
     * Obtains the index in the controlPoints {@link ArrayList} of the object which bounds the provided coordinates.
     * @param x		the x coordinate to test
     * @param y		the y coordinate to test
     * @return the int index in controlPoints of the last bounding object if such an object exists. Otherwise,
     * returns -1
     */
    private int getBoundingPointIndex(int x, int y) {
        for (int i = controlPoints.size() - 1; i > -1; i--) {
            if (controlPoints.get(i).contains(x, y)) {
                return i;
            }
        }
        return Constants.NULL_INDEX;
    }

    /**
     * Sets the location of the current selected point.
     * @param x		the destination x coordinate
     * @param y		the destination y coordinate
     */
    public void movePoint(int x, int y) {
        controlPoints.get(this.selectedIndex).setLocation(x, y);
        repaint();
    }

    /**
     * Instantiates a {@link CubicBezierCurve} object with the indices of 4 existing control points, and 
     * adds it to the list of stored {@link BezierCurve} objects.
     * @param controlIndices	an {@link ArrayList} of {@link Integer} objects representing indices in the
     * controlPoints {@link ArrayList}
     */
    public void createBezierCurve(ArrayList<Integer> controlIndices) {
    	if (controlIndices.size() == 4) {
    		bezierControlIndices.add(new ArrayList<Integer>(controlIndices));
    		bezierCurves.add(new CubicBezierCurve(controlPoints.get(controlIndices.get(0)),
                    controlPoints.get(controlIndices.get(1)),
                    controlPoints.get(controlIndices.get(2)),
                    controlPoints.get(controlIndices.get(3))));
    	}
    }
    
    /**
     * Adds a point onto the canvas in a random location and sets it as the current selection.
     */
    public void addRandomPoint() {
        int x = ThreadLocalRandom.current().nextInt(0, this.canvasWidth + 1);
        int y = ThreadLocalRandom.current().nextInt(0, this.canvasHeight + 1);
        controlPoints.add(new CanvasPoint(x, y));
        selectedIndex = controlPoints.size() - 1;
        repaint();
    }

    /**
     * Removes the current selected point and all {@link BezierCurve} objects dependent on it.
     */
    public void removeSelectedPoint() {
        if (selectedIndex != Constants.NULL_INDEX) {
            controlPoints.remove(selectedIndex);
            // update the bezierCurve list
            for (int i = bezierCurves.size()-1; i > -1; i--) {
                if (bezierControlIndices.get(i).contains(selectedIndex)) { // remove its dependencies
                    bezierCurves.remove(i);
                    bezierControlIndices.remove(i);
                } else { // update all indices that come after the removed index
                	for (int j = 0; j < bezierControlIndices.get(i).size(); j++) {
                		int cpIdx = bezierControlIndices.get(i).get(j);
                		if (cpIdx > selectedIndex) {
                			bezierControlIndices.get(i).set(j, cpIdx - 1);
                		}
                	}
                }
            }
            
            
        }
        selectedIndex = Constants.NULL_INDEX;
        repaint();
    }
    
    /**
     * Prints a string representation of the current state of the SketchPad object to System.out.
     */
    public void printState() {
    	System.out.println("controlPoints:");
    	System.out.println(this.controlPoints.toString());
    	System.out.println("selectedIndex:");
    	System.out.println(Integer.toString(selectedIndex));
    	System.out.println("bezierControlIndices:");
    	System.out.println(this.bezierControlIndices.toString());
    	System.out.println("globalT:");
    	System.out.println(this.globalT);
    }

    /**
     * Obtains the canvas dimensions
     * @return a {@link Dimension} object representing the canvas's size
     */
    public Dimension getPreferredSize() {
        return new Dimension(this.canvasWidth, this.canvasHeight);
    }
    
    /**
     * Draws a {@link BezierCurve} object onto the canvas.
     * @param g2d	the {@link Graphics2D} object controlling the canvas rendering
     * @param c		the {@link BezierCurve} object to be drawn
     */
    public void drawBezierCurve(Graphics2D g2d, BezierCurve c) {
    	if (c instanceof CubicBezierCurve) {
    		g2d.setColor(bezierPalette[c.getOrder()]);
    		if (showReferenceLines) { // draw all control lines
    			g2d.setStroke(referenceStroke);    			
    		} else { // draw only the guiding tangent lines between control points
    			g2d.setStroke(dashed);
    		}
    		Point2D[] curveControls = c.getControlPoints();
            g2d.draw(new Line2D.Double(curveControls[0].getX(), curveControls[0].getY(),
                    curveControls[1].getX(), curveControls[1].getY()));
            if (showReferenceLines) {
	            g2d.draw(new Line2D.Double(curveControls[1].getX(), curveControls[1].getY(),
	                    curveControls[2].getX(), curveControls[2].getY()));
            }
            g2d.draw(new Line2D.Double(curveControls[2].getX(), curveControls[2].getY(),
                    curveControls[3].getX(), curveControls[3].getY()));
            
            Point2D[] tracePoints = c.computePosition();
            
            if (showT & showReferenceLines) {
            	g2d.setStroke(referenceStroke); 
            	// draw all the other reference lines at T and their points
            	Point2D[] referencePoints = c.computeLerpsAtT(globalT);
            	int maxOrder = c.getOrder();
            	int orderCounter = c.getOrder();
            	int refPointIdx = referencePoints.length - 1;
            	while (refPointIdx > 0) {
            		Color currColor = bezierPalette[orderCounter - 1];
            		g2d.setColor(currColor);
            		while (orderCounter > 1) {
            			g2d.draw(new Line2D.Double(referencePoints[refPointIdx].getX(), referencePoints[refPointIdx].getY(),
            					referencePoints[refPointIdx - 1].getX(), referencePoints[refPointIdx - 1].getY()));
            			drawPoint(g2d, referencePoints[refPointIdx], currColor, false);
            			refPointIdx -= 1;
            			orderCounter -= 1;
            		}
            		drawPoint(g2d, referencePoints[refPointIdx], currColor, false);
            		refPointIdx -= 1;
            		maxOrder -= 1;
            		orderCounter = maxOrder;
            	}
            }
            
            // draw the curve itself
            g2d.setStroke(traceStroke);
            g2d.setColor(bezierPalette[0]);
            for (int i = 0; i < tracePoints.length - 1; i++) {
                g2d.draw(new Line2D.Double(tracePoints[i].getX(), tracePoints[i].getY(),
                        tracePoints[i+1].getX(), tracePoints[i+1].getY()));
            }
            
            if (showT) { // highlight the point at t
            	drawPoint(g2d, c.computeLerpsAtT(globalT)[0], tPointColor, false);
            }
    	}
    }
    
    /**
     * Draws a {@link Point2D} object onto the canvas
     * @param g2d	the {@link Graphics2D} object controlling the canvas rendering
     * @param pt	the {@link Point2D} object
     * @param color		the {@link Color} object specifying the color of the point
     * @param fill	a boolean specifying if the point should be filled
     */
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
    
    /**
     * Draws an {@link AnnotationShape} object onto the canvas
     * @param g2d	the {@link Graphics2D} object controlling the canvas rendering
     * @param ann	the {@link AnnotationShape} object to be drawn
     */
    public void drawAnnotationShape(Graphics2D g2d, AnnotationShape ann) {
    	g2d.setColor(ann.getColor());
    	g2d.setStroke(ann.getTraceStroke());
    	
    	Point2D[] tracePoints = ann.getTrace();
    	
    	for (int i = 0; i < tracePoints.length - 1; i++) {
            g2d.draw(new Line2D.Double(tracePoints[i].getX(), tracePoints[i].getY(),
                    tracePoints[i+1].getX(), tracePoints[i+1].getY()));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);

        // draw points
        if (isBezierCreationModeOn) {
            for (CanvasPoint point : controlPoints) {
            	drawPoint(g2d, point, pointDefaultColor, true);
            }
            g2d.setColor(Color.BLACK);
            for (int i = 0; i < currentBezierCreationList.size(); i++) {
                int controlPointIndex = currentBezierCreationList.get(i);
                CanvasPoint point = controlPoints.get(controlPointIndex);
                if (i == 0) {
                    g2d.drawString("P1", (int) (point.getX() + Constants.POINT_RADIUS), (int) (point.getY() - Constants.POINT_RADIUS));
                } else if (i == 1) {
                    g2d.drawString("C1", (int) (point.getX() + Constants.POINT_RADIUS), (int) (point.getY() - Constants.POINT_RADIUS));
                } else if (i == 2) {
                    g2d.drawString("C2", (int) (point.getX() + Constants.POINT_RADIUS), (int) (point.getY() - Constants.POINT_RADIUS));
                } else if (i == 3) {
                    g2d.drawString("P2", (int) (point.getX() + Constants.POINT_RADIUS), (int) (point.getY() - Constants.POINT_RADIUS));  	
                }
            }
        } else {
            for (int i = 0; i < controlPoints.size(); i++) {
                CanvasPoint point = controlPoints.get(i);
                if (i != selectedIndex) {
                	drawPoint(g2d, point, pointDefaultColor, true);
                }
            }
            if (selectedIndex != Constants.NULL_INDEX) {
                CanvasPoint point = controlPoints.get(selectedIndex);
                drawPoint(g2d, point, pointSelectedColor, true);
            }
        }
        
        // draw curves
        for (BezierCurve c : bezierCurves) {
        	drawBezierCurve(g2d, c);
        }
        
        // draw annotations
        for (AnnotationShape ann : annotationShapes) {
        	if (ann.getShowShape()) {
        		drawAnnotationShape(g2d, ann);
        	}
        }
    }

    public void setBezierCreationModeOn(boolean isOn) {
        if (isOn & selectedIndex != Constants.NULL_INDEX) {
            // if turning creation mode on and there is a selected point, deselect it and choose it as the first control point
            currentBezierCreationList.add(selectedIndex);
            selectedIndex = Constants.NULL_INDEX;
        } else if (!isOn) {
        	// if turning creation mode off, clear the current selections
        	currentBezierCreationList.clear();
        }
        isBezierCreationModeOn = isOn;
        repaint();
    }
    
    public void setT(double t) {
    	if (0 <= t & t <= 1) {
    		this.globalT = t;
    	}
    }
    
    public boolean isShowT() {
		return showT;
	}
    
    public void setShowT(boolean isOn) {
    	if (this.showT & !isOn) { // if turning showT off, also turn off showReferenceLines
    		this.showReferenceLines = isOn;
    	}
    	this.showT = isOn;
    	repaint();
    }

	public void setShowReferenceLines(boolean isOn) {
    	this.showReferenceLines = isOn;
    	repaint();
    }
	
	public boolean isPointSelected() {
		return this.selectedIndex != Constants.NULL_INDEX & selectedIndex < controlPoints.size();
	}
	
	public ArrayList<BezierCurve> getBezierCurves() {
		return this.bezierCurves;
	}
	
	public ArrayList<AnnotationShape> getAnnotationShapes() {
		return this.annotationShapes;
	}
	
    public int getCanvasWidth() {
		return canvasWidth;
	}

	public void setCanvasWidth(int canvasWidth) {
		this.canvasWidth = canvasWidth;
	}

	public int getCanvasHeight() {
		return canvasHeight;
	}

	public void setCanvasHeight(int canvasHeight) {
		this.canvasHeight = canvasHeight;
	}
}
