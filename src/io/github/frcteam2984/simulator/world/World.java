package io.github.frcteam2984.simulator.world;

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
	 * The field which the world contains
	 */
	private Field field;
	
	/**
	 * Constructs a new empty world
	 */
	public World(){
		this.entities = new ArrayList<Entity>();
		this.field = new Field();
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
	 * returns a list of all entities in the world, this includes the robot but not the fixed field.
	 * @return all the entities in the world
	 */
	public List<Entity> getEntities() {
		return this.entities;
	}

	/**
	 * Adds an entity to the world
	 * @param entity the entity to add
	 */
	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}

	/**
	 * Simulates the world
	 */
	public void simulate() {
		this.setChanged();
		this.notifyObservers();		
	}
	
}
