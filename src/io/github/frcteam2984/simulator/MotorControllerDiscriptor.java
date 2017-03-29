package io.github.frcteam2984.simulator;

import java.util.Observable;

/**
 * A description of a motor controller. Sensors hooked up, power, encoder state, and other information.
 * @author Max Apodaca
 *
 */
public class MotorControllerDiscriptor extends Observable{

	/**
	 * The type of motor controller
	 * @author Max Apodaca
	 *
	 */
	public static enum MotorControllerType {
		CAN,
		PWM
	}
	
	private MotorControllerType type;
	private int id;
	
	
	
	/**
	 * Crates a new motor controller descriptor
	 * @param type The type of motor controller, PWM or CAN
	 * @param id the id of the motor controller
	 */
	public MotorControllerDiscriptor(MotorControllerType type, int id){
		this.type = type;
		this.id = id;
	}
	
}
