package io.github.frcteam2984.simulator.ui;

import java.awt.Graphics;

import io.github.frcteam2984.simulator.world.Entity;

/**
 * the default render handler that draws a circle at the position of the entity
 * @author Max Apodaca
 *
 * @param <E> the entity's type that will be draw
 */
public class RenderHandler<E extends Entity> {

	private Class<E> type;
	
	public RenderHandler(Class<E> type){
		this.type = type;
	}
	
	/**
	 * Calls the render method with the Correct Entity Type
	 * @param g the graphics context to draw on
	 * @param entity the entity to draw
	 */
	@SuppressWarnings("unchecked")
	public final void renderManaged(Graphics g, Entity entity){
		if(this.getType().isAssignableFrom(entity.getClass()))
			this.render(g, (E) entity);
	}
	
	/**
	 * draws the entity on the graphics context
	 * @param g the graphics object that should be drawn on
	 * @param entity the entity that should be drawn
	 */
	public void render(Graphics g, E entity){
	}
	
	/**
	 * @return the type of the entity that this handler can render
	 */
	public Class<E> getType(){
		return this.type;
	}
	
}
