package org.usfirst.frc.team2180.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class MoveTowardSwitchPlate extends Command {

    public MoveTowardSwitchPlate() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	setTimeout(2.5); //the robot will align itself by ramming into the switch
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.guppyDrive.tankDrive(0.5, 0.5);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
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
