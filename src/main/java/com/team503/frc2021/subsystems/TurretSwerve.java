package com.team503.frc2021.subsystems;

import com.team503.frc2021.Constants;

public class TurretSwerve {

    private static TurretSwerve mInstance;
    private long lastTime;

    /* Turret attributes */
    private double tDemand;
    private double tTheta;
    private double tOmega;

    /* Translational attributes */
    private double xDemand, yDemand;
    private double xPosition, yPosition;
    private double xVelocity, yVelocity;

    public TurretSwerve() {
        lastTime = System.nanoTime();
    }

    public static TurretSwerve getInstance() {
        return mInstance == null ? mInstance = new TurretSwerve() : mInstance;
    }

    public void setTurretDemand(double tDemand) {
        this.tDemand = Math.signum(tDemand) * Math.min(Math.abs(tDemand), 1.0);
    }

    public void setTranslationalDemand(double xDemand, double yDemand) {
        this.xDemand = Math.signum(xDemand) * Math.min(Math.abs(xDemand), 1.0);
        this.yDemand = Math.signum(yDemand) * Math.min(Math.abs(yDemand), 1.0);

    }

    public void simulate() {
        long currentTime = System.nanoTime();
        double dt = (currentTime - lastTime) / 1000000000.0;
        lastTime = currentTime;

        // Kinematic integration
        double alpha = Constants.kTurretLoad * (Constants.kTurretMaxVelocity * tDemand - tOmega);
        alpha = Double.valueOf(alpha).isNaN() ? Math.signum(alpha) * Constants.kTurretMaxAcceleration : Math.signum(alpha) * Math.min(Constants.kTurretMaxAcceleration, Math.abs(alpha));
        System.out.println("RPM/S: " + alpha * 60 / 360);

        tOmega += alpha * dt;
        System.out.println("RPM: " + tOmega * 60 / 360);

        tTheta += tOmega * dt;
        System.out.println("Theta: " + tTheta);
    }

}
