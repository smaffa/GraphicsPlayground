package main.shapes;

/**
 * Interface for 2D geometric objects that can be subjected to linear transformations in the 2D plane.
 * @author smaffa
 *
 * @param <T> the object that can be transformed
 */
public interface Transformable2D<T> {
	
	/**
	 * Translates the object xDelta units right and yDelta units up.
	 * @param xDelta	the double representing the shift in the positive x direction
	 * @param yDelta	the double representing the shift in the positive y direction
	 * @return the translated object
	 */
	public T translate(double xDelta, double yDelta);
	
	/**
	 * Reflects the object over an axis specified by an input vector
	 * @param axisVectorX	the double representing the x component of the axis of reflection
	 * @param axisVectorY	the double representing the y component of the axis of reflection
	 * @return the reflected object
	 */
	public T reflect(double axisVectorX, double axisVectorY);
	
	/**
	 * Rescales the object using a dilation/contraction from the origin.
	 * @param xScale	the double scaling factor in the positive x direction
	 * @param yScale	the double scaling factor in the positive y direction
	 * @return the rescaled object
	 */
	public T scale(double xScale, double yScale);
	
	/**
	 * Rescales the object using a dilation/contraction from the origin.
	 * @param factor	the double scaling factor in the positive direction of both axes
	 * @return the rescaled object
	 */
	public T scale(double factor);
	
	/**
	 * Rotates the object around the origin
	 * @param radians	the double representing the angle of rotation about the origin, in radians
	 * @return the rotated object
	 */
	public T rotate(double radians);
	
	/**
	 * Shears the object in the x dimension
	 * @param factor	the double shear factor in the positive x direction
	 * @return the sheared object
	 */
	public T shearX(double factor);
	
	/**
	 * Shears the object in the y dimension
	 * @param factor	the double shear factor in the positive y direction
	 * @return the sheared object
	 */
	public T shearY(double factor);

}
