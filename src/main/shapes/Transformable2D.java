package main.shapes;

public interface Transformable2D<T> {
	
	public T translate(double xDelta, double yDelta);
	
	public T reflect(double axisVectorX, double axisVectorY);
	
	public T scale(double xScale, double yScale);
	
	public T scale(double factor);
	
	public T rotate(double radians);
	
	public T shearX(double factor);
	
	public T shearY(double factor);

}
