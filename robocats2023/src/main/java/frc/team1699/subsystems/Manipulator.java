package frc.team1699.subsystems;

import frc.team1699.subsystems.Pivot.PivotStates;
import frc.team1699.subsystems.Telescope.TelescopeStates;
//TODO: make it not telescope and pivot or vice versa

// IN TO 0, THEN PIVOT, THEN TELESCOPE
/** The manipulator class combines the pivot, telescope, and intake subsystems to manipulate gamepieces. */
public class Manipulator {
    private ManipulatorStates wantedState, currentState;
    private SequentialMovementStates currentMoveState;

    private Telescope telescope;
    private Pivot pivot;

    private final int waitTicks = 10;
    private int ticksWaited = 0;

    public Manipulator(){
        telescope = new Telescope();
        pivot = new Pivot();
        this.wantedState = ManipulatorStates.STORED;
        this.currentState = ManipulatorStates.STORED;
        this.currentMoveState = SequentialMovementStates.DONE;
    }

    public void update(){
        switch (wantedState){
            case STORED:
                handleSequentialMovement(PivotStates.STORED, TelescopeStates.STORED);
            break;
            case SHELF:
                handleSequentialMovement(PivotStates.SHELF, TelescopeStates.SHELF);
            break;
            case HIGH:
                handleSequentialMovement(PivotStates.HIGH, TelescopeStates.HIGH);
            break;
            case MID:
                handleSequentialMovement(PivotStates.MID, TelescopeStates.MID);
            break;
            case LOW:
                handleSequentialMovement(PivotStates.LOW, TelescopeStates.LOW);
            break;
            case STORED_FRONT:
                handleSequentialMovement(PivotStates.STORED_FRONT, TelescopeStates.STORED_FRONT);
            case FLOOR:
                handleSequentialMovement(PivotStates.FLOOR, TelescopeStates.FLOOR);
            break;
            case CUBE_MID:
                handleSequentialMovement(PivotStates.CUBE_MID, TelescopeStates.CUBE_MID);
            break;
            default:
            break;
        }           
        telescope.update();
        pivot.update();
    }

    public void handleStateTransition(){                                                
        this.currentState = this.wantedState;
    }

    public void setWantedState(ManipulatorStates wantedState) {
        if(this.wantedState != wantedState){
            this.wantedState = wantedState;
            handleStateTransition();
        }
    }

    //TODO: TEST
    public void handleSequentialMovement(PivotStates wantedPivotState, TelescopeStates wantedTelescopeState) {
        switch(currentMoveState) {
            case RETRACTING:
                if(telescope.getCurrentState() != TelescopeStates.STORED) {
                    telescope.setWantedState(TelescopeStates.STORED);
                    ticksWaited = 0;
                } else {
                    if(telescope.isDoneMoving() && ticksWaited >= waitTicks) {
                        currentMoveState = SequentialMovementStates.PIVOTING;
                        ticksWaited = 0;
                    } else {
                        ticksWaited++;
                    }
                } 
            break;
            case PIVOTING:
                if(pivot.getCurrentState() != wantedPivotState) {
                    pivot.setWantedState(wantedPivotState);
                    ticksWaited = 0;
                } else {
                    if(pivot.isDoneMoving() && ticksWaited >= waitTicks) {
                        currentMoveState = SequentialMovementStates.EXTENDING;
                        ticksWaited = 0;
                    } else {
                        ticksWaited++;
                    }
                }
            break;
            case EXTENDING:
                if(telescope.getCurrentState() != wantedTelescopeState) {
                    telescope.setWantedState(wantedTelescopeState);
                    ticksWaited = 0;
                } else {
                    if(telescope.isDoneMoving() && ticksWaited >= waitTicks) {
                        currentMoveState = SequentialMovementStates.DONE;
                        ticksWaited = 0;
                    } else {
                        ticksWaited++;
                    }
                }
            break;
            case DONE:
                if(wantedPivotState != pivot.getCurrentState() || wantedTelescopeState != telescope.getCurrentState()) {
                    currentMoveState = SequentialMovementStates.RETRACTING;
                }
            break;
            default:
            break;
        }
    }

    public void incrementPivotPosition() {
        pivot.incrementWantedPosition();
    }

    public void decrementPivotPosition() {
        pivot.decrementWantedPosition();
    }

    public void incrementTelescopePosition() {
        telescope.incrementTelescopePosition();
    }

    public void decrementTelescopePosition() {
        telescope.decrementTelescopePosition();
    }
    
    public ManipulatorStates getCurrentState() {
        return this.currentState;
    }

    public void printEncoderPositions() {
        System.out.println("Telescope");
        telescope.printEncoder();
        System.out.println("Pivot");
        pivot.printEncoder();
    }

    public void printLimitSwitches() {
        System.out.println("Telescope");
        telescope.printSwitch();
        System.out.println("Pivot");
        pivot.printLimitSwitch();
    }

    public boolean isDoneMoving() {
        if(telescope.isDoneMoving() && pivot.isDoneMoving()) {
            System.out.println("im done moving");
            return true;
        } else {
            return false;
        }
    }

    public void setBrakeMode() {
        telescope.setBrakeMode();
        pivot.setBrakeMode();
    }

    public void resetTelescopeEncoder() {
        telescope.resetEncoder();
    }

    public void resetPivotEncoder() {
        pivot.resetEncoder();
    }

    public enum ManipulatorStates {
        STORED,
        SHELF,
        HIGH,
        MID,
        LOW,
        STORED_FRONT,
        FLOOR,
        CUBE_MID
    }

    public enum SequentialMovementStates {
        RETRACTING,
        PIVOTING,
        EXTENDING,
        DONE
    }
}
