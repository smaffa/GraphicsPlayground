package main.sketch;

import main.utils.Constants;

import java.awt.geom.Point2D;

/**
 * A class representing points on a canvas. The underlying point data is a single pair of (x,y) coordinates,
 * but its canvas representation allows for interacting with the point within a specified radius.
 * @author smaffa
 *
 */
class CanvasPoint extends Point2D {
    private double x;
    private double y;
    private double radius = Constants.POINT_RADIUS;

    /**
     * Basic constructor based on a pair of (x,y) coordinates
     * @param x		the x coordinate of the point
     * @param y		the y coordinate of the point
     */
    public CanvasPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Full constructor based on a pair of (x, y) coordinates and a radius
     * @param x		the x coordinate of the point
     * @param y		the y coordinate of the point
     * @param radius	the radius of the canvas representation
     */
    public CanvasPoint(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setRadius(double r) {
        this.radius = r;
    }

    /**
     * Checks whether the input coordinates are within the canvas representation, or interaction circle, 
     * of the point
     * @param x		the x coordinate of the point
     * @param y		the y coordinate of the point
     * @return	a boolean indicating if the coordinates are within the interactive radius of the point
     */
    public boolean contains(double x, double y) {
        return Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2) <= Math.pow(this.radius, 2);
    }
    
    @Override
    public String toString() {
    	return "(" + String.valueOf(x) + ", " + String.valueOf(y) + ")";
    }
}
