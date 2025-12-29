package com.ja.user.service;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

@Service
public class OgImageService {

    public byte[] generate(Long userId) {
        try {
            BufferedImage image = new BufferedImage(1200, 630, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();

            g.setColor(new Color(99, 102, 241));
            g.fillRect(0, 0, 1200, 630);

            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 64));
            g.drawString("JavaArray Profile", 100, 120);

            g.setFont(new Font("SansSerif", Font.PLAIN, 40));
            g.drawString("User ID: " + userId, 100, 220);

            g.dispose();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "png", out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate OG image", e);
        }
    }
}
