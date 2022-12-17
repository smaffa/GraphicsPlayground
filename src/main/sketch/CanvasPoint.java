package main.sketch;

import main.utils.Constants;

import java.awt.geom.Point2D;

class CanvasPoint extends Point2D {
    private int x;
    private int y;
    private double radius = Constants.POINT_RADIUS;

    public CanvasPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public CanvasPoint(int x, int y, int radius) {
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
        this.x = (int) x;
        this.y = (int) y;
    }

    public void setRadius(double r) {
        this.radius = r;
    }

    public boolean contains(double x, double y) {
        return Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2) <= Math.pow(this.radius, 2);
    }
    
    public String toString() {
    	return "(" + Integer.toString(this.x) + ", " + Integer.toString(this.y) + ")";
    }
}
