package br.com.jairinho.jmc_estoque.service;

import java.io.FileReader;

import com.opencsv.CSVReader;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DadosCSV {
    private static final String ARQUIVO_CSV = "src/main/resources/teste_estoque.csv";

    public void lerCSV() {
        try {
            @SuppressWarnings("resource")
            CSVReader reader = new CSVReader(new FileReader(ARQUIVO_CSV));
            
            String[] linhas;
            while((linhas = reader.readNext()) != null) {
                for(int i = 0; i < linhas.length; i++) {
                    System.out.print(linhas[i] + " | ");
                }
                System.out.println();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
