package io.github.frcteam2984.simulator.ui;

import javax.swing.JFrame;

import io.github.frcteam2984.simulator.world.SimulationWorld;

/**
 * The frame to house all of the UI for the simulator
 * @author Max Apodaca
 *
 */
public class SimulatorFrame extends JFrame {

	private static final long serialVersionUID = 9043973739376592648L;

	/**
	 * The pane which will render the world
	 */
	private SimulationPane simulationPane;
	
	public SimulatorFrame(SimulationWorld world){
		
		this.setTitle("Team 2984's FRC Simulator");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(1400, 720);
		
		this.simulationPane = new SimulationPane();
		this.addKeyListener(this.simulationPane);
		world.addObserver(this.simulationPane);
		
		this.add(this.simulationPane);
		
		this.setVisible(true);
		this.setLocationRelativeTo(null); 
		
	}
	
}
