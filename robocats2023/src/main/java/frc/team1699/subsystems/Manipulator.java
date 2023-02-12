package frc.team1699.subsystems;

import frc.team1699.subsystems.Telescope.TelescopeStates;

/** The manipulator class combines the pivot, telescope, and intake subsystems to manipulate gamepieces. */
public class Manipulator {
    private ManipulatorStates wantedState, currentState;

    private Telescope telescope;
    private Pivot pivot;

    public Manipulator(){
        telescope = new Telescope();
        pivot = new Pivot();
        this.wantedState = ManipulatorStates.RETRACTED;
        this.currentState = ManipulatorStates.RETRACTED;
    }

    public void update(){
        telescope.update();
        pivot.update();
    }

    public void handleStateTransition(){
        switch (wantedState){
            case RETRACTED:
                telescope.setWantedState(TelescopeStates.RETRACTED);
            break;
            case SHELF:
                telescope.setWantedState(TelescopeStates.SHELF);
            break;
            case HIGH:
                telescope.setWantedState(TelescopeStates.HIGH);
            break;
            case MID:
                telescope.setWantedState(TelescopeStates.MID);
            break;
            case LOW:
                telescope.setWantedState(TelescopeStates.LOW);
            break;
            case FLOOR:
                telescope.setWantedState(TelescopeStates.FLOOR);
            break;
            default:

            break;
        }
        this.currentState = this.wantedState;
    }

    public void setWantedState(ManipulatorStates wantedState){
        if(this.wantedState != wantedState){
            this.wantedState = wantedState;
            handleStateTransition();
        }
    }

    public ManipulatorStates getCurrentState(){
        return this.currentState;
    }

    public enum ManipulatorStates {
        RETRACTED,
        SHELF,
        HIGH,
        MID,
        LOW,
        FLOOR
    }
}
