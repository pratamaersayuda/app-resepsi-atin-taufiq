package id.api.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import javax.imageio.ImageIO;

@Service
public class QrCodeService {

    private static final String FILE_PATH = "data/Daftar Tamu Undangan.xlsx";

    public String generateQRCodeBase64(String text, int width, int height) throws WriterException, IOException {
        // 1. Buat QR code image
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // 2. Tambahkan tinggi untuk teks
        int textHeight = 30; // tinggi ruang untuk teks
        BufferedImage combinedImage = new BufferedImage(width, height + textHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = combinedImage.createGraphics();

        // Putih background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height + textHeight);

        // 3. Tempel QR image ke atas
        g.drawImage(qrImage, 0, 0, null);

        // 4. Gambar teks di bawah QR code
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 14));

        FontMetrics fontMetrics = g.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);
        int xText = (width - textWidth) / 2;
        int yText = height + ((textHeight - fontMetrics.getHeight()) / 2) + fontMetrics.getAscent();

        g.drawString(text, xText, yText);
        g.dispose();

        // 5. Convert to Base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(combinedImage, "png", baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public void saveScanToExcel(String hasil, String waktuKedatangan) {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            throw new RuntimeException("File Excel tidak ditemukan di: " + FILE_PATH);
        }

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum() + 1;

            Row newRow = sheet.createRow(lastRowNum);
            newRow.createCell(1).setCellValue(hasil);
            newRow.createCell(2).setCellValue(waktuKedatangan);
            newRow.createCell(3).setCellValue("HADIR");

            fis.close(); // tutup dulu sebelum menulis ulang

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }

        } catch (IOException e) {
            throw new RuntimeException("Gagal menambahkan data ke Excel", e);
        }
    }
}
