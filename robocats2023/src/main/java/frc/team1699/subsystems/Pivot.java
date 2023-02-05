package frc.team1699.subsystems;

import edu.wpi.first.math.controller.PIDController;

/** The pivot class controls the rotation of the arm. */
public class Pivot {
    // The current and wanted states of the pivot
    private PivotStates currentState, wantedState;
    
    // Calculates the speed to rotate the arm
    private PIDController pivotSpeedLoop;
    private final double kPivotP = 0;
    private final double kPivotI = 0;
    private final double kPivotD = 0;

    /** Creates the pivot object, sets the default state to default */
    public Pivot(){
        this.currentState = PivotStates.STORED;
        pivotSpeedLoop = new PIDController(kPivotP, kPivotI, kPivotD);
        pivotSpeedLoop.setTolerance(0, 0);
    }

    public void update(){

    }

    public void handleStateTransition(){
        this.currentState = this.wantedState;
    }

    public void setWantedState(PivotStates wantedState){
        if(wantedState != this.wantedState){
        this.wantedState = wantedState;
        handleStateTransition();
        }
    }
    
    public PivotStates getPivotState(){
        return this.currentState;
    }

    public enum PivotStates {
        STORED,
        HIGHSUBSTATION,
        HIGHCUBE,
        MIDCUBE,
        LOWCUBE,
        HIGHCONE,
        MIDCONE,
        LOWCONE,
        FLOOR
    }
}
