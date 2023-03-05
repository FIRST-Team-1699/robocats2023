package frc.team1699.subsystems;

import frc.team1699.Constants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;

/** The pivot class controls the rotation of the arm. */
public class Pivot {
    // The current and wanted states of the pivot
    private PivotStates currentState, wantedState;
    
    // Calculates the speed to rotate the arm
    private CANSparkMax pivotMotor;
    private RelativeEncoder pivotEncoder;
    private SparkMaxPIDController pivotSpeedLoop;

    private final double kPivotP = .2;
    private final double kPivotI = 0;
    private final double kPivotD = 0;

    private final double kBackStoredPosition = 0;
    private final double kShelfPosition = 120;
    private final double kHighCubePosition = 0;
    private final double kMidCubePosition = 0;
    private final double kLowCubePosition = 0;
    private final double kFloorPosition = 0;
    private final double kFrontStoredPosition = 246;
    private double wantedPosition = 0;


    /** Creates the pivot object, sets the default state to default */
    public Pivot(){ 
        pivotMotor = new CANSparkMax(Constants.kPivotMotorID, MotorType.kBrushless);
        pivotEncoder = pivotMotor.getEncoder();
        pivotSpeedLoop = pivotMotor.getPIDController();
        pivotSpeedLoop.setP(kPivotP);
        pivotSpeedLoop.setI(kPivotI);
        pivotSpeedLoop.setD(kPivotD);
        pivotSpeedLoop.setOutputRange(-1, 1);
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
    }

    public void handleStateTransition(){
        switch (wantedState){
            case STORED:
                wantedPosition = kBackStoredPosition;
            break;
            case SHELF:
                wantedPosition = kShelfPosition;
            break;
            case HIGH:
                wantedPosition = kHighCubePosition;
            break;
            case MID:
                wantedPosition = kMidCubePosition;
            break;
            case LOW:
                wantedPosition = kLowCubePosition;
            break;
            case FLOOR:
                wantedPosition = kFloorPosition;
            break;
            case STORED_FRONT:
                wantedPosition = kFrontStoredPosition;
            break;
            default:
                wantedPosition = 0;
            break;
        }
        pivotSpeedLoop.setReference(wantedPosition, ControlType.kPosition);
        this.currentState = this.wantedState;
    }

    public void setWantedState(PivotStates wantedState){
        if(wantedState != this.wantedState){
            this.wantedState = wantedState;
            handleStateTransition();
        }
    }
    
    public PivotStates getCurrentState(){
        return this.currentState;
    }

    public boolean isDoneMoving() {
        if (pivotEncoder.getVelocity() > 0) {
            return false;
        } else {
            return true;
        }
    }
    
    public void incrementWantedPosition() {
        wantedPosition++;
        pivotSpeedLoop.setReference(wantedPosition, ControlType.kPosition);
    }

    public enum PivotStates {
        STORED,
        SHELF,
        HIGH,
        MID,
        LOW,
        FLOOR,
        STORED_FRONT
    }
}
