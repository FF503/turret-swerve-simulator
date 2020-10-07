package com.team503.frc2021;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TurretAnimation extends JFrame {

    private TurretAnimation() {
        super("FF503 Turret Animation");

        JPanel graphicsPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;
                try {
                    g2.drawImage(ImageIO.read(new File("field.jpg")), 0, 0, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.green);

                int thickness = 2;
                for (int i = 0; i <= thickness; i++) {
                    g2.draw3DRect(1200 - i, 500 - i, 150 + 2 * i, 150 + 2 * i, true);
                }

                g2.setStroke(new BasicStroke(2));

                g2.drawArc(1250, 510, 50, 50, 0, 360);
//                g2.drawOval(1250, 510, 50, 50);



            }
        };
        add(graphicsPanel);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            TurretAnimation frame = new TurretAnimation();
            frame.setVisible(true);
        });
    }
}
