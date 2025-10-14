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
    private static final String ARQUIVO_CSV2 = "src/main/resources/produtos.csv";
    
    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> lerCSV() {
        List<Produto> produtos = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(ARQUIVO_CSV2))) {
            String[] linha;
            //reader.readNext(); - Pular o cabeçalho do arquivo final
            int linhaAtual = 0;

            while((linha = reader.readNext()) != null) {
                /*for(int i = 0; i < linha.length; i++) {
                    System.out.print(linha[i] + " | ");
                }
                System.out.println();*/
                linhaAtual++;
                /*if (linha.length < 1 || linha[0] == null || !linha[0].matches("\\d+")) {
                    System.out.println("INFO: Linha " + linhaAtual + " ignorada (não é um produto válido).");
                    continue; 
                }*/

                if (linha.length < 1 || linha[0] == null) {
                    continue; 
                }
                
                String codigoLimpo = linha[0].trim();

                if (codigoLimpo.isEmpty() || !codigoLimpo.matches("\\d+")) {
                    System.out.println("INFO: Linha " + linhaAtual + " ignorada (código '" + linha[0] + "' não é um produto válido).");
                    continue;
                }

                try {
                    /* ADD PRODUTO CSV TESTE
                    Produto produto = Produto.builder()
                        .codigoBarras(Integer.parseInt(linha[0]))
                        .codigoSistema(Integer.parseInt(linha[1]))
                        .referencia(linha[2])
                        .nome(linha[3])
                        .preco(Double.parseDouble(linha[4].replace(",", ".")))
                        .marca(linha[5])
                        .build();
                    produtos.add(produto);
                    */

                    // ADD PRODUTO CSV FINAL
                    Produto produto = Produto.builder()
                        .codigoSistema(Integer.parseInt(codigoLimpo))
                        .nome(linha[1].trim())
                        .codigoBarras(Integer.parseInt(linha[2].trim()))
                        .referencia(linha[4].trim())
                        .marca(linha[5].trim())
                        .preco(Double.parseDouble(linha[6].trim().replace(",", ".")))
                        .build();
                    produtos.add(produto);
                /*
                } catch (Exception e) {
                    System.out.println("Erro ao processar linha: " + String.join(", ", linha));
                }
                */
                } catch (NumberFormatException e) {
                    System.err.println("ERRO: Valor numérico inválido na linha " + linhaAtual + ". Produto ignorado.");
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("ERRO: Número de colunas incorreto na linha " + linhaAtual + ". Produto ignorado.");
                }
            }
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
