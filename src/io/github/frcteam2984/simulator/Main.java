package io.github.frcteam2984.simulator;

import java.io.File;
import java.util.Scanner;

import org.json.JSONObject;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import io.github.frcteam2984.codeloader.JarClassLoader;
import io.github.frcteam2984.simulator.ui.DriveStartionFrame;
import io.github.frcteam2984.simulator.ui.SimulatorFrame;
import io.github.frcteam2984.simulator.world.SimulationWorld;
import io.github.frcteam2984.simulator.world.Tick;

public class Main {

	public static void main(String[] args){
		System.out.println("Starting 2D FRC Simulator");
		WPIManager.setupDummyObjects();
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
		DriverStation driverStation = DriverStation.getInstance();
		DriveStartionFrame ds = new DriveStartionFrame(driverStation);
		Class<? extends IterativeRobot> robot = JarClassLoader.loadRobotMain(new File("RobotCode.jar"), "org.usfirst.frc.team2984.robot.Robot");
		try {
			RobotTimer timer = new RobotTimer(robot.newInstance(), DriverStation.getInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
}
