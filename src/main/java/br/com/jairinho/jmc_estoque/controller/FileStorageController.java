package br.com.jairinho.jmc_estoque.controller;

import java.io.InputStream;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import br.com.jairinho.jmc_estoque.service.DadosCSV;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/file")
public class FileStorageController {
    private final DadosCSV dadosCSV;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {    
        try {
            InputStream inputStream = file.getInputStream();
            dadosCSV.salvarProdutosUpload(inputStream);

            return ResponseEntity.ok("Upload Realizado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao fazer upload do arquivo: " + e.getMessage());
        }
    }
    

}
