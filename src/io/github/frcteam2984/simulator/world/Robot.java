package io.github.frcteam2984.simulator.world;

import java.awt.geom.Path2D;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.json.JSONObject;

/**
 * A class representative of the robot
 * @author Maximilian Apodaca
 *
 */
public class Robot{

	private Body body;
	
	private Path2D drawingPath;
	
	/**
	 * Creates a robot in the given world based on the JSON parameters
	 * @param world the world to be created in
	 * @param obj the json object containing various parameters
	 */
	public Robot(World world, JSONObject obj){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(0, 37);
		bodyDef.linearVelocity.set((float)(200*Math.random()), (float)(10*Math.random()));
		
		PolygonShape shape = PolygonUtils.getPolygonFromJson(obj.getJSONArray("colliding"));
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 100;
		fixtureDef.friction = 0.3f;
		
		this.body = world.createBody(bodyDef);
		this.body.createFixture(fixtureDef);
		
		this.drawingPath = PolygonUtils.getPathFromJson(obj.getJSONArray("drawing"));
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
	
}
