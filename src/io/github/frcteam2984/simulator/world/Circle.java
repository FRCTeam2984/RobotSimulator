package io.github.frcteam2984.simulator.world;

public class Circle extends Shape{
	
	private double radius;
	
	public Circle(Point center, double radius){
		super(center);
		this.radius = radius;
	}
	
	public double getRadius(){
		return this.radius;
	}
	
	public String toString(){
		return "Circle{Center: " + this.getCenter() + ", Radius: " + this.radius + "}";
	}
	
}
