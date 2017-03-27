package io.github.frcteam2984.simulator.world;

import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.common.Vec3;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.json.JSONObject;

/**
 * A wheel object which obeys simplified laws of physics
 * @author Max Apodaca
 *
 */
public class Wheel {
	private Body body;
	private Robot robot;

    private float x;
    private float y;
    
    private float width;
    private float diameter;
    
    private float maxTorque;
    
    private float muStatic;
    private float muKenetic;
    
	private Path2D drawingPath;
    
    public Wheel(JSONObject obj, Robot robot, World world) {
    	this.x = (float) obj.getDouble("x");
    	this.y = (float) obj.getDouble("y");
    	
    	width = (float) obj.getDouble("width");
    	diameter = (float) obj.getDouble("diameter");
    	
    	this.maxTorque = (float) obj.getDouble("torque");
    	this.muStatic = (float) obj.getDouble("muStatic");
    	this.muKenetic = (float) obj.getDouble("muKenetic");
    	
    	this.robot = robot;
    	
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = robot.getBody().getWorldPoint(new Vec2(x, y));
        bodyDef.angle = robot.getBody().getAngle();

        FixtureDef fixture = new FixtureDef();
        fixture.density = 1.0f;
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

    }


    public void update(float power, float timeStep){
    	double angle = -this.body.getAngle();
    	Vec2 vel = this.body.getLinearVelocity();
    	float mass = this.robot.getBody().getMass();
    	float sidewaysVel = (float) -(vel.x * Math.cos(angle) - vel.y * Math.sin(angle));
    	Vec2 fT = new Vec2(0, power * this.maxTorque / this.diameter * 2);
    	Vec2 fVel = new Vec2(mass / 4 * sidewaysVel / timeStep, 0);
    	Vec2 fTotal = fT.add(fVel);
    	if(fTotal.length() > mass / 4 * Constants.g * this.muStatic){
    		fTotal = fTotal.mul(mass / 4 * Constants.g * this.muKenetic / fTotal.length());
    	}
    	Vec2 forceRotated = new Vec2((float)(fTotal.x * Math.cos(angle) - fTotal.y * Math.sin(angle)),
    			(float) -(fTotal.x * Math.sin(angle) + fTotal.y * Math.cos(angle)));
    	robot.getBody().applyForce(forceRotated, this.body.getWorldPoint(new Vec2(0,0)));
    	System.out.println(fTotal);
    }
    
    /**
	 * @return a path2d representation of the robot
	 */
	public Path2D getPath(){
		return this.drawingPath;
	}
}
