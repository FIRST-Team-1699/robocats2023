package frc.team1699;

/**
 * Constants are a bunch of public static final
 * variables of the primitive data types.
 * 
 * They should be used for constants that affect
 * all of the robot code, such as ports for actuators,
 * sensors, or controllers.
 * 
 * They should be in camel case and start with a "k".
 * An example would be:
 * public static final int kJoystickPort = 0;
 */

public class Constants {
    // CAN bus ID's
    // GAME PIECE MANIPULATION
    public static final int kIntakeMotorID = 1;
    public static final int kTelescopeMotorID = 2;
    public static final int kPivotMotorID = 3;
    public static final int kPlowMotorID = 4;
    // DRIVETRAIN
    public static final int kPortLeaderID = 21;
    public static final int kPortFollowerOneID = 22;
    public static final int kPortFollowerTwoID = 23;

    public static final int kStarLeaderID = 31;
    public static final int kStarFollowerOneID = 32;
    public static final int kStarFollowerTwoID = 33;

    // JOYSTICKS
    public static final int kDriveJoystickPort = 0;
    public static final int kOperatorJoystickPort = 1;
}
