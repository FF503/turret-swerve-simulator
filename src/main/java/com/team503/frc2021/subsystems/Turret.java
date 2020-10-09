package com.team503.frc2021.subsystems;

import com.team503.frc2021.Constants;

public class Turret {
    private static Turret mInstance;
    private double demand, theta, omega, alpha, dt;
    private long lastTime, currentTime;
    private ControlState mState;

    public Turret() {
        demand = theta = omega = dt = alpha = currentTime = 0;
        mState = ControlState.OPEN_LOOP;
        lastTime = System.nanoTime();
    }

    public static Turret getInstance() {
        return mInstance == null ? mInstance = new Turret() : mInstance;
    }

    public double getDemand() {
        return demand;
    }

    public void setDemand(double demand) {
        this.demand = Math.signum(demand) * Math.min(Math.abs(demand), 1.0);
    }

    public ControlState getState() {
        return mState;
    }

    public void setState(ControlState mState) {
        this.mState = mState;
    }

    public double getTheta() {
        currentTime = System.nanoTime();
        dt = (currentTime - lastTime) / 1000000000.0;
        lastTime = currentTime;

        // Kinematic integration
        alpha = Constants.kSimulatedLoad * (Constants.kMaxVelocity * demand - omega);
        alpha = new Double(alpha).isNaN() ? Math.signum(alpha) * Constants.kMaxAcceleration : Math.signum(alpha) * Math.min(Constants.kMaxAcceleration, Math.abs(alpha));
        System.out.println("RPM/S: " + alpha * 60 / 360);

        omega += alpha * dt;
        System.out.println("RPM: " + omega * 60  / 360);

        theta += omega * dt;
        System.out.println("Theta: " + theta);


        return theta;
    }

    public enum ControlState {
        OPEN_LOOP,
        MOTION_MAGIC
    }
}
