package io.github.frcteam2984.simulator.world;

/**
 * A class for a bounding box for AABB collision detection
 * @author max
 *
 */
public class BoundingBox extends Shape{
	
	/**
	 * the width and height of the box
	 */
	private double width, height;
	
	/**
	 * the center of the box that may be offset form the actual position of the box. This does not change
	 */
	private Point center;
	
	/**
	 * the position of the box that will be mapped to the position of the shape
	 */
	private Point position;
	
	/**
	 * create the bounding box with all the information it needs to work
	 * @param width the width of the box
	 * @param height the height of the box
	 * @param center the center of the box relative to the position
	 * @param position the position of the parent shape
	 */
	public BoundingBox(double width, double height, Point center, Point position){
		this.width = width;
		this.height = height;
		this.center = center;
		this.position = position;
	}
	
	/**
	 * figures out whether this and another box are intersecting
	 * @param b the other box to check with
	 * @return whether or not they are intersecting
	 */
	public boolean intersect(BoundingBox b){
		boolean intersect = true;
		intersect &= b.center.getX() + b.position.getX() + b.width/2 > this.center.getX() + this.position.getX() - this.width/2;
		intersect &= b.center.getX() + b.position.getX() - b.width/2 < this.center.getX() + this.position.getX() + this.width/2;
		intersect &= b.center.getY() + b.position.getY() + b.height/2 > this.center.getY() + this.position.getY() - this.height/2;
		intersect &= b.center.getY() + b.position.getY() - b.height/2 < this.center.getY() + this.position.getY() + this.height/2;
		return intersect;
	}
	
	/**
	 * @return the center of the box
	 */
	public Point getCenter(){
		return this.center;
	}
	
	/**
	 * set the center of the box
	 * @param center the center of the box
	 */
	public void setCenter(Point center){
		this.center = center;
	}
	
	/**
	 * update the position of the box
	 * @param p the new position of the box
	 */
	public void updatePosition(Point p){
		this.position = p;
	}
	
	public String toString(){
		return "BoundingBox {width: " + this.width + ", height: " + this.height + ", center: " + this.center + ", position: " + this.position + "}";
	}
	
}
