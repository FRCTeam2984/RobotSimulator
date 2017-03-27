package io.github.frcteam2984.simulator.world;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A class representative of the robot
 * @author Max Apodaca
 *
 */
public class Robot{

	private Body body;
	
	private Path2D drawingPath;
	
	private List<Wheel> wheels;
	
	private float[] powers = new float[4];
	
	/**
	 * Creates a robot in the given world based on the JSON parameters
	 * @param world the world to be created in
	 * @param obj the json object containing various parameters
	 */
	public Robot(World world, JSONObject obj){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(0, 37);
//		bodyDef.linearVelocity.set(200, 0);
//		bodyDef.angle = (float) (Math.PI/4 + Math.PI);
//		bodyDef.angularVelocity = 1;
		
		PolygonShape shape = PolygonUtils.getPolygonFromJson(obj.getJSONArray("colliding"));
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.01f;
		fixtureDef.friction = 0.3f;
		
		this.body = world.createBody(bodyDef);
		this.body.createFixture(fixtureDef);
		
		JSONArray wheels = obj.getJSONArray("wheels");
		this.wheels = new ArrayList<Wheel>();
		
		for(int i = 0; i<wheels.length(); i++){
			this.wheels.add(new Wheel(wheels.getJSONObject(i), this, world));
		}
		
		this.drawingPath = PolygonUtils.getPathFromJson(obj.getJSONArray("drawing"));
		
		for(Wheel wheel : this.wheels){
			this.drawingPath.append(wheel.getPath(), false);
		}
	}
	
	
	
	/**
	 * causes the wheels and friction to be applied
	 * @param timeStep the timestep over which to apply the function
	 */
	public void update(float timeStep){
		int i = 0;
		for(Wheel wheel : this.wheels){
			wheel.update(powers[i], timeStep);
			i++;
		}
	}
	
	/**
	 * @return the position of the robot
	 */
	public Vec2 getPos(){
		return this.body.getPosition();
	}
	
	/**
	 * @return the angle of the robot
	 */
	public double getAngle(){
		return this.body.getAngle();
	}
	
	/**
	 * @return a path2d representation of the robot
	 */
	public Path2D getPath(){
		return this.drawingPath;
	}
	
	/**
	 * @return the body of the robot
	 */
	public Body getBody(){
		return this.body;
	}
	
	public void setPowers(float[] powers){
		this.powers = powers;
	}
	
}
