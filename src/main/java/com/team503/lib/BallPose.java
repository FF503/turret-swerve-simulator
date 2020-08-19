package com.team503.lib;

public class BallPose extends Pose {
    private double speed;
    public BallPose(double x, double y, double theta, double speed) {
        super(x, y, theta);
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }
}
