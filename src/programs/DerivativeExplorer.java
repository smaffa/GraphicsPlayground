package programs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.sketch.SketchPad;
import main.sketch.plots.GraphicsPlot;
import main.sketch.plots.LinePlot;
import main.utils.Constants;
import main.utils.Utility;

public class DerivativeExplorer implements Runnable {

	private JFrame f = new JFrame();
	private LinePlot plot = new LinePlot();
	private SketchPad sketchPad = new SketchPad(plot.getFigureWidth(), plot.getFigureWidth());
	
	public DerivativeExplorer() {
		sketchPad.addRandomPoint();
		sketchPad.addRandomPoint();
		sketchPad.addRandomPoint();
		sketchPad.addRandomPoint();
		sketchPad.createBezierCurve(new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3)));
		
		// TODO: overwrite the old mouse listener in the sketchpad to update the velocity panel on events  
		//sketchPad.addMouseListener();
		
		// TODO: write the code in CubicBezierCurve to compute position and velocity vectors in matrix form
		
		ArrayList<Point2D> testLine = new ArrayList<Point2D>();
		
		double[] xVals = Utility.linspace(-50, 50, Constants.BEZIER_FINENESS + 1);
		
		for (int i = 0; i <= Constants.BEZIER_FINENESS; i++) {
            double x = xVals[i];
            double y = (((x - 50) * (x - 50)) / 140) - 45;
            testLine.add(new Point2D.Double(x, y));
        }		
		plot.addLine(testLine, Color.BLUE);
		
		plot.repaint();
	}
	
	@Override
	public void run() {
		
		Container cp = f.getContentPane();
        cp.setLayout(new GridLayout(1, 2));
        cp.add(sketchPad);
        cp.add(plot);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(2 * plot.getFigureWidth(), plot.getFigureHeight()));
        f.pack();
        f.setVisible(true);
        
	}
	
}
