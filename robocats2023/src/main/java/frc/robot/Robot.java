// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import frc.team1699.subsystems.Autonomous;
import frc.team1699.subsystems.DriveTrain;
import frc.team1699.subsystems.Intake;
import frc.team1699.subsystems.Manipulator;
import frc.team1699.subsystems.Intake.IntakeStates;
import frc.team1699.subsystems.Manipulator.ManipulatorStates;
import frc.team1699.utils.leds.LEDController;
import frc.team1699.utils.leds.colors.*;
//import frc.team1699.subsystems.Plow;
import frc.team1699.subsystems.DriveTrain.DriveStates;
//import frc.team1699.subsystems.Plow.PlowStates;
import frc.team1699.Constants;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  // Input
  private Joystick driveJoystick, opJoystick;

  // Subsystems
  private Manipulator manipulator;
  private Intake intake;
  // private Plow plow;
  private DriveTrain driveTrain;
  private Autonomous autonomous;

  // Extra
  private LEDController ledController;
  private int busOneStart = 0;
  private int busTwoStart = 43;

  private final int kCoefficientOfPeck = 19;

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Input
    driveJoystick = new Joystick(Constants.kDriveJoystickPort);
    opJoystick = new Joystick(Constants.kOperatorJoystickPort);

    // Subsystems
    manipulator = new Manipulator();
    intake = new Intake();
    // plow = new Plow();
    driveTrain = new DriveTrain(driveJoystick);
    driveTrain.calibrateGyro();
    autonomous = new Autonomous(driveTrain, intake, manipulator);

    // // Extras
    ledController = new LEDController(86, Constants.kLEDPort);
    ledController.alternateColors(new Blue(), new Yellow());
    ledController.start();
    CameraServer.startAutomaticCapture();
  }

  @Override
  public void robotPeriodic() {
    // uncomment for busses

    // ledController.rainbow();
  }

  @Override
  public void autonomousInit() {
    driveTrain.enableBrakeMode();
    autonomous.prepareForAuto();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    autonomous.update();
    ledController.bus(new Blue(), 43, busOneStart);
    ledController.bus(new Yellow(), 43, busTwoStart);
    busOneStart++;
    busTwoStart++;
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    ledController.alternateColors(new Yellow(), new Blue());
    driveTrain.setWantedState(DriveStates.MANUAL);
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // DRIVER STICK
    if (driveJoystick.getRawButton(3)) {
      intake.setWantedState(IntakeStates.INTAKING);
    }

    if (driveJoystick.getRawButton(4)) {
      intake.setWantedState(IntakeStates.PLACING);
    }

    if (driveJoystick.getRawButtonReleased(3) || driveJoystick.getRawButtonReleased(4)) {
      intake.setWantedState(IntakeStates.IDLE);
    }

    // if(driveJoystick.getRawButtonPressed(5)) {
    // if(plow.getCurrentState() == PlowStates.OUT) {
    // plow.setWantedState(PlowStates.IN);
    // } else {
    // plow.setWantedState(PlowStates.OUT);
    // }
    // }

    if (driveJoystick.getRawButtonPressed(11)) {
      driveTrain.setWantedState(DriveStates.AUTOBALANCE);
    }

    if (driveJoystick.getRawButtonReleased(11)) {
      driveTrain.setWantedState(DriveStates.MANUAL);
    }

    if (driveJoystick.getPOV() == 0) {
      manipulator.incrementTelescopePosition();
    }

    if (driveJoystick.getPOV() == 180) {
      manipulator.decrementTelescopePosition();
    }

    if (driveJoystick.getPOV() == 90) {
      manipulator.incrementPivotPosition();
    }

    if (driveJoystick.getPOV() == 270) {
      manipulator.decrementPivotPosition();
    }

    // OPERATOR STICK
    // FLOOR POSITION
    if (opJoystick.getRawButtonPressed(3)) {
      manipulator.setWantedState(ManipulatorStates.FLOOR);
    }

    if (opJoystick.getPOV() == 0) {
      manipulator.incrementTelescopePosition();
    }

    if (opJoystick.getPOV() == 180) {
      manipulator.decrementTelescopePosition();
    }

    if (opJoystick.getPOV() == 90) {
      manipulator.incrementPivotPosition();
    }

    if (opJoystick.getPOV() == 270) {
      manipulator.decrementPivotPosition();
    }

    // LOW
    if (opJoystick.getRawButtonPressed(11)) {
      manipulator.setWantedState(ManipulatorStates.CUBE_MID);
    }

    // // MID
    // if (opJoystick.getRawButtonPressed(9)) {
    // manipulator.setWantedState(ManipulatorStates.CUBE_MID);
    // }
    // HIGH SHOOT CUBE
    if (opJoystick.getRawButton(9)) {
      manipulator.setWantedState(ManipulatorStates.CUBE_HIGH);
    }

    // HIGH
    if (opJoystick.getRawButtonPressed(7)) {
      manipulator.setWantedState(ManipulatorStates.HIGH);
    }

    // SHELF
    if (opJoystick.getRawButtonPressed(12)) {
      manipulator.setWantedState(ManipulatorStates.SHELF);
    }

    // STORED BACK
    if (opJoystick.getRawButtonPressed(10)) {
      manipulator.setWantedState(ManipulatorStates.STORED);
    }

    // STORED FRONT
    // if(opJoystick.getRawButtonPressed(8)) {
    // manipulator.setWantedState(ManipulatorStates.STORED_FRONT);
    // }

    // if(opJoystick.getRawButton(6)) {
    // manipulator.incrementPivotPosition();
    // }

    // if(opJoystick.getRawButton(5)) {
    // manipulator.decrementPivotPosition();
    // }

    // PECK PECK
    if (opJoystick.getRawButtonPressed(5)) {
      for (int i = 0; i < kCoefficientOfPeck; i++) {
        manipulator.incrementPivotPosition();
      }
    }

    if (opJoystick.getRawButtonReleased(5)) {
      for (int i = 0; i < kCoefficientOfPeck; i++) {
        manipulator.decrementPivotPosition();
      }
    }

    if (opJoystick.getRawButton(2)) {
      manipulator.resetTelescopeEncoder();
      manipulator.resetPivotEncoder();
    }

    if (opJoystick.getRawButtonPressed(1)) {
      colorSwitch++;
      if(colorSwitch % 3 == 0) {
        ledController.alternateColors(new Yellow(), new Blue());
      } else if(colorSwitch % 3 == 1) {
        ledController.solidColor(new Purple());
      } else {
        ledController.solidColor(new Orange());
      }
    }

    manipulator.update();
    intake.update();
    // plow.update();
    driveTrain.update();
    manipulator.setBrakeMode();
    driveTrain.enableBrakeMode();
  }
  private int colorSwitch = 0;

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
    ledController.bus(new Blue(), 43, busOneStart);
    ledController.bus(new Yellow(), 43, busTwoStart);
    busOneStart++;
    busTwoStart++;
  }

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {

  }

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {
  }

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {
  }
}
