package com.team503.frc2021;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TurretAnimation extends JFrame {

    private TurretAnimation() {
        super("FF503 Turret Animation");
//        JComponent background = new JComponent() {
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                try {
//                    g.drawImage(ImageIO.read(new File("field.jpg")), 0, 0, this);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
        try {
            JLabel bg = new JLabel(new ImageIcon(ImageIO.read(new File("field.jpg"))));
            add(bg);
            System.out.println(bg.getSize());

        } catch (IOException e) {
            e.printStackTrace();
        }
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
