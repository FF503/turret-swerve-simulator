package com.team503.frc2021;

import com.team503.lib.ParticleFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Main {
    private static int kEstimateRadius = 25;
    private static int frameCount = -1;

    public static void main(String[] args) {
        System.out.println("Starting Navigator...");

        ParticleFilter pf = ParticleFilter.getInstance();
        int kNumberOfParticles = 500;
        pf.Init(kNumberOfParticles);


        JFrame frame = new JFrame("FF503 Navigator");
        frame.setSize(Constants.kFieldWidth + 12, Constants.kFieldLength + 38);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setFocusable(false);

        Canvas ctx = new Canvas();

        ctx.setBackground(Color.black);
        ctx.setSize(Constants.kFieldWidth, Constants.kFieldLength);
        frame.add(ctx);
        ctx.createBufferStrategy(3); // incorrect reference to canvas
        boolean running = true;

        //Set Robot Initial Starting Position 
        pf.SetRobotPosition(95, Constants.kFieldLength - 135, 180);

        while (running) {
            //bump the frame count 
            frameCount++;

            if (frameCount == 40 || frameCount == 80) {
                //simulate an odometry message that moves the robot back 3 ft 
                pf.ProcessOdometry(0.0, -36.0, 0.0);
            }

            //run particle filter for 1 frame
            pf.Process();

            BufferStrategy bufferStrategy = ctx.getBufferStrategy();
            Graphics graphics = bufferStrategy.getDrawGraphics();
            graphics.clearRect(0, 0, Constants.kFieldWidth, Constants.kFieldLength);

            //draw field outline 
            graphics.setColor(Color.GRAY);
            graphics.drawRect(0, 0, Constants.kFieldWidth, Constants.kFieldLength);

            //graphics.setColor(Color.GREEN);
            //graphics.drawString("This is some text at to left corner",5,15);

            //draw white starting lines 
            graphics.setColor(Color.WHITE);
            graphics.drawLine(0, 120, Constants.kFieldWidth, 120);
            graphics.drawLine(0, Constants.kFieldLength - 120, Constants.kFieldWidth, Constants.kFieldLength - 120);

            //draw red trench 
            graphics.setColor(Color.RED);
            graphics.drawLine(0, 206, 55, 206);
            graphics.drawLine(0, (Constants.kFieldLength - 206), 55, (Constants.kFieldLength - 206));
            graphics.drawLine(55, 206, 55, Constants.kFieldLength - 206);

            //draw blue trench
            graphics.setColor(Color.BLUE);
            graphics.drawLine(Constants.kFieldWidth, 206, Constants.kFieldWidth - 55, 206);
            graphics.drawLine(Constants.kFieldWidth, Constants.kFieldLength - 206, Constants.kFieldWidth - 55, Constants.kFieldLength - 206);
            graphics.drawLine(Constants.kFieldWidth - 55, 206, Constants.kFieldWidth - 55, Constants.kFieldLength - 206);


            //draw goal 
            graphics.setColor(Color.BLUE);
            graphics.drawLine(Constants.kFieldWidth - 95, 30, Constants.kFieldWidth - 95 - 24, 0);
            graphics.drawLine(Constants.kFieldWidth - 95, 30, Constants.kFieldWidth - 95 + 24, 0);


            //draw shield generator 
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setColor(Color.GRAY);
            g2d.translate(Constants.kFieldWidth / 2, Constants.kFieldLength / 2);
            g2d.rotate(-22.5);
            g2d.drawRect(-78, -78, 156, 156);

            //Undo the translate 
            g2d.rotate(22.5);
            g2d.translate(-Constants.kFieldWidth / 2, -Constants.kFieldLength / 2);

            //draw goal 
            graphics.setColor(Color.RED);
            graphics.drawLine(95, Constants.kFieldLength - 30, 95 - 24, Constants.kFieldLength);
            graphics.drawLine(95, Constants.kFieldLength - 30, 95 + 24, Constants.kFieldLength);

            //draw reload
            graphics.setColor(Color.RED);
            graphics.drawLine(95, 30, 95 - 30, 0);
            graphics.drawLine(95, 30, 95 + 30, 0);
            graphics.setColor(Color.BLUE);
            graphics.drawLine(Constants.kFieldWidth - 95, Constants.kFieldLength - 30, Constants.kFieldWidth - 95 - 30, Constants.kFieldLength);
            graphics.drawLine(Constants.kFieldWidth - 95, Constants.kFieldLength - 30, Constants.kFieldWidth - 95 + 30, Constants.kFieldLength);

            //draw Robot 
            graphics.setColor(Color.WHITE);
            int robotX = (int) pf.RobotPose.getX();
            int robotY = (int) pf.RobotPose.getY();
            //size of square robot
            int kRobotSize = 30;
            graphics.fillRect(robotX - kRobotSize / 2, robotY - kRobotSize / 2, kRobotSize, kRobotSize);

            //draw beacons 
            graphics.setColor(Color.YELLOW);
            for (int i = 0; i < pf.beacons.length; i++) {
                int kBeaconSize = 20;
                int beaconX = (int) (pf.beacons[i].getX() - kBeaconSize / 2);
                int beaconY = (int) (pf.beacons[i].getY() - kBeaconSize / 2);
                graphics.fillRect(beaconX, beaconY, kBeaconSize, kBeaconSize);
            }


            //draw particles 
            graphics.setColor(Color.BLUE);
            for (int i = 0; i < pf.particles.length; i++) {
                int partX = (int) pf.particles[i].getPose().getX();
                int partY = (int) pf.particles[i].getPose().getY();
                graphics.fillRect(partX, partY, 2, 2);

            }

            // draw estimated (belief) position as determined by the particles (red)
            graphics.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3));
            int estX = (int) (pf.estimateX) - (30 / 2);
            int estY = (int) (pf.estimateY) - (30 / 2);

            //  graphics.drawArc(estX, estY, 25, 25, 0, estR
            graphics.drawOval(estX, estY, 30, 30);


            bufferStrategy.show();
            graphics.dispose();
            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException x) {
                x.printStackTrace();
            }

        }


        //   pf.listParticles();

        //   System.out.println("After Process...");
        //   pf.listParticles();
        System.out.println("Stopping Navigator...");
    }
}