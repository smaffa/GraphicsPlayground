package main;

import main.sketch.SketchPad;
import main.utils.Constants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Run {
	private static boolean isDev = false;
	
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowCanvas();
            }
        });
    }

    private static void createAndShowCanvas() {
        JFrame f = new JFrame();

        SketchPad sketchPad = new SketchPad();

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addPointButton = new JButton("Add Control Point");
        buttonPanel.add(addPointButton);
        addPointButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sketchPad.addRandomPoint();
            }
        });

        JButton deletePointButton = new JButton("Remove Selected Point");
        buttonPanel.add(deletePointButton);
        deletePointButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sketchPad.removeSelectedPoint();
            }
        });

        JToggleButton bezierCreationButton = new JToggleButton("Cubic Bezier Creation");
        buttonPanel.add(bezierCreationButton);
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
        buttonPanel.add(tSlider);
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
        buttonPanel.add(showTButton);
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
        buttonPanel.add(referenceButton);
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
	        buttonPanel.add(testButton);
	        testButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                sketchPad.printState();
	            }
	        });
        }

        Container cp = f.getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(sketchPad, BorderLayout.CENTER);
        cp.add(buttonPanel, BorderLayout.SOUTH);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT);
        f.pack();
        f.setVisible(true);
    }
}
