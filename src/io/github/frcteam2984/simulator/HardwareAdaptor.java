package io.github.frcteam2984.simulator;

import java.util.Observable;

/**
 * An adaptor to sit as a link between the HAL written by WPI and the robot object.
 * The robot is an observer to the HardwareAdaptor and gets updates while the motor controllers and other WPI classes manipulate the HardwareAdaptor's state.
 * @author Max Apodaca
 *
 */
public class HardwareAdaptor extends Observable{

	private static HardwareAdaptor instance;
	
	private MotorControllerDiscriptor[] cANMotorOutputs;
	
	private HardwareAdaptor(){
		this.cANMotorOutputs = new MotorControllerDiscriptor[256];
	}
	
	/**
	 * Adds the motor controller description and notifies the observers
	 * @param controller the new controller dissipation
	 */
	public void addMotorController(MotorControllerDiscriptor controller){
		System.out.println(controller.toString());
		switch(controller.getType()){
		case CAN:
			this.cANMotorOutputs[controller.getId()] = controller;
			this.setChanged();
			this.notifyObservers(controller);
			break;
		default:
			System.out.println("Unkown motor controller type" + controller.getType());
		}
	}
	
	/**
	 * @param id the ID of the controller
	 * @return the motor controller description of the given id
	 */
	public MotorControllerDiscriptor getCANController(int id){
		throwIfNotInRange(id, 0, 255);
		return this.cANMotorOutputs[id];
	}
	
	private void throwIfNotInRange(int number, int low, int high){
		if(number < low || number > high){
			throw new IllegalArgumentException(number + " is not in the range ["  + low + "," + high + "]");
		}
	}
	
	/**
	 * @return the global hardware adaptor object
	 */
	public static HardwareAdaptor getInstance(){
		if(instance == null){
			instance = new HardwareAdaptor();
		}
		return instance;
	}
	
}
