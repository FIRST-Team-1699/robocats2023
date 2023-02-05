package frc.team1699.utils;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class LimeLight {

    private static LimeLight instance;
    private final NetworkTable table;

    private LimeLight() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
    }

    public static LimeLight getInstance() {
        if (instance == null) {
            instance = new LimeLight();
        }
        return instance;
    }

    public double getTV() {
        return table.getEntry("tv").getDouble(0);
    }

    public double getTX() {
        return table.getEntry("tx").getDouble(0);
    }

    public double getTY() {
        return table.getEntry("ty").getDouble(0);
    }

    public void turnOff() {
        table.getEntry("ledMode").setNumber(1);
    }

    public void turnOn() {
        table.getEntry("ledMode").setNumber(3);
    }

    public void blink() {
        table.getEntry("ledMode").setNumber(2);
    }

    public void setPipeline(double index){
        table.getEntry("pipeline").setNumber(index);
    }

    public void togglePipeline(){
        double lastPipeline = table.getEntry("pipeline").getDouble(0.0);
        if (lastPipeline == 0.0){
            table.getEntry("pipeline").setNumber(1.0);
        } else {
            table.getEntry("pipeline").setNumber(0.0);
        }
    }
    
    public double getDistanceFromTarget(){
        double targetOffsetAngle_Vertical = getTY();
        double limelightMountAngle = 47.0;
        double limelightHeight = 23.5;
        double targetHeight = 71;
        double angleToGoalRadians = (limelightMountAngle + targetOffsetAngle_Vertical) * (3.14159 / 180.0);
        double distanceToGoal = (targetHeight - limelightHeight)/Math.tan(angleToGoalRadians);
        return distanceToGoal;
    }
}