package org.usfirst.frc.team2180.robot.commands;

import org.usfirst.frc.team2180.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RunForward extends Command {

    public RunForward() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.talon1.setInverted(true);
		Robot.talon2.setInverted(true);
		Robot.talon3.setInverted(true);
    	Robot.talon1.setSelectedSensorPosition(0, 0, 10);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	SmartDashboard.putString("PID Status", "Forward");
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
