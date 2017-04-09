package io.github.frcteam2984.simulator.vision;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.common.Vec2;

/**
 * A class of useful methods relating to VisionWorld and Targets
 * @author Max Apodaca
 *
 */
public class VisionUtils {

	public static List<Target> getTargetsInFOV(VisionWorld world, RobotPosition pos, double fOV){
		List<Target> possibleTargets = world.getTargets();
		if(possibleTargets == null){
			return null;
		}
		Vec2 left = new Vec2((float) Math.sin(pos.getAngle() - fOV/2), (float) Math.cos(pos.getAngle() - fOV/2));
		Vec2 right = new Vec2((float) Math.sin(pos.getAngle() + fOV/2), (float) Math.cos(pos.getAngle() + fOV/2));
		List<Target> visibleTargets = new ArrayList<Target>();
		for(Target t : possibleTargets){
			double dx = t.getX() - pos.getX();
			double dy = t.getY() - pos.getY();
			Vec2 leftTarget = new Vec2((float) Math.sin(t.getAngle() - fOV/2), (float) Math.cos(pos.getAngle() - fOV/2));
			Vec2 rightTarget = new Vec2((float) Math.sin(t.getAngle() + fOV/2), (float) Math.cos(pos.getAngle() + fOV/2));
			boolean possible = true;
			possible &= !isLeft(left, dx, dy);
			possible &= isLeft(right, dx, dy);
			possible &= !isLeft(leftTarget, -dx, -dy);
			possible &= isLeft(rightTarget, -dx, -dy);
			if(possible){
				visibleTargets.add(t);
			}
		}
		return visibleTargets;
		
	}
	
	private static boolean isLeft(Vec2 vec, double dx, double dy){
	     return ((vec.x)*(dy) - (vec.y)*(dx)) > 0;
	}
	
}
