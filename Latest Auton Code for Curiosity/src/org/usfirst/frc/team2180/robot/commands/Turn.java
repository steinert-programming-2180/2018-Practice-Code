package org.usfirst.frc.team2180.robot.commands;

import org.usfirst.frc.team2180.robot.Constants;
import org.usfirst.frc.team2180.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// pls try implementing PID for gyros + turning, future Nikhil

public class Turn extends Command {
	
	int angle;
	
	/**
	 * Turns the robot to a specified angle during the autonomous period.
	 * 
	 * @param  angle  the angle to which the robot turns to
	 */
    public Turn(int angle) {
    	this.angle = angle;
    }

    protected void initialize() {
    	Robot.gyroPID.setSetpoint(angle);
    	Robot.gyroPID.enable();
    	
    	Robot.gyro.reset();
    	// No need to reset the Gyro. you don't want error from the previous command to carry over to this one.
    }

    protected void execute() {
//    	Robot.talon1.set(ControlMode.PercentOutput, Constants.autonSpeed + ((angle - Math.abs(Robot.gyro.getAngle())) * 0.003));
//    	Robot.regTalon1.set(Constants.autonSpeed + (-(angle - Math.abs(Robot.gyro.getAngle())) * 0.003));
    	
    	Robot.talon1.set(ControlMode.PercentOutput, -Robot.gyroPID.get());
    	Robot.regTalon1.set(Robot.gyroPID.get());
    	
    	SmartDashboard.putNumber("Turning speed", Robot.gyroPID.get());
    	
    	SmartDashboard.putNumber("Auton gyro error", Robot.gyroPID.getError());
    	
    	SmartDashboard.putNumber("Gyro setpoint", Robot.gyroPID.getSetpoint());
    	
    	SmartDashboard.putNumber("Gyro angle", Robot.gyro.getAngle());
    }

    protected boolean isFinished() {
    	if (Robot.gyroPID.onTarget()) {
    		return true;
    	}
    	return false;
    }

    protected void end() {
    	Robot.gyroPID.disable();
    	Robot.gyroPID.reset();
    	Robot.talon1.set(ControlMode.PercentOutput, 0.0);
    	Robot.regTalon1.set(0.0);
    }

    protected void interrupted() {
    	end();
    }
}
