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
    public static final int kIntakeMotorID = 30;
    public static final int kTelescopeMotorID = 27;
    public static final int kPivotMotorID = 26;
    // public static final int kPlowMotorID = 33;
    // DRIVETRAIN
    public static final int kPortLeaderID = 21;
    public static final int kPortFollowerOneID = 22;
    public static final int kPortFollowerTwoID = 28;

    public static final int kStarLeaderID = 24;
    public static final int kStarFollowerOneID = 25;
    public static final int kStarFollowerTwoID = 29;

    // JOYSTICKS
    public static final int kDriveJoystickPort = 0;
    public static final int kOperatorJoystickPort = 1;

    // LEDS
    public static final int kLEDPort = 9;

    // LIMIT SWITCHES
    public static final int kTelescopeSwitchPort = 4;
    public static final int kPivotSwitchPort = 7;
}
