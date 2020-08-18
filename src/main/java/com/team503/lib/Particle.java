package com.team503.lib;

import com.team503.frc2021.Constants;

public class Particle {
    private Pose pose;
    private double weight;

    public Particle(Pose pose, double weight) {
        this.pose = pose;
        this.weight = weight;
    }

    // randomize particle position
    public void randomize() {
        this.pose.setX(Math.floor(Math.random() * Constants.kFieldWidth));
        this.pose.setY(Math.floor(Math.random() * Constants.kFieldLength));
        this.pose.setTheta(Math.floor(Math.random() * 360.0));
    }

    // degrade weight: multiply current weight by the given amount
    //	(decimal between 0 and 1)
    public void degrade(double weight) {
        this.weight *= weight;
    }

    // normalizes weight around the maximum such that the particle with
    //	the most weight will now have a weight of 1.0, and all other
    //	particles' weights will scale accordingly
    public void normalize(double maxWeight) {
        if (maxWeight == 0.0) {
            System.out.println("error - maxWeight 0 (check weight function)");
            return;
        }
        this.weight /= maxWeight;
    }

    // returns this particle's weight
    public double getWeight() {
        return this.weight;
    }

    public Pose getPose() {
        return this.pose;
    }

    // returns a NEW particle object with position and weight identical
    //	to this one
    public Particle getClone() {
        Pose pCopy;
        Particle partCopy;
        //copy the Pose
        pCopy = new Pose(this.pose.getX(), this.pose.getY(), this.pose.getTheta());
        //copy the particle
        partCopy = new Particle(pCopy, this.weight);
        return partCopy;
    }

    public String toString() {
        return "X=" + this.pose.getX() + " Y=" + this.pose.getY() + " Theta=" + this.pose.getTheta() + " Weight=" + this.weight;
    }

}