/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.simulation;

import java.util.Observable;
import java.util.Observer;

import io.github.frcteam2984.simulator.HardwareAdaptor;
import io.github.frcteam2984.simulator.SensorDiscriptor;

/**
 * A simulator gyro which interfaces with the simulator. Mostly rewritten from WPI's implementation.
 *
 */
public class SimGyro implements Observer{
	private double position, velocity;
	
	private SensorDiscriptor positionDiscriptor;
	private SensorDiscriptor velocityDiscriptor;
	
	private double positionDelta;
	
	/**
	 * Creates a new simulation gyroscope and registers the sensor descriptors for velocity (SPI ID 1) and position (SPI ID 0) with the HardwareAdaptor.
	 */
	public SimGyro() {
		this.position = 0;
		this.velocity = 0;
		this.positionDelta = 0;
		
		this.positionDiscriptor = new SensorDiscriptor(SensorDiscriptor.SensorType.Analog, SensorDiscriptor.SensorLocation.SPI, 0, "Gyro Position");
		this.velocityDiscriptor = new SensorDiscriptor(SensorDiscriptor.SensorType.Analog, SensorDiscriptor.SensorLocation.SPI, 1, "Gyro Velocity");
		HardwareAdaptor.getInstance().addSensor(this.positionDiscriptor);
		HardwareAdaptor.getInstance().addSensor(this.velocityDiscriptor);
		this.positionDiscriptor.addObserver(this);
		this.velocityDiscriptor.addObserver(this);
	}
	
	/**
	 * Resets the position of the gyroscope, the reading after calling this method is zero
	 */
	public void reset() {
		this.positionDelta = this.position;
	}

	/**
	 * @return the angle from the last reset
	 */
	public double getAngle() {
		return position - this.positionDelta;
	}

	/**
	 * @return the velocity of the gyro
	 */
	public double getVelocity() {
		return velocity;
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o == this.positionDiscriptor){
			SensorDiscriptor position = (SensorDiscriptor) o;
			this.position = position.getAnalogValue();
		}
		if(o == this.velocityDiscriptor){
			SensorDiscriptor velocity = (SensorDiscriptor) o;
			this.velocity = velocity.getAnalogValue();
		}
	}
}
