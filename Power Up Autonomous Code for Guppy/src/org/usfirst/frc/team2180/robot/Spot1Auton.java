package org.usfirst.frc.team2180.robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Spot1Auton extends CommandGroup {
	
	//robot should be lined up with portal
	public Spot1Auton() {
		if (UsefulFunctions.switchIsLeftSideInAuton()) {
			addSequential(new MoveAwayFromAllianceWall(1)); //position 1
			addSequential(new TurnTowardSwitch(1));
			addSequential(new MoveTowardSwitchPlate());
		} else {
			addSequential(new MoveForwardSlightly());
			addSequential(new TurnTowardSwitch(1)); //can be reused for turning ninety degrees
			addSequential(new DriveAutonomously(264 - (Constants.robotLength/2), true)); //drive parallel to switch
			addSequential(new TurnTowardSwitch(3)); //can be reused, must be changed later
			addSequential(new DriveAutonomously(120 - 50 - (Constants.robotLength/2), true));
			addSequential(new TurnTowardSwitch(3)); //can be reused, must be changed later
			addSequential(new MoveTowardSwitchPlate());
		}
	}
}