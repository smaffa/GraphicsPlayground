package main.utils;

import java.awt.geom.Point2D;

import org.ejml.simple.SimpleMatrix;

public class Constants {

    public static final int BEZIER_FINENESS = 100;

    public static final int CANVAS_WIDTH = 1000;
    public static final int CANVAS_HEIGHT = 800;

    public static final int POINT_RADIUS = 10;

    public static final int NULL_INDEX = -1;
    
    public static final SimpleMatrix CUBIC_POINT_COEFFICIENT_MATRIX = new SimpleMatrix(new double[][] {
    	{1, 0, 0, 0},
    	{-3, 3, 0, 0},
    	{3, -6, 3, 0},
    	{-1, 3, -3, 1}
    });
    
    public static final double ERROR_TOLERANCE = 1e-9;
}
