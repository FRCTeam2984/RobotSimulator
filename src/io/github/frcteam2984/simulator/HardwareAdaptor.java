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
	
	private double[] cANMotorOutputs;
	
	private HardwareAdaptor(){
		this.cANMotorOutputs = new double[256];
	}
	
	/**
	 * Sets the power of the given motor controller to the given power
	 * @param id id of motor controller to change
	 * @param power the power [-1, 1] which is outputted
	 */
	public void setCANPower(int id, double power){
		throwIfNotInRange(id, 0, 255);
		this.cANMotorOutputs[id] = power;
		update();
	}
	
	/**
	 * Returns the current power of the 
	 * @param id
	 * @return
	 */
	public double getCANMotorPower(int id){
		throwIfNotInRange(id, 0, 255);
		return this.cANMotorOutputs[id];
	}
	
	private void throwIfNotInRange(int number, int low, int high){
		if(number < low || number > high){
			throw new IllegalArgumentException(number + " is not in the range ["  + low + "," + high + "]");
		}
	}
	
	private void update(){
		this.setChanged();
		this.notifyObservers();
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
