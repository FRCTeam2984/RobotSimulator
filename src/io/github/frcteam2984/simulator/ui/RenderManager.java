package io.github.frcteam2984.simulator.ui;

import java.util.HashMap;
import java.util.Map;

import io.github.frcteam2984.simulator.world.Entity;

/**
 * a class to store the handler for the rendering of the entities that are to be rendered on the screen
 * this class is a singleton and should be instantiated with {@link #getInstance() getInstance()}
 * @author Max Apodaca
 *
 */
public class RenderManager {

	/**
	 * the singleton render manager object
	 */
	private static RenderManager manager;
	
	/**
	 * the handlers that are registered to each of the classes of the entities
	 */
	private Map<Class<? extends Entity>,RenderHandler<?>> handlers;
	
	/**
	 * the default handler to be used if no explicit handler is provided for the class
	 */
	private RenderHandler<Entity> defaultHandler;
	
	/**
	 * The default constructor for the render handler. It registers all of the handlers for the rendering of the entities onto the screen
	 */
	private RenderManager(){
		this.handlers = new HashMap<Class<? extends Entity>, RenderHandler<?>>();
		this.defaultHandler = new RenderHandler<Entity>(Entity.class);
		
	}
	
	/**
	 * registers a handler for the manager to use. The generic type of the handler and the Class must be the same
	 * @param clazz the class of the entities to register the handler on
	 * @param handler the handler that should be used to render the entity
	 */
	private void registerHandler(RenderHandler<?> handler, Class<? extends Entity> clazz){
		if(!this.handlers.containsKey(clazz) && clazz.isAssignableFrom(handler.getType()))
			this.handlers.put(clazz, handler);
	}
	
	/**
	 * an overload of {@link #getHandler(Class) getHandler}
	 * @param entity the entity to find the handler for
	 * @return the handler for the given entity
	 */
	public RenderHandler<?> getHandler(Entity entity){
		return this.getHandler(entity.getClass());
	}
	
	/**
	 * returns the handler of the given entity class
	 * @param entityClass the class to check get the handler for
	 * @return returns the handler for the given entity
	 */
	public RenderHandler<?> getHandler(Class<? extends Entity> entityClass){
		if(this.handlers.containsKey(entityClass)){
			return this.handlers.get(entityClass);
		}
		return this.defaultHandler;
	}
	
	/**
	 * @return the instance of the singleton render manager
	 */
	public static RenderManager getInstance(){
		if(manager != null)
			return manager;
		return manager = new RenderManager();
	}
	
}
