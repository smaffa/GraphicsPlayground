package main.programs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.shapes.BezierCurve;
import main.shapes.CubicBezierCurve;
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
	
	public DerivativeExplorer() {
		sketchPad.addRandomPoint();
		sketchPad.addRandomPoint();
		sketchPad.addRandomPoint();
		sketchPad.addRandomPoint();
		sketchPad.createBezierCurve(new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3)));
		
		curve = sketchPad.getBezierCurves().get(0);
		CubicBezierCurve curveCopy = new CubicBezierCurve((CubicBezierCurve) curve);
		curveCopy.reflect(1, 0).centerAtP1().scale(1.0 / positionPlot.getFigureWidth(), 1.0 / positionPlot.getFigureHeight());
		positionPlot.addLine(new PlotLine(Arrays.asList(curveCopy.computePosition()), Color.BLUE));
		velocityPlot.addLine(new PlotLine(Arrays.asList(curveCopy.computeVelocity()), Color.BLUE));
		
		sketchPad.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (sketchPad.isPointSelected()) {
                    sketchPad.movePoint(e.getX(), e.getY());
                    curve = sketchPad.getBezierCurves().get(0);
                    
                    CubicBezierCurve curveCopy = new CubicBezierCurve((CubicBezierCurve) curve);
                    curveCopy.reflect(1, 0).centerAtP1().scale(1.0 / positionPlot.getFigureWidth(), 1.0 / positionPlot.getFigureHeight());
                    
                    positionPlot.setLine(0, new PlotLine(Arrays.asList(curveCopy.computePosition()), Color.BLUE));
                    positionPlot.repaint();
                    
                    velocityPlot.setLine(0, new PlotLine(Arrays.asList(curveCopy.computeVelocity()), Color.BLUE));
                    velocityPlot.repaint();
                }
            }
        });
	}
	
	@Override
	public void run() {
		
		Container cp = f.getContentPane();
        cp.setLayout(new GridLayout(1, 2));
        cp.add(sketchPad);
        cp.add(positionPlot);
        cp.add(velocityPlot);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(3 * positionPlot.getFigureWidth(), positionPlot.getFigureHeight() + 50));
        f.pack();
        f.setVisible(true);
        
	}
	
}
