package edu.wpi.first.wpilibj;

/**
 * A wrapper class to add the ADXRS450 gyro (FRC Gyro) support
 * @author Max Apodaca
 *
 */
public class ADXRS450_Gyro extends AnalogGyro{

	public ADXRS450_Gyro() {
		super(0);
		this.reset();
	}

}
