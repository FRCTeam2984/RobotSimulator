package io.github.frcteam2984.simulator.vision;

/**
 * A target which has a viewing range, rotation, and position
 * @author Max Apodaca
 *
 */
public interface Target {

	/**
	 * @return x position in world units
	 */
	public double getX();
	
	/**
	 * @return y position in world units
	 */
	public double getY();
	
	/**
	 * @return the horizontal view fov in radians. PI/4 would be PI/8 to either side of the mid-line.
	 */
	public double getViewingFOV();
	
	/**
	 * @return the angle, positive is a anti-clockwies rotation
	 */
	public double getAngle();
	
}
