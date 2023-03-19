package main.programs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.shapes.BezierCurve;
import main.shapes.CubicBezierCurve;
import main.shapes.Arrow;
import main.shapes.PlotLine;
import main.sketch.SketchPad;
import main.sketch.plots.GraphicsPlot;
import main.sketch.plots.LinePlot;
import main.utils.Constants;
import main.utils.Utility;

public class DerivativeExplorer implements Runnable {

	private JFrame f = new JFrame();
	private BezierCurve curve;
	private LinePlot positionPlot = new LinePlot();
	private LinePlot velocityPlot = new LinePlot();
	private SketchPad sketchPad = new SketchPad(positionPlot.getFigureWidth(), positionPlot.getFigureWidth());
	private double globalT = 0;
	private JPanel controlPanel = new JPanel(new FlowLayout());
	private double vectorScale = 1;
	
	public DerivativeExplorer() {
		sketchPad.addRandomPoint();
		sketchPad.addRandomPoint();
		sketchPad.addRandomPoint();
		sketchPad.addRandomPoint();
		sketchPad.createBezierCurve(new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3)));
		sketchPad.setShowT(false);
		sketchPad.setT(globalT);
		
		curve = Utility.invertYCoordinates((CubicBezierCurve) sketchPad.getBezierCurves().get(0), sketchPad.getCanvasHeight());
		CubicBezierCurve curveCopy = new CubicBezierCurve((CubicBezierCurve) curve);
		curveCopy.centerAtP1();
		Point2D[] position = curveCopy.computePosition();
		int idxPosition = (int) Math.round(this.globalT * position.length);
		Point2D[] velocity = curveCopy.computeVelocity();
		int idxVelocity = (int) Math.round(this.globalT * velocity.length);
		
		positionPlot.addLine(new PlotLine(Arrays.asList(curveCopy.computePosition()), Color.BLUE));
		positionPlot.addAnnotation(new Arrow(position[idxPosition].getX(), position[idxPosition].getY(),
				velocity[idxVelocity].getX(), velocity[idxVelocity].getY()));
		
		velocityPlot.addLine(new PlotLine(Arrays.asList(curveCopy.computeVelocity()), Color.BLUE));
		velocityPlot.addAnnotation(new Arrow(0, 0,
				velocity[idxVelocity].getX(), velocity[idxVelocity].getY()));
		
		positionPlot.setShowT(false);
		positionPlot.setT(globalT);
		positionPlot.setShowAnnotations(false);
		
		velocityPlot.setShowT(false);
		velocityPlot.setT(globalT);
		velocityPlot.setShowAnnotations(false);
		
		sketchPad.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (sketchPad.isPointSelected()) {
                    sketchPad.movePoint(e.getX(), e.getY());
                    curve = Utility.invertYCoordinates((CubicBezierCurve) sketchPad.getBezierCurves().get(0), sketchPad.getCanvasHeight());
                    
                    CubicBezierCurve curveCopy = new CubicBezierCurve((CubicBezierCurve) curve);
                    curveCopy.centerAtP1();
                    
                    Point2D[] position = curveCopy.computePosition();
            		int idxPosition = Utility.imputeTIndex(globalT, position.length);
            		Point2D[] velocity = curveCopy.computeVelocity();
            		int idxVelocity = Utility.imputeTIndex(globalT, velocity.length);
                    
                    positionPlot.setLine(0, new PlotLine(Arrays.asList(curveCopy.computePosition()), Color.BLUE));
                    positionPlot.setAnnotation(0, new Arrow(position[idxPosition].getX(), position[idxPosition].getY(),
				vectorScale * velocity[idxVelocity].getX(), vectorScale * velocity[idxVelocity].getY()));
                    positionPlot.repaint();
                    
                    velocityPlot.setLine(0, new PlotLine(Arrays.asList(curveCopy.computeVelocity()), Color.BLUE));
                    velocityPlot.setAnnotation(0, new Arrow(0, 0,
				velocity[idxVelocity].getX(), velocity[idxVelocity].getY()));
                    velocityPlot.repaint();
                }
            }
        });
		
		JSlider tSlider = new JSlider(JSlider.HORIZONTAL, 0, Constants.BEZIER_FINENESS, 0);
        controlPanel.add(tSlider);
        tSlider.setMajorTickSpacing(25);
        tSlider.setMinorTickSpacing(5);
        tSlider.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
        		int tIdx = ((JSlider) e.getSource()).getValue();
        		globalT = (double) tIdx / Constants.BEZIER_FINENESS;
        		
        		sketchPad.setT(globalT);
        		positionPlot.setT(globalT);
        		velocityPlot.setT(globalT);
        		
        		CubicBezierCurve curveCopy = new CubicBezierCurve((CubicBezierCurve) curve);
        		curveCopy.centerAtP1();
        		
        		Point2D[] position = curveCopy.computePosition();
        		int idxPosition = Utility.imputeTIndex(globalT, position.length);
        		Point2D[] velocity = curveCopy.computeVelocity();
        		int idxVelocity = Utility.imputeTIndex(globalT, velocity.length);
                
                positionPlot.setAnnotation(0, new Arrow(position[idxPosition].getX(), position[idxPosition].getY(),
			vectorScale * velocity[idxVelocity].getX(), vectorScale * velocity[idxVelocity].getY()));
                
                velocityPlot.setAnnotation(0, new Arrow(0, 0,
			velocity[idxVelocity].getX(), velocity[idxVelocity].getY()));
        		
        		sketchPad.repaint();
        		positionPlot.repaint();
        		velocityPlot.repaint();
        	}
        });
        
        JToggleButton showTButton = new JToggleButton("Show f(t)");
        controlPanel.add(showTButton);
        showTButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    sketchPad.setShowT(true);
                    positionPlot.setShowT(true);
                    velocityPlot.setShowT(true);
                } else {
                	sketchPad.setShowT(false);
                	positionPlot.setShowT(false);
                    velocityPlot.setShowT(false);
                }
            }
        });
        
        JSlider vectorSizeSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 100);
        controlPanel.add(vectorSizeSlider);
        vectorSizeSlider.setMajorTickSpacing(25);
        vectorSizeSlider.setMinorTickSpacing(5);
        vectorSizeSlider.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
        		int vScale = ((JSlider) e.getSource()).getValue();
        		vectorScale = (double) vScale / 100;
        		
        		CubicBezierCurve curveCopy = new CubicBezierCurve((CubicBezierCurve) curve);
        		curveCopy.centerAtP1();
        		
        		Point2D[] position = curveCopy.computePosition();
        		int idxPosition = Utility.imputeTIndex(globalT, position.length);
        		Point2D[] velocity = curveCopy.computeVelocity();
        		int idxVelocity = Utility.imputeTIndex(globalT, velocity.length);
                
                positionPlot.setAnnotation(0, new Arrow(position[idxPosition].getX(), position[idxPosition].getY(),
			vectorScale * velocity[idxVelocity].getX(), vectorScale * velocity[idxVelocity].getY()));
                
                velocityPlot.setAnnotation(0, new Arrow(0, 0,
			velocity[idxVelocity].getX(), velocity[idxVelocity].getY()));
        		
        		sketchPad.repaint();
        		positionPlot.repaint();
        		velocityPlot.repaint();
        	}
        });
        
        JToggleButton showAnnotationsButton = new JToggleButton("Show Vectors");
        controlPanel.add(showAnnotationsButton);
        showAnnotationsButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    positionPlot.setShowAnnotations(true);
                    velocityPlot.setShowAnnotations(true);
                } else {
                	positionPlot.setShowAnnotations(false);
                    velocityPlot.setShowAnnotations(false);
                }
            }
        });
	}
	
	@Override
	public void run() {
		
		Container cp = f.getContentPane();
		cp.setLayout(new BorderLayout());
		Container drawingPane = new JPanel(new GridLayout(1, 3));
		
        drawingPane.add(sketchPad);
        drawingPane.add(positionPlot);
        drawingPane.add(velocityPlot);
        cp.add(BorderLayout.CENTER, drawingPane);
        cp.add(BorderLayout.SOUTH, controlPanel);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(3 * positionPlot.getFigureWidth(), positionPlot.getFigureHeight() + 100));
        f.pack();
        f.setVisible(true);
        
	}
	
}
