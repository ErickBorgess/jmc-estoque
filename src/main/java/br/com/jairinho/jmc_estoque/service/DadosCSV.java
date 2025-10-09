package br.com.jairinho.jmc_estoque.service;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opencsv.CSVReader;
import br.com.jairinho.jmc_estoque.model.Produto;
import br.com.jairinho.jmc_estoque.repository.ProdutoRepository;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class DadosCSV {
    private static final String ARQUIVO_CSV = "src/main/resources/teste_estoque.csv";
    
    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> lerCSV() {
        List<Produto> produtos = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(ARQUIVO_CSV))) {
            String[] linha;
            //reader.readNext(); - Pular o cabeçalho do arquivo final

            while((linha = reader.readNext()) != null) {
                /*for(int i = 0; i < linha.length; i++) {
                    System.out.print(linha[i] + " | ");
                }
                System.out.println();*/
                try {
                    Produto produto = Produto.builder()
                        .codigoBarras(Integer.parseInt(linha[0]))
                        .codigoSistema(Integer.parseInt(linha[1]))
                        .referencia(linha[2])
                        .nome(linha[3])
                        .preco(Double.parseDouble(linha[4].replace(",", ".")))
                        .marca(linha[5])
                        .build();
                    produtos.add(produto);
                    produtoRepository.save(produto);
                } catch (Exception e) {
                    System.out.println("Erro ao processar linha: " + String.join(", ", linha));
                }
            }
            return produtos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void salvarProdutos(){
        List<Produto> produtos = lerCSV();
        if (produtos != null && !produtos.isEmpty()) {
            System.out.println("Limpando produtos existentes...");
            produtoRepository.deleteAllInBatch();
            System.out.println("Salvando " + produtos.size() + " produtos no banco de dados...");
            produtoRepository.saveAll(produtos);
            System.out.println("Produtos salvos com sucesso!");
        } else {
            System.out.println("Nenhum produto para salvar.");
        }
    }
    
}
