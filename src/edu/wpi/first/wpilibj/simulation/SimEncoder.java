/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2016. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.simulation;

public class SimEncoder {
	private double position, velocity;
	
	public SimEncoder(String topic) {
	}
	
	public void reset() {
		sendCommand("reset");
	}

	public void start() {
		sendCommand("start");
	}

	public void stop() {
		sendCommand("stop");
	}

	private void sendCommand(String cmd) {
	}

	public double getPosition() {
		return position;
	}

	public double getVelocity() {
		return velocity;
	}
}
