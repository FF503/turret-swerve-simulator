package com.team503.frc2021.subsystems;

public class Turret {
    private static Turret mInstance;
    private double demand = 0;
    private ControlState mState = ControlState.OPEN_LOOP;

    public Turret() {

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

    public enum ControlState {
        OPEN_LOOP,
        MOTION_MAGIC
    }
}
