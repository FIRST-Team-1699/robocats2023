package frc.team1699.subsystems;

import frc.team1699.Constants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
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
    private final double kTelescopeP = .25;
    private final double kTelescopeI = 0.000000000;
    private final double kTelescopeD = 0.2;
    private final double kMinOutput = -.5;
    private final double kMaxOutput = .5;
    
    // Positional constants
    private final double kStoredPercent = 0;
    private final double kShelfPercent = 0;
    private final double kHighPercent = 0;
    private final double kMidPercent = 128;
    private final double kLowPercent = 0;
    private final double kStoredFrontPercent = 0;
    private final double kFloorPercent = 0;

    private double wantedPercentage = 0;
    private double wantedPosition = calculateTelescopeRotations(wantedPercentage);

    // maximum amount of rotations of the encoder, used to calculate rotations by percentage
    // TODO: tune this value
    private static final double kMaxRotations = 1000;

    /** creates the telescope object, sets default state to retracted */
    public Telescope() {
        telescopeMotor = new CANSparkMax(Constants.kTelescopeMotorID, MotorType.kBrushless);
        telescopeMotor.setIdleMode(IdleMode.kBrake);
        telescopeEncoder = telescopeMotor.getEncoder();
        telescopeEncoder.setPosition(0);
        telescopeSpeedLoop = telescopeMotor.getPIDController();
        telescopeSpeedLoop.setFeedbackDevice(telescopeEncoder);
        telescopeSpeedLoop.setP(kTelescopeP);
        telescopeSpeedLoop.setI(kTelescopeI);
        telescopeSpeedLoop.setD(kTelescopeD);
        telescopeSpeedLoop.setOutputRange(kMinOutput, kMaxOutput);
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
        System.out.println(telescopeEncoder.getPosition());
    }

    public void setWantedState(TelescopeStates wantedState) {
        if(wantedState != this.wantedState){
            this.wantedState = wantedState;
            handleStateTransition();
        }
    }

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
        wantedPosition++;
        telescopeSpeedLoop.setReference(wantedPosition, ControlType.kPosition);
    }

    public void decrementTelescopePosition() {
        wantedPosition--;
        telescopeSpeedLoop.setReference(wantedPosition, ControlType.kPosition);
    }

    public boolean isDoneMoving() {
        if (telescopeEncoder.getVelocity() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public enum TelescopeStates {
        STORED,
        SHELF,
        HIGH,
        MID,
        LOW,
        STORED_FRONT,
        FLOOR
    }
}
