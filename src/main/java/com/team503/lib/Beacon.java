package com.team503.lib;

public class Beacon {
    private double x;
    private double y;

    public Beacon(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double newX) {
        this.x = newX;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double newY) {
        this.y = newY;
    }


    public String toString() {
        return "X=" + x + " Y=" + y;
    }

}