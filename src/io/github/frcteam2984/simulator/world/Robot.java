package io.github.frcteam2984.simulator.world;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.json.JSONArray;
import org.json.JSONObject;

import io.github.frcteam2984.simulator.HardwareAdaptor;
import io.github.frcteam2984.simulator.MotorControllerDiscriptor;
import io.github.frcteam2984.simulator.SensorDiscriptor;

/**
 * A class representative of the robot
 * @author Max Apodaca
 *
 */
public class Robot implements Observer{

	private Body body;
	
	private Path2D drawingPath;
	
	private MotorControllerDiscriptor.MotorControllerType controllerType;
	
	private Map<Integer, Wheel> wheels;
	private Map<Integer, Arm> arms;
	private List<MotorControllerDiscriptor> controllers;
	
	private SensorDiscriptor gyroPos;
	private SensorDiscriptor gyroVel;
	
	/**
	 * Creates a robot in the given world based on the JSON parameters
	 * @param world the world to be created in
	 * @param obj the json object containing various parameters
	 */
	public Robot(World world, JSONObject obj){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set((float)obj.getDouble("x"), (float)obj.getDouble("y"));
		bodyDef.angle = (float) (-Math.PI/2);
//		bodyDef.linearVelocity.set(200, 0);
//		bodyDef.angle = (float) (Math.PI/4 + Math.PI);
//		bodyDef.angularVelocity = 1;
		
		PolygonShape shape = PolygonUtils.getPolygonFromJson(obj.getJSONArray("colliding"));
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.001f;
		fixtureDef.friction = 0.3f;
		
		this.body = world.createBody(bodyDef);
		this.body.createFixture(fixtureDef);
		
		this.controllers = new ArrayList<MotorControllerDiscriptor>();
		
		String controllerType = obj.getString("controllerType");
		this.controllerType = MotorControllerDiscriptor.MotorControllerType.valueOf(controllerType);
		
		JSONArray wheels = obj.getJSONArray("wheels");
		this.wheels = new HashMap<Integer, Wheel>();
		
		for(int i = 0; i<wheels.length(); i++){
			Wheel wheel = new Wheel(wheels.getJSONObject(i), this, world);
			this.wheels.put(wheels.getJSONObject(i).getInt("controllerID"), wheel);
		}
		
		JSONArray arms = obj.getJSONArray("arms");
		this.arms = new HashMap<Integer, Arm>();
		
		for(int i = 0; i<arms.length(); i++){
			Arm arm = new Arm(arms.getJSONObject(i));
			this.arms.put(wheels.getJSONObject(i).getInt("controllerID"), arm);
		}
		
		this.drawingPath = PolygonUtils.getPathFromJson(obj.getJSONArray("drawing"));
		
		for(Wheel wheel : this.wheels.values()){
			this.drawingPath.append(wheel.getPath(), false);
		}
		
		HardwareAdaptor.getInstance().addObserver(this);
	}
	
	
	
	/**
	 * causes the wheels and friction to be applied and the sensor to be updated
	 * @param timeStep the timestep over which to apply the function
	 */
	public void update(float timeStep){
		if(this.gyroPos != null){
			this.gyroPos.setValue(Math.toDegrees(this.body.getAngle()));
		}
		if(this.gyroVel != null){
			this.gyroVel.setValue(Math.toDegrees(this.body.getAngularVelocity()));
		}
		for(MotorControllerDiscriptor controller : this.controllers){
			this.wheels.get(controller.getId()).update((float) controller.getPower(), timeStep);
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
	

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg0.getClass().isAssignableFrom(HardwareAdaptor.class) && arg1 != null && arg1.getClass().isAssignableFrom(MotorControllerDiscriptor.class)){
			MotorControllerDiscriptor controller = (MotorControllerDiscriptor) arg1;
			if(controller.getType().equals(this.controllerType) && this.wheels.keySet().contains(controller.getId())){
				this.controllers.add(controller);
				SensorDiscriptor sensor = this.wheels.get(controller.getId()).getSensor();
				if(sensor != null){
					controller.addSensor(sensor);
				}
			}
		}
		if(arg0.getClass().isAssignableFrom(HardwareAdaptor.class) && arg1 != null && arg1.getClass().isAssignableFrom(SensorDiscriptor.class)){
			if(((SensorDiscriptor)arg1).getLocation() == SensorDiscriptor.SensorLocation.SPI &&
					((SensorDiscriptor)arg1).getId() == 0){
				this.gyroPos = (SensorDiscriptor) arg1;
			} else if(((SensorDiscriptor)arg1).getLocation() == SensorDiscriptor.SensorLocation.SPI &&
					((SensorDiscriptor)arg1).getId() == 1){
				this.gyroVel = (SensorDiscriptor) arg1;
			}
		}
	}
	
}
