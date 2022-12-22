package test.shapes;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.geom.Point2D;

import org.junit.jupiter.api.Test;

import main.shapes.CubicBezierCurve;
import main.utils.Constants;

class CubicBezierCurveTest {

	/**
	 * Test constructors for curves
	 */
	@Test
	void testConstructor() {
		assertNotNull(new CubicBezierCurve(new Point2D.Double(-1,-1), 
				new Point2D.Double(-1,1), 
				new Point2D.Double(1,1), 
				new Point2D.Double(1,-1)));
		assertNotNull(new CubicBezierCurve(new Point2D.Double(-1,-1), 
				new Point2D.Double(-1,1), 
				new Point2D.Double(1,1), 
				new Point2D.Double(1,-1), 
				50));
	}
	
	/**
	 * Test getters
	 */
	@Test
	void testGetters() {
		CubicBezierCurve curve = new CubicBezierCurve(new Point2D.Double(-1,-1), 
				new Point2D.Double(-1,1), 
				new Point2D.Double(1,1), 
				new Point2D.Double(1,-1),
				50);
		assertEquals(curve.getP1(), new Point2D.Double(-1,-1));
		assertEquals(curve.getC1(), new Point2D.Double(-1,1));
		assertEquals(curve.getC2(), new Point2D.Double(1,1));
		assertEquals(curve.getP2(), new Point2D.Double(1,-1));
		assertEquals(curve.getBezierFineness(), 50);
		assertEquals(curve.getControlPoints()[0], curve.getP1());
		assertEquals(curve.getControlPoints()[1], curve.getC1());
		assertEquals(curve.getControlPoints()[2], curve.getC2());
		assertEquals(curve.getControlPoints()[3], curve.getP2());
		assertEquals(curve.getOrder(), 3);
	}
	
	/**
	 * Test setters
	 */
	@Test
	void testSetters() {
		CubicBezierCurve curve = new CubicBezierCurve(new Point2D.Double(-1,-1), 
				new Point2D.Double(-1,1), 
				new Point2D.Double(1,1), 
				new Point2D.Double(1,-1),
				50);
		curve.setP1(new Point2D.Double(2, 3));
		curve.setC1(new Point2D.Double(5, 7));
		curve.setC2(new Point2D.Double(11, 13));
		curve.setP2(new Point2D.Double(17, 19));
		curve.setBezierFineness(10);
		assertEquals(curve.getP1(), new Point2D.Double(2,3));
		assertEquals(curve.getC1(), new Point2D.Double(5,7));
		assertEquals(curve.getC2(), new Point2D.Double(11,13));
		assertEquals(curve.getP2(), new Point2D.Double(17,19));
		assertEquals(curve.getBezierFineness(), 10);
	}
	
	/**
	 * Test hashCode() and equals()
	 */
	@Test
	void testHashCodeAndEquals() {
		CubicBezierCurve base = new CubicBezierCurve(new Point2D.Double(-1,-1), 
				new Point2D.Double(-1,1), 
				new Point2D.Double(1,1), 
				new Point2D.Double(1,-1),
				50);
		CubicBezierCurve pt1 = new CubicBezierCurve(new Point2D.Double(-1,1), 
				new Point2D.Double(-1,1), 
				new Point2D.Double(1,1), 
				new Point2D.Double(1,-1),
				50);
		CubicBezierCurve pt2 = new CubicBezierCurve(new Point2D.Double(-1,-1), 
				new Point2D.Double(-2,1), 
				new Point2D.Double(1,1), 
				new Point2D.Double(1,-1),
				50);
		CubicBezierCurve pt3 = new CubicBezierCurve(new Point2D.Double(-1,-1), 
				new Point2D.Double(-1,1), 
				new Point2D.Double(3,1), 
				new Point2D.Double(1,-1),
				50);
		CubicBezierCurve pt4 = new CubicBezierCurve(new Point2D.Double(-1,-1), 
				new Point2D.Double(-1,1), 
				new Point2D.Double(1,1), 
				new Point2D.Double(1,-4),
				50);
		CubicBezierCurve fineness = new CubicBezierCurve(new Point2D.Double(-1,-1), 
				new Point2D.Double(-1,1), 
				new Point2D.Double(1,1), 
				new Point2D.Double(1,-1),
				100);
		CubicBezierCurve base2 = new CubicBezierCurve(new Point2D.Double(-1,-1), 
				new Point2D.Double(-1,1), 
				new Point2D.Double(1,1), 
				new Point2D.Double(1,-1),
				50);
		
		assertTrue(base.equals(base));
		assertFalse(base.equals(pt1) | base.equals(pt2) | base.equals(pt3) | base.equals(pt4) | base.equals(fineness));
		assertTrue(base.equals(base2) && base2.equals(base));
		
		assertTrue(base.hashCode() == base.hashCode());
		assertFalse(base.hashCode() == pt1.hashCode() | 
				base.hashCode() == pt2.hashCode() | 
				base.hashCode() == pt3.hashCode() | 
				base.hashCode() == pt4.hashCode() |
				base.hashCode() == fineness.hashCode());
		assertTrue(base.hashCode() == base2.hashCode());
	}
	
	@Test
	void testCopyConstructor() {
		CubicBezierCurve a = new CubicBezierCurve(new Point2D.Double(-1,-1), 
				new Point2D.Double(-1,1), 
				new Point2D.Double(1,1), 
				new Point2D.Double(1,-1));
		CubicBezierCurve b = new CubicBezierCurve(a);
		
		assertTrue(a.equals(b) && b.equals(a));
		a.setC1(new Point2D.Double(0, 0));
		assertFalse(a.equals(b));
		
	}
	
	/**
	 * Test calculation of position array
	 */
	@Test
	void testComputePosition() {
		CubicBezierCurve unitSquare = new CubicBezierCurve(new Point2D.Double(-16,16), 
				new Point2D.Double(16,16), 
				new Point2D.Double(16,-16), 
				new Point2D.Double(-16,-16),
				4);
		Point2D[] curvePoints = unitSquare.computePosition();
		assertEquals(curvePoints[0], new Point2D.Double(-16, 16));
		assertEquals(curvePoints[1], new Point2D.Double(2, 11));
		assertEquals(curvePoints[2], new Point2D.Double(8, 0));
		assertEquals(curvePoints[3], new Point2D.Double(2, -11));
		assertEquals(curvePoints[4], new Point2D.Double(-16, -16));
	}
	
	@Test
	void testComputeVelocity() {
		// computable
		CubicBezierCurve square = new CubicBezierCurve(new Point2D.Double(-1,1), 
				new Point2D.Double(1,1), 
				new Point2D.Double(1,-1), 
				new Point2D.Double(-1,-1));
		Point2D[] squareVelocity = square.computeVelocity();
		assertEquals(squareVelocity[0], new Point2D.Double(6, 0));
		assertEquals(squareVelocity[squareVelocity.length - 1], new Point2D.Double(-6, 0));
		
		// translational invariance: direction and magnitude
		CubicBezierCurve translatedSquare = new CubicBezierCurve(new Point2D.Double(4,6), 
				new Point2D.Double(6,6), 
				new Point2D.Double(6,4), 
				new Point2D.Double(4,4));
		Point2D[] translatedSquareVelocity = translatedSquare.computeVelocity();
		for (int i = 0; i < translatedSquareVelocity.length; i++) {
			assertEquals(squareVelocity[i].getX(), translatedSquareVelocity[i].getX(), Constants.ERROR_TOLERANCE);
			assertEquals(squareVelocity[i].getY(), translatedSquareVelocity[i].getY(), Constants.ERROR_TOLERANCE);
		}
		
		// scale invariance: direction
		CubicBezierCurve largeSquare = new CubicBezierCurve(new Point2D.Double(-3,3), 
				new Point2D.Double(3,3), 
				new Point2D.Double(3,-3), 
				new Point2D.Double(-3,-3));
		Point2D[] largeSquareVelocity = largeSquare.computeVelocity();
		for (int i = 0; i < largeSquareVelocity.length; i++) {
			assertEquals(largeSquareVelocity[i].getY() / largeSquareVelocity[i].getX(), 
					squareVelocity[i].getY() / squareVelocity[i].getX(),
					Constants.ERROR_TOLERANCE);
		}
		
		// rotational invariance: magnitude
		CubicBezierCurve rotatedSquare = new CubicBezierCurve(new Point2D.Double(-1,1), 
				new Point2D.Double(1,1), 
				new Point2D.Double(1,-1), 
				new Point2D.Double(-1,-1));
		Point2D[] rotatedSquareVelocity = rotatedSquare.computeVelocity();
		for (int i = 0; i < rotatedSquareVelocity.length; i++) {
			assertEquals(Math.pow(rotatedSquareVelocity[i].getX(), 2) + Math.pow(rotatedSquareVelocity[i].getY(), 2), 
					Math.pow(squareVelocity[i].getX(), 2) + Math.pow(squareVelocity[i].getY(), 2),
					Constants.ERROR_TOLERANCE);
		}
	}
	
	@Test
	void testComputeAcceleration() {
		// computable
		CubicBezierCurve square = new CubicBezierCurve(new Point2D.Double(-1,1), 
				new Point2D.Double(1,1), 
				new Point2D.Double(1,-1), 
				new Point2D.Double(-1,-1));
		Point2D[] squareAcceleration = square.computeAcceleration();
		assertEquals(squareAcceleration[0], new Point2D.Double(-12, -12));
		assertEquals(squareAcceleration[squareAcceleration.length - 1], new Point2D.Double(-12, 12));
		
		// translational invariance: direction and magnitude
		CubicBezierCurve translatedSquare = new CubicBezierCurve(new Point2D.Double(4,6), 
				new Point2D.Double(6,6), 
				new Point2D.Double(6,4), 
				new Point2D.Double(4,4));
		Point2D[] translatedSquareAcceleration = translatedSquare.computeAcceleration();
		for (int i = 0; i < translatedSquareAcceleration.length; i++) {
			assertEquals(squareAcceleration[i].getX(), translatedSquareAcceleration[i].getX(), Constants.ERROR_TOLERANCE);
			assertEquals(squareAcceleration[i].getY(), translatedSquareAcceleration[i].getY(), Constants.ERROR_TOLERANCE);
		}
		
		// scale invariance: direction
		CubicBezierCurve largeSquare = new CubicBezierCurve(new Point2D.Double(-3,3), 
				new Point2D.Double(3,3), 
				new Point2D.Double(3,-3), 
				new Point2D.Double(-3,-3));
		Point2D[] largeSquareAcceleration = largeSquare.computeAcceleration();
		for (int i = 0; i < largeSquareAcceleration.length; i++) {
			assertEquals(largeSquareAcceleration[i].getY() / largeSquareAcceleration[i].getX(), 
					squareAcceleration[i].getY() / squareAcceleration[i].getX(),
					Constants.ERROR_TOLERANCE);
		}
		
		// rotational invariance: magnitude
		CubicBezierCurve rotatedSquare = new CubicBezierCurve(new Point2D.Double(-1,1), 
				new Point2D.Double(1,1), 
				new Point2D.Double(1,-1), 
				new Point2D.Double(-1,-1));
		Point2D[] rotatedSquareAcceleration = rotatedSquare.computeAcceleration();
		for (int i = 0; i < rotatedSquareAcceleration.length; i++) {
			assertEquals(Math.pow(rotatedSquareAcceleration[i].getX(), 2) + Math.pow(rotatedSquareAcceleration[i].getY(), 2), 
					Math.pow(squareAcceleration[i].getX(), 2) + Math.pow(squareAcceleration[i].getY(), 2),
					Constants.ERROR_TOLERANCE);
		}
	}
	
	static void assertPointsMatch(Point2D p1, Point2D p2) {
		assertEquals(p1.getX(), p2.getX(), Constants.ERROR_TOLERANCE);
		assertEquals(p1.getY(), p1.getY(), Constants.ERROR_TOLERANCE);
	}
	
	static void assertPositionsMatch(CubicBezierCurve c1, CubicBezierCurve c2) {
		Point2D[] c1Position = c1.computePosition();
		Point2D[] c2Position = c2.computePosition();
		for (int i = 0; i < c1Position.length; i++) {
			assertPointsMatch(c1Position[i], c2Position[i]);
		}
	}
	
	@Test
	void testTranslate() {
		CubicBezierCurve square = new CubicBezierCurve(new Point2D.Double(-1,1), 
				new Point2D.Double(1,1), 
				new Point2D.Double(1,-1), 
				new Point2D.Double(-1,-1));
		
		square.translate(5, 4);
		
		CubicBezierCurve translatedSquare = new CubicBezierCurve(new Point2D.Double(4,5), 
				new Point2D.Double(6,5), 
				new Point2D.Double(6,3), 
				new Point2D.Double(4,3));
		
		assertTrue(square.equals(translatedSquare));
		assertPositionsMatch(square, translatedSquare);
		
		square.centerAtP1();
		
		CubicBezierCurve centeredSquare = new CubicBezierCurve(new Point2D.Double(0,0), 
				new Point2D.Double(2,0), 
				new Point2D.Double(2,-2), 
				new Point2D.Double(0,-2));
		
		assertTrue(square.equals(centeredSquare));
		assertPositionsMatch(square, centeredSquare);
	}
	
	@Test
	void testReflect() {
		CubicBezierCurve rect = new CubicBezierCurve(new Point2D.Double(-1,3), 
				new Point2D.Double(5,3), 
				new Point2D.Double(5,-1), 
				new Point2D.Double(-1,-1));
		 
		// reflect over x-axis
		CubicBezierCurve yFlipper = new CubicBezierCurve(rect);
		yFlipper.reflect(1, 0);
		
		CubicBezierCurve yFlipped = new CubicBezierCurve(new Point2D.Double(-1,-3), 
				new Point2D.Double(5,-3), 
				new Point2D.Double(5,1), 
				new Point2D.Double(-1, 1));
		assertPositionsMatch(yFlipped, yFlipped);
		
		// reflect over y-axis
		CubicBezierCurve xFlipper = new CubicBezierCurve(rect);
		xFlipper.reflect(0, 1);
		
		CubicBezierCurve xFlipped = new CubicBezierCurve(new Point2D.Double(1,3), 
				new Point2D.Double(-5,3), 
				new Point2D.Double(-5,-1), 
				new Point2D.Double(1, -1));
		assertPositionsMatch(xFlipped, xFlipped);
		
		// reflect over y=x
		CubicBezierCurve diagFlipper = new CubicBezierCurve(rect);
		diagFlipper.reflect(Math.sqrt(2), Math.sqrt(2));
		
		CubicBezierCurve diagFlipped = new CubicBezierCurve(new Point2D.Double(3,-1), 
				new Point2D.Double(3, 5), 
				new Point2D.Double(-1,5), 
				new Point2D.Double(-1, -1));
		assertPositionsMatch(diagFlipper, diagFlipped);
		
		// invariance to axis vector's magnitude
		CubicBezierCurve flipCopy1 = new CubicBezierCurve(rect);
		CubicBezierCurve flipCopy2 = new CubicBezierCurve(rect);
		flipCopy1.reflect(1.5, 2);
		flipCopy2.reflect(4.5, 6);
		assertPositionsMatch(flipCopy1, flipCopy2);
	}
	
	@Test
	void testScale() {
		// scale disproportionately
		CubicBezierCurve stretcher = new CubicBezierCurve(new Point2D.Double(-1,1), 
				new Point2D.Double(4,2), 
				new Point2D.Double(1,-2), 
				new Point2D.Double(-1,-1));
		stretcher.scale(2, 3);
		
		CubicBezierCurve stretched = new CubicBezierCurve(new Point2D.Double(-2,3), 
				new Point2D.Double(8,6), 
				new Point2D.Double(2,-6), 
				new Point2D.Double(-2,-3));
		assertPositionsMatch(stretcher, stretched);
		
		// scale proportionally
		CubicBezierCurve scaler = new CubicBezierCurve(new Point2D.Double(-1,1), 
				new Point2D.Double(4,2), 
				new Point2D.Double(1,-2), 
				new Point2D.Double(-1,-1));
		scaler.scale(0.5);
		
		CubicBezierCurve scaled = new CubicBezierCurve(new Point2D.Double(-0.5,0.5), 
				new Point2D.Double(2, 1), 
				new Point2D.Double(0.5,-1), 
				new Point2D.Double(-0.5,-0.5));
		assertPositionsMatch(scaler, scaled);
	}
	
	@Test
	void testRotate() {
		CubicBezierCurve square = new CubicBezierCurve(new Point2D.Double(-1,1), 
				new Point2D.Double(1,1), 
				new Point2D.Double(1,-1), 
				new Point2D.Double(-1,-1));
		
		// rotate in units of PI/2
		CubicBezierCurve square1 = new CubicBezierCurve (square);
		square1.rotate(Math.PI / 2);
		CubicBezierCurve square1Rotated = new CubicBezierCurve(new Point2D.Double(-1,-1), 
				new Point2D.Double(-1,1), 
				new Point2D.Double(1,1), 
				new Point2D.Double(1,-1));
		assertPositionsMatch(square1, square1Rotated);
		
		CubicBezierCurve square2 = new CubicBezierCurve (square);
		square2.rotate(Math.PI);
		CubicBezierCurve square2Rotated = new CubicBezierCurve(new Point2D.Double(1,-1), 
				new Point2D.Double(-1,-1), 
				new Point2D.Double(-1,1), 
				new Point2D.Double(1,1));
		assertPositionsMatch(square2, square2Rotated);
		
		CubicBezierCurve square3 = new CubicBezierCurve (square);
		square3.rotate(3 * Math.PI / 2);
		CubicBezierCurve square3Rotated = new CubicBezierCurve(new Point2D.Double(1,1), 
				new Point2D.Double(1,-1), 
				new Point2D.Double(-1,-1), 
				new Point2D.Double(-1,1));
		assertPositionsMatch(square3, square3Rotated);
		
		// rotate at non-standard angles
		CubicBezierCurve square4 = new CubicBezierCurve (square);
		square4.rotate(Math.PI / 4);
		CubicBezierCurve square4Rotated = new CubicBezierCurve(new Point2D.Double(-Math.sqrt(2),0), 
				new Point2D.Double(0,Math.sqrt(2)), 
				new Point2D.Double(Math.sqrt(2),0), 
				new Point2D.Double(0,-Math.sqrt(2)));
		assertPositionsMatch(square4, square4Rotated);
	}
	
	@Test
	void testShear() {
		CubicBezierCurve rect = new CubicBezierCurve(new Point2D.Double(-1,3), 
				new Point2D.Double(5,3), 
				new Point2D.Double(5,-1), 
				new Point2D.Double(-1,-1));
		rect.shearY(0.5);		
		CubicBezierCurve ySheared = new CubicBezierCurve(new Point2D.Double(-1, 2.5), 
				new Point2D.Double(5,5.5), 
				new Point2D.Double(5,1.5), 
				new Point2D.Double(-1,-1.5));
		assertPositionsMatch(rect, ySheared);
		rect.shearX(1);
		CubicBezierCurve xySheared = new CubicBezierCurve(new Point2D.Double(1.5, 2.5), 
				new Point2D.Double(10.5,5.5), 
				new Point2D.Double(6.5,1.5), 
				new Point2D.Double(-2.5,-1.5));
		assertPositionsMatch(rect, xySheared);
	}
	
	@Test
	void testIdentities() {
		CubicBezierCurve square = new CubicBezierCurve(new Point2D.Double(-1,1), 
				new Point2D.Double(1,1), 
				new Point2D.Double(1,-1), 
				new Point2D.Double(-1,-1));
		
		// translation * -translation
		CubicBezierCurve translationIdentity = new CubicBezierCurve(square);
		translationIdentity.translate(11, -0.6).translate(-11, 0.6);
		assertPositionsMatch(square, translationIdentity);
		
		// reflection * reflection
		CubicBezierCurve reflectionIdentity = new CubicBezierCurve(square);
		reflectionIdentity.reflect(0.6, 0.8).reflect(0.6, 0.8);
		assertPositionsMatch(square, reflectionIdentity);
		
		// rotation * ... * rotate {sum(angle = 2pi)}
		CubicBezierCurve rotationIdentity4 = new CubicBezierCurve(square);
		rotationIdentity4.rotate(Math.PI / 2).rotate(Math.PI / 2).rotate(Math.PI / 2).rotate(Math.PI / 2);
		assertPositionsMatch(square, rotationIdentity4);
		
		CubicBezierCurve rotationIdentity3 = new CubicBezierCurve(square);
		rotationIdentity3.rotate(2 * Math.PI / 3).rotate(2 * Math.PI / 3).rotate(2 * Math.PI / 3);
		assertPositionsMatch(square, rotationIdentity3);
		
		CubicBezierCurve rotationIdentityIrreg = new CubicBezierCurve(square);
		rotationIdentityIrreg.rotate(Math.PI / 6).rotate(Math.PI / 3).rotate(Math.PI / 2).rotate(Math.PI);
		assertPositionsMatch(square, rotationIdentityIrreg);
		
		// scale * 1/scale
		CubicBezierCurve scaleIdentity = new CubicBezierCurve(square);
		scaleIdentity.scale(2.0 / 3.0, 4).scale(1.5, 0.25);
		assertPositionsMatch(square, scaleIdentity);
		
		// one-directional shears are invertible: shear * -shear
		CubicBezierCurve shearXIdentity = new CubicBezierCurve(square);
		shearXIdentity.shearX(3).shearX(-3);
		assertPositionsMatch(square, shearXIdentity);
		
		CubicBezierCurve shearYIdentity = new CubicBezierCurve(square);
		shearYIdentity.shearY(-0.4).shearY(0.4);
		assertPositionsMatch(square, shearYIdentity);
	}

}
