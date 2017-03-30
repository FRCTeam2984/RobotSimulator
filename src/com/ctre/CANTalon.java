package com.ctre;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.MotorSafetyHelper;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import io.github.frcteam2984.simulator.HardwareAdaptor;
import io.github.frcteam2984.simulator.MotorControllerDiscriptor;

public class CANTalon implements MotorSafety, PIDOutput, PIDSource, GadgeteerUartClient, Observer {
	private MotorSafetyHelper m_safetyHelper;
	private boolean m_isInverted = false;
	protected PIDSourceType m_pidSource = PIDSourceType.kDisplacement;

	private TalonControlMode m_controlMode;
	
	private static List<CANTalon> controllers = new ArrayList<CANTalon>();

	public static void disableTalons(){
		for(CANTalon talon : controllers){
			talon.disable();
		}
	}
	
	public static void enableTalons(){
		for(CANTalon talon : controllers){
			talon.enable();
		}
	}
	
	public static enum TalonControlMode {
		PercentVbus(0), Position(1), Speed(2), Current(3), Voltage(4), Follower(5), MotionProfile(6), MotionMagic(
				7), Disabled(15);

		public final int value;


		private TalonControlMode(int value) {
			this.value = value;
		}

		public boolean isPID() {
			return (this == Current) || (this == Speed) || (this == Position);
		}

		public int getValue() {
			return value;
		}
	}

	private static enum UsageFlags {
		Default(0), PercentVbus(1), Position(2), Speed(4), Current(8), Voltage(16), Follower(32), MotionProfile(
				64), MotionMagic(128),

		VRampRate(4194304), CurrentLimit(8388608), ZeroSensorI(16777216), ZeroSensorF(33554432), ZeroSensorR(
				67108864), ForwardLimitSwitch(134217728), ReverseLimitSwitch(268435456), ForwardSoftLimit(
						536870912), ReverseSoftLimit(1073741824), MultiProfile(Integer.MIN_VALUE);

		private UsageFlags(int value) {
		}
	}

	public static enum FeedbackDevice {
		QuadEncoder(0), AnalogPot(2), AnalogEncoder(3), EncRising(4), EncFalling(5), CtreMagEncoder_Relative(
				6), CtreMagEncoder_Absolute(7), PulseWidth(8);

		public int value;

	public static FeedbackDevice valueOf(int value)
    {
      return null;
    }

		private FeedbackDevice(int value) {
			this.value = value;
		}
	}

	public static enum FeedbackDeviceStatus {
		FeedbackStatusUnknown(0), FeedbackStatusPresent(1), FeedbackStatusNotPresent(2);

		public int value;

	public static FeedbackDeviceStatus valueOf(int value)
    {
      return null;
    }

		private FeedbackDeviceStatus(int value) {
			this.value = value;
		}
	}

	public static enum StatusFrameRate {
		General(0), Feedback(1), QuadEncoder(2), AnalogTempVbat(3), PulseWidth(4);

		public int value;

	public static StatusFrameRate valueOf(int value)
    {
      return null;
    }

		private StatusFrameRate(int value) {
			this.value = value;
		}
	}

	public static enum SetValueMotionProfile {
		Disable(0), Enable(1), Hold(2);

		public int value;

	public static SetValueMotionProfile valueOf(int value)
    {
      return null;
    }

		private SetValueMotionProfile(int value) {
			this.value = value;
		}
	}

	public static class TrajectoryPoint {
		public double position;

		public double velocity;

		public int timeDurMs;

		public int profileSlotSelect;

		public boolean velocityOnly;

		public boolean isLastPoint;

		public boolean zeroPos;

		public TrajectoryPoint() {
		}
	}

	public static class MotionProfileStatus {
		public int topBufferRem;

		public int topBufferCnt;

		public int btmBufferCnt;

		public boolean hasUnderrun;

		public boolean isUnderrun;

		public boolean activePointValid;

		public CANTalon.TrajectoryPoint activePoint = new CANTalon.TrajectoryPoint();

		public CANTalon.SetValueMotionProfile outputEnable;

		public MotionProfileStatus() {
		}
	}

	private MotorControllerDiscriptor discriptor;
	
	private int m_deviceNumber;
	private boolean m_controlEnabled;
	private int m_profile;

	double m_setPoint;

	int m_codesPerRev;

	int m_numPotTurns;

	FeedbackDevice m_feedbackDevice;

	public CANTalon(int deviceNumber) {
		m_profile = 0;
		m_setPoint = 0.0D;
		m_codesPerRev = 0;
		m_numPotTurns = 0;
		m_feedbackDevice = FeedbackDevice.QuadEncoder;
		setProfile(m_profile);
		applyControlMode(TalonControlMode.PercentVbus);
		this.discriptor = new MotorControllerDiscriptor(MotorControllerDiscriptor.MotorControllerType.CAN, deviceNumber);
		HardwareAdaptor.getInstance().addMotorController(this.discriptor);
		this.discriptor.addObserver(this);
		controllers.add(this);
	}

	public CANTalon(int deviceNumber, int controlPeriodMs) {
		this(deviceNumber);
	}

	public CANTalon(int deviceNumber, int controlPeriodMs, int enablePeriodMs) {
		this(deviceNumber);
	}

	public void pidWrite(double output) {
		if (getControlMode() == TalonControlMode.PercentVbus) {
			set(output);
		} else {
			throw new IllegalStateException("PID only supported in PercentVbus mode");
		}
	}

	public void setPIDSourceType(PIDSourceType pidSource) {
		m_pidSource = pidSource;
	}

	public PIDSourceType getPIDSourceType() {
		return m_pidSource;
	}

	public double pidGet() {
		return getPosition();
	}

	public void delete() {
//TODO Implement Method
	}

	public void set(double outputValue){
		if(this.m_controlMode == TalonControlMode.PercentVbus){
			this.discriptor.set(Math.max(Math.min(outputValue, 1), -1));
		}
	}

	public void setInverted(boolean isInverted) {
		m_isInverted = isInverted;
	}

	public boolean getInverted() {
		return m_isInverted;
	}

	public void reset() {
		disable();
		clearIAccum();
	}

	public boolean isEnabled() {
		return isControlEnabled();
	}

	public double getError() {
		return getClosedLoopError();
	}

	public void setSetpoint(double setpoint) {
		set(setpoint);
	}

	public void reverseSensor(boolean flip) {
//TODO Implement Method
}

	public void reverseOutput(boolean flip) {
//TODO Implement Method
}

	public double get(){
		return 0; //TODO Implement
    }
    
    
	public int getEncPosition() {
		//TODO Implement Method
		return 0;
	}

	public void setEncPosition(int newPosition) {
//TODO Implement Method
}

	public int getEncVelocity() {
//TODO Implement Method
		return -1;
}

	public int getPulseWidthPosition() {
//TODO Implement Method
		return -1;
}

	public void setPulseWidthPosition(int newPosition) {
//TODO Implement Method
}

	public int getPulseWidthVelocity() {
//TODO Implement Method
		return -1;
}

	public int getPulseWidthRiseToFallUs() {
//TODO Implement Method
		return -1;
}

	public int getPulseWidthRiseToRiseUs() {
//TODO Implement Method
		return -1;
}

	public FeedbackDeviceStatus isSensorPresent(FeedbackDevice feedbackDevice){
//TODO Implement Method
		return FeedbackDeviceStatus.FeedbackStatusUnknown;
}

	public int getNumberOfQuadIdxRises() {
//TODO Implement Method
		return -1;
}

	public int getPinStateQuadA() {
//TODO Implement Method
		return -1;
}

	public int getPinStateQuadB() {
//TODO Implement Method
		return -1;
}

	public int getPinStateQuadIdx() {
//TODO Implement Method
		return -1;
}

	public void setAnalogPosition(int newPosition) {
//TODO Implement Method
}

	public int getAnalogInPosition() {
//TODO Implement Method
		return -1;
}

	public int getAnalogInRaw() {
		return getAnalogInPosition() & 0x3FF;
	}

	public int getAnalogInVelocity() {
//TODO Implement Method
		return -1;
}

	public int getClosedLoopError() {
//TODO Implement Method
		return -1;
}

	public void setAllowableClosedLoopErr(int allowableCloseLoopError) {
		//TODO Implement Method
	}

	public boolean isFwdLimitSwitchClosed() {
//TODO Implement Method
		return false;
}

	public boolean isRevLimitSwitchClosed() {
//TODO Implement Method
		return false;
}

	public boolean isZeroSensorPosOnIndexEnabled() {
//TODO Implement Method
		return false;
}

	public boolean isZeroSensorPosOnRevLimitEnabled() {
//TODO Implement Method
		return false;
}

	public boolean isZeroSensorPosOnFwdLimitEnabled() {
//TODO Implement Method
		return false;
}

	public boolean getBrakeEnableDuringNeutral() {
//TODO Implement Method
		return false;
}

	public void configEncoderCodesPerRev(int codesPerRev) {
//TODO Implement Method
}

	public void configPotentiometerTurns(int turns) {
//TODO Implement Method
}

	public double getTemperature() {
//TODO Implement Method
		return -1;
}

	public double getOutputCurrent() {
//TODO Implement Method
		return -1;
}

	public double getOutputVoltage() {
//TODO Implement Method
		return -1;
}

	public double getBusVoltage() {
//TODO Implement Method
		return -1;
}

	public double getPosition() {
//TODO Implement Method
		return -1;
}

	public void setPosition(double pos) {
//TODO Implement Method
}

	public double getSpeed() {
//TODO Implement Method
		return -1;
}

	public TalonControlMode getControlMode() {
		return m_controlMode;
	}

	public void setControlMode(int mode) {
		//TODO Implement Method Stub
	}

	private void applyUsageStats(UsageFlags usage) {
		//TODO Implement Method Stub
	}

	private void applyControlMode(TalonControlMode controlMode) {
		m_controlMode = controlMode;
		//TODO Implement Method Stub
	}

	public void changeControlMode(TalonControlMode controlMode) {
		if (m_controlMode != controlMode) {

			applyControlMode(controlMode);
		}
	}

	public void setFeedbackDevice(FeedbackDevice device) {
		m_feedbackDevice = device;

		//TODO Implement Method Stub
	}

	public void setStatusFrameRateMs(StatusFrameRate stateFrame, int periodMs) {
		//TODO Implement Method Stub
		}

	public void enableControl() {
		changeControlMode(m_controlMode);
		m_controlEnabled = true;
	}

	public void enable() {
		enableControl();
	}

	public void disableControl() {
		//TODO Implement Method Stub
		m_controlEnabled = false;
		this.discriptor.set(0);
		this.discriptor.setDisabled(true);
	}

	public boolean isControlEnabled() {
		return m_controlEnabled;
	}

	public double getP() {
//TODO Implement Method
		return -1;
}

	public double getI() {
//TODO Implement Method
		return -1;
}

	public double getD() {
//TODO Implement Method
		return -1;
}

	public double getF() {
//TODO Implement Method
		return -1;
}

	public double getIZone() {
//TODO Implement Method
		return -1;
}

	public double getCloseLoopRampRate() {
//TODO Implement Method
		return -1;
}

	public long GetFirmwareVersion() {
//TODO Implement Method
		return -1;
}

	public long GetIaccum() {
		return -1;
//TODO Implement Method
}

	public void setP(double p) {
//TODO Implement Method
}

	public void setI(double i) {
//TODO Implement Method
}

	public void setD(double d) {
//TODO Implement Method
}

	public void setF(double f) {
//TODO Implement Method
}

	public void setIZone(int izone) {
//TODO Implement Method
}

	public void setCloseLoopRampRate(double rampRate) {
//TODO Implement Method
}

	public void setVoltageRampRate(double rampRate) {
//TODO Implement Method
}

	public void setVoltageCompensationRampRate(double rampRate) {
//TODO Implement Method
}

	public void ClearIaccum() {
//TODO Implement Method
}

	private ITable m_table;

	private ITableListener m_tableListener;

	public void setPID(double p, double i, double d, double f, int izone, double closeLoopRampRate, int profile) {
		if ((profile != 0) && (profile != 1)) {
			throw new IllegalArgumentException("Talon PID profile must be 0 or 1.");
		}
		m_profile = profile;
		setProfile(profile);
		setP(p);
		setI(i);
		setD(d);
		setF(f);
		setIZone(izone);
		setCloseLoopRampRate(closeLoopRampRate);
	}

	public void setPID(double p, double i, double d) {
		setPID(p, i, d, 0.0D, 0, 0.0D, m_profile);
	}

	public double getSetpoint() {
		return m_setPoint;
	}

	public void setProfile(int profile) {
		//TODO Implement Method

		applyUsageStats(UsageFlags.MultiProfile);
	}

	@Deprecated
	public void stopMotor() {
		disableControl();
	}

	public void disable() {
		disableControl();
	}

	public int getDeviceID() {
		return m_deviceNumber;
	}

	public void clearIAccum() {
		//TODO Implement Method Stub
	}

	public void setForwardSoftLimit(double forwardLimit) {
//TODO Implement Method
}

	public int getForwardSoftLimit() {
		return -1;
//TODO Implement Method
}

	public void enableForwardSoftLimit(boolean enable) {
//TODO Implement Method
}

	public boolean isForwardSoftLimitEnabled() {
		return false;
//TODO Implement Method
}

	public void setReverseSoftLimit(double reverseLimit) {
//TODO Implement Method
}

	public int getReverseSoftLimit() {
		return -1;
//TODO Implement Method
}

	public void enableReverseSoftLimit(boolean enable) {
//TODO Implement Method
}

	public boolean isReverseSoftLimitEnabled() {
		return false;
//TODO Implement Method
}

	public void configMaxOutputVoltage(double voltage) {
		configPeakOutputVoltage(voltage, -voltage);
	}

	public void configPeakOutputVoltage(double forwardVoltage, double reverseVoltage) {
		if (forwardVoltage > 12.0D) {
			forwardVoltage = 12.0D;
		} else if (forwardVoltage < 0.0D) {
			forwardVoltage = 0.0D;
		}
		if (reverseVoltage > 0.0D) {
			reverseVoltage = 0.0D;
		} else if (reverseVoltage < -12.0D) {
			reverseVoltage = -12.0D;
		}

		//TODO Implement Method
	}

	public void configNominalOutputVoltage(double forwardVoltage, double reverseVoltage) {
		if (forwardVoltage > 12.0D) {
			forwardVoltage = 12.0D;
		} else if (forwardVoltage < 0.0D) {
			forwardVoltage = 0.0D;
		}
		if (reverseVoltage > 0.0D) {
			reverseVoltage = 0.0D;
		} else if (reverseVoltage < -12.0D) {
			reverseVoltage = -12.0D;
		}
		//TODO Implement Method
	}

	public void clearStickyFaults() {
//TODO Implement Method
}

	public void enableLimitSwitch(boolean forward, boolean reverse) {
//TODO Implement Method
	}

	public void ConfigFwdLimitSwitchNormallyOpen(boolean normallyOpen) {
//TODO Implement Method
}

	public void ConfigRevLimitSwitchNormallyOpen(boolean normallyOpen) {
//TODO Implement Method
}

	public void enableBrakeMode(boolean brake) {
//TODO Implement Method
}

	public int getFaultOverTemp() {
		return -1;
//TODO Implement Method
}

	public int getFaultUnderVoltage() {
		return -1;
//TODO Implement Method
}

	public int getFaultForLim() {
		return -1;
//TODO Implement Method
}

	public int getFaultRevLim() {
		return -1;
//TODO Implement Method
}

	public int getFaultHardwareFailure() {
		return -1;
//TODO Implement Method
}

	public int getFaultForSoftLim() {
		return -1;
//TODO Implement Method
}

	public int getFaultRevSoftLim() {
		return -1;
//TODO Implement Method
}

	public int getStickyFaultOverTemp() {
		return -1;
//TODO Implement Method
}

	public int getStickyFaultUnderVoltage() {
		return -1;
//TODO Implement Method
}

	public int getStickyFaultForLim() {
		return -1;
//TODO Implement Method
}

	public int getStickyFaultRevLim() {
		return -1;
//TODO Implement Method
}

	public int getStickyFaultForSoftLim() {
		return -1;
//TODO Implement Method
}

	public int getStickyFaultRevSoftLim() {
		return -1;
//TODO Implement Method
}

	private double getNativeUnitsPerRotationScalar(FeedbackDevice devToLookup)
  {
    double retval = 0.0D;
  //TODO Implement Method Stub
    return retval;
  }

	private int scaleRotationsToNativeUnits(FeedbackDevice devToLookup, double fullRotations) {
		int retval = (int) fullRotations;

		double scalar = getNativeUnitsPerRotationScalar(devToLookup);

		if (scalar > 0.0D) {
			retval = (int) (fullRotations * scalar);
		}
		return retval;
	}

	private int scaleVelocityToNativeUnits(FeedbackDevice devToLookup, double rpm) {
		int retval = (int) rpm;

		double scalar = getNativeUnitsPerRotationScalar(devToLookup);

		if (scalar > 0.0D) {
			retval = (int) (rpm * 0.0016666666666666668D * scalar);
		}
		return retval;
	}

	private double scaleNativeUnitsToRotations(FeedbackDevice devToLookup, int nativePos) {
		double retval = nativePos;

		double scalar = getNativeUnitsPerRotationScalar(devToLookup);

		if (scalar > 0.0D) {
			retval = nativePos / scalar;
		}
		return retval;
	}

	private double scaleNativeUnitsToRpm(FeedbackDevice devToLookup, long nativeVel) {
		double retval = nativeVel;

		double scalar = getNativeUnitsPerRotationScalar(devToLookup);

		if (scalar > 0.0D) {
			retval = nativeVel / (scalar * 0.0016666666666666668D);
		}
		return retval;
	}

	public void enableZeroSensorPositionOnIndex(boolean enable, boolean risingEdge) {
		//TODO Implement Method

		applyUsageStats(UsageFlags.ZeroSensorI);
	}

	public void enableZeroSensorPositionOnForwardLimit(boolean enable) {
		//TODO Implement Method
		applyUsageStats(UsageFlags.ZeroSensorF);
	}

	public void enableZeroSensorPositionOnReverseLimit(boolean enable) {
//TODO Implement Method
}

	public void changeMotionControlFramePeriod(int periodMs) {
//TODO Implement Method
}

	public void clearMotionProfileTrajectories() {
//TODO Implement Method
}

	public int getMotionProfileTopLevelBufferCount() {
		return -1;
//TODO Implement Method
}

	public boolean pushMotionProfileTrajectory(TrajectoryPoint trajPt) {
		if (isMotionProfileTopLevelBufferFull()) {
			return false;
		}

		int targPos = scaleRotationsToNativeUnits(m_feedbackDevice, /*position*/-1);
		int targVel = scaleVelocityToNativeUnits(m_feedbackDevice, /*velocity*/-1);

		int timeDurMs = -1;

		if (timeDurMs > 255) {
			timeDurMs = 255;
		}
		if (timeDurMs < 0) {
			timeDurMs = 0;
		}

		//TODO Implement Method Stub
		return true;
	}

	public boolean isMotionProfileTopLevelBufferFull() {
		return false;
//TODO Implement Method
}

	public void processMotionProfileBuffer() {
//TODO Implement Method
}

	public void getMotionProfileStatus(MotionProfileStatus motionProfileStatus) {
//TODO Implement Method
}

	protected void setMotionProfileStatusFromJNI(MotionProfileStatus motionProfileStatus, int flags,
			int profileSlotSelect, int targPos, int targVel, int topBufferRem, int topBufferCnt, int btmBufferCnt,
			int outputEnable) {
		//TODO Implement Method Stub
	}

	public void clearMotionProfileHasUnderrun() {
//TODO Implement Method
}

	public void setMotionMagicCruiseVelocity(double motMagicCruiseVeloc) {
//TODO Implement Method
}

	public void setMotionMagicAcceleration(double motMagicAccel) {
//TODO Implement Method
}

	public double getMotionMagicCruiseVelocity() {
		return -1;
//TODO Implement Method
}

	public double getMotionMagicAcceleration() {
		//TODO Implement Method Stub
		return -1;
	}

	public void setCurrentLimit(int amps) {
		//TODO Implement Method Stub
	}

	public void EnableCurrentLimit(boolean enable) {
		//TODO Implement Method Stub
	}

	public int GetGadgeteerStatus(GadgeteerUartClient.GadgeteerUartStatus status)
  {
		//TODO Implement Method Stub
		return -1;
  }

	public String getLastError() {
//TODO Implement Method
		return "Unknown error status";
	}

	public void setExpiration(double timeout) {
//TODO Implement Method
}

	public double getExpiration() {
		return m_safetyHelper.getExpiration();
	}

	public boolean isAlive() {
		return m_safetyHelper.isAlive();
	}

	public boolean isSafetyEnabled() {
		return m_safetyHelper.isSafetyEnabled();
	}

	public void setSafetyEnabled(boolean enabled) {
		m_safetyHelper.setSafetyEnabled(enabled);
	}

	public String getDescription() {
		return "CANTalon ID " + m_deviceNumber;
	}

	public void initTable(ITable subtable) {
		m_table = subtable;
		updateTable();
	}

	public void updateTable() {
		//TODO Implement Method Stub
	}

	public ITable getTable() {
		return m_table;
	}

	public void startLiveWindowMode() {
		set(0.0D);
		m_table.addTableListener(m_tableListener, true);
	}

	public void stopLiveWindowMode() {
		set(0.0D);

		m_table.removeTableListener(m_tableListener);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		
	}
}
