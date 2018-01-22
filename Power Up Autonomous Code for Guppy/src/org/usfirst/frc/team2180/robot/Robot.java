/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2180.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {

	public static Command autonomousCommand;
	public static SendableChooser<Command> chooser;
	
	public static DifferentialDrive guppyDrive;
	public static Joystick left, right;
	public static WPI_TalonSRX leftMotor, rightMotor;
	public static Gyro gyro;
	
	public static String gameData;
	
	

	@Override
	public void robotInit() {
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);
		
		leftMotor = new WPI_TalonSRX(10);
		rightMotor = new WPI_TalonSRX(11);
		
		guppyDrive = new DifferentialDrive(leftMotor, rightMotor);
		
		gyro = new ADXRS450_Gyro();
		
		chooser = new SendableChooser<Command>();
		
		chooser.addDefault("Spot 1", new Spot1Auton());
		chooser.addObject("Spot 2", new Spot2Auton());
		chooser.addObject("Spot 3", new Spot3Auton());
		
		SmartDashboard.putData("Autonomous mode chooser", chooser);
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
		
		gameData = DriverStation.getInstance().getGameSpecificMessage();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null) {
			autonomousCommand.start();
		}
		
		gyro.reset(); //resets the heading to zero degrees
	}

	@Override
	public void autonomousPeriodic() {
		
		double angle = gyro.getAngle();
		guppyDrive.tankDrive(0.5, (angle*Constants.Kp) + 0.5); //closed-loop system using Proportional Constant
		
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
		
		guppyDrive.tankDrive(0.0, 0.0);
	}

	@Override
	public void teleopPeriodic() {
		
		guppyDrive.tankDrive(left.getRawAxis(1), right.getRawAxis(1));
		
		Scheduler.getInstance().run();
	}

	@Override
	public void testPeriodic() {
		
	}
}
