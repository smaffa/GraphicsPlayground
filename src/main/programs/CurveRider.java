package main.programs;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import main.utils.Utility;

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
	
	private double[] cumulativeDistance;
	
	private Timer timer = new Timer(Constants.FRAME_DELAY_MS, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			baseT += tRate * Constants.FRAME_DELAY_MS / 1000;
			if (baseT > 1) {
				baseT -= 1;
			}
			
			baseS += sRate * Constants.FRAME_DELAY_MS / 1000;
			if (baseS > 1) {
				baseS -= 1;
			}
			
			realignPoints();
			
			sketchPad.repaint();
		}
	});;
	
	public CurveRider() {
		sketchPad.addRandomPoint();
		sketchPad.addRandomPoint();
		sketchPad.addRandomPoint();
		sketchPad.addRandomPoint();
		sketchPad.createBezierCurve(new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3)));
		curve = sketchPad.getBezierCurves().get(0);
		cumulativeDistance = new double[curve.getBezierFineness() + 1];
		
		computeArcLengthDistance();
		realignPoints();
		
		sketchPad.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (sketchPad.isPointSelected()) {
                    sketchPad.movePoint(e.getX(), e.getY());
                    
                    computeArcLengthDistance();
                    realignPoints();
                }
            }
        });
		
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
        
        // unused
        JSlider tRateSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) Math.round(tRate * 100));
        tRateSlider.setMajorTickSpacing(25);
        tRateSlider.setMinorTickSpacing(5);
        tRateSlider.setPaintTicks(true);
        tRateSlider.setSnapToTicks(true);
        tRateSlider.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
        		int tRateIdx = ((JSlider) e.getSource()).getValue();
        		tRate = (double) tRateIdx / 100;
        		
        		realignPoints();
        		
        		sketchPad.repaint();
        	}
        });
        
        JSlider sRateSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) Math.round(tRate * 100));
        sRateSlider.setMajorTickSpacing(25);
        sRateSlider.setMinorTickSpacing(5);
        sRateSlider.setPaintTicks(true);
        sRateSlider.setSnapToTicks(true);
        sRateSlider.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
        		int sRateIdx = ((JSlider) e.getSource()).getValue();
        		sRate = (double) sRateIdx / 100;
        		
        		realignPoints();
        		
        		sketchPad.repaint();
        	}
        });
        
	}
	
	public void computeArcLengthDistance() {
		Point2D[] position = curve.computePosition();
		cumulativeDistance[0] = 0;
		for (int i = 1; i < position.length; i++) {
			cumulativeDistance[i] = Math.sqrt(Math.pow(position[i].getX() - position[i-1].getX(), 2) + 
					Math.pow(position[i].getY() - position[i-1].getY(), 2)) + cumulativeDistance[i-1];
		}
		for (int i = 1; i < position.length; i++) {
			cumulativeDistance[i] = cumulativeDistance[i] / cumulativeDistance[cumulativeDistance.length-1];
		}
	}
	
	public double arcLengthApproximateT(double s) {
		int maxIdxLessThan = 0;
		int minIdxGreaterThan = cumulativeDistance.length - 1;
		double sPartial = 0;
		for (int i = 0; i < cumulativeDistance.length - 1; i++) {
			if (cumulativeDistance[i] <= s & s <= cumulativeDistance[i+1]) {
				maxIdxLessThan = i;
				minIdxGreaterThan = i + 1;
				sPartial = (s - cumulativeDistance[i]) / (cumulativeDistance[i+1] - cumulativeDistance[i]);
			}
		}
		double tLow = (double) maxIdxLessThan / curve.getBezierFineness();
		double tHigh = (double) minIdxGreaterThan / curve.getBezierFineness();
		return Utility.lerp(tLow, tHigh, sPartial);
	}
	
	public static RegularPolygon makeRider(double x, double y, int nSides, double radius, double orientation, 
			Color c, Stroke stroke) {
		RegularPolygon polygon = new RegularPolygon(x, y, nSides, radius, orientation);
		polygon.setColor(c);
		polygon.setTraceStroke(stroke);
		return polygon;
	}
	
	public static RegularPolygon makeDefaultRider(double x, double y) {
		return makeRider(x, y, 24, 5, 0, Color.BLUE, new BasicStroke(3.0f));
	}
		
	public void realignPoints() {
		ArrayList<AnnotationShape> annotationShapes = sketchPad.getAnnotationShapes();
		// trim the list to the appropriate size
		while (annotationShapes.size() > 2 * nPoints) {
			annotationShapes.remove(annotationShapes.size() - 1);
		}
		while (annotationShapes.size() < 2 * nPoints) {
			annotationShapes.add(makeRider(0, 0, 3, 5, -Math.PI / 2, Color.BLUE, new BasicStroke(3.0f)));
			annotationShapes.add(makeRider(0, 0, 4, 5, Math.PI / 4, new Color(255, 125, 50), new BasicStroke(3.0f)));
		}
		
		annotationShapes.get(0).setColor(new Color(150, 0, 255));
		annotationShapes.get(0).setTraceStroke(new BasicStroke(5.0f));
		annotationShapes.get(1).setColor(new Color(255, 100, 100));
		annotationShapes.get(1).setTraceStroke(new BasicStroke(5.0f));
		
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
			
			double arcLengthT = arcLengthApproximateT(rolledS);
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
