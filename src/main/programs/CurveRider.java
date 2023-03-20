package main.programs;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

import main.shapes.BezierCurve;
import main.shapes.RegularPolygon;
import main.shapes.AnnotationShape;
import main.sketch.SketchPad;
import main.utils.Constants;

/**
 * A class for interactively playing with animation along a curve. The program allows the user to adjust the position
 * of the curve in real time, update animation speeds, and explore two types of curve parameterizations: by arclength
 * and by time.
 * @author smaffa
 *
 */
public class CurveRider implements Runnable {

	private JFrame f = new JFrame();
	private BezierCurve curve;
	private SketchPad sketchPad = new SketchPad();
	private JPanel controlPanel = new JPanel(new GridBagLayout());
	private double baseT = 0;
	private double tRate = 0.2;
	private boolean showTimeParameterized = true;
	private double baseS = 0;
	private double sRate = 0.2;
	private int nPoints = 1;
	private boolean showArcLengthParameterized = true;
	
	private final static Color DEFAULT_PRIMARY_T_COLOR = new Color(150, 0, 255);
	private final static Color DEFAULT_SECONDARY_T_COLOR = Color.BLUE;
	private final static int DEFAULT_T_RIDER_NSIDES = 3;
	private final static double DEFAULT_T_RIDER_RADIUS = 5;
	private final static double DEFAULT_T_RIDER_ORIENTATION = -Math.PI / 2;
	
	private final static Color DEFAULT_PRIMARY_S_COLOR = new Color(255, 100, 100);
	private final static Color DEFAULT_SECONDARY_S_COLOR = new Color(255, 125, 50);
	private final static int DEFAULT_S_RIDER_NSIDES = 4;
	private final static double DEFAULT_S_RIDER_RADIUS = 5;
	private final static double DEFAULT_S_RIDER_ORIENTATION = Math.PI / 4;
	
	private final static Stroke DEFAULT_PRIMARY_STROKE = new BasicStroke(5.0f);
	private final static Stroke DEFAULT_SECONDARY_STROKE = new BasicStroke(3.0f);
	
	
	private double[] cumulativeDistance;
	
	private Timer timer = new Timer(Constants.FRAME_DELAY_MS, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			//update the universal time parameter according to the rate and ensure it remains between 0 and 1
			baseT += tRate * Constants.FRAME_DELAY_MS / Constants.MS_PER_S;
			if (baseT > 1) {
				baseT -= 1;
			}
			
			//update the universal arclength parameter according to the rate and ensure it remains between 0 and 1
			baseS += sRate * Constants.FRAME_DELAY_MS / Constants.MS_PER_S;
			if (baseS > 1) {
				baseS -= 1;
			}
			
			realignPoints();
			
			sketchPad.repaint();
		}
	});;
	
	/**
	 * Default constructor for the CurveRider class
	 */
	public CurveRider() {
		// instantiate 4 points and connect them into a curve
		sketchPad.addRandomPoint();
		sketchPad.addRandomPoint();
		sketchPad.addRandomPoint();
		sketchPad.addRandomPoint();
		sketchPad.createBezierCurve(new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3)));
		curve = sketchPad.getBezierCurves().get(0);
		cumulativeDistance = curve.computeCumulativeArcLengthDistance();
		
		realignPoints();
		
		// overwrite the sketchPad's mouse listener to additionally update the auxiliary curve data
		sketchPad.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (sketchPad.isPointSelected()) {
                    sketchPad.movePoint(e.getX(), e.getY());
                    
                    cumulativeDistance = curve.computeCumulativeArcLengthDistance();
                    realignPoints();
                }
            }
        });
		
		// a button that starts and pauses the animation
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 0;
		JToggleButton timerButton = new JToggleButton("Start animation");
        controlPanel.add(timerButton, c);
        timerButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (timer.isRunning()) {
                    timer.stop();
                    timerButton.setText("Resume animation");
                } else {
                	timer.start();
                	timerButton.setText("Pause animation");
                }
            }
        });
        
        // a slider that controls the number of evenly-spaced points to display along the curve
        c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 1;
		c.gridy = 0;
		JLabel nPointsLabel = new JLabel("Number of Points", JLabel.CENTER);
        nPointsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(nPointsLabel, c);
        
        c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 1;
		c.gridy = 1;
        JSlider nPointSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, nPoints);
        controlPanel.add(nPointSlider, c);
        nPointSlider.setMajorTickSpacing(5);
        nPointSlider.setMinorTickSpacing(1);
        nPointSlider.setPaintTicks(true);
        nPointSlider.setSnapToTicks(true);
        nPointSlider.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
        		nPoints = ((JSlider) e.getSource()).getValue();
        		
        		realignPoints();
        		
        		sketchPad.repaint();
        	}
        });
        
        // a button that controls whether to display the points associated with the time parameter
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 0.5;
		c.gridx = 2;
		c.gridy = 0;
        JButton showTimeParameterButton = new JButton("Hide Time Parameterization");
        showTimeParameterButton.setForeground(new Color(150, 0, 255));
        controlPanel.add(showTimeParameterButton, c);
        showTimeParameterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showTimeParameterized) {
                	showTimeParameterized = false;
                    showTimeParameterButton.setText("Show Time Parameterization");
                } else {
                	showTimeParameterized = true;
                    showTimeParameterButton.setText("Hide Time Parameterization");
                }
                
                for (int i = 0; i < nPoints; i++) {
        			sketchPad.getAnnotationShapes().get(2 * i).setShowShape(showTimeParameterized);
                }
                sketchPad.repaint();
            }
        });
        
        // a button that controls whether to display the points associated with the arclength parameter
        c.fill = GridBagConstraints.BOTH;
		c.gridx = 2;
		c.gridy = 1;
        JButton showArcLengthParameterButton = new JButton("Hide Arclength Parameterization");
        showArcLengthParameterButton.setForeground(new Color(255, 100, 100));
        controlPanel.add(showArcLengthParameterButton, c);
        showArcLengthParameterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showArcLengthParameterized) {
                	showArcLengthParameterized = false;
                    showArcLengthParameterButton.setText("Show Arclength Parameterization");
                } else {
                	showArcLengthParameterized = true;
                    showArcLengthParameterButton.setText("Hide Arclength Parameterization");
                }
                
                for (int i = 0; i < nPoints; i++) {
        			sketchPad.getAnnotationShapes().get((2 * i) + 1).setShowShape(showArcLengthParameterized);
                }
                sketchPad.repaint();
            }
        });
        
        // a slider that controls the animation speed of points along the curve
        c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 3;
		c.gridy = 0;
		JLabel speedLabel = new JLabel("Animation Speed", JLabel.CENTER);
        speedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(speedLabel, c);
        
        c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 3;
		c.gridy = 1;
        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) Math.round(sRate * 100));
        controlPanel.add(speedSlider, c);
        speedSlider.setMajorTickSpacing(25);
        speedSlider.setMinorTickSpacing(5);
        speedSlider.setPaintTicks(true);
        speedSlider.setSnapToTicks(true);
        speedSlider.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
        		int speedIdx = ((JSlider) e.getSource()).getValue();
        		tRate = (double) speedIdx / 100;
        		sRate = (double) speedIdx / 100;
        		
        		realignPoints();
        		
        		sketchPad.repaint();
        	}
        });
        
//        JSlider tRateSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) Math.round(tRate * 100));
//        tRateSlider.setMajorTickSpacing(25);
//        tRateSlider.setMinorTickSpacing(5);
//        tRateSlider.setPaintTicks(true);
//        tRateSlider.setSnapToTicks(true);
//        tRateSlider.addChangeListener(new ChangeListener() {
//        	public void stateChanged(ChangeEvent e) {
//        		int tRateIdx = ((JSlider) e.getSource()).getValue();
//        		tRate = (double) tRateIdx / 100;
//        		
//        		realignPoints();
//        		
//        		sketchPad.repaint();
//        	}
//        });
//        
//        JSlider sRateSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) Math.round(tRate * 100));
//        sRateSlider.setMajorTickSpacing(25);
//        sRateSlider.setMinorTickSpacing(5);
//        sRateSlider.setPaintTicks(true);
//        sRateSlider.setSnapToTicks(true);
//        sRateSlider.addChangeListener(new ChangeListener() {
//        	public void stateChanged(ChangeEvent e) {
//        		int sRateIdx = ((JSlider) e.getSource()).getValue();
//        		sRate = (double) sRateIdx / 100;
//        		
//        		realignPoints();
//        		
//        		sketchPad.repaint();
//        	}
//        });
        
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
	
	/**
	 * Creates a point that moves along the curve, represented as a {@link RegularPolygon} centered at the point
	 * with default styling.
	 * @param x		the x coordinate of the point
	 * @param y		the y coordinate of the point
	 * @return a {@link RegularPolygon} centered at (x,y) with default visual properties
	 */
	public static RegularPolygon makeDefaultRider(double x, double y) {
		return makeRider(x, y, 24, DEFAULT_T_RIDER_RADIUS, 0, DEFAULT_PRIMARY_T_COLOR, DEFAULT_PRIMARY_STROKE);
	}
		

	/**
	 * Updates the stored data to recompute the position of all points along the curve corresponding to nPoints
	 * evenly spaced intervals in the curve parameters.
	 */
	public void realignPoints() {
		ArrayList<AnnotationShape> annotationShapes = sketchPad.getAnnotationShapes();
		// trim the list to the appropriate size (2 * nPoints, arranged by alternating t, s parameters)
		while (annotationShapes.size() > 2 * nPoints) {
			annotationShapes.remove(annotationShapes.size() - 1);
		}
		while (annotationShapes.size() < 2 * nPoints) {
			annotationShapes.add(makeRider(0, 0, DEFAULT_T_RIDER_NSIDES, DEFAULT_T_RIDER_RADIUS, 
					DEFAULT_T_RIDER_ORIENTATION, DEFAULT_SECONDARY_T_COLOR, DEFAULT_SECONDARY_STROKE));
			annotationShapes.add(makeRider(0, 0, DEFAULT_S_RIDER_NSIDES, DEFAULT_S_RIDER_RADIUS,
					DEFAULT_S_RIDER_ORIENTATION, DEFAULT_SECONDARY_S_COLOR, DEFAULT_SECONDARY_STROKE));
		}
		
		// set the visual specifications for the primary points
		annotationShapes.get(0).setColor(DEFAULT_PRIMARY_T_COLOR);
		annotationShapes.get(0).setTraceStroke(DEFAULT_PRIMARY_STROKE);
		annotationShapes.get(1).setColor(DEFAULT_PRIMARY_S_COLOR);
		annotationShapes.get(1).setTraceStroke(DEFAULT_PRIMARY_STROKE);
		
		// compute all offsets in t and s necessary to define nPoints and roll them into the domain [0, 1]
		// then, compute the positions of the corresponding points
		double offset = 1.0 / (double) nPoints;
		for (int i = 0; i < nPoints; i++) {
			double rolledT = baseT + (i * offset);
			if (rolledT > 1) {
				rolledT -= 1;
			}
			Point2D ptT = curve.computePositionAtT(rolledT);
			((RegularPolygon) annotationShapes.get(2 * i)).setX(ptT.getX());
			((RegularPolygon) annotationShapes.get(2 * i)).setY(ptT.getY());
			annotationShapes.get(2 * i).setShowShape(showTimeParameterized);
			
			double rolledS = baseS + (i * offset);
			if (rolledS > 1) {
				rolledS -= 1;
			}
			
			double arcLengthT = curve.arcLengthApproximateT(this.cumulativeDistance, rolledS);
			Point2D ptS = curve.computePositionAtT(arcLengthT);
			((RegularPolygon) annotationShapes.get((2 * i) + 1)).setX(ptS.getX());
			((RegularPolygon) annotationShapes.get((2 * i) + 1)).setY(ptS.getY());
			annotationShapes.get((2 * i) + 1).setShowShape(showArcLengthParameterized);
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Container cp = f.getContentPane();
		cp.setLayout(new BorderLayout());
		
		cp.add(BorderLayout.CENTER, sketchPad);
		cp.add(BorderLayout.SOUTH, controlPanel);
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT);
        f.pack();
        f.setVisible(true);
	}

}
