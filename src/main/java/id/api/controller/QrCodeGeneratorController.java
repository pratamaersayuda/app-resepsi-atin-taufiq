package id.api.controller;

import com.google.zxing.WriterException;
import id.api.service.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class QrCodeGeneratorController {

    @Autowired
    private QrCodeService qrCodeService;

    @GetMapping("/generate/qrcode")
    public String showForm() {
        return "generate";
    }

    @PostMapping("/generate/qrcode")
    public String generate(@RequestParam("text") String text, Model model) {
        try {
            String base64Qr = qrCodeService.generateQRCodeBase64(text, 250, 250);
            model.addAttribute("qrImage", base64Qr);
            model.addAttribute("text", text);
        } catch (Exception e) {
            model.addAttribute("qrImage", null);
            model.addAttribute("error", "Gagal generate QR: " + e.getMessage());
        }
        return "generate";
    }
}
