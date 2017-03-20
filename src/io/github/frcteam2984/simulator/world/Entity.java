package io.github.frcteam2984.simulator.world;

import java.awt.geom.Path2D;

import org.json.JSONObject;

/**
 * A class which is a basic entity. It has a coefficient of friction, a mass,
 * a moment of inertia, and collision shape which are used to calculate collisions.
 * 
 * @author Max Apodaca
 *
 */
public class Entity {

	/**
	 * Intrinsic properties of the object
	 */
	private double mass;
	private double moi;
	private double coefficentOfFriction;
	private Shape collisionShape;
	private Path2D drawingShape;
	
	/**
	 * Extrinsic properties of the object
	 */
	private Vector velocity;
	private Point position;
	private double rotation;
	private double rotationalVelocity;
	
	/**
	 * Default constructor which sets all extrinsic properties
	 *  to zero and all intrinsic properties to one and a collision
	 *  shape of a sphere.
	 */
	public Entity(){
		this.mass = 1;
		this.moi = 1;
		this.coefficentOfFriction = 1;
		this.collisionShape = new Circle(new Point(0,0), 1);
		this.drawingShape = new Path2D.Double();
		
		this.velocity = new Vector(0, 0);
		this.position = new Point(0, 0);
		this.rotation = 0;
		this.rotationalVelocity = 0;
	}
	
	public Entity(JSONObject json){
		
	}
	
}
