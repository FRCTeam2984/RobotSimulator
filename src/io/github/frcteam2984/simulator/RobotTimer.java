package io.github.frcteam2984.simulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Timer;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * A timer which listens to the drive station and calls the corresponding methods in iterative robot.
 * @author Max Apodaca
 *
 */
public class RobotTimer implements ActionListener, Observer{

	public static final int DEFAULT_DELAY = 20;
	
	/**
	 * the timer used for the time interval
	 */
	private Timer timer;
	
	/**
	 * the world that gets simulated
	 */
	private IterativeRobot robot;
	
	private boolean isDisabled;
	private boolean isAutonomous;
	private boolean isTest;
	private boolean first;
	
	/**
	 * creates a new robot timer
	 * @param robot the robot which to run the methods of
	 * @param ds the driver station to monitor
	 */
	public RobotTimer(IterativeRobot robot, DriverStation ds){
		this.robot = robot;
		ds.addObserver(this);
		this.timer = new Timer(DEFAULT_DELAY, this);
		this.timer.start();
		this.isAutonomous = false;
		this.isDisabled = true;
		this.isTest = false;
		this.first = true;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(this.isDisabled){
			this.robot.disabledPeriodic();
		} else if(this.isAutonomous){
			this.robot.autonomousPeriodic();
		} else if(this.isTest){
			this.robot.testPeriodic();
		} else {
			this.robot.teleopPeriodic();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if(this.first){
			this.robot.robotInit();
			this.first = false;
		}
		if(o.getClass().isAssignableFrom(DriverStation.class)){
			DriverStation ds = (DriverStation)o;
			if(ds.isAutonomous() != this.isAutonomous || ds.isDisabled() != this.isDisabled || ds.isTest() != this.isTest){
				this.isDisabled = ds.isDisabled();
				this.isAutonomous = ds.isAutonomous();
				this.isTest = ds.isTest();
				if(this.isDisabled){
					this.robot.disabledInit();
				} else if(this.isAutonomous){
					this.robot.autonomousInit();
				} else if(this.isTest){
					this.robot.testInit();
				} else {
					this.robot.teleopInit();
				}
			}
		}
	}
	
}