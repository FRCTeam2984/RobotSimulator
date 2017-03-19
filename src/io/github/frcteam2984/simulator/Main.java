package io.github.frcteam2984.simulator;

import io.github.frcteam2984.simulator.ui.SimulatorFrame;
import io.github.frcteam2984.simulator.world.Tick;
import io.github.frcteam2984.simulator.world.World;

public class Main {

	public static void main(String[] args){
		System.out.println("Starting 2D FRC Simulator");
		World world = new World();
		Tick tick = new Tick(world);
		tick.start();
		SimulatorFrame frame = new SimulatorFrame(world);
	}
	
}
