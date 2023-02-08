package frc.team1699.utils;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.Encoder;

public class PoseHandler {
    private Gyroscope gyro;
    private Pose2d currentPose;
    private DifferentialDriveOdometry driveOdometry;
    private static Encoder tempLeft = new Encoder(0,1);
    private static Encoder tempRight = new Encoder(2,3);

    private PoseHandler(){
        gyro = new Gyroscope();
    }

    // initializes the gyro and sets up the position of the robot
    public void initialize(){
        gyro.initialize();
        Rotation2d angle = new Rotation2d(gyro.getYaw());
        driveOdometry = new DifferentialDriveOdometry(angle, tempLeft.getDistance(), tempRight.getDistance());
    }

    /* 
    * if an apriltag is in sight, resets the robot position based on encoder values, angle and robot position calculated from the apriltag
    * otherwise, updates pos based on driveOdometry's built in math
    */ 
    public void updatePos(){
        Rotation2d robotRotation = new Rotation2d(gyro.getYaw());
        if(LimeLight.getInstance().getTV() > 0 && LimeLight.getInstance().getPipeline() == 1){
            driveOdometry.resetPosition(robotRotation, tempLeft.getDistance(), tempRight.getDistance(), calculatePose2d());
        } else {
            driveOdometry.update(robotRotation, tempLeft.getDistance(), tempRight.getDistance());
        }
        currentPose = new Pose2d(driveOdometry.getPoseMeters().getX(), driveOdometry.getPoseMeters().getY(), robotRotation);
    }

    public Pose2d getPos(){
        return currentPose;
    }

    public double getCenteredHeading(){
        return gyro.getCenteredHeading();
    }

    private Pose2d calculatePose2d(){
        // TODO: implement case to determine what target it is looking at instead of assuming that the target is 0, 0 
        // Defining variables to start
        Pose2d pose;
        Rotation2d rotation;
        double distanceToTag;
        double robotAngle;
        double robotAngleToTargetDegrees;
        double robotAngleToTargetRadians;
        double robotX;
        double robotY;
        LimeLight limeLight = LimeLight.getInstance();

        limeLight.setPipeline(1);
        if(limeLight.getTV() > 0){
            // calculates the distance to the closest apriltag
            distanceToTag = limeLight.getDistanceFromTarget();

            // calculates the angle of the robot, assuming that facing where the robot started is the center I believe
            if(gyro.getYaw() > 0){
                robotAngle = gyro.getYaw() - getCenteredHeading();
            } else {
                robotAngle = gyro.getYaw() + getCenteredHeading();
            }
            rotation = new Rotation2d(robotAngle);

            // calculates the angle of the robot to the target based on limelight reading and robot angle decided earlier
            robotAngleToTargetDegrees = robotAngle + limeLight.getTX();

            // calculates the X and Y of the robot using trig relative to the apriltag
            robotAngleToTargetRadians = Math.toRadians(robotAngleToTargetDegrees);
            robotX = Math.sin(robotAngleToTargetRadians) * distanceToTag;
            robotY = Math.cos(robotAngleToTargetRadians) * distanceToTag;

            // defines pose
            pose = new Pose2d(robotX, robotY, rotation);
        } else {
            pose = driveOdometry.getPoseMeters();
        }
        return pose;
    }
}