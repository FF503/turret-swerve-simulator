package com.team503.frc2021;

import com.team503.frc2021.subsystems.TurretSwerve;
import com.team503.lib.FrogPIDF;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TurretSwerveAnimation extends JFrame {

    public static final int DELAY = 5;

    private final JPanel graphicsPanel;
    private double turretTheta;
    private double robotX;
    private double robotY;
    private double robotHeading;

    private TurretSwerveAnimation() {
        super("FF503 Turret Animation");

        graphicsPanel = new JPanel() {

            @Override
            public void addNotify() {
                super.addNotify();

                FrogPIDF pidController = new FrogPIDF(Constants.kTurretP, Constants.kTurretI, Constants.kTurretD, FrogPIDF.ControlMode.Position_Control);

                Thread animator = new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                        String msg = String.format("Thread interrupted: %s", e.getMessage());

                        JOptionPane.showMessageDialog(graphicsPanel, msg, "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }

                    TurretSwerve.getInstance();
                    long beforeTime, timeDiff, sleep;

                    pidController.setSetpoint(2970);
                    beforeTime = System.currentTimeMillis();

                    while (true) {
                        TurretSwerve.getInstance().setPolarSwerveDemand(0.07, 45, 0.5);
                        TurretSwerve.getInstance().setTurretDemand(0.00);
                        cycle();
                        repaint();

                        timeDiff = System.currentTimeMillis() - beforeTime;
                        sleep = DELAY - timeDiff;

                        if (sleep < 0) {
                            sleep = 0;
                        }

                        try {
                            //noinspection BusyWait
                            Thread.sleep(sleep);
                        } catch (InterruptedException e) {

                            String msg = String.format("Thread interrupted: %s", e.getMessage());

                            JOptionPane.showMessageDialog(graphicsPanel, msg, "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }

                        beforeTime = System.currentTimeMillis();
                    }
                });
                animator.start();
            }

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;
                try {
                    g2.drawImage(ImageIO.read(new File("field.jpg")), 0, 0, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.translate(robotX, -robotY);
                g2.rotate(Math.toRadians(robotHeading), 1275, 575);
                g2.setColor(new Color(26, 26, 26));

                int thickness = 2;
                for (int i = 0; i <= thickness; i++) {
                    g2.draw3DRect(1200 - i, 500 - i, 150 + 2 * i, 150 + 2 * i, true);
                }

                g2.setColor(new Color(76, 214, 58));
                g2.setStroke(new BasicStroke(2));

                Polygon turretShape = new Polygon(new int[]{1255, 1295, 1295, 1285, 1285, 1265, 1265, 1255}, new int[]{505, 505, 555, 555, 560, 560, 555, 555}, 8);

                g2.rotate(Math.toRadians(turretTheta), 1275, 530);
                g2.drawPolygon(turretShape);

                Toolkit.getDefaultToolkit().sync();
            }
        };
        add(graphicsPanel);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            TurretSwerveAnimation frame = new TurretSwerveAnimation();
            frame.setVisible(true);
        });
    }

    private void cycle() {
        TurretSwerve.getInstance().simulate();
        turretTheta = TurretSwerve.getInstance().getTurretTheta();
        robotX = TurretSwerve.getInstance().getRobotX();
        robotY = TurretSwerve.getInstance().getRobotY();
        robotHeading = TurretSwerve.getInstance().getRobotHeading();
    }
}
