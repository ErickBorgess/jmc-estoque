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
    private static final String ARQUIVO_CSV = "src/main/resources/produtos.csv";
    
    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> lerCSV() {
        List<Produto> produtos = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(ARQUIVO_CSV))) {
            String[] linha;
            //reader.readNext(); - Pular o cabeçalho do arquivo final
            int linhaAtual = 0;
            int linhasInvalidas = 0;
            int produtosValidos = 0;

            while((linha = reader.readNext()) != null) {
                linhaAtual++;
                String codigoLimpo = linha[0].trim();

                if (linha.length < 1 || linha[0] == null) {
                    continue; 
                }
                
                if (codigoLimpo.isEmpty() && !codigoLimpo.matches("\\d+")) {
                    System.out.println("INFO: Linha " + linhaAtual + " ignorada (código '" + linha[0] + "' não é um produto válido).");
                    linhasInvalidas++;
                    continue;
                }

                try {
                    Produto produto = Produto.builder()
                            .codigoSistema(codigoLimpo)        
                            .nome(linha[1] != null ? linha[1].trim() : "N/A")
                            .codigoBarras(linha[2] != null ? linha[2].trim() : "")
                            .referencia(linha[4] != null ? linha[4].trim() : "")
                            .marca(linha[5].trim())
                            .preco(Double.parseDouble(linha[6].trim().replace(",", ".")))
                            .build();
                    produtos.add(produto);
                    produtosValidos++;
                } catch (NumberFormatException e) {
                    System.err.println("ERRO: Valor numérico inválido na linha " + linhaAtual + ". Produto ignorado.");
                    linhasInvalidas++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("ERRO: Número de colunas incorreto na linha " + linhaAtual + ". Produto ignorado.");
                    linhasInvalidas++;
                }
                
            }
            System.out.println("-----------------------------------------------------");
            System.out.println("Leitura do CSV finalizada.");
            System.out.println("Total de linhas processadas: " + linhaAtual);
            System.out.println("Produtos válidos encontrados: " + produtosValidos);
            System.out.println("Linhas inválidas/ignoradas: " + linhasInvalidas);
            System.out.println("-----------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return produtos;
    }

    public void salvarProdutos(){
        List<Produto> produtos = this.lerCSV();
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
