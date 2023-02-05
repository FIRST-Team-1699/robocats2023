package frc.team1699.subsystems;

/** The intake class controls the spinning of the intake wheels. */
public class Intake {
    // Speeds for the motors on the intake
    private final double kIntakeSpeed = .5;
    private final double kPlacingSpeed = .5;

    // Establishing the object's state
    private IntakeStates currentState, wantedState;

    /** Creates the intake object. Sets the default wanted state to idle. */
    public Intake() {
        this.wantedState = IntakeStates.IDLE;
    }

    // Runs every 20 ms, handles changes in wantedState and effects of current state.
    public void update() {
        switch (currentState) {
            case INTAKING:

            break;
            case PLACING:

            break;
            case IDLE:

            break;
            default:
            break;
        }
    }

    // Runs when the wanted state changes
    private void handleStateTransition(){
        switch (wantedState) {
            case INTAKING:

            break;
            case PLACING:

            break;
            case IDLE:

            break;
            default:
            break;
        }
        this.currentState = this.wantedState;
    }

    // Sets the wanted state of the intake and calls handle state transition
    public void setWantedState(IntakeStates wantedState) {
        if(wantedState != this.wantedState) {
            this.wantedState = wantedState;
            handleStateTransition();
        }
    }

    public enum IntakeStates {
        INTAKING,
        PLACING,
        IDLE
    }
}
