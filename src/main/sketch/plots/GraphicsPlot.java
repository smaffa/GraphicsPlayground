package main.sketch.plots;

import javax.swing.JPanel;

public abstract class GraphicsPlot extends JPanel {
	
	/**
	 * Converts internal data coordinates into graphics image coordinates
	 */
	public abstract void computePlotProjection();
	
}
