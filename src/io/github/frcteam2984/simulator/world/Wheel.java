package io.github.frcteam2984.simulator.world;

import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.json.JSONObject;

import io.github.frcteam2984.simulator.SensorDiscriptor;

/**
 * A wheel object which obeys simplified laws of physics
 * @author Max Apodaca
 *
 */
public class Wheel {
	/**
	 * The two bodies used by the wheel, one of the robot and one of itself
	 */
	private Body body;
	private Robot robot;
	
	private SensorDiscriptor sensor;
	private double ticksPerRev;
	
	private float lastX, lastY;

	/**
	 * the local position of the  wheel
	 */
    private float x;
    private float y;
    
    /**
     * intrinsic wheel parameters
     */
    private float width;
    private float diameter;
    
    private MotorPreformanceCurve maxTorque;
    private float transmissionRatio;
	private float freeSpinTorque;
    
    private float muStatic;
    private float muKenetic;
    
    /**
     * the path used to draw the wheel with respect to the robot
     */
	private Path2D drawingPath;
    
    public Wheel(JSONObject obj, Robot robot, World world) {
    	this.x = (float) obj.getDouble("x");
    	this.y = (float) obj.getDouble("y");
    	
    	width = (float) obj.getDouble("width");
    	diameter = (float) obj.getDouble("diameter");
    	
    	this.maxTorque = new MotorPreformanceCurve(obj.getJSONObject("torque"));
    	this.transmissionRatio = (float) obj.getDouble("transmissionRatio");
    	this.freeSpinTorque = (float) obj.getDouble("freeSpinTroque");
    	this.muStatic = (float) obj.getDouble("muStatic");
    	this.muKenetic = (float) obj.getDouble("muKenetic");
    	
    	this.robot = robot;
    	
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = robot.getBody().getWorldPoint(new Vec2(x, y));
        bodyDef.angle = robot.getBody().getAngle();

        FixtureDef fixture = new FixtureDef();
        fixture.density = this.robot.getBody().getMass()/(this.width*this.diameter)/4;
        fixture.isSensor = true;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, diameter / 2);
        fixture.shape = shape;

        body = world.createBody(bodyDef);
        body.createFixture(fixture);
        PrismaticJointDef joint = new PrismaticJointDef();
        joint.initialize(robot.getBody(), body, body.getWorldCenter(), new Vec2(1, 0));
        joint.enableLimit = true;
        joint.lowerTranslation = 0;
        joint.upperTranslation = 0;
        world.createJoint(joint);
        
		this.drawingPath = PolygonUtils.getPathFromJson(obj.getJSONArray("drawing"));
		AffineTransform transform = new AffineTransform();
		transform.translate(this.x, this.y);
		this.drawingPath.transform(transform);
		
		if(obj.has("sensor")){
			JSONObject sensor = obj.getJSONObject("sensor");
			SensorDiscriptor.SensorType sensorType = SensorDiscriptor.SensorType.valueOf(sensor.getString("type"));
			SensorDiscriptor.SensorLocation sensorLocation = SensorDiscriptor.SensorLocation.valueOf(sensor.getString("location"));
			this.sensor = new SensorDiscriptor(sensorType, sensorLocation, 0, "Wheel Sensor");
			this.ticksPerRev = sensor.getDouble("ticksPerRev");
			this.lastX = Float.NaN;
			this.lastY = Float.NaN;
		}
    }
    
    public SensorDiscriptor getSensor(){
    	return this.sensor;
    }
    
    /**
     * Applies the forces which the wheel would be exerting on the body. The equations are as follows:
     * <ul>
     * <li>V<sub>x</sub> - the velocity of the ground in the x Direction</li>
     * <li>V<sub>y</sub> - the velocity of the ground in the y Direction</li>
     * <li>m  - the mass approximately supported by the wheel</li>
     * <li>T - the torque exerted by the motor</li>
     * <li>Δt - the timestep</li>
     * <li>r - the radius of the wheel</li>
     * </ul>
     * F<sub>t</sub> = m * V<sub>x</sub> / Δt + T / r<br>
     * if ||F<sub>t</sub>|| > m * g * μ<sub>s</sub> then <br>
     * F<sub>t</sub> = F̂<sub>t</sub> * m * g * μ<sub>k</sub> <br>
     * this force then gets applied at the center of the wheel to the robot.
     * @param power the percent power [-1, 1] to apply from the motor
     * @param timeStep the amount of time applied
     */
    public void update(float power, float timeStep){
    	if(this.sensor != null){
    		if(Float.isNaN(this.lastX)){
    			this.lastX = this.body.getPosition().x;
        		this.lastY = this.body.getPosition().y;
    		}
    		float dx = this.body.getPosition().x - this.lastX;
    		float dy = this.body.getPosition().y - this.lastY;
    		this.lastX = this.body.getPosition().x;
    		this.lastY = this.body.getPosition().y;
    		double angle = 0;
    		if(dx == 0){
    			angle = Math.PI/2;
    			if(dy < 0){
    				angle += Math.PI;
    			}
    		} else {
    			angle = Math.atan(dy / dx);
    			if(dx < 0){
    				angle += Math.PI;
    			}
    		}
    		double ticksForward = -dx * Math.cos(angle) + dy * Math.sin(angle);
    		ticksForward *= this.ticksPerRev/this.diameter/Math.PI;
    		this.sensor.setValue(this.sensor.getAnalogValue() + ticksForward);
    	}
    	double angle = -this.body.getAngle();
    	Vec2 vel = this.body.getLinearVelocity();
    	float mass = this.robot.getBody().getMass();
    	float sidewaysVel = (float) -(vel.x * Math.cos(angle) - vel.y * Math.sin(angle));
    	float forwardVel = (float) -(vel.x * Math.sin(angle) + vel.y * Math.cos(angle));
    	double rpm = forwardVel / Math.PI / this.diameter / this.transmissionRatio;
    	Vec2 fT = new Vec2(0, power * (float)this.maxTorque.getTroque(rpm) / this.diameter * 2);
        fT = fT.add(new Vec2(0, Math.copySign(Math.min(Math.abs(forwardVel)/100, 1), -forwardVel) * this.freeSpinTorque / this.diameter * 2));

    	Vec2 fVel = new Vec2(mass / 4 * sidewaysVel / timeStep, 0);
    	if(fVel.length() > mass / 4 * Constants.g * this.muStatic){
    		fVel = fVel.mul(mass / 4 * Constants.g * this.muKenetic / fVel.length());
    	}
    	Vec2 fTotal = fT.add(fVel);
    	Vec2 forceRotated = new Vec2((float)(fTotal.x * Math.cos(angle) - fTotal.y * Math.sin(angle)),
    			(float) -(fTotal.x * Math.sin(angle) + fTotal.y * Math.cos(angle)));
    	robot.getBody().applyForce(forceRotated, this.body.getWorldPoint(new Vec2(0,0)));
    }
    
    /**
	 * @return a path2d representation of the robot
	 */
	public Path2D getPath(){
		return this.drawingPath;
	}
}
