package com.team503.frc2021.subsystems;

import com.team503.frc2021.Constants;

public class Turret {
    private static Turret mInstance;
    private double demand, theta, omega, alpha, dt;
    private long lastTime, currentTime;
    private ControlState mState;

    public Turret() {
        demand = theta = omega = dt = alpha = lastTime = currentTime = 0;
        mState = ControlState.OPEN_LOOP;
    }

    public static Turret getInstance() {
        return mInstance == null ? mInstance = new Turret() : mInstance;
    }

    public double getDemand() {
        return demand;
    }

    public void setDemand(double demand) {
        this.demand = demand;
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

        System.out.println(alpha);

        omega += alpha * dt;

        theta += omega * dt;

//        if (mState == ControlState.OPEN_LOOP) {
//
//        } else {
//
//        }


        return theta;
    }

    public enum ControlState {
        OPEN_LOOP,
        MOTION_MAGIC
    }
}
