package io.github.frcteam2984.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * The simulation world which contains the robot, the field, and all entities.
 * Entities are considered field interactive objects such as gears and fuel.
 * @author Max Apodaca
 *
 */
public class World extends Observable {

	/**
	 * A list of the non-static entities.
	 */
	private List<Entity> entities;
	
	/**
	 * Constructs a new empty world
	 */
	public World(){
		this.entities = new ArrayList<Entity>();
	}

	/**
	 * returns the width of the world in inches
	 * @return the width of the world in inches
	 */
	public int getWidth() {
		return 0;
	}

	/**
	 * returns the height of the world in inches
	 * @return the height of the world in inches
	 */
	public double getHeight() {
		return 0;
	}

	/**
	 * returns a list of all entities in the world, this includes the robot but not the fixed field.
	 * @return all the entities in the world
	 */
	public List<Entity> getEntities() {
		return this.entities;
	}
	
}
