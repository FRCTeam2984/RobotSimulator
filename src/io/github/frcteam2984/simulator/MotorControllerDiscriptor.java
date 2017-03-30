package io.github.frcteam2984.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * A description of a motor controller. Sensors hooked up, power, encoder state, and other information.
 * @author Max Apodaca
 *
 */
public class MotorControllerDiscriptor extends Observable implements Observer{

	/**
	 * The type of motor controller
	 * @author Max Apodaca
	 *
	 */
	public static enum MotorControllerType {
		CAN,
		PWM
	}
	
	/**
	 * The types of updates send by the motor controller
	 * @author Max Apodaca
	 *
	 */
	public static enum MotorControllerEvent {
		POWER,
		BREAK_COAST_MODE,
		DISABLE_ENABLE
	}
	
	private MotorControllerType type;
	private int id;
	private List<SensorDiscriptor> sensors;
	private double power;
	private boolean coast;
	private boolean disabled;
	
	/**
	 * Crates a new motor controller descriptor
	 * @param type The type of motor controller, PWM or CAN
	 * @param id the id of the motor controller
	 */
	public MotorControllerDiscriptor(MotorControllerType type, int id){
		this.type = type;
		this.id = id;
		this.sensors = new ArrayList<SensorDiscriptor>();
		this.power = 0;
		this.coast = false;
		this.disabled = true;
	}
	
	/**
	 * Adds a sensor to the motor controller description.
	 * @param sensor the sensor to add
	 */
	public void addSensor(SensorDiscriptor sensor){
		this.sensors.add(sensor);
		sensor.addObserver(this);
		this.setChanged();
		this.notifyObservers(sensor);
	}
	
	/**
	 * Sets the power and notifies the observers
	 * @param power the new power
	 */
	public void set(double power){
		this.power = power;
		this.setChanged();
		this.notifyObservers(MotorControllerEvent.POWER);
	}
	
	/**
	 * Sets the break coast mode of the controller.
	 * @param coast whether to coast, true = coast; false = break
	 */
	public void setBreakCoastMode(boolean coast){
		this.coast = coast;
		this.setChanged();
		this.notifyObservers(MotorControllerEvent.BREAK_COAST_MODE);
	}
	
	/**
	 * Sets the motor controller into a disabled mode, or enables it
	 * @param disabled whether it is disabled
	 */
	public void setDisabled(boolean disabled){
		if(this.disabled != disabled){
			this.setChanged();
			this.disabled = disabled;
			this.notifyObservers(MotorControllerEvent.DISABLE_ENABLE);
		}
	}

	/**
	 * @return the type
	 */
	public MotorControllerType getType() {
		return type;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return the sensors
	 */
	public List<SensorDiscriptor> getSensors(){
		return this.sensors;
	}

	/**
	 * @return the power
	 */
	public double getPower() {
		return power;
	}

	/**
	 * @return the break/coast mode, true for coast; false for break.
	 */
	public boolean getBreakCoastMode() {
		return coast;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg0.getClass().isAssignableFrom(SensorDiscriptor.class)){
			this.setChanged();
			this.notifyObservers(arg0);
		}
	}
	
	public String toString(){
		return this.type + " Motor Controller with id " + this.id;
	}
	
}
