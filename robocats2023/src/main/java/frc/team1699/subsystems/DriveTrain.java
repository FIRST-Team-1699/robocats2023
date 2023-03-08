package frc.team1699.subsystems;

import frc.team1699.Constants;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Joystick;

public class DriveTrain {
    private final double kDeadZone = 0;
    private DriveStates wantedState, currentState;

    // MOTORS
    private CANSparkMax portLeader, portFollowerOne, portFollowerTwo;
    private CANSparkMax starLeader, starFollowerOne, starFollowerTwo;
    private RelativeEncoder portEncoder, starEncoder;

    private Joystick joystick;
    private AHRS gyro;
    //balancing constants
    private final double kBalanceP = .35;
    private final double kBalanceI = 0.0;
    private final double kBalanceD = 0.0;
    private final double kLevelPitch = 0.0;
    public static final double kBalanceTolerance = 2.5;
    private PIDController balanceController;

    // centering constants
    private final double kCenterP = 0.0;
    private final double kCenterI = 0.0;
    private final double kCenterD = 0.0;
    private final double kCenterTolerance = 2.0;
    private PIDController centerController;

    private double centerHeading = 0.0;

    public DriveTrain(Joystick joystick) {
        portLeader = new CANSparkMax(Constants.kPortLeaderID, MotorType.kBrushless);
        portFollowerOne = new CANSparkMax(Constants.kPortFollowerOneID, MotorType.kBrushless);
        portFollowerTwo = new CANSparkMax(Constants.kPortFollowerTwoID, MotorType.kBrushless);
        starLeader = new CANSparkMax(Constants.kStarLeaderID, MotorType.kBrushless);
        starFollowerOne = new CANSparkMax(Constants.kStarFollowerOneID, MotorType.kBrushless);
        starFollowerTwo = new CANSparkMax(Constants.kStarFollowerTwoID, MotorType.kBrushless);

        portEncoder = portLeader.getEncoder();
        starEncoder = starLeader.getEncoder();

        portFollowerOne.follow(portLeader);
        portFollowerTwo.follow(portLeader);
        starFollowerOne.follow(starLeader);
        starFollowerTwo.follow(starLeader);

        this.joystick = joystick;
        this.gyro = new AHRS();

        balanceController = new PIDController(kBalanceP, kBalanceI, kBalanceD);
        balanceController.setSetpoint(kLevelPitch);
        balanceController.setTolerance(kBalanceTolerance);

        centerController = new PIDController(kCenterP, kCenterI, kCenterD);
        centerController.setSetpoint(centerHeading);
        centerController.setTolerance(kCenterTolerance);

        wantedState = DriveStates.MANUAL;
        currentState = DriveStates.MANUAL;
    }

    /** runArcadeDrive is a magic method used every year. It it what we use to control the robot's movement */
    public void runArcadeDrive(double rotate, double throttle) {
        double portOutput = 0.0;
        double starOutput = 0.0;

        // deadband, makes it easier/possible to drive straight since it doesn't take tiny inputs
        //TODO: tune deadband?
        if(currentState == DriveStates.MANUAL && Math.abs(rotate) < kDeadZone) {
            rotate = 0;
        }

        rotate = Math.copySign(rotate * rotate, rotate);
        throttle = Math.copySign(throttle * throttle, throttle);

        double maxInput = Math.copySign(Math.max(Math.abs(rotate), Math.abs(throttle)), rotate);

        if (rotate >= 0.0) {
            // First quadrant, else second quadrant
            if (throttle >= 0.0) {
                portOutput = maxInput;
                starOutput = rotate - throttle;
            } else {
                portOutput = rotate + throttle;
                starOutput = maxInput;
            }
        } else {
            // Third quadrant, else fourth quadrant
            if (throttle >= 0.0) {
                portOutput = rotate + throttle;
                starOutput = maxInput;
            } else {
                portOutput = maxInput;
                starOutput = rotate - throttle;
            }
        }
        // set motors to port/star output here or else nothing happens lol
        portLeader.set(portOutput);
        starLeader.set(starOutput);
    }

    public void update() {
        switch(currentState) {
            case MANUAL:
                runArcadeDrive(joystick.getX(), -joystick.getY());
            break;
            case AUTOBALANCE:
                runArcadeDrive(centerController.calculate(getYaw()), balanceController.calculate(getPitch()));
                // TODO: TUNE the CONTROLLER AND TOLERANCE AND LEVEL PITCH ETC
            break;
        }
    }
    public void setWantedState(DriveStates wantedState) {
        if(this.currentState != wantedState) {
            this.wantedState = wantedState;
            handleStateTransition();
        }
    }

    public void handleStateTransition() {
        switch (wantedState) {
            case MANUAL:

            break;
            case AUTOBALANCE:
                centerHeading = getYaw();
            break;
        }
    }

    public DriveStates getCurrentState() {
        return this.currentState;
    }

    public void resetEncoders() {
        portEncoder.setPosition(0);
        starEncoder.setPosition(0);
    }

    public double[] getEncoderRotations() {
        return new double[]{portEncoder.getPosition(), starEncoder.getPosition()};
    }

    public void calibrateGyro() {
        this.gyro.calibrate();
    }

    public double getYaw() {
        return this.gyro.getYaw();
    }

    public double getPitch() {
        return this.gyro.getPitch();
    }

    public double getRoll() {
        return this.gyro.getRoll();
    }

    public enum DriveStates {
        MANUAL,
        AUTOBALANCE
    }
}
