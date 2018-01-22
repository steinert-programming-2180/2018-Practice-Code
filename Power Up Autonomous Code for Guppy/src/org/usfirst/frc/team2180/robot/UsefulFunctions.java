package org.usfirst.frc.team2180.robot;

public class UsefulFunctions {
	
	public static boolean switchIsLeftSideInAuton() {
		String gameData = Robot.gameData;
		
		if (gameData.charAt(0) == 'L') {
			return true;
		}
		
		return false;
	}
	
}
