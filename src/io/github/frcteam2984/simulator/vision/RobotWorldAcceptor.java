package io.github.frcteam2984.simulator.vision;

/**
 * An interface used to give the fake vision tracker a world
 * @author Max Apodaca
 *
 */
public interface RobotWorldAcceptor {

	/**
	 * used to set the world of the vision tracker 
	 * @param world the world which we are in
	 */
	public void reciveWorld(VisionWorld world);
	
	/**
	 * used to set the new robot position of the vision tracker 
	 * @param position the robot position where the camera is
	 */
	public void robotPositionUpdate(RobotPosition position);
	
}
