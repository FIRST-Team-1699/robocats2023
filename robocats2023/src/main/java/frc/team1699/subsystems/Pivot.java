package frc.team1699.subsystems;

import frc.team1699.Constants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;

import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;

/** The pivot class controls the rotation of the arm. */
public class Pivot {
    // The current and wanted states of the pivot
    private PivotStates currentState, wantedState;
    
    // Calculates the speed to rotate the arm
    private CANSparkMax pivotMotor;
    private RelativeEncoder pivotEncoder;
    private SparkMaxPIDController pivotSpeedLoop;

    private final double kPivotP = .025;
    private final double kPivotI = 0;
    private final double kPivotD = 0;

    private final double kSmartPivotP = 0.025;
    private final double kSmartPivotI = 0;
    private final double kSmartPivotD = 0;
    private final double kSmartPivotF = 0;
    private final double kMaxError = 3;

    private final double kBackStoredPosition = 0;
    private final double kShelfPosition = 182.77;
    private final double kHighPosition = 180;
    private final double kMidPosition = 200;
    private final double kLowPosition = 220;
    private final double kFloorPosition = 233;
    private final double kFrontStoredPosition = 200;
    private final double kCubeShootMidPosition = 46;
    private final double kCubeShootHighPosition = 64;
    private double wantedPosition = 0;

    private final double movingTolerance = 5;

    private final PIDSlots desiredSlot;

    // LIMIT SWITCH
    private DigitalInput zeroSwitch;

    /** Creates the pivot object, sets the default state to default */
    public Pivot(){ 
        zeroSwitch = new DigitalInput(Constants.kPivotSwitchPort);

        pivotMotor = new CANSparkMax(Constants.kTelescopeMotorID, MotorType.kBrushless);
        pivotMotor.setIdleMode(IdleMode.kBrake);
        pivotEncoder = pivotMotor.getEncoder();
        pivotSpeedLoop = pivotMotor.getPIDController();
        pivotSpeedLoop.setFeedbackDevice(pivotEncoder);
        pivotSpeedLoop.setP(kPivotP, 0);
        pivotSpeedLoop.setI(kPivotI, 0);
        pivotSpeedLoop.setD(kPivotD, 0);
        
        pivotSpeedLoop.setP(kSmartPivotP, 1);
        pivotSpeedLoop.setI(kSmartPivotI, 1);
        pivotSpeedLoop.setD(kSmartPivotD, 1);
        pivotSpeedLoop.setFF(kSmartPivotF, 1);
        pivotSpeedLoop.setSmartMotionAllowedClosedLoopError(kMaxError, 1);
        pivotSpeedLoop.setOutputRange(-1, 1);

        this.desiredSlot = PIDSlots.SMART_MOTION;
        this.currentState = PivotStates.STORED;
    }

    public void update(){
        switch (currentState){
            case STORED:

            break;
            case SHELF:

            break;
            case HIGH:

            break;
            case MID:

            break;
            case LOW:

            break;
            case FLOOR:

            break;
            default:
            break;
        }
        if(!zeroSwitch.get()) {
            pivotEncoder.setPosition(0);
            pivotSpeedLoop.setReference(0, ControlType.kVoltage);
        }
    }

    public void handleStateTransition() {
        switch (wantedState) {
            case STORED:
                wantedPosition = kBackStoredPosition;
            break;
            case SHELF:
                wantedPosition = kShelfPosition;
            break;
            case HIGH:
                wantedPosition = kHighPosition;
            break;
            case MID:
                wantedPosition = kMidPosition;
            break;
            case LOW:
                wantedPosition = kLowPosition;
            break;
            case FLOOR:
                wantedPosition = kFloorPosition;
            break;
            case STORED_FRONT:
                wantedPosition = kFrontStoredPosition;
            break;
            case CUBE_MID:
                wantedPosition = kCubeShootMidPosition;
            break;
            case CUBE_HIGH:
                wantedPosition = kCubeShootHighPosition;
            break;
            default:
                wantedPosition = 0;
            break;
        }
        if(desiredSlot == PIDSlots.SMART_MOTION) {
            pivotSpeedLoop.setReference(wantedPosition, CANSparkMax.ControlType.kSmartMotion, 1);
        } else {
            pivotSpeedLoop.setReference(wantedPosition, CANSparkMax.ControlType.kPosition, 0);
        }
        this.currentState = this.wantedState;
    }

    public void setWantedState(PivotStates wantedState){
        if(wantedState != this.wantedState){
            this.wantedState = wantedState;
        }
        handleStateTransition();
    }
    
    public PivotStates getCurrentState(){
        return this.currentState;
    }

    public boolean isDoneMoving() {
        // if (Math.abs(pivotEncoder.getVelocity()) > 3) {
        //     return false;
        // } else {
        //     return true;
        // }
        double currentPosition = pivotEncoder.getPosition();
        currentPosition -= wantedPosition;
        currentPosition = Math.abs(currentPosition);
        if(currentPosition < movingTolerance) {
            return true;
        } else {
            return false;
        }
    }
    
    public void incrementWantedPosition() {
        wantedPosition += 0.7; // 1.0
        pivotSpeedLoop.setReference(wantedPosition, ControlType.kPosition);
        wantedState = PivotStates.MANUAL;
    }

    public void decrementWantedPosition() {
        wantedPosition -= 1.0;
        pivotSpeedLoop.setReference(wantedPosition, ControlType.kPosition);
        wantedState = PivotStates.MANUAL;
    }

    public void printEncoder() {
        System.out.println(pivotEncoder.getPosition());
    }

    public void resetEncoder() {
        pivotEncoder.setPosition(0);
    }

    public void printLimitSwitch() {
        System.out.println(zeroSwitch.get());
    }

    public void setBrakeMode() {
        pivotMotor.setIdleMode(IdleMode.kBrake);
    }

    public enum PivotStates {
        STORED,
        SHELF,
        HIGH,
        MID,
        LOW,
        FLOOR,
        STORED_FRONT,
        CUBE_MID,
        CUBE_HIGH,
        MANUAL
    }

    public enum PIDSlots {
        SMART_MOTION,
        DUMB_MOTION
    }
}
