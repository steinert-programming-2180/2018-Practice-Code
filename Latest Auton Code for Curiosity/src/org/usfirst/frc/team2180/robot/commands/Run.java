package org.usfirst.frc.team2180.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class Run extends CommandGroup {

    public Run(boolean isUsingCuriosity) {
    	addSequential(new RunForward(14400));
//    	addSequential(new RunBackward(isUsingCuriosity));
    	addSequential(new Turn(180));
//    	addSequential(new RunForward(1440));
//    	addSequential(new Turn(90));
    	addSequential(new RunForward(14400));
    	addSequential(new Turn(180));
    	addSequential(new RunForward(3000));
    }
}