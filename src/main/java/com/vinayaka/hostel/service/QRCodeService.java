package com.vinayaka.hostel.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class QRCodeService {

    public String generateQRCodeImage(String text, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        // Convert BitMatrix to BufferedImage
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageConfig config = new MatrixToImageConfig(MatrixToImageConfig.BLACK, MatrixToImageConfig.WHITE);
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream, config);
        byte[] pngData = pngOutputStream.toByteArray();

        // Load image into BufferedImage for editing
        ByteArrayInputStream bis = new ByteArrayInputStream(pngData);
        BufferedImage validQR = ImageIO.read(bis);

        // Overlay "GGU" Logo
        BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) combined.getGraphics();
        g.drawImage(validQR, 0, 0, null);

        // Settings for Logo Overlay
        String overlayText = "GGU";
        g.setColor(Color.WHITE);
        int centerX = width / 2;
        int centerY = height / 2;
        int rectWidth = 40;
        int rectHeight = 25;
        
        // Draw White Background Box for Text
        g.fillRect(centerX - (rectWidth/2), centerY - (rectHeight/2), rectWidth, rectHeight);

        // Draw Text
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        // Center text in the box (approximate centering)
        g.drawString(overlayText, centerX - 15, centerY + 5);

        g.dispose();

        // Convert back to Base64
        ByteArrayOutputStream finalOs = new ByteArrayOutputStream();
        ImageIO.write(combined, "png", finalOs);
        return Base64.getEncoder().encodeToString(finalOs.toByteArray());
    }
}
