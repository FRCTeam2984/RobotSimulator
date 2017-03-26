package io.github.frcteam2984.simulator.world;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.Timer;

/**
 * the tick class for the game. Every tick the world gets simulated
 * @author max
 *
 */
public class Tick extends Observable implements ActionListener{

	public static final int DEFAULT_DELAY = 1000/60;
	
	/**
	 * the timer used for the time interval
	 */
	private Timer timer;
	
	/**
	 * the world that gets simulated
	 */
	private SimulationWorld world;
	
	/**
	 * the constructor to create a new tick
	 * @param delay the delay between ticks
	 * @param world the world to update
	 */
	public Tick(SimulationWorld world){
		this.world = world;
		timer = new Timer(DEFAULT_DELAY, this);
		timer.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.world.simulate();
	}
	
	public void setStopped(boolean stopped){
		if(stopped){
			this.stop();
		} else {
			this.start();
		}
	}
	
	public void start(){
		this.timer.start();
		this.setChanged();
		this.notifyObservers();
	}
	
	public void stop(){
		this.timer.stop();
		this.setChanged();
		this.notifyObservers();
	}

}