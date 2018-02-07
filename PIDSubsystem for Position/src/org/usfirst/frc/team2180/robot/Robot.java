/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2180.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2180.robot.commands.ExampleCommand;
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

	@Override
	public void robotInit() {
		
		// PID gains. Try tuning these through the web dashboard
		kP = 0.1; // tune first.
		kI = 0.0; // tune last. start with 0.01*kP
		kD = 0.0; // tune second. start with 10*kP to 100*kP
		
		chooser = new SendableChooser<>();
		chooser.addDefault("Default Auto", new ExampleCommand());
		SmartDashboard.putData("Auto mode", chooser);
		
		sensorInPhase = true; // don't change these
		motorInverted = false; // don't change these
		
		autoDrive = new Drivetrain();
		
		talon1 = new WPI_TalonSRX(10); // master
		talon2 = new WPI_TalonSRX(20);
		talon3 = new WPI_TalonSRX(30);
		
		talon2.set(talon1.getDeviceID()); // a slave of talon1
		talon3.set(talon1.getDeviceID()); // a slave of talon1
		
		// begin configuration
		
		talon1.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
		talon1.setSensorPhase(sensorInPhase);
		talon1.setInverted(motorInverted);
		talon1.configAllowableClosedloopError(0, 0, 10); // 1st parameter, the error, should be changed to allow more tolerance
		
		talon1.configNominalOutputForward(0, 10);
		talon1.configNominalOutputReverse(0, 10);
		talon1.configPeakOutputForward(1, 10);
		talon1.configPeakOutputReverse(-1, 10);
		
		/* Methods for Tuning Gains. Uncomment this if you're not using the web dashboard
		talon1.config_kF(0, 0.0, 10);
		talon1.config_kP(0, kP, 10);
		talon1.config_kI(0, kI, 10);
		talon1.config_kD(0, kD, 10);
		*/
		
		absolutePosition = (talon1.getSensorCollection().getPulseWidthPosition() / 4) & 0xFFF;
		
		if (sensorInPhase) {
			absolutePosition *= -1;
		}
			
		if (motorInverted) {
			absolutePosition *= -1;
		}
		
		talon1.setSelectedSensorPosition(absolutePosition, 0, 10);
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
		
		autonSpeed = 1.0;
		numRevs = 10;
		targetPositionRotations = (numRevs * 1440) / autonSpeed; // 1440 ticks per revolution, amassed to 10 revolutions, scaled to desired speed
	}

	@Override
	public void autonomousPeriodic() {
		
		talon1.set(ControlMode.Position, targetPositionRotations);
		
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
		Scheduler.getInstance().run();
	}

	@Override
	public void testPeriodic() {
	}
}
