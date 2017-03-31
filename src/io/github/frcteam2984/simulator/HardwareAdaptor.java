package io.github.frcteam2984.simulator;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
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
	private Map<SensorDiscriptor.SensorLocation, SensorDiscriptor[]> sensors;
	
	private HardwareAdaptor(){
		this.cANMotorOutputs = new MotorControllerDiscriptor[256];
		this.sensors = new EnumMap<SensorDiscriptor.SensorLocation, SensorDiscriptor[]>(SensorDiscriptor.SensorLocation.class);
		for(SensorDiscriptor.SensorLocation location : SensorDiscriptor.SensorLocation.values()){
			this.sensors.put(location, new SensorDiscriptor[256]);
		}
	}
	
	/**
	 * Adds the motor controller description and notifies the observers
	 * @param controller the new controller dissipation
	 */
	public void addMotorController(MotorControllerDiscriptor controller){
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
	 * Adds the following sensor descriptor to the hardware adaptor allowing it to be accessed
	 * @param sensor the sensor to add
	 */
	public void addSensor(SensorDiscriptor sensor){
		throwIfNotInRange(sensor.getId(), 0, 255);
		SensorDiscriptor[] sensorLocations = this.sensors.get(sensor.getLocation());
		sensorLocations[sensor.getId()] = sensor;
		this.setChanged();
		this.notifyObservers(sensor);
	}
	
	/**
	 * Returns the sensor at the following location with the following ID
	 * @param location the location of the sensor
	 * @param id the id of the sensor
	 * @return the sensor
	 */
	public SensorDiscriptor getSensor(SensorDiscriptor.SensorLocation location, int id){
		throwIfNotInRange(id, 0, 255);
		return this.sensors.get(location)[id];
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
