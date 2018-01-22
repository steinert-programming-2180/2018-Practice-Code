package org.usfirst.frc.team2180.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class MoveAwayFromAllianceWall extends Command {
	
	public int pos;
    public MoveAwayFromAllianceWall(int pos) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	
    	this.pos = pos;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	if (pos == 1 || pos == 3) {
    		setTimeout((Constants.autoLineDistance + 30)/Constants.robotSpeed); //this command will stop after 1.5 seconds
    	} else {
    		
    		//120 is distance from alliance wall to auto line
    		//20 is how many inches the pile of cubes is from the auto line
    		//the other 20 is for wiggle room while turning
    		setTimeout((Constants.autoLineDistance-20-20-Constants.robotLength)/(Constants.robotSpeed*0.5));
    	}
    	Robot.gyro.reset();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.guppyDrive.tankDrive(0.5, (Robot.gyro.getAngle()*Constants.Kp) + 0.5);
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
