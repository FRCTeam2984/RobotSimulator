package io.github.frcteam2984.simulator.world;

import org.json.JSONObject;

/**
 * A class to handle the calculations relating to the motor performance at different rpms to give a more realistic motor experience.
 * @author Max Apodaca
 *
 */
public class MotorPreformanceCurve {
	
	private double slope;
	private double interncept;
	
	/**
	 * Creates a new motor curve from the given obj. Two keys are used "slope", and "intercept" in the formula y = mx + b where y is in torque and x is in rpm.
	 * @param obj
	 */
	public MotorPreformanceCurve(JSONObject obj){
		this.slope = obj.getDouble("slope");
		this.interncept = obj.getDouble("intercept");
	}
	
	/**
	 * returns the torque which the motor offers at the given rpm
	 * @param rpm the rpm which the motor is moving at
	 * @return the torque which it produces
	 */
	public double getTroque(double rpm){
		return this.slope * rpm + this.interncept;
	}
	
}
