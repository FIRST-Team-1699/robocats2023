package frc.team1699.subsystems;

import frc.team1699.Constants;
import edu.wpi.first.math.controller.PIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder;
import com.revrobotics.SparkMaxPIDController;       

/** The telescoping tubes for the arm of our robot are  defined and controlled in this class. */
public class Telescope {
    // sets the current and wanted state for the robot
    private TelescopeStates currentState, wantedState;

    private CANSparkMax telescopeMotor;
    private RelativeEncoder telescopeEncoder;
    private SparkMaxPIDController telescopeSpeedLoop;

    // creates the PIDController values for the telescoping arm
    private final double kTelescopeP = .1;
    private final double kTelescopeI = 0;
    private final double kTelescopeD = 0;
    private final double kMinOutput = -.5;
    private final double kMaxOutput = .5;

    /** creates the telescope object, sets default state to retracted */
    public Telescope() {
        telescopeMotor = new CANSparkMax(Constants.kTelescopeMotorID, MotorType.kBrushless);
        telescopeEncoder = telescopeMotor.getEncoder();
        telescopeSpeedLoop = telescopeMotor.getPIDController();
        telescopeSpeedLoop.setFeedbackDevice(telescopeEncoder);
        telescopeSpeedLoop.setP(kTelescopeP);
        telescopeSpeedLoop.setI(kTelescopeI);
        telescopeSpeedLoop.setD(kTelescopeD);
        telescopeSpeedLoop.setOutputRange(kMinOutput, kMaxOutput);
        this.currentState = TelescopeStates.RETRACTED;
    }

    public void update(){
        switch (currentState){
            case FLOOR:
                if(telescopeEncoder.getPosition() != .5){
                    telescopeMotor.set(.1);
                }
                // telescopeSpeedLoop.setReference(5.0, CANSparkMax.ControlType.kPosition);
            break;
            case HIGH:

            break;
            case MID:

            break;
            case LOW:

            break;
            case SHELF:

            break;
            case RETRACTED:

            break;
            default:
            break;    
        }
    }
    public void setWantedState(TelescopeStates wantedState) {
        if(wantedState != this.wantedState){
            this.wantedState = wantedState;
            handleStateTransition();
        }
    }

    public void handleStateTransition() {
        switch (wantedState) {
            case FLOOR:
            
            break;
            case HIGH:

            break;
            case MID:

            break;
            case LOW:

            break;
            case SHELF:

            break;
            case RETRACTED:

            break;
            default:
            break;
        }
    }

    public TelescopeStates getCurrentState(){
        return this.currentState;
    }

    public enum TelescopeStates {
        RETRACTED,
        SHELF,
        HIGH,
        MID,
        LOW,
        FLOOR
    }
}
