package br.com.jairinho.jmc_estoque.service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.opencsv.CSVReader;
import br.com.jairinho.jmc_estoque.model.Produto;
import br.com.jairinho.jmc_estoque.repository.ProdutoRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DadosCSV {
    private static final String ARQUIVO_CSV = "produtos2710.csv";

    private final ProdutoRepository produtoRepository;

    public List<Produto> lerCSV() {
        List<Produto> produtos = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(ARQUIVO_CSV);
                CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {

            if (inputStream == null) {
                System.err.println("ERRO FATAL: Não foi possível encontrar o arquivo CSV inicial: " + ARQUIVO_CSV);
                return null;
            }

            String[] linha;
            int linhaAtual = 0;
            int linhasInvalidas = 0;
            int produtosValidos = 0;

            while ((linha = reader.readNext()) != null) {
                linhaAtual++;
                String codigoLimpo = linha[0].trim();

                if (linha.length < 1 || linha[0] == null) {
                    continue;
                }

                if (codigoLimpo.isEmpty() && !codigoLimpo.matches("\\d+")) {
                    System.out.println("INFO: Linha " + linhaAtual + " ignorada (código '" + linha[0]
                            + "' não é um produto válido).");
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

    @Transactional
    public void salvarProdutos() {
        List<Produto> produtos = this.lerCSV();
        if (produtos != null && !produtos.isEmpty()) {
            System.out.println("Limpando produtos existentes...");
            produtoRepository.deleteAllInBatch();

            LocalDateTime data = LocalDateTime.now();
            produtos.forEach(p -> p.setDataAtualizacao(data));

            System.out.println("Salvando " + produtos.size() + " produtos no banco de dados...");
            produtoRepository.saveAll(produtos);
            System.out.println("Produtos salvos com sucesso!");
        } else {
            System.out.println("Nenhum produto para salvar.");
        }
    }

    public List<Produto> lerCSVUpload(InputStream inputStream) {
        List<Produto> produtos = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new java.io.InputStreamReader(inputStream))) {
            String[] linha;
            int linhaAtual = 0;
            int linhasInvalidas = 0;
            int produtosValidos = 0;

            while ((linha = reader.readNext()) != null) {
                linhaAtual++;
                String codigoLimpo = linha[0].trim();

                if (linha.length < 1 || linha[0] == null) {
                    continue;
                }

                if (codigoLimpo.isEmpty() && !codigoLimpo.matches("\\d+")) {
                    System.out.println("INFO: Linha " + linhaAtual + " ignorada (código '" + linha[0]
                            + "' não é um produto válido).");
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
                    linhasInvalidas++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println(
                            "ERRO: Número de colunas incorreto na linha " + linhaAtual + ". Produto ignorado.");
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

    @Transactional
    public void salvarProdutosUpload(InputStream inputStream) {
        List<Produto> produtos = this.lerCSVUpload(inputStream);

        if (produtos == null) {
            throw new RuntimeException("Falha ao ler o arquivo CSV. A transação será revertida.");
        }

        if (produtos != null && !produtos.isEmpty()) {
            System.out.println("Limpando produtos existentes...");
            produtoRepository.deleteAllInBatch();

            LocalDateTime data = LocalDateTime.now();
            produtos.forEach(p -> p.setDataAtualizacao(data));

            System.out.println("Salvando " + produtos.size() + " produtos no banco de dados...");
            produtoRepository.saveAll(produtos);

            System.out.println("Produtos salvos com sucesso!");
        } else {
            System.out.println("Nenhum produto para salvar.");
        }
    }

}
