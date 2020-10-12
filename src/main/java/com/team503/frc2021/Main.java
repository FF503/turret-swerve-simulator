package com.team503.frc2021;

import com.team503.frc2021.subsystems.TurretSwerve;
import com.team503.lib.BallPose;
import com.team503.lib.FrogPIDF;
import com.team503.lib.Pose;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Main {
    private static int frameCount = -1;

    public static void main(String[] args) {
        System.out.println("Starting Turret...");

        TurretSwerve.getInstance();

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

        Pose turretPos = new Pose(200, 100, 90); //assuming 0 is front/up
        ArrayList<BallPose> shotBalls = new ArrayList<>();
        shotBalls.add(new BallPose(10, 10, 180, 0.2));
        shotBalls.add(new BallPose(100, 350, 180, 0.2));

        FrogPIDF pidController = new FrogPIDF(.005, 0, 0, FrogPIDF.ControlMode.Position_Control);
        pidController.setSetpoint(720);

        ActionListener mainLoop = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                double turretTheta = TurretSwerve.getInstance().getTheta();

                TurretSwerve.getInstance().setDemand(pidController.calculateOutput(turretTheta));

//                System.out.println(turretTheta);
                //bump the frame count
                frameCount++;

                if (frameCount == 40 || frameCount == 80) {
                    //simulate an odometry message that moves the robot back 3 ft

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

                //draw Robot theoretically centered
                double robotX = turretPos.getX();
                double robotY = turretPos.getY();
                graphics.setColor(Color.LIGHT_GRAY);
                graphics.fillOval((int) robotX - (Constants.kRobotSize / 2), (int) robotY - (Constants.kRobotSize / 2), Constants.kRobotSize, Constants.kRobotSize);
                double lineX = robotX + Math.cos(-1 * (turretTheta + 90) * (Math.PI / 180)) * (Constants.kRobotSize / 2.0);
                double lineY = robotY + Math.sin(-1 * (turretTheta + 90) * (Math.PI / 180)) * (Constants.kRobotSize / 2.0);
                graphics.setColor(Color.green);
                graphics.drawLine((int) robotX, (int) robotY, (int) lineX, (int) lineY);
                //size of square robot
                // int kRobotSize = 30;
                // graphics.fillRect(robotX - kRobotSize / 2, robotY - kRobotSize / 2, kRobotSize, kRobotSize);

                // System.out.println("There are " + shotBalls.size() + " balls on the field");
                for (int i = shotBalls.size() - 1; i >= 0; i--) {
                    BallPose ball = shotBalls.get(i);
                    //draw ball
                    graphics.setColor(Color.YELLOW);
                    graphics.fillOval((int) ball.getX() - Constants.ballDiameter / 2, (int) ball.getY() - Constants.ballDiameter / 2, Constants.ballDiameter, Constants.ballDiameter);
                    //move ball
                    ball.setX(ball.getX() + Math.cos(-1 * (ball.getTheta() + 90) * (Math.PI / 180)) * ball.getSpeed());
                    ball.setY(ball.getY() + Math.sin(-1 * (ball.getTheta() + 90) * (Math.PI / 180)) * ball.getSpeed());
                    if (ball.getX() < 0 || ball.getX() > Constants.kFieldWidth || ball.getY() < 0 || ball.getY() > Constants.kFieldLength) {
                        shotBalls.remove(i);
                        System.out.println("A ball has left the field");
                    }
                }

                bufferStrategy.show();
                graphics.dispose();

            }
        };

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                double y2 = turretPos.getY();
                double x2 = turretPos.getX();
                double t2 = turretPos.getTheta();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        System.out.println("Fire!");
                        shotBalls.add(new BallPose(turretPos.getX(), turretPos.getY(), turretPos.getTheta(), Constants.ballSpeed));
                        break;
                    case KeyEvent.VK_UP:
                        System.out.println("Up");
                        y2 -= Constants.robotSpeed;
                        if (y2 < 0) {
                            y2 = 0;
                        }
                        turretPos.setY(y2);
                        break;
                    case KeyEvent.VK_DOWN:
                        System.out.println("Down");
                        y2 += Constants.robotSpeed;
                        if (y2 > Constants.kFieldLength) {
                            y2 = Constants.kFieldLength;
                        }
                        turretPos.setY(y2);
                        break;
                    case KeyEvent.VK_LEFT:
                        System.out.println("Left");
                        x2 -= Constants.robotSpeed;
                        if (x2 < 0) {
                            x2 = 0;
                        }
                        turretPos.setX(x2);
                        break;
                    case KeyEvent.VK_RIGHT:
                        System.out.println("Right");
                        x2 += Constants.robotSpeed;
                        if (x2 > Constants.kFieldWidth) {
                            x2 = Constants.kFieldWidth;
                        }
                        turretPos.setX(x2);
                        break;
                    case KeyEvent.VK_D:
                        System.out.println("Turn Right");
//                        t2 -= Constants.turnSpeed;
                        TurretSwerve.getInstance().setDemand(-1.0);
//                        turretPos.setTheta(t2);
                        break;
                    case KeyEvent.VK_A:
                        System.out.println("Turn Left");
//                        t2 += Constants.turnSpeed;
                        TurretSwerve.getInstance().setDemand(1.0);
//                        turretPos.setTheta(t2);
                        break;
                    default:
                        System.out.println("Unknown key");
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        Timer graphics = new Timer(1000 / 60, mainLoop);
        graphics.start();
    }
}