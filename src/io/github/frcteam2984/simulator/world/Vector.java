package io.github.frcteam2984.simulator.world;

/**
 * A Class to do simple vector math in the physics engine
 * @author Max Apodaca
 *
 */
public class Vector {

	/**
	 * the x component of the vector
	 */
	private double x;
	
	/**
	 * the y component of the vector
	 */
	private double y;
	
	/**
	 * the constructor for the vector that sets the two components of the vector
	 * @param x the x component of the vector
	 * @param y the y component of the vector
	 */
	public Vector(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Creates a vector from point one to point two
	 * @param p1 the origin point
	 * @param p2 the ending point
	 */
	public Vector(Point p1, Point p2){
		this(p2.getX() - p1.getX(), p2.getY() - p1.getY());
	}
	
	/**
	 * adds this vector to another vector and returns a third vector that is the sum of the two
	 * @param v the other vector to add to this one
	 * @return the resultant vector
	 */
	public Vector add(Vector v){
		return new Vector(this.x + v.x, this.y + v.y);
	}
	
	/**
	 * subtracts this vector from another vector and returns a third vector that is the difference of the two
	 * @param v the other vector to subtract from this one
	 * @return the resultant vector
	 */
	public Vector sub(Vector v){
		return new Vector(this.x - v.x, this.y - v.y);
	}
	
	/**
	 * multiplies this vector by another vector and returns a third vector that is the product of the two
	 * @param v the other vector to multiply by this one
	 * @return the resultant vector
	 */
	public Vector multiply(Vector v){
		return new Vector(this.x * v.x, this.y * v.y);
	}
	
	/**
	 * scales this vector by the indicated factor
	 * @param scale the factor to scale this vector by
	 * @return the resultant vector
	 */
	public Vector scale(double scale){
		return new Vector(this.x * scale, this.y * scale);
	}
	
	/**
	 * Rotates the vector by the angle in radians CCW
	 * @param newAngle the angle to rotate by
	 * @return the resultant vector
	 */
	public Vector rotate(double newAngle){
		double mag = Math.sqrt(this.x*this.x + this.y+this.y);
		double angle = Math.atan2(this.y, this.x);
		angle += newAngle;
		return new Vector(Math.cos(angle)*mag, Math.sin(angle)*mag);
	}
	
	/**
	 * adds another vector to this vector
	 * @param v the other vector to add to this one
	 * @return this vector
	 */
	public Vector addTo(Vector v){
		this.x += v.x;
		this.y += v.y;
		return this;
	}
	
	/**
	 * subtracts another vector from this vector
	 * @param v the other vector to subtract from this one
	 * @return this vector
	 */
	public Vector subTo(Vector v){
		this.x -= v.x;
		this.y -= v.y;
		return this;
	}
	
	/**
	 * multiplies this vector by another vector
	 * @param v the other vector to multiply by this one
	 * @return this vector
	 */
	public Vector multiplyTo(Vector v){
		this.x *= v.x;
		this.y *= v.y;
		return this;
	}
	
	/**
	 * scales this vector by the indicated factor
	 * @param scale the factor to scale this vector by
	 * @return this vector
	 */
	public Vector scaleTo(double scale){
		this.x *= scale;
		this.y *= scale;
		return this;
	}
	
	/**
	 * rotates the vector by the angle CCW
	 * @param newAngle the new angle to be rotated more
	 * @return this vector
	 */
	public Vector rotateTo(double newAngle){
		double mag = Math.sqrt(this.x*this.x + this.y+this.y);
		double angle = Math.atan2(this.y, this.x);
		angle += newAngle;
		this.x = Math.cos(angle)*mag;
		this.y = Math.sin(angle)*mag;
		return this;
	}
	
	/**
	 * @return the x component of the vector
	 */
	public double getX(){
		return this.x;
	}
	
	/**
	 * @return the y component of the vector
	 */
	public double getY(){
		return this.y;
	}
	
	/**
	 * returns the length of the vector
	 * @return the length of the vector
	 */
	public double getLength(){
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}
	
	/**
	 * Sets the length of the vector to the given length
	 * @param length the length the vector should be
	 */
	public void setLength(double length){
		double scale = length / this.getLength();
		this.scaleTo(scale);
	}
	
	/**
	 * returns the angle from the horizontal that the vector is at
	 * @return the angle that the vector is at
	 */
	public double getAngle(){
		return Math.atan2(this.y, this.x);
	}
	
	/**
	 * Generates a normal to this vector that may be facing either way
	 * @return a normal to this vector
	 */
	public Vector normal(){
		return new Vector(this.y, this.x);
	}
	
	/**
	 * clones the vector
	 * @return a vector identical to this one
	 */
	public Vector clone(){
		return new Vector(this.x, this.y);
	}
	
	/**
	 * Returns a string representation of the object
	 */
	public String toString(){
		return "[" + this.x + ", " + this.y + "]";
	}
	
}
