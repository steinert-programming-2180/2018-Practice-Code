package org.usfirst.frc.team2180.robot.commands;

import org.usfirst.frc.team2180.robot.Constants;
import org.usfirst.frc.team2180.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RunForward extends Command {
	
	int ticks;
	
    public RunForward(int ticks) {
        this.ticks = ticks;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	Robot.talon1.configPeakOutputForward(Constants.autonSpeed, 10);
		Robot.talon1.configPeakOutputReverse(-Constants.autonSpeed, 10);
    	
//    	if (isUsingCuriosity) {
		Robot.talon1.setInverted(true);
		Robot.talon2.setInverted(true);
		Robot.talon3.setInverted(true);
		
		Robot.regTalon1.setInverted(false);
		Robot.regTalon2.setInverted(false);
		Robot.regTalon3.setInverted(false);
//    	} 
//    	else {
//    		Robot.talon1.setInverted(true);
//    		Robot.talon2.setInverted(true);
//    	}
    	
    	Robot.talon1.setSelectedSensorPosition(0, 0, 10);
    	
    	Robot.gyro.reset();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	Robot.talon1.set(ControlMode.Position, ticks);
    	Robot.regTalon1.set(Constants.autonSpeed - (Robot.gyro.getAngle() * 0.03));
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (ticks - (Robot.talon1.getSelectedSensorPosition(0)) < 40) {
    		Robot.regTalon1.set(0.0);
    		return true;
    	}
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	SmartDashboard.putString("PID Status", "Done");
    	Robot.talon1.set(ControlMode.PercentOutput, 0.0);
    	
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}