package io.github.frcteam2984.simulator;

import java.util.Observable;

/**
 * A description of a sensor: If it is via the roboRIO, CAN, I2C; what type it is, Digital or Analog; and a name for debug purposes.
 * @author Max Apodaca
 *
 */
public class SensorDiscriptor extends Observable{

	/**
	 * The type of sensor
	 * @author Max Apodaca
	 *
	 */
	public static enum SensorType {
		Digital,
		Analog,
		Other
	}
	
	/**
	 * The location of the sensor, used for keeping ID sorted out
	 * @author Max Apodaca
	 *
	 */
	public static enum SensorLocation {
		RoboRIO,
		CAN,
		I2C,
		SPI
	}
	
	private SensorType type;
	private SensorLocation location;
	private int id;
	private String name;
	
	private double analogValue;
	private boolean digitalValue;
	
	public SensorDiscriptor(SensorType type, SensorLocation location, int id, String name){
		this.type = type;
		this.location = location;
		this.id = id;
		this.name = name;
		this.analogValue = 0;
		this.digitalValue = false;
	}
	
	public void setValue(double value){
		this.analogValue = value;
		this.setChanged();
		this.notifyObservers(value);
	}
	
	public void setValue(boolean value){
		this.digitalValue = value;
		this.setChanged();
		this.notifyObservers(value);
	}
	
	public boolean getDigitalValue(){
		return this.digitalValue;
	}
	
	public double getAnalogValue(){
		return this.analogValue;
	}

	/**
	 * @return the type of sensor, Digital, Analog, Other
	 */
	public SensorType getType() {
		return type;
	}

	/**
	 * @return the location of the sensor, ie CAN, RoboRIO, SPI, ect
	 */
	public SensorLocation getLocation() {
		return location;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	public String toString(){
		return "Sensor named : " + name + " of type " + this.type + " has id " + this.id +
				", location " + this.location + ", and value " + ((this.type == SensorType.Digital) ? this.digitalValue : this.analogValue);
	}
	
}
