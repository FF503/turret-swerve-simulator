package com.team503.frc2021;

import com.team503.frc2021.subsystems.TurretSwerve;
import com.team503.lib.FrogPIDF;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class TurretSwerveAnimation extends JFrame {

    public static final int DELAY = 5;

    private final JPanel graphicsPanel;
    private double turretTheta;
    private double robotX;
    private double robotY;
    private double robotHeading;

    private static volatile boolean wPressed = false;

    public static boolean isWPressed() {
        synchronized (TurretSwerveAnimation.class) {
            return wPressed;
        }
    }

    private static volatile boolean aPressed = false;

    public static boolean isAPressed() {
        synchronized (TurretSwerveAnimation.class) {
            return aPressed;
        }
    }

    private static volatile boolean sPressed = false;

    public static boolean isSPressed() {
        synchronized (TurretSwerveAnimation.class) {
            return sPressed;
        }
    }

    private static volatile boolean dPressed = false;

    public static boolean isDPressed() {
        synchronized (TurretSwerveAnimation.class) {
            return dPressed;
        }
    }

    private static volatile boolean leftPressed = false;

    public static boolean isLeftPressed() {
        synchronized (TurretSwerveAnimation.class) {
            return leftPressed;
        }
    }

    private static volatile boolean rightPressed = false;

    public static boolean isRightPressed() {
        synchronized (TurretSwerveAnimation.class) {
            return rightPressed;
        }
    }

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

                    beforeTime = System.currentTimeMillis();

                    while (true) {
                        double x, y, r;
                        TurretSwerve.getInstance().setCartesianSwerveDemand(
                                x = (isAPressed() ? -0.3 : 0) + (isDPressed() ? 0.3 : 0),
                                y = (isSPressed() ? -0.3 : 0) + (isWPressed() ? 0.3 : 0),
                                r = (isLeftPressed() ? -0.3 : 0) + (isRightPressed() ? 0.3 : 0)
                        );

                        System.out.println("X: " + x);
                        System.out.println("Y: " + y);
                        System.out.println("R: " + r);

                        pidController.setSetpoint(getTurretLockedDirection());
                        TurretSwerve.getInstance().setTurretDemand(pidController.calculateOutput(turretTheta));

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

                g2.setColor(Color.red);
                g2.drawOval(1315, 150, 1, 1);

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
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(ke -> {
            synchronized (TurretSwerveAnimation.class) {
                switch (ke.getID()) {
                    case KeyEvent.KEY_PRESSED:
                        if (ke.getKeyCode() == KeyEvent.VK_W) {
                            wPressed = true;
                        }
                        if (ke.getKeyCode() == KeyEvent.VK_A) {
                            aPressed = true;
                        }
                        if (ke.getKeyCode() == KeyEvent.VK_S) {
                            sPressed = true;
                        }
                        if (ke.getKeyCode() == KeyEvent.VK_D) {
                            dPressed = true;
                        }
                        if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                            leftPressed = true;
                        }
                        if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                            rightPressed = true;
                        }
                        break;

                    case KeyEvent.KEY_RELEASED:
                        if (ke.getKeyCode() == KeyEvent.VK_W) {
                            wPressed = false;
                        }
                        if (ke.getKeyCode() == KeyEvent.VK_A) {
                            aPressed = false;
                        }
                        if (ke.getKeyCode() == KeyEvent.VK_S) {
                            sPressed = false;
                        }
                        if (ke.getKeyCode() == KeyEvent.VK_D) {
                            dPressed = false;
                        }
                        if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                            leftPressed = false;
                        }
                        if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                            rightPressed = false;
                        }
                        break;
                }
                return false;
            }
        });
        EventQueue.invokeLater(() -> {
            TurretSwerveAnimation frame = new TurretSwerveAnimation();
            frame.setVisible(true);
        });
    }

    private double getTurretLockedDirection() {
        double turretX = 1275 + robotX;
        double turretY = 530 - robotY;

        return 270 - robotHeading - Math.toDegrees(Math.atan2(turretY - 150, 1315 - turretX));
    }

    private void cycle() {
        TurretSwerve.getInstance().simulate();
        turretTheta = TurretSwerve.getInstance().getTurretTheta();
        robotX = TurretSwerve.getInstance().getRobotX();
        robotY = TurretSwerve.getInstance().getRobotY();
        robotHeading = TurretSwerve.getInstance().getRobotHeading();
    }
}
