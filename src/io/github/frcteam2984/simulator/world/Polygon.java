package io.github.frcteam2984.simulator.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Polygon extends Shape{

	private List<Point> origional;
	private List<Point> points;
	private double rotation;
	private BoundingBox boundingBox;
	private boolean changed;
	
	public Polygon(Point center){
		this(center, new ArrayList<Point>());
	}
	
	public Polygon(Point center, Point... points){
		this(center, Arrays.asList(points));
	}
	
	public Polygon(Point center, Collection<Point> points){
		super(center);
		this.points = new ArrayList<Point>(points);
		this.origional = new ArrayList<Point>(points.size());
		for(Point p : points){
			this.origional.add(p.clone());
		}
		this.changed = true;
	}
	
	public void rotate(double theta){
		this.rotation += theta;
		for(Point p : this.points){
			p.rotate(0, 0, -theta);
		}
		this.changed = true;
	}
	
	public List<Point> getPoints(){
		return this.points;
	}
	
	public double getRotation(){
		return this.rotation;
	}
	
	private void calulateBoundingBox(){
		if(this.points.size() < 1)
			return;
		Point max = this.points.get(0).clone();
		Point min = max.clone();
		for(Point p : this.points){
			Point.max(max, max, p);
			Point.min(min, min, p);
		}
		this.boundingBox = new BoundingBox(max.getX() - min.getX(), max.getY() - min.getY(), new Point((max.getX() + min.getX())/2, (max.getY() + min.getY())/2), this.getCenter());
	}
	
	public BoundingBox getBoundingBox(){
		if(this.changed){
			this.calulateBoundingBox();
			this.changed = false;
		}
		else
			this.boundingBox.updatePosition(this.getCenter());
		return this.boundingBox;
	}
	
	public java.awt.Polygon asPolygon(){
		java.awt.Polygon polygon = new java.awt.Polygon();
		for(Point p : this.points){
			polygon.addPoint((int)(p.getX()+0.5), (int)(p.getY()+0.5));
		}
		return polygon;
	}
	
	public String toString(){
		StringBuilder b = new StringBuilder();
		b.append("Polygon {");
		if(this.points.size() > 1){
			b.append("Points: [" + this.points.get(0));
			for(int i = 1; i<this.points.size(); i++){
				b.append(this.points.get(i) + ",");
			}
			b.append("], ");
		}
		b.append("Position: " + this.getCenter() + ", Rotation: " + this.getRotation() + "}");
		return b.toString();
	}

	public void setRotation(double theta) {
		this.points.clear();
		for(Point p : this.origional){
			p = p.clone();
			p.rotate(0, 0, theta);
			this.points.add(p);
		}
	}
	
}
