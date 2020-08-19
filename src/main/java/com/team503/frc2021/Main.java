package com.team503.frc2021;

import com.team503.lib.Pose;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Main {
    private static int frameCount = -1;

    public static void main(String[] args) {
        System.out.println("Starting Turret...");

        JFrame frame = new JFrame("FF503 Turret Simulator");
        frame.setSize(Constants.kFieldWidth + 12, Constants.kFieldLength + 38);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // on close listener
                System.out.println("Stopping Turret Sim");
            }
        });
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setFocusable(true);

        Canvas ctx = new Canvas();

        ctx.setBackground(Color.black);
        ctx.setSize(Constants.kFieldWidth, Constants.kFieldLength);
        frame.add(ctx);
        ctx.createBufferStrategy(3); // incorrect reference to canvas

        Pose turretPos = new Pose(20,100, 0); //assuming 0 is front/up
        ArrayList<Pose> shotBalls = new ArrayList<>();

        while (true) {
            //bump the frame count 
            frameCount++;

            if (frameCount == 40 || frameCount == 80) {
                //simulate an odometry message that moves the robot back 3 ft 

            }

            if (frameCount % 10 == 0) {
                System.out.println(frameCount + "frames");
            }
            //run particle filter for 1 frame


            BufferStrategy bufferStrategy = ctx.getBufferStrategy();
            Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
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
            Graphics2D g2d = graphics;
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
            //size of square robot
           // int kRobotSize = 30;
           // graphics.fillRect(robotX - kRobotSize / 2, robotY - kRobotSize / 2, kRobotSize, kRobotSize);

            bufferStrategy.show();
            graphics.dispose();
            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException x) {
                x.printStackTrace();
            }

        }
    }
}