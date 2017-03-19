package io.github.frcteam2984.simulator.world;

public class Point {

	private double x;
	private double y;

	/**
	 * makes a new point at these two coordinates
	 * @param x the x coordinate of the point
	 * @param y the y coordinate of the point
	 */
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * moves this point along the given vector
	 * @param v the vector to move this point along
	 * @return a reference to this point
	 */
	public Point translate(Vector v){
		this.translate(v.getX(), v.getY());
		return this;
	}
	
	/**
	 * translates this point by another point's x and y values
	 * @param p the point to translate this one by
	 * @return a reference to this point
	 */
	public Point translate(Point p){
		this.translate(p.x, p.y);
		return this;
	}
	
	/**
	 * translates this point by the given x and y values
	 * @param x the x distance to translate this point by
	 * @param y the y distance to translate this point by
	 * @return a reference to this point
	 */
	public Point translate(double x, double y){
		this.x += x;
		this.y += y;
		return this;
	}
	
	/**
	 * an overloaded version of {@link Point#rotate(double, double, double) rotate} to use with a point instead
	 * @param p the point to rotate about
	 * @param theta the angle to rotate by
	 */
	public void rotate(Point p, double theta){
		this.rotate(p.x, p.y, theta);
	}
	
	/**
	 * rotates the point about given x and y values by angle theta
	 * @param x the x coordinate to rotate this point about
	 * @param y the y coordinate to rotate this point about
	 * @param theta the angle to rotate this point by
	 */
	public void rotate(double x, double y, double theta){
		double sin = Math.sin(theta);
		double cos = Math.cos(theta);
		this.translate(-x, -y);
		double nX = this.x * cos - this.y * sin;
		double nY = this.x * sin + this.y * cos;
		this.x = nX;
		this.y = nY;
		this.translate(x, y);
	}
	
	/**
	 * solves for the number of vector lengths the point is away from the origin when projected onto the vector
	 * @param axis the vector that runs through the x-axis to be projected on to
	 * @return the number of vector lengths to reach the projected point, maybe be negative
	 */
	public double projectOntoAxis(Vector axis){
		double a = axis.getY();
		double b = axis.getX();
		if(a != 0 && b!= 0)
			return (a*this.y + b*this.x)/(a*a+b*b);
		else if(a != 0)
			return this.y/axis.getY();
		else
			return this.x/axis.getX();
			
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	@Override
	public Point clone(){
		return new Point(this.x, this.y);
	}
	
	public String toString(){
		return "(" + this.x + ", " + this.y + ")";
	}
	
	/**
	 * finds the max and sets it to finial
	 * @param finial the point to modify
	 * @param p1 the first point to check
	 * @param p2 the second point to check
	 */
	public static void max(Point finial, Point p1, Point p2){
		finial.x = Math.max(p1.x, p2.x);
		finial.y = Math.max(p1.y, p2.y);
	}
	
	/**
	 * finds the min and sets it to finial
	 * @param finial the point to modify
	 * @param p1 the first point to check
	 * @param p2 the second point to check
	 */
	public static void min(Point finial, Point p1, Point p2){
		finial.x = Math.min(p1.x, p2.x);
		finial.y = Math.min(p1.y, p2.y);
	}
	
}
