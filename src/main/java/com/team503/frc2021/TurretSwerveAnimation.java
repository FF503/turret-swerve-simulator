package com.team503.frc2021;

import com.aromajoin.sdk.core.device.AromaShooter;
import com.aromajoin.sdk.jvm.DiscoverCallback;
import com.aromajoin.sdk.jvm.usb.USBASController;
import com.team503.frc2021.subsystems.TurretSwerve;
import com.team503.lib.FrogPIDF;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TurretSwerveAnimation extends JFrame {

    public static final int DELAY = 5;
    private static volatile boolean wPressed = false;
    private static volatile boolean aPressed = false;
    private static volatile boolean sPressed = false;
    private static volatile boolean dPressed = false;
    private static volatile boolean leftPressed = false;
    private static volatile boolean rightPressed = false;
    private final JPanel graphicsPanel;
    private double turretTheta;
    private double robotX;
    private double robotY;
    private double robotHeading;
    private static USBASController controller = new USBASController();

    private TurretSwerveAnimation() {
        super("FF503 Turret Animation");

        graphicsPanel = new JPanel() {

            @Override
            public void addNotify() {
                super.addNotify();

                FrogPIDF pidController = new FrogPIDF(Constants.kTurretP, Constants.kTurretI, Constants.kTurretD, FrogPIDF.ControlMode.Position_Control);

                Thread animator = new Thread(() -> {

                    TurretSwerve.getInstance();
                    long beforeTime, timeDiff, sleep;

                    beforeTime = System.currentTimeMillis();

                    while (true) {
                        double x, y, r;
                        TurretSwerve.getInstance().setCartesianSwerveDemand(
                                x = (isAPressed() ? -1.0 : 0) + (isDPressed() ? 1.0 : 0),
                                y = (isSPressed() ? -1.0 : 0) + (isWPressed() ? 1.0 : 0),
                                r = (isLeftPressed() ? -1.0 : 0) + (isRightPressed() ? 1.0 : 0)
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
                g2.fillOval(1312, 147, 7, 7);

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
                g2.drawLine(1275, 560, 1275, 10000);


                Toolkit.getDefaultToolkit().sync();
            }
        };
        add(graphicsPanel);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static boolean isWPressed() {
        synchronized (TurretSwerveAnimation.class) {
            return wPressed;
        }
    }

    public static boolean isAPressed() {
        synchronized (TurretSwerveAnimation.class) {
            return aPressed;
        }
    }

    public static boolean isSPressed() {
        synchronized (TurretSwerveAnimation.class) {
            return sPressed;
        }
    }

    public static boolean isDPressed() {
        synchronized (TurretSwerveAnimation.class) {
            return dPressed;
        }
    }

    public static boolean isLeftPressed() {
        synchronized (TurretSwerveAnimation.class) {
            return leftPressed;
        }
    }

    public static boolean isRightPressed() {
        synchronized (TurretSwerveAnimation.class) {
            return rightPressed;
        }
    }

    public static void main(String[] args) {
//        controller.scanAndConnect(new DiscoverCallback() {
//
//            @Override
//            public void onDiscovered(List<AromaShooter> aromaShooters) {
//                for(AromaShooter aromaShooter : aromaShooters){
//                    System.out.println(aromaShooter.toString());
//                }
//            }
//
//            @Override
//            public void onFailed(String msg) {
//                System.out.println("Couldn't detect controller");
//            }
//        });
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
        double turretX = 1275 + 45 * Math.sin(Math.toRadians(robotHeading)) + robotX;
        double turretY = 575 - 45 * Math.cos(Math.toRadians(robotHeading)) - robotY;

        System.out.println("Turret T " + robotHeading);
//        System.out.println("Turret Y " + turretY);

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
