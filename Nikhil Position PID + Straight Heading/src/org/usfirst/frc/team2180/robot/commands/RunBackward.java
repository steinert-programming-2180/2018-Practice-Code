package org.usfirst.frc.team2180.robot.commands;

import org.usfirst.frc.team2180.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RunBackward extends Command {

	boolean isUsingCuriosity;
	
    public RunBackward(boolean isUsingCuriosity) {
    	this.isUsingCuriosity = isUsingCuriosity;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	if (isUsingCuriosity) {
    		Robot.talon1.setInverted(false);
    		Robot.talon2.setInverted(false);
    		Robot.talon3.setInverted(false);
    	} else {
    		Robot.talon1.setInverted(false);
    		Robot.talon2.setInverted(false);
    	}
    	
		Robot.talon1.setSelectedSensorPosition(0, 0, 10);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		
		Robot.talon1.set(ControlMode.Position, 14400);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (14400 - (Robot.talon1.getSelectedSensorPosition(0)) < 40) {
    		return true;
    	}
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}