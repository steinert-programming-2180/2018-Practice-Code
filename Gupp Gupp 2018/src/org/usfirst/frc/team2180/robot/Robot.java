/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2180.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {

	Command m_autonomousCommand;
	SendableChooser<Command> m_chooser = new SendableChooser<>();
	
	public static WPI_TalonSRX driveTalon1, driveTalon2;
	Joystick left, right;
	
	StringBuilder sb = new StringBuilder();
	int loops = 0;
	double targetPositionRotations1, targetPositionRotations2;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		// chooser.addObject("My Auto", new MyAutoCommand());
		
		driveTalon1 = new WPI_TalonSRX(11);
		driveTalon2 = new WPI_TalonSRX(10);
		
		left = new Joystick(0);
		right = new Joystick(1);
		
		SmartDashboard.putData("Auto mode", m_chooser);
		
		int absolutePosition2 = driveTalon2.getSelectedSensorPosition(Constants.kTimeoutMs) & 0xFFF;
		
		driveTalon2.setSelectedSensorPosition(absolutePosition2, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
		
		driveTalon2.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
		driveTalon2.setSensorPhase(true);
		
		driveTalon2.configNominalOutputForward(0, Constants.kTimeoutMs);
		driveTalon2.configNominalOutputReverse(0, Constants.kTimeoutMs);
		driveTalon2.configPeakOutputForward(1, Constants.kTimeoutMs);
		driveTalon2.configPeakOutputReverse(-1, Constants.kTimeoutMs);
		
		driveTalon2.configAllowableClosedloopError(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs); /* always servo */

		driveTalon2.config_kF(Constants.kPIDLoopIdx, 0.0, Constants.kTimeoutMs);
		driveTalon2.config_kP(Constants.kPIDLoopIdx, 0.1, Constants.kTimeoutMs);
		driveTalon2.config_kI(Constants.kPIDLoopIdx, 0.0, Constants.kTimeoutMs);
		driveTalon2.config_kD(Constants.kPIDLoopIdx, 0.0, Constants.kTimeoutMs);
		
		////////////////////////////////////////////////////////////////////////////////////////////
		
		int absolutePosition1 = driveTalon1.getSelectedSensorPosition(Constants.kTimeoutMs) & 0xFFF;
		
		driveTalon1.setSelectedSensorPosition(absolutePosition1, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
		
		driveTalon1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
		driveTalon1.setSensorPhase(true);
		
		driveTalon1.configNominalOutputForward(0, Constants.kTimeoutMs);
		driveTalon1.configNominalOutputReverse(0, Constants.kTimeoutMs);
		driveTalon1.configPeakOutputForward(1, Constants.kTimeoutMs);
		driveTalon1.configPeakOutputReverse(-1, Constants.kTimeoutMs);
		
		driveTalon1.configAllowableClosedloopError(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs); /* always servo */

		driveTalon1.config_kF(Constants.kPIDLoopIdx, 0.0, Constants.kTimeoutMs);
		driveTalon1.config_kP(Constants.kPIDLoopIdx, 0.1, Constants.kTimeoutMs);
		driveTalon1.config_kI(Constants.kPIDLoopIdx, 0.0, Constants.kTimeoutMs);
		driveTalon1.config_kD(Constants.kPIDLoopIdx, 0.0, Constants.kTimeoutMs);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		m_autonomousCommand = m_chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (m_autonomousCommand != null) {
			m_autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		
		double motorOutput2 = driveTalon2.getMotorOutputPercent();
		
		sb.append("\tout:");
		sb.append(motorOutput2);
        sb.append("\tpos:");
        sb.append(driveTalon2.getSelectedSensorPosition(0));
        
        targetPositionRotations2 = Constants.autonMotorPower * 50.0 * 4096;
        driveTalon2.set(ControlMode.Position, targetPositionRotations2);
        
        sb.append("\terrNative:");
    	sb.append(driveTalon2.getClosedLoopError(0));
    	sb.append("\ttrg:");
    	sb.append(targetPositionRotations2);
    	
    	////////////////////////////////////////////////////////////////////////////
    	
    	double motorOutput1 = driveTalon1.getMotorOutputPercent();
		
		sb.append("\tout:");
		sb.append(motorOutput1);
        sb.append("\tpos:");
        sb.append(driveTalon1.getSelectedSensorPosition(0));
        
        targetPositionRotations1 = Constants.autonMotorPower * 50.0 * 4096;
        driveTalon1.set(ControlMode.Position, targetPositionRotations1);
        
        sb.append("\terrNative:");
    	sb.append(driveTalon1.getClosedLoopError(0));
    	sb.append("\ttrg:");
    	sb.append(targetPositionRotations1);
    	
    	if(++loops >= 10) {
        	loops = 0;
        	SmartDashboard.putString("Auton info", sb.toString());
        }
    	
    	sb.setLength(0);
		
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		driveTalon1.set(left.getRawAxis(1) * 0.5);
		driveTalon2.set(right.getRawAxis(1) * 0.5);
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
