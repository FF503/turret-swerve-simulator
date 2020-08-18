package com.team503.lib;

public class Pose {
    private double x;
    private double y;
    private double theta;

    public Pose(double x, double y, double theta) {
        this.x = x;
        this.y = y;
        this.theta = theta;
    }

    public double getX() {
        return x;
    }

    public void setX(double newX) {
        this.x = newX;
    }

    public double getY() {
        return y;
    }

    public void setY(double newY) {
        this.y = newY;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double newTheta) {
        this.theta = newTheta;
    }
}