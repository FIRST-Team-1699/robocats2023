package frc.team1699.subsystems;

import frc.team1699.Constants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;  

/** The telescoping tubes for the arm of our robot are  defined and controlled in this class. */
public class Telescope {
    // sets the current and wanted state for the robot
    private TelescopeStates currentState, wantedState;

    private CANSparkMax telescopeMotor;
    private RelativeEncoder telescopeEncoder;
    private SparkMaxPIDController telescopeSpeedLoop;

    // creates the PIDController values for the telescoping arm
    private final double kTelescopeP = .5;
    private final double kTelescopeI = 0.0;
    private final double kTelescopeD = 0.0;
    private final double kMinOutput = -1;
    private final double kMaxOutput = 1;
    
    // Positional constants
    private final double kStoredPercent = 0;
    private final double kShelfPercent = 0;
    private final double kHighPercent = 350;
    private final double kMidPercent = 128;
    private final double kLowPercent = 0;
    private final double kStoredFrontPercent = 0;
    private final double kFloorPercent = 0;
    private final double kCubeHighShootPosition = 0;

    private double wantedPercentage = 0;
    private double wantedPosition = calculateTelescopeRotations(wantedPercentage);

    // Limit switch
    // DigitalInput zeroSwitch;

    // maximum amount of rotations of the encoder, used to calculate rotations by percentage
    // TODO: tune this value
    private static final double kMaxRotations = 1000;

    private final double movingTolerance = 5;
    /** creates the telescope object, sets default state to retracted */
    public Telescope() {
        telescopeMotor = new CANSparkMax(Constants.kTelescopeMotorID, MotorType.kBrushless);
        telescopeMotor.setIdleMode(IdleMode.kBrake);
        telescopeEncoder = telescopeMotor.getEncoder();
        telescopeSpeedLoop = telescopeMotor.getPIDController();
        telescopeSpeedLoop.setFeedbackDevice(telescopeEncoder);
        telescopeSpeedLoop.setP(kTelescopeP);
        telescopeSpeedLoop.setI(kTelescopeI);
        telescopeSpeedLoop.setD(kTelescopeD);
        telescopeSpeedLoop.setOutputRange(kMinOutput, kMaxOutput);

        // zeroSwitch = new DigitalInput(Constants.kTelescopeSwitchPort);

        this.currentState = TelescopeStates.STORED;
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
            case STORED_FRONT:

            break;
            case FLOOR:
               
            break;
            default:
            break;    
        }
        // if(!zeroSwitch.get()) {
        //     telescopeEncoder.setPosition(0);
        //     telescopeSpeedLoop.setReference(0, ControlType.kVoltage);
        // }
    }

    public void setWantedState(TelescopeStates wantedState) {
        if(wantedState != this.wantedState){
            this.wantedState = wantedState;
        }
        handleStateTransition();
    }

    // TODO: needs to be tuned, decide if using percentages is good and if it works 
    public void handleStateTransition() {
        switch (wantedState) {
            case STORED:
                wantedPercentage = kStoredPercent;
                wantedPosition = kStoredPercent;
            break;
            case SHELF:
                wantedPercentage = kShelfPercent;
                wantedPosition = kShelfPercent;
            break;
            case HIGH:
                wantedPercentage = kHighPercent;
                wantedPosition = kHighPercent;
            break;
            case MID:
                wantedPercentage = kMidPercent;
                wantedPosition = kMidPercent;
            break;
            case LOW:
                wantedPercentage = kLowPercent;
                wantedPosition = kLowPercent;
            break;
            case STORED_FRONT:
                wantedPercentage = kStoredFrontPercent;
                wantedPosition = kStoredFrontPercent;
            break;
            case FLOOR:
                wantedPercentage = kFloorPercent;
                wantedPosition = kFloorPercent;
            break;
            case CUBE_MID:
                wantedPosition = kCubeHighShootPosition;
            break;
            default:
            break;
        }
        
        // wantedPosition = calculateTelescopeRotations(wantedPercentage);
        telescopeSpeedLoop.setReference(wantedPosition, CANSparkMax.ControlType.kPosition);
        this.currentState = this.wantedState;
    }

    public TelescopeStates getCurrentState(){
        return this.currentState;
    }

    public void resetEncoder(){
        telescopeEncoder.setPosition(0);
    }

    public void printEncoder(){
        System.out.println(telescopeEncoder.getPosition());
    }

    public static double calculateTelescopeRotations(double percentage) {
        return (percentage / 100) * kMaxRotations;
    }

    public void incrementTelescopePosition() {
        wantedPosition += 3;
        telescopeSpeedLoop.setReference(wantedPosition, ControlType.kPosition);
        wantedState = TelescopeStates.MANUAL;
    }

    public void decrementTelescopePosition() {
        wantedPosition -= 3;
        telescopeSpeedLoop.setReference(wantedPosition, ControlType.kPosition);
        wantedState = TelescopeStates.MANUAL;
    }

    public boolean isDoneMoving() {
        // if (Math.abs(telescopeEncoder.getVelocity()) > 3) {
        //     return false;
        // } else {
        //     return true;
        // }
        double currentPosition = telescopeEncoder.getPosition();
        currentPosition -= wantedPosition;
        currentPosition = Math.abs(currentPosition);
        if(currentPosition < movingTolerance) {
            return true;
        } else {
            return false;
        }
    }

    public void setBrakeMode() {
        telescopeMotor.setIdleMode(IdleMode.kBrake);
    }

    // public void printSwitch() {
    //     System.out.println(zeroSwitch.get());
    // }

    public enum TelescopeStates {
        STORED,
        SHELF,
        HIGH,
        MID,
        LOW,
        STORED_FRONT,
        FLOOR,
        CUBE_MID,
        MANUAL
    }
}
