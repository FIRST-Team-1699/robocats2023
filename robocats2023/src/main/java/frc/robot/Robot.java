// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import frc.team1699.subsystems.Intake;
import frc.team1699.subsystems.Manipulator;
import frc.team1699.subsystems.Intake.IntakeStates;
import frc.team1699.subsystems.Manipulator.ManipulatorStates;
import frc.team1699.subsystems.Plow;
import frc.team1699.subsystems.Plow.PlowStates;
import frc.team1699.Constants;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Joystick driveJoystick;
  private Manipulator manipulator;
  private Intake intake;
  private Plow plow;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    driveJoystick = new Joystick(Constants.kDriveJoystickPort);
    manipulator = new Manipulator();
    intake = new Intake();
    plow = new Plow();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {}

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    if(driveJoystick.getRawButton(1)){
      intake.setWantedState(IntakeStates.INTAKING);
    }

    if(driveJoystick.getRawButton(2)){
      intake.setWantedState(IntakeStates.PLACING);
    }

    if(driveJoystick.getRawButtonReleased(1) || driveJoystick.getRawButtonReleased(2)){
      intake.setWantedState(IntakeStates.IDLE);
    }

    if(driveJoystick.getRawButtonPressed(8)){
      System.out.println(intake.getCurrentState());
    }

    if(driveJoystick.getRawButton(3)){
      manipulator.setWantedState(ManipulatorStates.HIGH);
    }

    if(driveJoystick.getRawButton(4)){
      manipulator.setWantedState(ManipulatorStates.LOW);
    }

    if(driveJoystick.getRawButtonReleased(3) || driveJoystick.getRawButtonReleased(4)){
      manipulator.setWantedState(ManipulatorStates.RETRACTED);
    }

    if(driveJoystick.getRawButton(6)) {
      plow.setWantedState(PlowStates.OUT);
    } else {
      plow.setWantedState(PlowStates.IN);
    }
    
    manipulator.update();
    intake.update();
    plow.update();
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
