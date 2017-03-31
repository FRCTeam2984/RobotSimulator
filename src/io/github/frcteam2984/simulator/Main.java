package io.github.frcteam2984.simulator;

import java.io.File;
import java.util.Scanner;

import org.json.JSONObject;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import io.github.frcteam2984.codeloader.JarClassLoader;
import io.github.frcteam2984.simulator.ui.DriveStartionFrame;
import io.github.frcteam2984.simulator.ui.SimulatorFrame;
import io.github.frcteam2984.simulator.vision.RobotPosition;
import io.github.frcteam2984.simulator.vision.RobotWorldAcceptor;
import io.github.frcteam2984.simulator.world.SimulationWorld;
import io.github.frcteam2984.simulator.world.Tick;

public class Main {

	public static void main(String[] args){
		System.out.println("Starting 2D FRC Simulator");
		
		String worldFile = "DemoWorld.json";
		String jarFile = "RobotCode.jar";
		String robotMain = "org.usfirst.frc.team2984.robot.Robot";
		String visionAugment = "VisionAugment.jar";
		String[] visionAugmentClasses = new String[]{"org.usfirst.frc.team2984.robot.util.VisionTracker"};
		
		if(args.length >= 3){
			worldFile = args[0];
			jarFile = args[1];
			robotMain = args[2];
		}
		
		WPIManager.setupDummyObjects();
		String content = "";
		try{
			Scanner scanner = new Scanner(new File(worldFile));
			content = scanner.useDelimiter("\\Z").next();
			scanner.close();
		} catch(Exception e){}
		SimulationWorld world = new SimulationWorld(new JSONObject(content));
		Tick tick = new Tick(world);
		tick.start();
		SimulatorFrame frame = new SimulatorFrame(world);
		DriverStation driverStation = DriverStation.getInstance();
		DriveStartionFrame ds = new DriveStartionFrame(driverStation);
		
		VisionAndRobotClass visionAndRobotClass = JarClassLoader.testComboLoad(new File(jarFile), new File(visionAugment), robotMain, visionAugmentClasses);
		Class<? extends RobotWorldAcceptor>[] visionAugments = visionAndRobotClass.getRobotWorldAcceptors();
		Class<? extends IterativeRobot> robot = visionAndRobotClass.getRobotMain();
		for(Class<? extends RobotWorldAcceptor> visionAugmentClass : visionAugments){
			try {
				RobotWorldAcceptor rwAcceptor = visionAugmentClass.newInstance();
				rwAcceptor.reciveWorld(world);
				rwAcceptor.robotPositionUpdate(new RobotPosition(0,0,0));
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		
		
		try {
			RobotTimer timer = new RobotTimer(robot.newInstance(), DriverStation.getInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		DriverStation.getInstance().update();
	}
	
}
