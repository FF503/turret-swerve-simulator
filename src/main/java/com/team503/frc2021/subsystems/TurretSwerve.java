package com.team503.frc2021.subsystems;

import com.team503.frc2021.Constants;

public class TurretSwerve {

    private static TurretSwerve mInstance;
    private long lastTime;

    /* Turret attributes */
    private double tDemand;
    private double tTheta;
    private double tOmega;

    /* Translation attributes */
    private double xDemand, yDemand;
    private double xPosition, yPosition;
    private double xVelocity, yVelocity;

    /* Robot rotation attributes */
    private double rDemand;
    private double rTheta;
    private double rOmega;

    public TurretSwerve() {
        lastTime = System.nanoTime();
    }

    public static TurretSwerve getInstance() {
        return mInstance == null ? mInstance = new TurretSwerve() : mInstance;
    }

    public void setTurretDemand(double tDemand) {
        this.tDemand = Math.signum(tDemand) * Math.min(Math.abs(tDemand), 1.0);
    }

    public void setCartesianTranslationDemand(double xDemand, double yDemand) {
        this.xDemand = Math.signum(xDemand) * Math.min(Math.abs(xDemand), 1.0);
        this.yDemand = Math.signum(yDemand) * Math.min(Math.abs(yDemand), 1.0);
    }

    public void setPolarTranslationDemand(double r, double theta) {
        setCartesianTranslationDemand(r * Math.cos(Math.toRadians(theta)), r * Math.sin(Math.toRadians(theta)));
    }

    public void setRotationalDemand(double rDemand) {
        this.rDemand = Math.signum(rDemand) * Math.min(Math.abs(rDemand), 1.0);
    }

    public void setCartesianSwerveDemand(double xDemand, double yDemand, double rDemand) {
        setCartesianTranslationDemand(xDemand, yDemand);
        setRotationalDemand(rDemand);
    }

    public void setPolarSwerveDemand(double r, double theta, double rDemand) {
        setPolarTranslationDemand(r, theta);
        setRotationalDemand(rDemand);
    }

    public void simulate() {
        long currentTime = System.nanoTime();
        double dt = (currentTime - lastTime) / 1000000000.0;
        lastTime = currentTime;

        // Kinematic integration
        double tAlpha = Constants.kTurretLoad * (Constants.kTurretMaxVelocity * tDemand - tOmega);
        double xAccel = Constants.kTranslationLoad * (Constants.kTranslationMaxVelocity * xDemand - xVelocity);
        double yAccel = Constants.kTranslationLoad * (Constants.kTranslationMaxVelocity * yDemand - yVelocity);
        double rAlpha = Constants.kRotationLoad * (Constants.kRotationMaxVelocity * rDemand - rOmega);


        tAlpha = Double.valueOf(tAlpha).isNaN() ? Math.signum(tAlpha) * Constants.kTurretMaxAcceleration : Math.signum(tAlpha) * Math.min(Constants.kTurretMaxAcceleration, Math.abs(tAlpha));
        xAccel = Double.valueOf(xAccel).isNaN() ? Math.signum(xAccel) * Constants.kTranslationMaxAcceleration : Math.signum(xAccel) * Math.min(Constants.kTranslationMaxAcceleration, Math.abs(xAccel));
        yAccel = Double.valueOf(yAccel).isNaN() ? Math.signum(yAccel) * Constants.kTranslationMaxAcceleration : Math.signum(yAccel) * Math.min(Constants.kTranslationMaxAcceleration, Math.abs(yAccel));
        rAlpha = Double.valueOf(rAlpha).isNaN() ? Math.signum(rAlpha) * Constants.kRotationMaxAcceleration : Math.signum(rAlpha) * Math.min(Constants.kRotationMaxAcceleration, Math.abs(rAlpha));

        System.out.println("RPM/S: " + tAlpha * 60 / 360);

        tOmega += tAlpha * dt;
        xVelocity += xAccel * dt;
        yVelocity += yAccel * dt;
        rOmega += rAlpha * dt;

        System.out.println("RPM: " + tOmega * 60 / 360);

        tTheta += tOmega * dt;
        xPosition += xVelocity * dt;
        yPosition += yVelocity * dt;
        rTheta += rOmega * dt;

        System.out.println("Theta: " + tTheta);
    }

    public double getTurretTheta() {
        return tTheta;
    }

    public double getRobotX() {
        return xPosition;
    }

    public double getRobotY() {
        return yPosition;
    }

    public double getRobotHeading() {
        return rTheta;
    }
}
