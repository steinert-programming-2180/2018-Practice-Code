package org.usfirst.frc.team2180.robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Spot2Auton extends CommandGroup {
	
	public Spot2Auton() {
		
		if (UsefulFunctions.switchIsLeftSideInAuton()) {
			addSequential(new MoveAwayFromAllianceWall(2)); //position 2
		} else {
			
		}
	}
}