package io.github.frcteam2984.simulator.vision;

import java.util.List;

/**
 * An interface for a world which is comparable with the vision system
 * @author Max Apodaca
 *
 */
public interface VisionWorld {

	/**
	 * @return all the targets in the world, whether they are visible or not
	 */
	public List<Target> getTargets();
	
}
