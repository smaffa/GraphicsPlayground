package main.programs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.sketch.SketchPad;
import main.utils.Constants;

/**
 * A class for interactively connecting points into {@link BezierCurves} and adjusting their positions
 * @author smaffa
 *
 */
public class BezierCreator implements Runnable {
	
	private boolean isDev = false;
	private JFrame f = new JFrame();
	private SketchPad sketchPad = new SketchPad();
	private JPanel controlPanel = new JPanel(new FlowLayout());
	
	/**
	 * Basic constructor for the BezierCreator class
	 * @param isDev		a boolean specifying whether to create the object in development mode
	 */
	public BezierCreator(boolean isDev) {
		this.isDev = isDev;
		
		// a button that spawns a new point in a random location when pressed
		JButton addPointButton = new JButton("Add Control Point");
        controlPanel.add(addPointButton);
        addPointButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sketchPad.addRandomPoint();
            }
        });

        // a button that deletes the current selected point when pressed
        JButton deletePointButton = new JButton("Remove Selected Point");
        controlPanel.add(deletePointButton);
        deletePointButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sketchPad.removeSelectedPoint();
            }
        });

        // a button that toggles between curve connecting and point adjustment modes
        JToggleButton bezierCreationButton = new JToggleButton("Cubic Bezier Creation");
        controlPanel.add(bezierCreationButton);
        bezierCreationButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    sketchPad.setBezierCreationModeOn(true);
                } else {
                    sketchPad.setBezierCreationModeOn(false);
                }
            }
        });
        
        // a slider that controls the curve parameter
        JSlider tSlider = new JSlider(JSlider.HORIZONTAL, 0, Constants.BEZIER_FINENESS, 0);
        controlPanel.add(tSlider);
        tSlider.setMajorTickSpacing(25);
        tSlider.setMinorTickSpacing(5);
        tSlider.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
        		int tIdx = ((JSlider) e.getSource()).getValue();
        		double t = (double) tIdx / Constants.BEZIER_FINENESS;
        		sketchPad.setT(t);
        		sketchPad.repaint();
        	}
        });
        
        // a button that toggles whether the curve parameter is highlighted
        JToggleButton showTButton = new JToggleButton("Show f(t)");
        controlPanel.add(showTButton);
        showTButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    sketchPad.setShowT(true);
                } else {
                	sketchPad.setShowT(false);
                }
            }
        });
        
        // a button that toggles whether to show all linear interpolations that produce the curve
        JToggleButton referenceButton = new JToggleButton("Show Reference Lines");
        controlPanel.add(referenceButton);
        referenceButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    sketchPad.setShowReferenceLines(true);
                } else {
                	sketchPad.setShowReferenceLines(false);
                }
            }
        });
        
        if (this.isDev) {
        	// a button that prints the sketchPad state to the console
	        JButton testButton = new JButton("Print sketchPad state");
	        controlPanel.add(testButton);
	        testButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                sketchPad.printState();
	            }
	        });
        }
	}
	
	/**
	 * Default constructor for the BezierCreator class
	 */
	public BezierCreator() {
		new BezierCreator(false);
	}

	@Override
	public void run() {
        Container cp = f.getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(sketchPad, BorderLayout.CENTER);
        cp.add(controlPanel, BorderLayout.SOUTH);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT);
        f.pack();
        f.setVisible(true);
		
	}

}
