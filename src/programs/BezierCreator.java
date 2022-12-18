package programs;

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

public class BezierCreator implements Runnable {
	
	private boolean isDev = false;
	private JFrame f = new JFrame();
	private SketchPad sketchPad = new SketchPad();
	private JPanel controlPanel = new JPanel(new FlowLayout());
	
	public BezierCreator(boolean isDev) {
		this.isDev = isDev;
		
		JButton addPointButton = new JButton("Add Control Point");
        controlPanel.add(addPointButton);
        addPointButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sketchPad.addRandomPoint();
            }
        });

        JButton deletePointButton = new JButton("Remove Selected Point");
        controlPanel.add(deletePointButton);
        deletePointButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sketchPad.removeSelectedPoint();
            }
        });

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
        
        if (isDev) {
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
