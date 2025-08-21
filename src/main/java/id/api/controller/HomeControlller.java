package id.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/***********************************************************************************************************
 * Author:  Ersa Yuda Pratama
 * Created: 24/07/2025 15:20
 * About this file : 
 *
 ***********************************************************************************************************/
@Controller
public class HomeControlller {

    @GetMapping("/")
    public String home() {
        return "home"; // file: home.html
    }

    @GetMapping("/download/excel")
    public ResponseEntity<Resource> downloadExcelTamu() throws IOException {
        String filePath = "data/Daftar Tamu Undangan.xlsx";
        Path path = Paths.get(filePath);
        Resource resource = (Resource) new UrlResource(path.toUri());

        if (!resource.exists()) {
            throw new FileNotFoundException("File Excel tidak ditemukan");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
