package io.github.frcteam2984.simulator.world;

import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

/**
 * A class for easy transportation of a BodyDef and FixtureDef
 * and can be used to create a body with a single fixture.
 * @author Max Apodaca
 *
 */
public class CompleteBodyDefinition {

	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	
	/**
	 * Creates a new CompleteBodyDefinition based on the body and fixture definitions
	 * @param bodyDef the body definition of the body
	 * @param fixtureDef the fixture definition of the body
	 */
	public CompleteBodyDefinition(BodyDef bodyDef, FixtureDef fixtureDef){
		this.bodyDef = bodyDef;
		this.fixtureDef = fixtureDef;
	}

	/**
	 * @return the bodyDef
	 */
	public BodyDef getBodyDef() {
		return bodyDef;
	}

	/**
	 * @return the fixtureDef
	 */
	public FixtureDef getFixtureDef() {
		return fixtureDef;
	}
	
}
