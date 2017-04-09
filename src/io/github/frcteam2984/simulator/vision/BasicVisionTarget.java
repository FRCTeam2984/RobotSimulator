package io.github.frcteam2984.simulator.vision;

public class BasicVisionTarget implements Target{

	double x, y, angle, viewingFOV;
	
	public BasicVisionTarget(double x, double y, double angle, double viewingFOV){
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.viewingFOV = viewingFOV;
	}
	
	@Override
	public double getX() {
		return this.x;
	}

	@Override
	public double getY() {
		return this.y;
	}

	@Override
	public double getViewingFOV() {
		return this.viewingFOV;
	}

	@Override
	public double getAngle() {
		return this.angle;
	}

}
