package org.usfirst.frc.team2180.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class Run extends CommandGroup {

    public Run(boolean isUsingCuriosity) {
    	addSequential(new RunForward(isUsingCuriosity));
//    	addSequential(new RunBackward(isUsingCuriosity));
    	addSequential(new Turn(90));
    }
}