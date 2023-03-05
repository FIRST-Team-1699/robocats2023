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
    private final String choiceThree = "Score and Mobility";
    private final String choiceFour = "Score and Do Nothing";
    private final String choiceFive = "Mobility Only";
    private final String choiceSix = "Score and Pickup";
    private String autonChoice;

    private AutonStates currentState;

    private static int placingTicks = 0;
    private static double rotationHeading = 0;
    // TODO: TUNE
    private final int kMobilityTaxiRotations = 500;

    // robot components
    private DriveTrain driveTrain;
    private Intake intake;
    private Manipulator manipulator;

    public Autonomous(DriveTrain driveTrain, Intake intake, Manipulator manipulator) {
        autonChooser = new SendableChooser<String>();
        autonChooser.setDefaultOption(choiceOne, choiceOne);
        autonChooser.addOption(choiceTwo, choiceTwo);
        autonChooser.addOption(choiceThree, choiceThree);
        autonChooser.addOption(choiceFour, choiceFour);
        autonChooser.addOption(choiceFive, choiceFive);
        autonChooser.addOption(choiceSix, choiceSix);
        SmartDashboard.putData("Autonomous Choices", autonChooser);

        this.driveTrain = driveTrain;
        this.intake = intake;
        this.manipulator = manipulator;

        currentState = AutonStates.STARTING;
    }

    public void takeChosenAuto() {
        this.autonChoice = autonChooser.getSelected();
    }

    public void update() {
        switch (autonChoice) {
            case choiceOne:
                // do nothing lol
            break;
            case choiceTwo:
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
            case choiceThree:
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
                                    driveTrain.resetEncoders();
                                }
                            }
                        }
                    break;
                    case TAXIING:
                        if (driveTrain.getEncoderRotations()[0] <= kMobilityTaxiRotations) {
                            driveTrain.runArcadeDrive(0, .3);
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
                        driveTrain.runArcadeDrive(0,0);
                    break;
                    default:
                    break;
                }
            break;
            case choiceFour:
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
                                    driveTrain.resetEncoders();
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
                switch (currentState) {
                    case STARTING:
                        currentState = AutonStates.TAXIING;
                        driveTrain.resetEncoders();
                    break;
                    case PLACING:
                        System.out.println("Placing state reached (something went wrong )");
                    break;
                    case TAXIING:
                        if (driveTrain.getEncoderRotations()[0] <= kMobilityTaxiRotations) {
                            driveTrain.runArcadeDrive(0, .3);
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
                        System.out.println("Done");
                        driveTrain.runArcadeDrive(0, 0);
                    break;
                    default:
                    break;
                }
            break;
            case choiceSix:
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
                                    driveTrain.resetEncoders();
                                }
                            }
                        }
                    break;
                    case TAXIING:
                        if (driveTrain.getEncoderRotations()[0] <= kMobilityTaxiRotations) {
                            driveTrain.runArcadeDrive(0, .3);
                        } else {
                            driveTrain.runArcadeDrive(0, 0);
                            currentState = AutonStates.TURNING;
                            rotationHeading = driveTrain.getYaw();
                        }
                    break;
                    case TURNING:
                        if (Math.abs(driveTrain.getYaw()) + 2.0 > Math.abs(rotationHeading)) {
                            driveTrain.runArcadeDrive(.3, 0);
                        } else {
                            currentState = AutonStates.COLLECTING;
                        }
                    break;
                    case COLLECTING:
                        if (manipulator.getCurrentState() != ManipulatorStates.FLOOR || intake.getCurrentState() != IntakeStates.INTAKING) {
                            manipulator.setWantedState(ManipulatorStates.FLOOR);
                        } else {
                            System.out.println("Picking up I hope");
                        }
                    break;
                    case BALANCING:
                        System.out.println("Balancing state reached (something went wrong )");
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
