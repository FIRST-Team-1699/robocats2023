package frc.team1699.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1699.subsystems.DriveTrain.DriveStates;
import frc.team1699.subsystems.Intake.IntakeStates;
import frc.team1699.subsystems.Manipulator.ManipulatorStates;

public class Autonomous {
    private SendableChooser<String> autonChooser;
    private final String choiceOne = "Do Nothing";
    private final String choiceTwo = "Score and Balance";
    private final String scoreAndMobility = "Score and Mobility";
    private final String scoreAndDoNothing = "Score and Do Nothing";
    private final String choiceFive = "Mobility Only";
    private final String choiceSix = "Score and Pickup";
    private String autonChoice;

    private AutonStates currentState;

    private static int placingTicks = 0;
    private static int pivotingTicks = 0;
    private static double rotationHeading = 0;
    // TODO: TUNE
    private final double kMobilityTaxiRotations = 55;
    private final double kToChargeStationRotations = 25;

    // robot components
    private DriveTrain driveTrain;
    private Intake intake;
    private Manipulator manipulator;

    public Autonomous(DriveTrain driveTrain, Intake intake, Manipulator manipulator) {
        autonChooser = new SendableChooser<String>();
        autonChooser.setDefaultOption(choiceOne, choiceOne);
        autonChooser.addOption(choiceTwo, choiceTwo);
        autonChooser.addOption(scoreAndMobility, scoreAndMobility);
        autonChooser.addOption(scoreAndDoNothing, scoreAndDoNothing);
        autonChooser.addOption(choiceFive, choiceFive);
        autonChooser.addOption(choiceSix, choiceSix);
        SmartDashboard.putData("Autonomous Choices", autonChooser);

        this.driveTrain = driveTrain;
        this.intake = intake;
        this.manipulator = manipulator;

        currentState = AutonStates.STARTING;
    }

    public void prepareForAuto() {
        this.autonChoice = autonChooser.getSelected();
        driveTrain.resetEncoders();
        driveTrain.setWantedState(DriveStates.AUTONOMOUS);
        manipulator.setWantedState(ManipulatorStates.STORED);
        intake.setWantedState(IntakeStates.IDLE);
        currentState = AutonStates.STARTING;
        
    }

    public void update() {
        switch (autonChoice) {
            case choiceOne:
                // WORKING
                // do nothing lol
            break;
            case choiceTwo:
                // NOT WORKING, NO BALANCE ALSO NO SCORE
                // score and balance
                switch (currentState) {
                    case STARTING:
                        manipulator.setWantedState(ManipulatorStates.HIGH);
                        if (manipulator.isDoneMoving()) {
                            intake.setWantedState(IntakeStates.PLACING);
                            currentState = AutonStates.PLACING;
                        }
                    break;
                    case PLACING:
                        if (placingTicks < 50) {
                            if (intake.getCurrentState() != IntakeStates.PLACING_AUTO) {
                                intake.setWantedState(IntakeStates.PLACING_AUTO);
                            }
                            placingTicks++;
                        } else {
                            if (intake.getCurrentState() == IntakeStates.PLACING) {
                                intake.setWantedState(IntakeStates.IDLE);
                            }
                            if (manipulator.getCurrentState() != ManipulatorStates.STORED) {
                                manipulator.setWantedState(ManipulatorStates.STORED);
                            } else {
                                if (manipulator.isDoneMoving()) {
                                    currentState = AutonStates.TAXIING;
                                }
                            }
                        }
                    break;
                    case TAXIING:
                        if (Math.abs(driveTrain.getPitch()) > DriveTrain.kBalanceTolerance) {
                            currentState = AutonStates.BALANCING;
                        } else {
                            driveTrain.runArcadeDrive(0, -.3);
                        }
                    break;
                    case TURNING:
                        System.out.println("Turning state reached (something went wrong");
                    break;
                    case COLLECTING:
                        System.out.println("Collecting state reached (something went wrong");
                    break;
                    case BALANCING:
                        if (driveTrain.getCurrentState() != DriveStates.AUTOBALANCE) {
                            driveTrain.setWantedState(DriveStates.AUTOBALANCE);
                        }
                    break;
                    case DONE:
                        driveTrain.runArcadeDrive(0, 0);
                    break;
                    default:
                    break;
                }
            break;
            case scoreAndMobility:
                // WORKING
                // mobility score mid ??
                switch (currentState) {
                    case STARTING:
                        if(manipulator.getCurrentState() != ManipulatorStates.CUBE_MID) {
                            manipulator.setWantedState(ManipulatorStates.CUBE_MID);
                            intake.setWantedState(IntakeStates.IDLE);
                            pivotingTicks = 0;
                            placingTicks = 0;
                        } else if (manipulator.isDoneMoving() && pivotingTicks > 25) {
                            intake.setWantedState(IntakeStates.PLACING);
                            currentState = AutonStates.PLACING;
                            pivotingTicks = 0;
                            placingTicks = 0;
                        } else {
                            pivotingTicks++;
                        }
                    break;
                    case PLACING:
                        if (placingTicks < 50) {
                            if (intake.getCurrentState() != IntakeStates.PLACING) {
                                intake.setWantedState(IntakeStates.PLACING);
                            }
                            placingTicks++;
                        } else {
                            if (intake.getCurrentState() == IntakeStates.PLACING) {
                                intake.setWantedState(IntakeStates.IDLE);
                            }
                            if (manipulator.getCurrentState() != ManipulatorStates.STORED) {
                                manipulator.setWantedState(ManipulatorStates.STORED);
                            } else {
                                if (manipulator.isDoneMoving()) {
                                    currentState = AutonStates.TAXIING;
                                }
                            }
                        }
                    break;
                    case TAXIING:
                        driveTrain.setWantedState(DriveStates.AUTONOMOUS);
                        if (Math.abs(driveTrain.getEncoderRotations()[0]) <= kMobilityTaxiRotations) {
                            driveTrain.runArcadeDrive(0.0, .5);
                            System.out.println("taxxiing");
                            System.out.println(driveTrain.getCurrentState());
                        } else {
                            driveTrain.runArcadeDrive(0, 0);
                            currentState = AutonStates.DONE;
                        }
                    break;
                    case TURNING:
                        System.out.println("Turning state reached (something went wrong");
                    break;
                    case COLLECTING:
                        System.out.println("Collecting state reached (something went wrong");
                    break;
                    case BALANCING:
                        System.out.println("Balancing state reached (something went wrong )");
                    break;
                    case DONE:
                    break;
                    default:
                    break;
                }
            break;
            case scoreAndDoNothing:
            // WORKING
            // score cube mid do nothing
                switch (currentState) {
                    case STARTING:
                        if(manipulator.getCurrentState() != ManipulatorStates.CUBE_MID) {
                            manipulator.setWantedState(ManipulatorStates.CUBE_MID);
                            intake.setWantedState(IntakeStates.IDLE);
                            pivotingTicks = 0;
                            placingTicks = 0;
                        } else if (manipulator.isDoneMoving() && pivotingTicks > 25) {
                            intake.setWantedState(IntakeStates.PLACING);
                            currentState = AutonStates.PLACING;
                            pivotingTicks = 0;
                            placingTicks = 0;
                        } else {
                            pivotingTicks++;
                        }
                    break;
                    case PLACING:
                        if (placingTicks < 50) {
                            if (intake.getCurrentState() != IntakeStates.PLACING) {
                                intake.setWantedState(IntakeStates.PLACING);
                            }
                            placingTicks++;
                        } else {
                            if (intake.getCurrentState() == IntakeStates.PLACING) {
                                intake.setWantedState(IntakeStates.IDLE);
                            }
                            if (manipulator.getCurrentState() != ManipulatorStates.STORED) {
                                manipulator.setWantedState(ManipulatorStates.STORED);
                            } else {
                                if (manipulator.isDoneMoving()) {
                                    currentState = AutonStates.DONE;
                                }
                            }
                        }
                    break;
                    case TAXIING:
                        System.out.println("Taxiing state reached (something went wrong )");
                    break;
                    case TURNING:
                        System.out.println("Turning state reached (something went wrong");
                    break;
                    case COLLECTING:
                        System.out.println("Collecting state reached (something went wrong");
                    break;
                    case BALANCING:
                        System.out.println("Balancing state reached (something went wrong )");
                    break;
                    case DONE:
                    break;
                    default:
                    break;
                }
            break;
            case choiceFive:
                // WORKING
                // mobility only
                switch (currentState) {
                    case STARTING:
                        currentState = AutonStates.TAXIING;
                    break;
                    case PLACING:
                        System.out.println("Placing state reached (something went wrong )");
                    break;
                    case TAXIING:
                    driveTrain.setWantedState(DriveStates.AUTONOMOUS);
                        if (Math.abs(driveTrain.getEncoderRotations()[0]) <= kMobilityTaxiRotations) {
                            driveTrain.runArcadeDrive(0.0, .5);
                            System.out.println("taxxiing");
                            System.out.println(driveTrain.getCurrentState());
                        } else {
                            driveTrain.runArcadeDrive(0, 0);
                            currentState = AutonStates.DONE;
                        }
                    break;
                    case TURNING:
                        System.out.println("Turning state reached (something went wrong");
                    break;
                    case COLLECTING:
                        System.out.println("Collecting state reached (something went wrong");
                    break;
                    case BALANCING:
                        System.out.println("Balancing state reached (something went wrong)");
                    break;
                    case DONE:
                        driveTrain.runArcadeDrive(0, 0);
                    break;
                    default:
                    break;
                }
            break;
            case choiceSix:
                // NOT WORKING MAYBE STRETCH GOAL
                switch (currentState) {
                    case STARTING:
                        if(manipulator.getCurrentState() != ManipulatorStates.CUBE_MID) {
                            manipulator.setWantedState(ManipulatorStates.CUBE_MID);
                            intake.setWantedState(IntakeStates.IDLE);
                            pivotingTicks = 0;
                            placingTicks = 0;
                        } else if (manipulator.isDoneMoving() && pivotingTicks > 25) {
                            intake.setWantedState(IntakeStates.PLACING);
                            currentState = AutonStates.PLACING;
                            pivotingTicks = 0;
                            placingTicks = 0;
                        } else {
                            pivotingTicks++;
                        }
                    break;
                    case PLACING:
                        if (placingTicks < 50) {
                            if (intake.getCurrentState() != IntakeStates.PLACING) {
                                intake.setWantedState(IntakeStates.PLACING);
                            }
                            placingTicks++;
                        } else {
                            if (intake.getCurrentState() == IntakeStates.PLACING) {
                                intake.setWantedState(IntakeStates.IDLE);
                            }
                            if (manipulator.getCurrentState() != ManipulatorStates.STORED) {
                                manipulator.setWantedState(ManipulatorStates.STORED);
                            } else {
                                if (manipulator.isDoneMoving()) {
                                    currentState = AutonStates.TAXIING;
                                }
                            }
                        }
                    break;
                    case TAXIING:
                        driveTrain.setWantedState(DriveStates.AUTONOMOUS);
                        if (Math.abs(driveTrain.getEncoderRotations()[0]) <= kToChargeStationRotations) {
                            driveTrain.runArcadeDrive(0.0, .4);
                            System.out.println("taxxiing");
                            System.out.println(driveTrain.getCurrentState());
                        } else {
                            driveTrain.runArcadeDrive(0, 0);
                            currentState = AutonStates.BALANCING;
                        }
                    break;
                    case TURNING:
                        System.out.println("Turning state reached (something went wrong");
                    break;
                    case COLLECTING:
                        if (manipulator.getCurrentState() != ManipulatorStates.FLOOR || intake.getCurrentState() != IntakeStates.INTAKING) {
                            manipulator.setWantedState(ManipulatorStates.FLOOR);
                        } else {
                            System.out.println("Picking up I hope");
                        }
                    break;
                    case BALANCING:
                        driveTrain.setWantedState(DriveStates.AUTOBALANCE);
                        System.out.println(driveTrain.getCurrentState());
                        System.out.println("balancing!!");
                    break;
                    case DONE:
                        driveTrain.runArcadeDrive(0,0);
                    break;
                    default:
                    break;
                }
            break;
            default:
            break;
        }
        driveTrain.update();
        manipulator.update();
        intake.update();
    }

    // All possible autonomous states
    public enum AutonStates {
        STARTING,
        PLACING,
        TAXIING,
        TURNING,
        COLLECTING,
        BALANCING,
        DONE
    }
}
