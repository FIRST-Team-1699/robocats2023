package frc.team1699.subsystems;

import frc.team1699.Constants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;       

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
    
    private double wantedPosition = 25;
    private final double kMaxError = 0.1;
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
            case RETRACTED:
                
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
        if (Math.abs(telescopeEncoder.getPosition() - wantedPosition) <= kMaxError) {
            telescopeSpeedLoop.setReference(telescopeEncoder.getPosition(), ControlType.kPosition);
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
            case RETRACTED:
                wantedPosition = 0;
            break;
            case SHELF:
                wantedPosition = 50;
            break;
            case HIGH:
                wantedPosition = 60;
            break;
            case MID:
            wantedPosition = 40;
            break;
            case LOW:
                wantedPosition = 20;
            break;
            case FLOOR:
                wantedPosition = 30;
            break;
            default:
            break;
        }
        
        telescopeSpeedLoop.setReference(wantedPosition, CANSparkMax.ControlType.kPosition);
        this.currentState = this.wantedState;
    }

    public TelescopeStates getCurrentState(){
        return this.currentState;
    }

    public void resetEncoder(){
        telescopeEncoder.setPosition(0);
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
