package br.com.jairinho.jmc_estoque.controller;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Path;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.jairinho.jmc_estoque.configuration.FileStorageProperties;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/file")
public class FileStorageController {
    private final Path fileStorageLocation;

    public FileStorageController(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Path.of(fileStorageProperties.getUploadDir())
            .toAbsolutePath().normalize();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    
        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            file.transferTo(targetLocation);

            /*String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/file/download/")
                .path(fileName)
                .toUriString();*/

            return ResponseEntity.ok("Upload Realizado!");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao fazer upload do arquivo: " + e.getMessage());
        }
    }
    

}
