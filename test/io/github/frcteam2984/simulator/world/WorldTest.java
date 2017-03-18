package io.github.frcteam2984.simulator.world;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * A test to test the World class
 * @author Max Apodaca
 *
 */
public class WorldTest {

	private World world;
	
	@Before
	public void before(){
		this.world = new World();
	}
	
	@Test
	public void addEntityAddsTheEntity(){
		this.world.addEntity(new Entity());
		assertEquals(this.world.getEntities().size(), 1);
	}
	
}
