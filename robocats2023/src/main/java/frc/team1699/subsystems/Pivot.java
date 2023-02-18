package frc.team1699.subsystems;

import frc.team1699.Constants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;

/** The pivot class controls the rotation of the arm. */
public class Pivot {
    // The current and wanted states of the pivot
    private PivotStates currentState, wantedState;
    
    // Calculates the speed to rotate the arm
    private CANSparkMax pivotMotor;
    private SparkMaxPIDController pivotSpeedLoop;

    private final double kPivotP = 0;
    private final double kPivotI = 0;
    private final double kPivotD = 0;

    private double wantedPosition = 0;

    /** Creates the pivot object, sets the default state to default */
    public Pivot(){ 
        pivotMotor = new CANSparkMax(Constants.kPivotMotorID, MotorType.kBrushless);
        pivotSpeedLoop = pivotMotor.getPIDController();
        pivotSpeedLoop.setP(kPivotP);
        pivotSpeedLoop.setI(kPivotI);
        pivotSpeedLoop.setD(kPivotD);
        pivotSpeedLoop.setOutputRange(-.5, .5);
        this.currentState = PivotStates.STORED;
    }

    public void update(){
        switch (currentState){
            case STORED:

            break;
            case SHELF:

            break;
            case HIGHCUBE:

            break;
            case MIDCUBE:

            break;
            case LOWCUBE:

            break;
            case HIGHCONE:

            break;
            case MIDCONE:

            break;
            case LOWCONE:

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

            break;
            case SHELF:

            break;
            case HIGHCUBE:

            break;
            case MIDCUBE:

            break;
            case LOWCUBE:

            break;
            case HIGHCONE:

            break;
            case MIDCONE:

            break;
            case LOWCONE:

            break;
            case FLOOR:

            break;
            default:
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

    
    public enum PivotStates {
        STORED,
        SHELF,
        HIGHCUBE,
        MIDCUBE,
        LOWCUBE,
        HIGHCONE,
        MIDCONE,
        LOWCONE,
        FLOOR
    }
}
