package org.usfirst.frc.team2180.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TurnTowardSwitch extends Command {
	
	public int pos;
	
    public TurnTowardSwitch(int pos) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	
    	this.pos = pos;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	setTimeout(5);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (pos == 1) {
    		Robot.guppyDrive.tankDrive(0.5, -0.5);
    	} else {
    		Robot.guppyDrive.tankDrive(-0.5, 0.5);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (pos == 1 && Robot.gyro.getAngle() > 90.0) {
    		return true;
    	} else if (pos == 3 && Robot.gyro.getAngle() < -90.0) {
    		return true;
    	}
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.guppyDrive.tankDrive(0.0, 0.0);
    	Timer.delay(0.5);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
