package frc.team1699.utils;

import com.kauailabs.navx.frc.AHRS;

public class Gyroscope extends AHRS {
    // instance data
    private AHRS gyro;
    /** The gyro heading at the start. This can be referenced to autocenter if necessary */
    private double centeredHeading;
    private boolean isInitialized;

    public Gyroscope(){
        gyro = new AHRS();
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
