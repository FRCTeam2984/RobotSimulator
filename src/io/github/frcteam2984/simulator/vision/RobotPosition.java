package io.github.frcteam2984.simulator.vision;

/**
 * The position of on object on the robot used for interfacing with outside code
 * @author Max Apodaca
 *
 */
public class RobotPosition {

	private double x;
	private double y;
	private double angle;
	
	/**
	 * Creates a new robot position
	 * @param x the x position
	 * @param y the y position
	 * @param angle the angle
	 */
	public RobotPosition(double x, double y, double angle){
		this.x = x;
		this.y = y;
		this.angle = angle;
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return the angle
	 */
	public double getAngle() {
		return angle;
	}
	
}
