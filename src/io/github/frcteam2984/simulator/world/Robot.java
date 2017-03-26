package io.github.frcteam2984.simulator.world;

import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.json.JSONObject;

public class Robot{

	public Robot(World world, JSONObject obj){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.setZero();
		bodyDef.linearVelocity.set(10, 0);
	}
	
}
