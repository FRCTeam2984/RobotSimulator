package io.github.frcteam2984.simulator.world;

import org.json.JSONObject;

import io.github.frcteam2984.simulator.SensorDiscriptor;

/**
 * A class to simulate an arm so that action which require an arm to move get timed correctly
 * @author max
 *
 */
public class Arm {

	private double angle;
	private double mass;
	private double distance;
	
	private double angularVelocity;
	
	private MotorPreformanceCurve torque;
	
	private SensorDiscriptor sensor;
	private double ticksPerRev;
	
	private double[] limits;
	
	private double lastX;
	private double transmissionRatio;
	
	/**
	 * Creates a new arm based on the json object
	 * @param obj the obj to base the arm off of
	 */
	public Arm(JSONObject obj){
		this.angle = obj.getDouble("initialAngle");
		this.mass = obj.getDouble("mass");
		this.distance = obj.getDouble("distanceFromPivot");
		this.torque = new MotorPreformanceCurve(obj.getJSONObject("torque"));
    	this.transmissionRatio = obj.getDouble("transmissionRatio");
		this.angularVelocity = 0;
		this.limits = new double[]{obj.getJSONArray("limits").getDouble(0), obj.getJSONArray("limits").getDouble(1)};
		
		if(obj.has("sensor")){
			JSONObject sensor = obj.getJSONObject("sensor");
			SensorDiscriptor.SensorType sensorType = SensorDiscriptor.SensorType.valueOf(sensor.getString("type"));
			SensorDiscriptor.SensorLocation sensorLocation = SensorDiscriptor.SensorLocation.valueOf(sensor.getString("location"));
			this.sensor = new SensorDiscriptor(sensorType, sensorLocation, 0, "Arm Sensor");
			this.ticksPerRev = sensor.getDouble("ticksPerRev");
			this.lastX = Double.NaN;
		}
	}
	
	/**
	 * updates the arm
	 * @param power the power to apply to the motor
	 * @param timeStep the time which to step forward
	 */
	public void update(double power, double timeStep){
//		System.out.println(this.sensor.getAnalogValue());
    	if(this.sensor != null){
    		if(Double.isNaN(this.lastX)){
    			this.lastX = this.angle;
    		}
    		double dx = this.angle - this.lastX;
    		this.lastX = this.angle;
    		this.sensor.setValue(this.sensor.getAnalogValue() + dx * this.ticksPerRev / Math.PI/2);
    	}
    	double rpm = this.angularVelocity/this.transmissionRatio;
    	double torque = power * Math.copySign(this.torque.getTroque(rpm), this.transmissionRatio);
    	
    	double deltaV = timeStep * torque / this.distance / this.mass;
    	this.angularVelocity += deltaV;
    	this.angle += this.angularVelocity * timeStep;
    	if(this.angle > this.limits[1] ){
    		this.angle = this.limits[1];
    		this.angularVelocity = 0;
    	}
    	if(this.angle < this.limits[0]){
    		this.angle = this.limits[0];
    		this.angularVelocity = 0;
    	}
    	
    }

	/**
	 * @return the sensor description of the arm
	 */
	public SensorDiscriptor getSensor() {
		return this.sensor;
	}
	
}
