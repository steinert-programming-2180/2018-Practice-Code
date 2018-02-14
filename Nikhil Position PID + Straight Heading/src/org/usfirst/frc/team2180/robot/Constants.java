package org.usfirst.frc.team2180.robot;

public class Constants {
	
	public static final double kP = 10.0;
	public static final double kI = 0.00001;
	public static final double kD = 320.0;
	
	public static final double autonSpeed = 0.3;
	
	public static final double gyroKP = 0.03;
	public static final double gyroKI = 0.0;
	public static final double gyroKD = 0.0;
	
	public static final int allowableAutonPositionError = 40;
	public static final double allowableGyroError = 2; // two degrees
	
	public static final int autonTicks = 14400;
}
