package test.shapes;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.geom.Point2D;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import main.shapes.PlotLine;
import main.sketch.plots.LinePlot;

class LinePlotTest {

	@Test
	void testConstructor() {
		assertNotNull(new LinePlot());
		assertNotNull(new LinePlot(600, 700));
		assertNotNull(new LinePlot(600, 700, 350, 100, 0.01));
	};
	
	@Test
	void testGetters() {
		LinePlot defaultLP = new LinePlot();
		assertEquals(defaultLP.getFigureWidth(), 500);
		assertEquals(defaultLP.getFigureHeight(), 500);
		assertEquals(defaultLP.getBorderProportion(), 0.05);
		assertEquals(defaultLP.getAxisX(), 250);
		assertEquals(defaultLP.getAxisY(), 250);
		assertTrue(defaultLP.getLines().isEmpty());
		assertTrue(defaultLP.getProjections().isEmpty());
		
		LinePlot customDimensions = new LinePlot(450, 800);
		assertEquals(customDimensions.getFigureWidth(), 450);
		assertEquals(customDimensions.getFigureHeight(), 800);
		assertEquals(customDimensions.getBorderProportion(), 0.05);
		assertEquals(customDimensions.getAxisY(), 225);
		assertEquals(customDimensions.getAxisX(), 400);
		assertTrue(customDimensions.getLines().isEmpty());
		assertTrue(customDimensions.getProjections().isEmpty());
		
		LinePlot customAxesValid = new LinePlot(400, 600, 210, 150, 0.02);
		assertEquals(customAxesValid.getFigureWidth(), 400);
		assertEquals(customAxesValid.getFigureHeight(), 600);
		assertEquals(customAxesValid.getBorderProportion(), 0.02);
		assertEquals(customAxesValid.getAxisY(), 210);
		assertEquals(customAxesValid.getAxisX(), 150);
		assertTrue(customAxesValid.getLines().isEmpty());
		assertTrue(customAxesValid.getProjections().isEmpty());
		
		LinePlot customAxesInvalid1 = new LinePlot(400, 600, 590, 39, 0.1);
		assertEquals(customAxesInvalid1.getFigureWidth(), 400);
		assertEquals(customAxesInvalid1.getFigureHeight(), 600);
		assertEquals(customAxesInvalid1.getBorderProportion(), 0.1);
		assertEquals(customAxesInvalid1.getAxisY(), 200);
		assertEquals(customAxesInvalid1.getAxisX(), 300);
		assertTrue(customAxesInvalid1.getLines().isEmpty());
		assertTrue(customAxesInvalid1.getProjections().isEmpty());
		
		LinePlot customAxesInvalid2 = new LinePlot(400, 600, 3, 595, 0.01);
		assertEquals(customAxesInvalid2.getFigureWidth(), 400);
		assertEquals(customAxesInvalid2.getFigureHeight(), 600);
		assertEquals(customAxesInvalid2.getBorderProportion(), 0.01);
		assertEquals(customAxesInvalid2.getAxisY(), 200);
		assertEquals(customAxesInvalid2.getAxisX(), 300);
		assertTrue(customAxesInvalid2.getLines().isEmpty());
		assertTrue(customAxesInvalid2.getProjections().isEmpty());
		
		LinePlot customAxesInvalid3 = new LinePlot(400, 600, 3, 595, 0.6);
		assertEquals(customAxesInvalid3.getFigureWidth(), 400);
		assertEquals(customAxesInvalid3.getFigureHeight(), 600);
		assertEquals(customAxesInvalid3.getBorderProportion(), 0.05);
		assertEquals(customAxesInvalid3.getAxisY(), 200);
		assertEquals(customAxesInvalid3.getAxisX(), 300);
		assertTrue(customAxesInvalid3.getLines().isEmpty());
		assertTrue(customAxesInvalid3.getProjections().isEmpty());
	}
	
	@Test
	void testAddLine() {
		LinePlot blankPlot = new LinePlot();
		assertTrue(blankPlot.getLines().isEmpty());
		blankPlot.addLine(new PlotLine(new Point2D[] {new Point2D.Double(1, 4), 
				new Point2D.Double(2, 6),
				new Point2D.Double(3, 7),
				new Point2D.Double(4, -1)}));
		assertEquals(blankPlot.getLines().get(0).getTrace().get(0), new Point2D.Double(1, 4));
		assertEquals(blankPlot.getLines().get(0).getTrace().get(1), new Point2D.Double(2, 6));
		assertEquals(blankPlot.getLines().get(0).getTrace().get(2), new Point2D.Double(3, 7));
		assertEquals(blankPlot.getLines().get(0).getTrace().get(3), new Point2D.Double(4, -1));
	}
	
	@Test
	void testComputePlotProjection() {
		// No border, plot bounds should be (-5, -5, 5, 5)
		LinePlot blankPlot = new LinePlot(10, 10, 5, 5, 0);
		assertTrue(blankPlot.getLines().isEmpty());
		blankPlot.addLine(new PlotLine(new Point2D[] {new Point2D.Double(1, 4), 
				new Point2D.Double(2, 6),
				new Point2D.Double(3, 20),
				new Point2D.Double(4, -1)}));
		// point that must be fit into the bounds of +5 is (3, 20), which defines the scale as (5/20) = (1/4)
		blankPlot.computePlotProjection();
		System.out.println(blankPlot.getLines().get(0).getMaxX());
		System.out.println(blankPlot.getLines().get(0).getMaxY());
		System.out.println(blankPlot.getLines().get(0).getMinX());
		System.out.println(blankPlot.getLines().get(0).getMinY());
		System.out.println(blankPlot.getProjections().get(0).getTrace().get(0));
		assertEquals(blankPlot.getProjections().get(0).getTrace().get(0), new Point2D.Double(0.25 + 5, 1 + 5));
		assertEquals(blankPlot.getProjections().get(0).getTrace().get(1), new Point2D.Double(0.5 + 5, 1.5 + 5));
		assertEquals(blankPlot.getProjections().get(0).getTrace().get(2), new Point2D.Double(0.75 + 5, 5 + 5));
		assertEquals(blankPlot.getProjections().get(0).getTrace().get(3), new Point2D.Double(1 + 5, -0.25 + 5));
	}

}
