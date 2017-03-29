package io.github.frcteam2984.simulator;

import edu.wpi.first.wpilibj.HLUsageReporting;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Timer.Interface;
import edu.wpi.first.wpilibj.Timer.StaticInterface;

public class WPIManager {

	public static void setupDummyObjects(){
		Timer.SetImplementation(new StaticInterface(){

			@Override
			public double getFPGATimestamp() {
				return System.nanoTime();
			}

			@Override
			public double getMatchTime() {
				return System.currentTimeMillis();
			}

			@Override
			public void delay(double seconds) {
				
			}

			@Override
			public Interface newTimer() {
				return new Interface(){

					@Override
					public double get() {
						return 0;
					}

					@Override
					public void reset() {
						
					}

					@Override
					public void start() {
						
					}

					@Override
					public void stop() {
						
					}

					@Override
					public boolean hasPeriodPassed(double period) {
						return false;
					}
					
				};
			}
			
		});
		
		
		HLUsageReporting.SetImplementation(new DummyReporter());
	}
	
}
