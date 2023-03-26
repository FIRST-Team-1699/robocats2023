package frc.team1699.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1699.subsystems.DriveTrain.DriveStates;
import frc.team1699.subsystems.Intake.IntakeStates;
import frc.team1699.subsystems.Manipulator.ManipulatorStates;

public class Autonomous {
    private SendableChooser<String> autonChooser;
    private final String doNothing = "Do Nothing";
    private final String scoreMobilityBalance = "Score, Mobility and Balance";
    private final String scoreAndMobility = "Score and Mobility";
    private final String scoreAndDoNothing = "Score and Do Nothing";
    private final String mobilityOnly = "Mobility Only";
    private final String scoreAndBalance = "Score and Balance";
    private final String balanceOnly = "Balance Only";
    private String autonChoice;

    public static boolean autoIsDone = false;

    private AutonStates currentState;

    private static int placingTicks = 0;
    private static int pivotingTicks = 0;
    // TODO: TUNE
    private final double kMobilityTaxiRotations = 55;
    private final double kPastChargeStationRotations = 55;
    private final double kToChargeStationRotations = 25;
    private final double kBackUpChargeStationRotations = 10;
    private final int kMaxPivotTicks = 90;

    // robot components
    private DriveTrain driveTrain;
    private Intake intake;
    private Manipulator manipulator;

    public Autonomous(DriveTrain driveTrain, Intake intake, Manipulator manipulator) {
        autonChooser = new SendableChooser<String>();
        autonChooser.setDefaultOption(doNothing, doNothing);
        autonChooser.addOption(scoreMobilityBalance, scoreMobilityBalance);
        autonChooser.addOption(scoreAndMobility, scoreAndMobility);
        autonChooser.addOption(scoreAndDoNothing, scoreAndDoNothing);
        autonChooser.addOption(mobilityOnly, mobilityOnly);
        autonChooser.addOption(scoreAndBalance, scoreAndBalance);
        autonChooser.addOption(balanceOnly, balanceOnly);
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
        autoIsDone = false;

    }

    public AutonStates getCurrentState() {
        return this.currentState;
    }

    public void update() {
        switch (autonChoice) {
            case doNothing:
            // WORKING
            // do nothing lol
            break;
            case scoreMobilityBalance:
                // NOT WORKING
                // score and mobility and balance
                switch (currentState) {
                    case STARTING:
                        if (pivotingTicks == 0) {
                            manipulator.setWantedState(ManipulatorStates.CUBE_HIGH);
                            intake.setWantedState(IntakeStates.IDLE);
                            pivotingTicks = 1;
                            placingTicks = 0;
                            System.out.println(getCurrentState());
                        } else if (manipulator.isDoneMoving() && pivotingTicks > kMaxPivotTicks) {
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
                        if (Math.abs(driveTrain.getEncoderRotations()[0]) <= kPastChargeStationRotations) {
                            driveTrain.runArcadeDrive(0.0, .45);
                            System.out.println("taxxiing");
                            System.out.println(driveTrain.getCurrentState());
                        } else {
                            driveTrain.runArcadeDrive(0, 0);
                            driveTrain.resetEncoders();
                            currentState = AutonStates.DRIVING_BACK;
                        }
                    break;
                    case DRIVING_BACK:
                        driveTrain.setWantedState(DriveStates.AUTONOMOUS);
                        if (Math.abs(driveTrain.getEncoderRotations()[0]) <= kBackUpChargeStationRotations + 15) {
                            driveTrain.runArcadeDrive(0.0, -.45);
                            System.out.println("driving back");
                            System.out.println(driveTrain.getCurrentState());
                        } else {
                            driveTrain.runArcadeDrive(0, 0);
                            currentState = AutonStates.BALANCING;
                            autoIsDone = true;
                        }
                    break;
                    case COLLECTING:
                    break;
                    case BALANCING:
                        driveTrain.setWantedState(DriveStates.AUTOBALANCE);
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
                // HIGH UNTESTED
                // mobility score mid ??
                switch (currentState) {
                    case STARTING:
                        if (manipulator.getCurrentState() != ManipulatorStates.CUBE_HIGH) {
                            manipulator.setWantedState(ManipulatorStates.CUBE_HIGH);
                            intake.setWantedState(IntakeStates.IDLE);
                            pivotingTicks = 0;
                            placingTicks = 0;
                        } else if (manipulator.isDoneMoving() && pivotingTicks > kMaxPivotTicks) {
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
                            autoIsDone = true;
                        }
                    break;
                    case DRIVING_BACK:
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
                        if (manipulator.getCurrentState() != ManipulatorStates.CUBE_HIGH) {
                            manipulator.setWantedState(ManipulatorStates.CUBE_HIGH);
                            intake.setWantedState(IntakeStates.IDLE);
                            pivotingTicks = 0;
                            placingTicks = 0;
                        } else if (manipulator.isDoneMoving() && pivotingTicks > kMaxPivotTicks) {
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
                            autoIsDone = true;
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
                    case DRIVING_BACK:
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
            case mobilityOnly:
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
                            autoIsDone = true;
                        }
                    break;
                    case DRIVING_BACK:
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
            case scoreAndBalance:
                // tested once, it works
                switch (currentState) {
                    case STARTING:
                        if (manipulator.getCurrentState() != ManipulatorStates.CUBE_HIGH) {
                            manipulator.setWantedState(ManipulatorStates.CUBE_HIGH);
                            intake.setWantedState(IntakeStates.IDLE);
                            pivotingTicks = 0;
                            placingTicks = 0;
                            System.out.println("setting manipulator pos");
                        } else if (manipulator.isDoneMoving() && pivotingTicks > kMaxPivotTicks) {
                            intake.setWantedState(IntakeStates.PLACING);
                            currentState = AutonStates.PLACING;
                            pivotingTicks = 0;
                            placingTicks = 0;
                            System.out.println("done!");
                        } else {
                            pivotingTicks++;
                            System.out.println("manipulator is moving");
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
                    case DRIVING_BACK:
                        System.out.println("Turning state reached (something went wrong");
                    break;
                    case COLLECTING:
                    break;
                    case BALANCING:
                        driveTrain.setWantedState(DriveStates.AUTOBALANCE);
                        autoIsDone = true;
                    break;
                    case DONE:
                        driveTrain.runArcadeDrive(0, 0);
                    break;
                    default:
                    break;
                }
            break;
            case balanceOnly:
                switch (currentState) {
                    case STARTING:
                        currentState = AutonStates.TAXIING;
                    break;
                    case PLACING:
                        System.out.println("placing state reached (something went wrong)");
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
                    case DRIVING_BACK:
                        System.out.println("Turning state reached (something went wrong");
                    break;
                    case COLLECTING:
                    break;
                    case BALANCING:
                        driveTrain.setWantedState(DriveStates.AUTOBALANCE);
                        autoIsDone = true;
                    break;
                    case DONE:
                        driveTrain.runArcadeDrive(0, 0);
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
        STARTING, PLACING, TAXIING, DRIVING_BACK, COLLECTING, BALANCING, DONE
    }
}
