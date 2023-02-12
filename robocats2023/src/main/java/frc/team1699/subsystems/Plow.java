package frc.team1699.subsystems;

import frc.team1699.Constants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

public class Plow {
    private PlowStates wantedState, currentState;
    private CANSparkMax plowMotor;
    private RelativeEncoder plowEncoder;
    private SparkMaxPIDController plowSpeedLoop;

    private final double kPlowP = 0;
    private final double kPlowI = 0;
    private final double kPlowD = 0;
    private final double kMinOutput = -.5;
    private final double kMaxOutput = .5;

    public Plow() {
        plowMotor = new CANSparkMax(Constants.kPlowMotorID, MotorType.kBrushless);
        plowEncoder = plowMotor.getEncoder();
        plowSpeedLoop = plowMotor.getPIDController();
        plowSpeedLoop.setFeedbackDevice(plowEncoder);
        plowSpeedLoop.setP(kPlowP);
        plowSpeedLoop.setI(kPlowI);
        plowSpeedLoop.setD(kPlowD);
        plowSpeedLoop.setOutputRange(kMinOutput, kMaxOutput);
        this.currentState = PlowStates.IN;
    }

    public void update() {
        switch (currentState) {
            case IN:
                
            break;
            case OUT:
                
            break;
            default:

            break;
        }
    }

    public void handleStateTransition() {
        switch (wantedState) {
            case IN:
                // TODO: MOTOR GO/STAY IN!!!
            break;
            case OUT:
                // TODO: MOTOR GO/STAY OUT!!!
            break;
            default:

            break;
        }
        this.currentState = this.wantedState;
    }

    public void setWantedState(PlowStates wantedState) {
        if(this.wantedState != wantedState){
            this.wantedState = wantedState;
            handleStateTransition();
        }
    }

    public enum PlowStates {
        IN,
        OUT
    }
}
