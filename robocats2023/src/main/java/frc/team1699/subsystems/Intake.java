package frc.team1699.subsystems;

import frc.team1699.Constants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/** The intake class controls the spinning of the intake wheels. */
public class Intake {
    // Establishing the object's state
    private IntakeStates currentState, wantedState;

    // Motor controller for the motor
    private CANSparkMax intakeMotor;

    // Speeds for the motors on the intake
    private final double kIntakeSpeed = .5;
    private final double kPlacingSpeed = -.5;


    /** Creates the intake object. Sets the default wanted state to idle. */
    public Intake() {
        intakeMotor = new CANSparkMax(Constants.kIntakeMotorID, MotorType.kBrushless);
        this.currentState = IntakeStates.IDLE;
    }

    // Runs every 20 ms, handles changes in wantedState and effects of current state.
    public void update() {
        switch (currentState) {
            case INTAKING:
                intakeMotor.set(kIntakeSpeed);
            break;
            case PLACING:
                intakeMotor.set(kPlacingSpeed);
            break;
            case IDLE:
                intakeMotor.set(0);
            break;
            default:
            break;
        }
    }

    // Runs when the wanted state changes
    private void handleStateTransition(){
        switch (wantedState) {
            case INTAKING:
                intakeMotor.set(kIntakeSpeed);
            break;
            case PLACING:
                intakeMotor.set(kPlacingSpeed);
            break;
            case IDLE:
                intakeMotor.set(0);
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

    public IntakeStates getCurrentState(){
        return this.currentState;
    }

    public enum IntakeStates {
        INTAKING,
        PLACING,
        IDLE
    }
}
