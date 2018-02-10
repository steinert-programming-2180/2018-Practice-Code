/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2180.robot.subsystems;

import org.usfirst.frc.team2180.robot.Robot;

import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class Drivetrain extends PIDSubsystem {
	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	
	public Drivetrain() {
		super("Drivetrain", 1.0, 0.0, 0.0);
		setAbsoluteTolerance(0.05);
        getPIDController().setContinuous(false);
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	@Override
	protected double returnPIDInput() {
		return ((Robot.talon1.getSensorCollection().getPulseWidthPosition() / 4) & 0xFFF);
	}

	@Override
	protected void usePIDOutput(double output) {
		
		Robot.talon1.pidWrite(output);
	}
}