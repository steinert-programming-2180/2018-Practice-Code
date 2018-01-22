package org.usfirst.frc.team2180.robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveAutonomously extends Command {
	
	public double distance;
	public boolean isForward;
	
    public DriveAutonomously(double distance, boolean isForward) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	this.distance = distance;
    	this.isForward = isForward;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	setTimeout(distance/(Constants.robotSpeed*0.5));
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (isForward) {
    		Robot.guppyDrive.tankDrive(0.5, 0.5 + (Robot.gyro.getAngle()*Constants.Kp));
    	} else {
    		Robot.guppyDrive.tankDrive(-0.5, -0.5 + (Robot.gyro.getAngle()*Constants.Kp));
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.guppyDrive.tankDrive(0.0, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
