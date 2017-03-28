package io.github.frcteam2984.simulator;

import java.io.File;
import java.util.Scanner;

import org.json.JSONObject;

import io.github.frcteam2984.simulator.ui.DriveStartionFrame;
import io.github.frcteam2984.simulator.ui.SimulatorFrame;
import io.github.frcteam2984.simulator.world.SimulationWorld;
import io.github.frcteam2984.simulator.world.Tick;

public class Main {

	public static void main(String[] args){
		System.out.println("Starting 2D FRC Simulator");
		String content = "";
		try{
			Scanner scanner = new Scanner(new File("DemoWorld.json"));
			content = scanner.useDelimiter("\\Z").next();
			scanner.close();
		} catch(Exception e){}
		SimulationWorld world = new SimulationWorld(new JSONObject(content));
		Tick tick = new Tick(world);
		tick.start();
		SimulatorFrame frame = new SimulatorFrame(world);
		DriveStartionFrame ds = new DriveStartionFrame();
	}
	
}
