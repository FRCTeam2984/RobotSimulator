package io.github.frcteam2984.simulator.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import io.github.frcteam2984.simulator.world.Entity;
import io.github.frcteam2984.simulator.world.World;

/**
 * A panel to do the rendering of the field and its elements
 * @author Max Apodaca
 *
 */
public class SimulationPane extends JPanel implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9205063175488138762L;

	/**
	 * a local instance of the world
	 */
	private World world;

	/**
	 * the scale, x, and y
	 */
	private double scale, x, y;

	/**
	 * the constructor of the pane that gets a singleton of the {@link io.github.frcteam2984.simulator.ui.RenderManager RenderManager}
	 * class using the {@link io.github.frcteam2984.simulator.ui.RenderManager#getInstance() getInstance} method
	 */
	public SimulationPane(){
		this.setBackground(new Color(0xFFFFFF));
		this.scale = 1;
		this.x = 0;
		this.y = 0;
	}

	/**
	 * constructor to use when you don't want a 1inch to 2pixle scale
	 * @param scale the scale to start out with
	 */
	public SimulationPane(double scale){
		this();
		this.scale = scale;
	}

	/**
	 * calls the appropriate {@link io.github.frcteam2984.simulator.ui.RenderHandler RenderHandlers} to draw each element in the world
	 */
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(world == null)
			return;
		double width = this.getWidth();
		double height = this.getHeight();
		((Graphics2D)g).translate(width/2, height/2);
		for(Shape shape : world.getField().getDrawingPaths()){
			((Graphics2D)g).draw(shape);
		}
		for(Entity entity : world.getEntities()){
		}
	}

	/**
	 * sets the scale of the world
	 * @param scale the new scale
	 */
	public void setScale(double scale){
		this.scale = scale;
	}

	/**
	 * sets the x and y of the view
	 * @param x the x
	 * @param y the y
	 */
	public void setXandY(double x, double y){
		this.x = x;
		this.y = y;
	}

	/**
	 * Called when the world is updated and repaints the screen
	 */
	@Override
	public void update(Observable world, Object message) {
		this.world = (World) world;
		repaint();
	}

}
