package io.github.frcteam2984.simulator.world;

import java.util.List;
import java.util.Observable;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.json.JSONObject;

import io.github.frcteam2984.simulator.vision.Target;
import io.github.frcteam2984.simulator.vision.VisionWorld;

/**
 * The simulation world which contains the robot, the field, and all entities.
 * Entities are considered field interactive objects such as gears and fuel.
 * @author Max Apodaca
 *
 */
public class SimulationWorld extends Observable implements VisionWorld {

	private static final float TIME_STEP = 1/60f;
	
	/**
	 * The JBox2d world
	 */
	private World world;
	
	/**
	 * The field which the world contains
	 */
	private Field field;
	
	/**
	 * The robot which is currently active
	 */
	private Robot robot;
	
	/**
	 * Constructs a new world based on the json object
	 */
	public SimulationWorld(JSONObject json){
		this.world = new World(new Vec2(0,0), false);
		
		this.field = new Field(json.getJSONObject("field"));
		for(CompleteBodyDefinition def : this.field.getCollisionShapes()){
			this.addBody(def);
		}
		this.robot = new Robot(world, json.getJSONObject("robot"));
	}
	
	/**
	 * returns the width of the world in inches
	 * @return the width of the world in inches
	 */
	public double getWidth() {
		return this.field.getWidth();
	}

	/**
	 * returns the length of the world in inches
	 * @return the length of the world in inches
	 */
	public double getLength() {
		return this.field.getLength();
	}

	/**
	 * Adds a body to the world based on the body definition
	 * @param bodyDef the body definition to use
	 * @param fixtrueDef the feature definition of the body
	 * @return the body created
	 */
	public Body addBody(BodyDef bodyDef, FixtureDef fixtrueDef) {
		Body body = this.world.createBody(bodyDef);
		body.createFixture(fixtrueDef);
		return body;
	}
	
	/**
	 * Adds the following body to the world
	 * @param bodyDef the body definition to use
	 * @return the body which was created
	 */
	public Body addBody(CompleteBodyDefinition bodyDef){
		return this.addBody(bodyDef.getBodyDef(), bodyDef.getFixtureDef());
	}
	
	/**
	 * Returns the field of the world
	 * @return the field
	 */
	public Field getField(){
		return this.field;
	}
	
	/**
	 * Returns the robot which is active
	 * @return the robot
	 */
	public Robot getRobot(){
		return this.robot;
	}

	/**
	 * Simulates the world
	 */
	public void simulate() {
		world.step(TIME_STEP, 6, 2);
		this.robot.update(TIME_STEP);
		this.setChanged();
		this.notifyObservers();		
	}

	@Override
	public List<Target> getTargets() {
		return null;
	}
	
}
