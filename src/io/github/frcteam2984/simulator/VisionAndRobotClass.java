package io.github.frcteam2984.simulator;

import edu.wpi.first.wpilibj.IterativeRobot;
import io.github.frcteam2984.simulator.vision.RobotWorldAcceptor;

public class VisionAndRobotClass {

	private Class<? extends RobotWorldAcceptor>[] robotWorldAcceptors;
	private Class<? extends IterativeRobot> robotMain;
	
	public VisionAndRobotClass(Class<? extends IterativeRobot> robotMain, Class<? extends RobotWorldAcceptor>[] robotWorldAcceptors){
		this.robotWorldAcceptors = robotWorldAcceptors;
		this.robotMain = robotMain;
	}

	/**
	 * @return the robotWorldAcceptors
	 */
	public Class<? extends RobotWorldAcceptor>[] getRobotWorldAcceptors() {
		return robotWorldAcceptors;
	}

	/**
	 * @return the robotMain
	 */
	public Class<? extends IterativeRobot> getRobotMain() {
		return robotMain;
	}
	
}
