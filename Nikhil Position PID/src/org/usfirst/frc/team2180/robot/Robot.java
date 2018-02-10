/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2180.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2180.robot.commands.ExampleCommand;
import org.usfirst.frc.team2180.robot.commands.Run;
import org.usfirst.frc.team2180.robot.subsystems.Drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Robot extends TimedRobot {
	public static Drivetrain autoDrive;
			

	Command autonomousCommand;
	SendableChooser<Command> chooser;
	
	public static WPI_TalonSRX talon1, talon2, talon3;
	
	public static boolean sensorInPhase, motorInverted;
	
	public static int absolutePosition, numRevs;
	
	public static double kP, kI, kD;
	
	public static double autonSpeed, targetPositionRotations;
	
	public static double position;
	
	public static Joystick stick;
	
	public static Timer timer;

	public static int i;
	
	@Override
	public void robotInit() {
		
		// PID gains. Try tuning these through the web dashboard
		kP = 0.6; // tune first.
		kI = 0.0; // tune last. start with 0.01*kP. You can tune this further using something called an I-zone
		kD = 0.0; // tune second. start with 10*kP to 100*kP
		
		stick = new Joystick(0);
		
		chooser = new SendableChooser<>();
		chooser.addDefault("Default Auto", new ExampleCommand());
		SmartDashboard.putData("Auto mode", chooser);
		
		sensorInPhase = false; // don't change these
		motorInverted = true; // don't change these
		
		autoDrive = new Drivetrain();
		
		talon1 = new WPI_TalonSRX(10); // master
		talon2 = new WPI_TalonSRX(20);
		talon3 = new WPI_TalonSRX(30);
		
		talon2.follow(talon1); // a slave of talon1
		talon3.follow(talon1); // a slave of talon1
		
		// begin configuration
		
		talon1.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
		talon1.setSensorPhase(sensorInPhase);
		talon1.setInverted(motorInverted);
		talon2.setInverted(motorInverted);
		talon3.setInverted(motorInverted);
		talon1.configAllowableClosedloopError(0, 0, 10); // 1st parameter, the error, should be changed to allow more tolerance
		
		talon1.configNominalOutputForward(0, 10);
		talon1.configNominalOutputReverse(0, 10);
		talon1.configPeakOutputForward(0.8, 10);
		talon1.configPeakOutputReverse(-0.8, 10);
		
		///* Methods for Tuning Gains. Uncomment this if you're not using the web dashboard
		talon1.config_kF(0, 0.0, 10);
		talon1.config_kP(0, kP, 10);
		talon1.config_kI(0, kI, 10);
		talon1.config_kD(0, kD, 10);
		//*/
		
		absolutePosition = talon1.getSelectedSensorPosition(0);
		
		if (sensorInPhase) {
			absolutePosition *= -1;
		}
			
		if (motorInverted) {
			absolutePosition *= -1;
		}
		
		talon1.setSelectedSensorPosition(absolutePosition, 0, 10);
		
		SmartDashboard.putNumber("Initial Position", absolutePosition);
		
		timer = new Timer();
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
		
		timer.start();
		
		autonSpeed = 1.0;
		numRevs = 1;
		targetPositionRotations = (numRevs * 360) / autonSpeed; // 1440 ticks per revolution, amassed to 10 revolutions, scaled to desired speed
//		absolutePosition = talon1.getSelectedSensorPosition(0);
		
		
		
		talon1.setSelectedSensorPosition(0, 0, 10);
		
		i = 0;
		
		Run auton = new Run();
		
		if (auton != null) {
			auton.start();
		}
		
//		talon1.setInverted(true);
//		talon2.setInverted(true);
//		talon3.setInverted(true);
	}

	@Override
	public void autonomousPeriodic() {
		
		
		
//		position = talon1.getSelectedSensorPosition(0);
//		
//		SmartDashboard.putNumber("Position", position);
////		SmartDashboard.putNumber("Target Position", position + targetPositionRotations);
////		SmartDashboard.putNumber("Test", 7);
////		SmartDashboard.putNumber("Clock time", Timer.getFPGATimestamp());
//		
//		SmartDashboard.putNumber("Error", 14400 - (position));
//		
////		if (2000 + (position - absolutePosition) < 0) {
////			talon1.set(ControlMode.PercentOutput, 0);
//////			talon1.set(0);
//////			talon1.stopMotor();
////			SmartDashboard.putString("PID status", "stopped");
////		} else {
//		i = 0;
////		talon1.setSelectedSensorPosition(0, 0, 10);
//		SmartDashboard.putNumber("i", i);
//		
//		if (i < 2) {
//			if (i == 1) {
//				SmartDashboard.putString("PID Status", "Backward");
//				talon1.setInverted(false);
//				talon2.setInverted(false);
//				talon3.setInverted(false);
//				SmartDashboard.putNumber("Position debugging", position);
//				talon1.set(ControlMode.Position, 14400);
//				i = 2;
//				SmartDashboard.putNumber("i", i);
//			} else {
//				SmartDashboard.putString("PID Status", "Forward");
////				talon1.setInverted(true);
////				talon2.setInverted(true);
////				talon3.setInverted(true);
//				talon1.set(ControlMode.Position, 14400);
//				i = 1;
//				SmartDashboard.putNumber("i", i);
//			}
//		}
		
		
//			SmartDashboard.putNumber("Talon Speed", talon1.get());
//		}
		
		SmartDashboard.putNumber("Talon1 Motor Output", talon1.getMotorOutputPercent());
		
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {

		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
	}

	@Override
	public void teleopPeriodic() {
		
		talon1.set(stick.getRawAxis(1));
		
		talon1.set(ControlMode.PercentOutput, stick.getRawAxis(1));
		
//		talon1.set
		
		position = talon1.getSelectedSensorPosition(0);
		
		SmartDashboard.putNumber("Position", position);
		
		Scheduler.getInstance().run();
	}

	@Override
	public void testPeriodic() {
	}
}