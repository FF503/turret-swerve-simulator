package com.team503.lib;

import com.team503.frc2021.Constants;

public class ParticleFilter {

    private static ParticleFilter instance = null;

    public Particle[] particles;
    public Beacon[] beacons;
    public double estimateX;
    public double estimateY;
    public Pose RobotPose;
    private Pose pose;
    private int particleCount = 0;
    private int curFrame;
    private double xPos;
    private double yPos;
    private double tPos;
    private double maxDist;
    private double movedX = 0.0;
    private double movedY = 0.0;
    private double movedTheta = 0.0;


    //constructor
    public ParticleFilter() {
    }

    public static ParticleFilter getInstance() {
        return instance == null ? instance = new ParticleFilter() : instance;
    }

    public void Init(int NumberOfParticles) {
        System.out.println("Particle Filter Init..");

        this.particleCount = NumberOfParticles;
        int beaconCount = Constants.KNoOfBeacons;

        //allocate memory for arrays 
        particles = new Particle[particleCount];
        beacons = new Beacon[beaconCount];

        //initialize particle array with random locations 
        for (int i = 0; i < particleCount; i++) {
            pose = new Pose(0.0, 0.0, 0.0);
            particles[i] = new Particle(pose, 1.0);
            particles[i].randomize();
        }

        //initialize beacon array 
        beacons[0] = new Beacon(Constants.kFieldWidth - 94, 10);
        beacons[1] = new Beacon(190, 220);
        beacons[2] = new Beacon(94, 10);
        beacons[3] = new Beacon(94, Constants.kFieldLength - 10);
        beacons[4] = new Beacon(Constants.kFieldWidth - 190, Constants.kFieldLength - 220);
        beacons[5] = new Beacon(Constants.kFieldWidth - 94, Constants.kFieldLength - 10);


        //Initialize other variables 
        curFrame = -1;
        maxDist = Math.floor(
          Math.sqrt(Constants.kFieldWidth * Constants.kFieldWidth + Constants.kFieldLength * Constants.kFieldLength));

        //Initialize Robot position to a random location on field 
        xPos = Math.floor(Math.random() * Constants.kFieldWidth);
        yPos = Math.floor(Math.random() * Constants.kFieldLength);
        tPos = Math.floor(Math.random() * 360.0);
        RobotPose = new Pose(xPos, yPos, tPos);
    }

    // public void listParticles() {
    //     for(int i=0; i<particleCount; i++){
    //         System.out.println("Particle "+i+": "+ particles[i].getParticle());
    //     }
    // }

    public void SetRobotPosition(double newX, double newY, double newTheta) {
        RobotPose.setX(newX);
        RobotPose.setY(newY);
        RobotPose.setTheta(newTheta);
        xPos = newX;
        yPos = newY;
        tPos = newTheta;
    }

    //updates filter with distance moved(inches) according to odometry
    public void ProcessOdometry(double odomX, double odomY, double odomTheta) {
        movedX = odomX;
        movedY = odomY;
        movedTheta = odomTheta;

        SetRobotPosition(RobotPose.getX() + movedX, RobotPose.getY() + movedY, RobotPose.getTheta() + movedTheta);
    }


    // Particle Filter: run the actual particle filter algorithm here
    public void Process() {
        double totalX = 0.0d;
        double totalY = 0.0d;
        double totalWX = 0.0d;
        double totalWY = 0.0d;
        double totalW = 0.0d;

        curFrame++;

        for (Particle particle : particles) {
            pose = particle.getPose();
            totalX += pose.getX();
            totalY += pose.getY();
            double weight = particle.getWeight();
            totalWX += (weight * pose.getX());
            totalWY += (weight * pose.getY());
            totalW += weight;
        }

        // direct average location of all particles
        estimateX = Math.floor(totalX / particles.length);
        estimateY = Math.floor(totalY / particles.length);

        // weighted average of all particles
        double estimateWX = Math.floor(totalWX / totalW);
        double estimateWY = Math.floor(totalWY / totalW);

        // 1. if robot moved, update all particles
        //	  by the same amount as the robot movement
        if (movedX != 0.0 || movedY != 0.0) {
            for (Particle particle : particles) {
                pose = particle.getPose();
                pose.setX((pose.getX() + movedX));
                pose.setY((pose.getY() + movedY));
            }
        }

        // 2. do a random walk if on random walk frame
        if (Constants.kWalkFrequency != 0 && (curFrame % Constants.kWalkFrequency) == 0) {
            for (Particle particle : particles) {
                pose = particle.getPose();
                double dX = Math.floor(Math.random() * (Constants.KWalkMax + 1)) - Constants.KWalkMax / 2;
                double dY = Math.floor(Math.random() * (Constants.KWalkMax + 1)) - Constants.KWalkMax / 2;

                pose.setX((pose.getX() + dX));
                pose.setY((pose.getY() + dY));
            }
        }

        // 3. estimate weights of every particle
        double maxWeight = 0.0;
        for (Particle particle : particles) {
            pose = particle.getPose();
            double weightSum = 0.0;
            for (Beacon beacon : beacons) {
                // get distance to beacon of both the particle and the robot
                double robotDistToBeacon = distance(xPos, yPos,
                  beacon.getX(), beacon.getY());
                double particleDistToBeacon = distance(pose.getX(), pose.getY(),
                  beacon.getX(), beacon.getY());
                weightSum += getWeight(robotDistToBeacon, particleDistToBeacon);
            }
            double weight = weightSum / beacons.length;
            particle.degrade(weight);
            if (weight > maxWeight)
                maxWeight = weight;
        }

        // 4. normalize weights
        double weightSum = 0;

        for (Particle particle : particles) {
            particle.normalize(maxWeight);
            weightSum += particle.getWeight();
        }

        // 5. resample: pick each particle based on probability
        Particle[] newParticles;
        //allocate memory for arrays 
        int newParticleCount = 0;
        newParticles = new Particle[particleCount];
        int numParticles = particles.length;
        for (int i = 0; i < numParticles; i++) {
            double choice = Math.random() * weightSum;
            int index = -1;
            do {
                index++;
                choice -= particles[index].getWeight();
            } while (choice > 0.0);

            newParticles[newParticleCount] = particles[index].getClone();
            newParticleCount++;
        }

        //Particle Aray becomes new particles 
        particles = newParticles;

        //Reset Moved distances 
        movedX = movedY = movedTheta = 0.0;

    }

    // Distance Formula: returns the distance between two given points
    private double distance(double x1, double y1, double x2, double y2) {
        double distX = x1 - x2;
        double distY = y1 - y2;
        return Math.floor(Math.sqrt(distX * distX + distY * distY));
    }

    // computes distance weight between a
    private double getWeight(double robotDistToBeacon, double particleDistToBeacon) {
        double diff = Math.abs(robotDistToBeacon - particleDistToBeacon);
        return (maxDist - diff) / maxDist;
    }
}