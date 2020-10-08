package com.team503.frc2021;

import com.team503.frc2021.subsystems.Turret;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TurretAnimation extends JFrame {

    public static final int DELAY = 25;

    private JPanel graphicsPanel = null;
    private double turretTheta = 0.0;

    private TurretAnimation() {
        super("FF503 Turret Animation");

        graphicsPanel = new JPanel() {

            @Override
            public void addNotify() {
                super.addNotify();

                Thread animator = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Turret.getInstance();
                        Turret.getInstance().setDemand(1);
                        long beforeTime, timeDiff, sleep;

                        beforeTime = System.currentTimeMillis();

                        while (true) {

                            cycle();
                            repaint();

                            timeDiff = System.currentTimeMillis() - beforeTime;
                            sleep = DELAY - timeDiff;

                            if (sleep < 0) {
                                sleep = 2;
                            }

                            try {
                                Thread.sleep(sleep);
                            } catch (InterruptedException e) {

                                String msg = String.format("Thread interrupted: %s", e.getMessage());

                                JOptionPane.showMessageDialog(graphicsPanel, msg, "Error",
                                  JOptionPane.ERROR_MESSAGE);
                            }

                            beforeTime = System.currentTimeMillis();
                        }
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

                g2.setColor(new Color(26, 26, 26));

                int thickness = 2;
                for (int i = 0; i <= thickness; i++) {
                    g2.draw3DRect(1200 - i, 500 - i, 150 + 2 * i, 150 + 2 * i, true);
                }

                g2.setStroke(new BasicStroke(3));
                g2.drawArc(1250, 510, 50, 50, 0, 360);
//                g2.drawArc();

//                Polygon turretShape = new Polygon(new int[] {1260, 1290, 1290, 1285, 1285, 1265, 1265, 1260}, new int[] {505, 505, 565, 565, 570, 570, 565, 565}, 8);
//                g2.drawPolygon(turretShape);

                g2.setColor(new Color(76, 214, 58));
                g2.setStroke(new BasicStroke(2));

                g2.fillOval(1273, 533, 5, 5);
                g2.drawLine(1275, 535, (int)(1275 - 100 * Math.cos(Math.toRadians(turretTheta))), (int)(535 + 100 * Math.sin(Math.toRadians(turretTheta))));
            }
        };
        add(graphicsPanel);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
    }

    private void cycle() {
        turretTheta = Turret.getInstance().getTheta();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            TurretAnimation frame = new TurretAnimation();
            frame.setVisible(true);
        });
    }
}
