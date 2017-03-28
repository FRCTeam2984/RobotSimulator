/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.simulation;

public class SimGyro {
	private double position, velocity;
	
	public SimGyro(String topic) {
	}
	
	public void reset() {
		sendCommand("reset");
	}

	private void sendCommand(String cmd) {
	}

	public double getAngle() {
		return position;
	}

	public double getVelocity() {
		return velocity;
	}
}
