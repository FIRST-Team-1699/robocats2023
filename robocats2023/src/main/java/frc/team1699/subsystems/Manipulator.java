package frc.team1699.subsystems;

import frc.team1699.subsystems.Pivot.PivotStates;
import frc.team1699.subsystems.Telescope.TelescopeStates;

/** The manipulator class combines the pivot, telescope, and intake subsystems to manipulate gamepieces. */
public class Manipulator {
    private ManipulatorStates wantedState, currentState;

    private Telescope telescope;
    private Pivot pivot;

    public Manipulator(){
        telescope = new Telescope();
        pivot = new Pivot();
        this.wantedState = ManipulatorStates.STORED;
        this.currentState = ManipulatorStates.STORED;
    }

    public void update(){
        telescope.update();
        pivot.update();
    }

    public void handleStateTransition(){
        switch (wantedState){
            case STORED:
                telescope.setWantedState(TelescopeStates.STORED);
                pivot.setWantedState(PivotStates.STORED);
            break;
            case SHELF:
                telescope.setWantedState(TelescopeStates.SHELF);
                pivot.setWantedState(PivotStates.SHELF);
            break;
            case HIGH:
                telescope.setWantedState(TelescopeStates.HIGH);
                pivot.setWantedState(PivotStates.HIGH);
            break;
            case MID:
                telescope.setWantedState(TelescopeStates.MID);
                pivot.setWantedState(PivotStates.MID);
            break;
            case LOW:
                telescope.setWantedState(TelescopeStates.LOW);
                pivot.setWantedState(PivotStates.LOW);
            break;
            case STORED_FRONT:
                telescope.setWantedState(TelescopeStates.STORED_FRONT);
                pivot.setWantedState(PivotStates.STORED_FRONT);
            case FLOOR:
                telescope.setWantedState(TelescopeStates.FLOOR);
                pivot.setWantedState(PivotStates.FLOOR);
            break;
            default:
            break;
        }
        this.currentState = this.wantedState;
    }

    public void setWantedState(ManipulatorStates wantedState) {
        if(this.wantedState != wantedState){
            this.wantedState = wantedState;
            handleStateTransition();
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

    public void printTelescopeEncoder() {
        telescope.printEncoder();
    }

    public boolean isDoneMoving() {
        if(telescope.isDoneMoving() && pivot.isDoneMoving()) {
            return true;
        } else {
            return false;
        }
    }

    public enum ManipulatorStates {
        STORED,
        SHELF,
        HIGH,
        MID,
        LOW,
        STORED_FRONT,
        FLOOR
    }
}
