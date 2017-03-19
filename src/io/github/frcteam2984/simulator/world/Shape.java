package io.github.frcteam2984.simulator.world;

public abstract class Shape{
	
	private Point center;
	
	public Shape(){
		this(new Point(0,0));
	}
	
	public Shape(Point center){
		this.center = center;
	}
	
	public Point getCenter(){
		return this.center;
	}
	
	public void move(Vector v){
		this.center.translate(v);
	}
	
	public void setCenter(Point center){
		this.center = center;
	}
	
}
