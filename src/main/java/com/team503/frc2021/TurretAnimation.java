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
        BufferedImage background = null;
        try {
             background = ImageIO.read(new File("field.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setContentPane(new ImagePanel(background));
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            TurretAnimation frame = new TurretAnimation();
            frame.setVisible(true);
        });
    }

    class FieldPanel extends JPanel {

    }

    static class ImagePanel extends JComponent {
        private Image image;
        public ImagePanel(Image image) {
            this.image = image;
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }

}
