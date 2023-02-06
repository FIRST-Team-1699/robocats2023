package frc.team1699.utils;

/*
 * A wrapper class for the KauaiLabs NavX. Contains additions such as a datapoint for the
 * yaw value of being centered on the charging port, and methods like isCentered to, well,
 * check if the robot is centered.
 * 
 * NOT TESTED.
 */

import com.kauailabs.navx.frc.AHRS;

public class Gyroscope extends AHRS {
    /** The gyro heading at the start. This can be referenced to autocenter if necessary */
    private double centeredHeading;

    private boolean isInitialized;

    public Gyroscope(){
        isInitialized = false;
    }

    // run this in teleop init
    public void initialize(){
        this.centeredHeading = this.getYaw();
        isInitialized = true;
    }

    public boolean wasInitialized(){
        return isInitialized;
    }

    public double getCenteredHeading(){
        return centeredHeading;
    }

    public boolean isCentered(){
        if(Math.abs(Math.abs(centeredHeading) - Math.abs(getYaw())) < 2){
            return true;
        }
        return false;
    }
}
