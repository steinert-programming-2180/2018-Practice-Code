/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2180.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team2180.robot.commands.Run;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Robot extends TimedRobot {
		
	Command autonomousCommand;
	SendableChooser<Command> chooser;
	public static WPI_TalonSRX talon1, talon2, talon3;
	public static WPI_TalonSRX regTalon1, regTalon2, regTalon3;
	public static boolean sensorInPhase, motorInverted;
	public static double position;
	public static Joystick stick;
	public static boolean isUsingCuriosity;
	public static ADXRS450_Gyro gyro;
	public static PIDController gyroPID;
	public static PIDOutput gyroPIDOutput;
	public static Joystick left, right;
	
	@Override
	public void robotInit() {
		
		left = new Joystick(0);
		right = new Joystick(1);
		
		regTalon1 = new WPI_TalonSRX(11);
		regTalon2 = new WPI_TalonSRX(22);
		regTalon3 = new WPI_TalonSRX(33);
		 
		regTalon2.follow(regTalon1);
		regTalon3.follow(regTalon1);
		
		stick = new Joystick(0);
		
		chooser = new SendableChooser<>();
		SmartDashboard.putData("Auto mode", chooser);
		
		gyro = new ADXRS450_Gyro();
		
		setupDrivetrainPID();
		setupGyroPID();
	}

	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();

		if (autonomousCommand != null) {
			autonomousCommand.start();
		}

		talon1.setSelectedSensorPosition(0, 0, 10);
		
		Run auton = new Run(isUsingCuriosity); // true if using Curiosity
		
		if (auton != null) {
			auton.start();
		}
	}

	@Override
	public void autonomousPeriodic() {
		
		SmartDashboard.putNumber("Position", talon1.getSelectedSensorPosition(0));
		
		SmartDashboard.putNumber("Error", Constants.autonTicks - talon1.getSelectedSensorPosition(0));
		
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {

		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
		
		gyro.reset();
		
		talon1.configPeakOutputForward(1.0, 10);
		talon1.configPeakOutputReverse(-1.0, 10);
	}

	@Override
	public void teleopPeriodic() {
		
		talon1.set(-left.getRawAxis(1));
		regTalon1.set(-right.getRawAxis(1));
		
		SmartDashboard.putNumber("Gyro factor", gyro.getAngle()*Constants.gyroKP);
		
//		regTalon1.set(0.3 - (gyro.getAngle()*Constants.gyroKP));
		
		SmartDashboard.putNumber("Teleop Talon Speed", regTalon1.getMotorOutputPercent());
		
//		talon1.set(ControlMode.PercentOutput, stick.getRawAxis(1));
		
		position = talon1.getSelectedSensorPosition(0);
		
		SmartDashboard.putNumber("Position", position);
		
		Scheduler.getInstance().run();
	}

	@Override
	public void testPeriodic() {
	}
	
	public void setupDrivetrainPID() {
		
		sensorInPhase = false; // don't change these
		motorInverted = true; // don't change these
		
		// FOR CURIOSITY
		talon1 = new WPI_TalonSRX(10); // master
		talon2 = new WPI_TalonSRX(20);
		talon3 = new WPI_TalonSRX(30);
		
		talon2.follow(talon1); // a slave of talon1
		talon3.follow(talon1); // a slave of talon1
		
		talon1.setInverted(motorInverted);
		talon2.setInverted(motorInverted);
		talon3.setInverted(motorInverted);
		
		isUsingCuriosity = true;
		
		// ------------------------------------------- //
		
//		// FOR PSEUDOZERO
//		talon1 = new WPI_TalonSRX(20);
//		talon2 = new WPI_TalonSRX(40);
//		
//		talon2.follow(talon1);
//		
//		talon1.setInverted(motorInverted);
//		talon2.setInverted(motorInverted);
//		
//		isUsingCuriosity = false;
//		
//		regTalon1 = new WPI_TalonSRX(10);
//		regTalon2 = new WPI_TalonSRX(30);
		
		// ------------------------------------------- //
		
		// begin configuration
		
		talon1.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
		talon1.setSensorPhase(sensorInPhase);
		
		talon1.configAllowableClosedloopError(0, Constants.allowableAutonPositionError, 10);
		
		talon1.configNominalOutputForward(0, 10);
		talon1.configNominalOutputReverse(0, 10);
		talon1.configPeakOutputForward(Constants.autonSpeed, 10);
		talon1.configPeakOutputReverse(-Constants.autonSpeed, 10);
		
		talon1.config_kF(0, 0.0, 10);
		talon1.config_kP(0, Constants.kP, 10);
		talon1.config_kI(0, Constants.kI, 10);
		talon1.config_kD(0, Constants.kD, 10);
	}
	
	public void setupGyroPID() {
		gyroPIDOutput = new PIDOutput() {
			public void pidWrite(double output) {
				// an empty trick!
			}
		};
		
		gyroPID = new PIDController(Constants.gyroKP, Constants.gyroKI, Constants.gyroKD, gyro, gyroPIDOutput);
//		gyroPID.setContinuous(true);
		gyroPID.setAbsoluteTolerance(Constants.allowableGyroError);
	}
}