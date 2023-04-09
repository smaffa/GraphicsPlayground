package main.programs;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Stroke;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.shapes.Arrow;
import main.shapes.BezierCurve;
import main.shapes.Circle;
import main.shapes.CubicBezierCurve;
import main.shapes.PolyLine;
import main.shapes.RegularPolygon;
import main.sketch.SketchPad;
import main.sketch.plots.LinePlot;
import main.utils.Constants;
import main.utils.Utility;

public class ContinuityExplorer implements Runnable {
	
	private JFrame f = new JFrame();
	private SketchPad sketchPad = new SketchPad((int) (1.7 * Constants.CANVAS_WIDTH), Constants.CANVAS_HEIGHT);
	private int nCurves = 3;
	private double globalT = 0;
	private double vectorScale = 50;
	private JPanel controlPanel = new JPanel(new FlowLayout());
	
	private final static Color DEFAULT_T_COLOR = Color.BLUE;
	private final static double DEFAULT_T_RADIUS = 10;
	
	private final static Color TANGENT_COLOR = Color.GREEN;
	private final static Stroke TANGENT_STROKE = new BasicStroke(3.0f);
	
	private final static Color NORMAL_COLOR = Color.ORANGE;
	private final static Stroke NORMAL_STROKE = new BasicStroke(3.0f);
	
	private final static Color OSCULATING_CIRCLE_COLOR = Color.MAGENTA;
	private final static Stroke OSCULATING_CIRCLE_STROKE = new BasicStroke(2.0f);
	
	private final static Color CURVATURE_COMB_COLOR = Color.GRAY;
	private final static Stroke CURVATURE_COMB_STROKE  = new BasicStroke(1.0f);
	
	private int curvatureSubdivisions = 10;
	private double curvatureCombScale = 5000;
	
	public ContinuityExplorer() {
		int height = sketchPad.getCanvasHeight();
		int width = sketchPad.getCanvasWidth();
		
		sketchPad.addRandomPoint(0, 0, (int) (width / (this.nCurves + 1)), height);
		for (int i = 0; i <= nCurves; i++) {
			sketchPad.addRandomPoint((int) (i * width / (this.nCurves + 1)), 0, 
					(int) ((i + 1) * width / (this.nCurves + 1)), height);
			sketchPad.addRandomPoint((int) (i * width / (this.nCurves + 1)), 0, 
					(int) ((i + 1) * width / (this.nCurves + 1)), height);
			sketchPad.addRandomPoint((int) (i * width / (this.nCurves + 1)), 0, 
					(int) ((i + 1) * width / (this.nCurves + 1)), height);
			
			sketchPad.createBezierCurve(new ArrayList<Integer>(Arrays.asList((i*3), (i*3)+1, (i*3)+2, (i*3)+3)));
		}
		
		updateGlobalTAnnotations(true);
		
		// overwrite the sketchPad's mouse listener to additionally update the auxiliary curve data
		sketchPad.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (sketchPad.isPointSelected()) {
                    sketchPad.movePoint(e.getX(), e.getY());
                    
                    updateGlobalTAnnotations(true);
                }
            }
        });
		
		
		// a slider that controls the curve parameter
        JSlider tSlider = new JSlider(JSlider.HORIZONTAL, 0, (this.nCurves + 1) * Constants.BEZIER_FINENESS, 0);
        controlPanel.add(tSlider);
        tSlider.setMajorTickSpacing(Constants.BEZIER_FINENESS);
        tSlider.setMinorTickSpacing(25);
        tSlider.setPaintTicks(true);
        tSlider.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
        		int tIdx = ((JSlider) e.getSource()).getValue();
        		globalT = (double) tIdx / Constants.BEZIER_FINENESS;
        		
        		updateGlobalTAnnotations(true);

        		sketchPad.repaint();
        	}
        });
        Dimension tSliderDim = tSlider.getPreferredSize();
        tSlider.setPreferredSize(new Dimension(tSliderDim.width+100,tSliderDim.height));
	}
	
	public int getCurveIndex(double t) {
		int curveIndex = (int) Math.floor(t);
		if (curveIndex == this.nCurves + 1) {
			curveIndex -= 1;
		}
		return curveIndex;
	}
	
	public Point2D computePositionAtGlobalT(double t) {
		int curveIndex = getCurveIndex(t);
		double curveT = t - (double) curveIndex;
		return this.sketchPad.getBezierCurves().get(curveIndex).computePositionAtT(curveT);
	}
	
	public void updateGlobalTAnnotations(boolean showT) {
		sketchPad.getAnnotationShapes().clear();
		ArrayList<BezierCurve> curves = sketchPad.getBezierCurves();
		
		for (int i = 0; i < curves.size(); i++) {
			BezierCurve currentCurve = curves.get(i);
			Point2D[] curvePosition = currentCurve.computePosition();
			Point2D[] curveVelocity = ((CubicBezierCurve) currentCurve).computeVelocity();
			
			double[] curveCurvature = ((CubicBezierCurve) currentCurve).computeCurvature();
			Point2D[] curvatureCombShaft = new Point2D[curveCurvature.length];
			
			for (int j = 0; j < curveCurvature.length; j++) {
				Point2D tangentVector = Utility.normalizeVector(curveVelocity[j]);
				Point2D normalVector = new Point2D.Double(-tangentVector.getY(), tangentVector.getX());
				
				// curvature comb positions are c(t) = x(t) - d * K(t) * n(t)
				// TODO: try applying a monotonic transformation to K(t); maybe arctan?
				Point2D curvatureCombVector = Utility.scaleVector(normalVector, this.curvatureCombScale * curveCurvature[j]);
				curvatureCombShaft[j] = new Point2D.Double(curvePosition[j].getX() - curvatureCombVector.getX(), 
						curvePosition[j].getY() + curvatureCombVector.getY());	
				
				if (j % this.curvatureSubdivisions == 0) {
					PolyLine combTooth = new PolyLine(new Point2D[] {
							curvePosition[j],
							curvatureCombShaft[j]	
					});
					combTooth.setTraceStroke(CURVATURE_COMB_STROKE);
					combTooth.setColor(CURVATURE_COMB_COLOR);
					sketchPad.getAnnotationShapes().add(combTooth);
				}
			}
			
			PolyLine curvatureComb = new PolyLine(curvatureCombShaft);
			curvatureComb.setTraceStroke(CURVATURE_COMB_STROKE);
			curvatureComb.setColor(CURVATURE_COMB_COLOR);
			
			sketchPad.getAnnotationShapes().add(curvatureComb);
		}
		
		if (showT) {
			int currentCurveIndex = getCurveIndex(this.globalT);
			BezierCurve currentCurve = curves.get(currentCurveIndex);
			Point2D currentPt = computePositionAtGlobalT(this.globalT);
			double curveT = this.globalT - (double) currentCurveIndex;
			
			Point2D velocityVector = ((CubicBezierCurve) currentCurve).computeVelocityAtT(curveT);
			Point2D tangentVector = Utility.normalizeVector(velocityVector);
			Point2D normalVector = new Point2D.Double(-tangentVector.getY(), tangentVector.getX());
			double curvature = ((CubicBezierCurve) currentCurve).computeCurvatureAtT(curveT);
			double radiusOfCurvature = 1 / curvature;
			Point2D scaledTangentVector = Utility.scaleVector(tangentVector, this.vectorScale);
			Point2D scaledNormalVector = Utility.scaleVector(normalVector, -this.vectorScale);
			Point2D osculatingRadiusVector = Utility.scaleVector(normalVector, radiusOfCurvature);
			
			Circle rider = new Circle(currentPt, DEFAULT_T_RADIUS);
			rider.setFill(true);
			rider.setColor(DEFAULT_T_COLOR);
			
			Arrow tangentArrow = new Arrow(currentPt, scaledTangentVector);
			tangentArrow.setTraceStroke(TANGENT_STROKE);
			tangentArrow.setColor(TANGENT_COLOR);
			
			Arrow normalArrow = new Arrow(currentPt, scaledNormalVector);
			normalArrow.setTraceStroke(NORMAL_STROKE);
			normalArrow.setColor(NORMAL_COLOR);
			
			PolyLine osculatingRadius = new PolyLine(new Point2D[] {
					new Point2D.Double(currentPt.getX(), currentPt.getY()),
					new Point2D.Double(currentPt.getX() + osculatingRadiusVector.getX(), 
							currentPt.getY() + osculatingRadiusVector.getY())
			});
			osculatingRadius.setTraceStroke(OSCULATING_CIRCLE_STROKE);
			osculatingRadius.setColor(OSCULATING_CIRCLE_COLOR);
			
			Circle osculatingCircle = new Circle(currentPt.getX() + osculatingRadiusVector.getX(), 
					currentPt.getY() + osculatingRadiusVector.getY(), Utility.computeVectorNorm(osculatingRadiusVector));
			osculatingCircle.setTraceStroke(OSCULATING_CIRCLE_STROKE);
			osculatingCircle.setColor(OSCULATING_CIRCLE_COLOR);
			
			sketchPad.getAnnotationShapes().add(rider);
			sketchPad.getAnnotationShapes().add(tangentArrow);
			sketchPad.getAnnotationShapes().add(normalArrow);
			sketchPad.getAnnotationShapes().add(osculatingRadius);
			sketchPad.getAnnotationShapes().add(osculatingCircle);
		}
	}
	
	/**
	 * Creates a point that moves along the curve, represented as a {@link RegularPolygon} centered at the point.
	 * @param x		the x coordinate of the point
	 * @param y		the y coordinate of the point
	 * @param nSides	the number of sides to the polygon representing the point
	 * @param radius	the radius of the polygon representing the point
	 * @param orientation	the angular orientation of the primary vertex of the polygon
	 * @param c		the color of the polygon
	 * @param stroke	the linestyle of the polygon's border
	 * @return a {@link RegularPolygon} centered at (x,y) with the specified visual properties
	 */
	public static RegularPolygon makeRider(double x, double y, int nSides, double radius, double orientation, 
			Color c, Stroke stroke) {
		RegularPolygon polygon = new RegularPolygon(x, y, nSides, radius, orientation);
		polygon.setColor(c);
		polygon.setTraceStroke(stroke);
		return polygon;
	}

	@Override
	public void run() {
		Container cp = f.getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(sketchPad, BorderLayout.CENTER);
        cp.add(controlPanel, BorderLayout.SOUTH);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize((int) (1.7 * Constants.CANVAS_WIDTH), Constants.CANVAS_HEIGHT);
        f.pack();
        f.setVisible(true);
	}

}
