package com.team503.lib;


public class FrogPIDF {
    private final double p, i, d, f;
    private double state;
    private double tolerance;
    private double setPoint;
    private long lastTime;
    private double lastError = 0;
    private double integral;
    private final ControlMode control;
    private double error;

    public FrogPIDF(double p, double i, double d, ControlMode controlMode) {
        this.p = p;
        this.i = i;
        this.d = d;
        this.control = controlMode;
        this.f = 0.0;
    }

    public FrogPIDF(double p, double i, double d, double f, ControlMode controlMode) {
        this.p = p;
        this.i = i;
        this.d = d;
        this.control = controlMode;
        this.f = (control == ControlMode.Velocity_Control) ? f : 0.0;
    }

    public void setSetpoint(double setPoint) {
        this.setPoint = setPoint;
        this.integral = 0;
        if (this.lastTime == 0) {
            this.lastTime = System.nanoTime();
        }
    }

    public double calculateOutput(double sensorState) {
        this.state = sensorState;
        double error = setPoint - sensorState;

//        System.out.println("PID Error: " + sensorState);

        this.error = error;
        if (Math.abs(error) < tolerance) {
            return 0.0;
        }
        double dError = error - lastError;
        long time = System.nanoTime();
        double dt = (time - lastTime) / 1000000000.0;
//        System.out.println("Current time: " + time);
//        System.out.println("Last time: " + lastTime);
        double derivative = dError / dt;
        integral += error * dt;
        double pOut = p * error;
        double iOut = i * integral;
        double dOut = d * derivative;
        double fOut = f * setPoint;


//        System.out.println("DT: " + dt);

        lastTime = time;
        lastError = error;
        // System.out.println("calc:"+Math.max(-1, Math.min(pOut + iOut + dOut + fOut,
        // 1)));

        return Math.max(-1, Math.min(pOut + iOut + dOut + fOut, 1));
    }

    public void setTolerance(double t) {
        this.tolerance = t;
    }

    public boolean onTarget() {
        return Math.abs(state - setPoint) < tolerance;
    }

    public double getError() {
        return error;
    }

    public enum ControlMode {
        Velocity_Control, Position_Control
    }

}