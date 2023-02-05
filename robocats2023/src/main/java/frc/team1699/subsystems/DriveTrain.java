package frc.team1699.subsystems;

public class DriveTrain {
    
    /** runArcadeDrive is a magic method used every year. It it what we use to control the robot's movement */
    public void runArcadeDrive(double rotate, double throttle) {
        double portOutput = 0.0;
        double starOutput = 0.0;


        //TODO add deadband
        rotate = Math.copySign(rotate * rotate, rotate);
        throttle = Math.copySign(throttle * throttle, throttle);

        double maxInput = Math.copySign(Math.max(Math.abs(rotate), Math.abs(throttle)), rotate);

        if (rotate >= 0.0) {
            // First quadrant, else second quadrant
            if (throttle >= 0.0) {
                portOutput = maxInput;
                starOutput = rotate - throttle;
            } else {
                portOutput = rotate + throttle;
                starOutput = maxInput;
            }
        } else {
            // Third quadrant, else fourth quadrant
            if (throttle >= 0.0) {
                portOutput = rotate + throttle;
                starOutput = maxInput;
            } else {
                portOutput = maxInput;
                starOutput = rotate - throttle;
            }
        }
        // set motors to port/star output here or else nothing happens lol
    }
}
