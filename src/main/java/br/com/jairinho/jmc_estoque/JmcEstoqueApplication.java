package br.com.jairinho.jmc_estoque;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import br.com.jairinho.jmc_estoque.repository.ProdutoRepository;
import br.com.jairinho.jmc_estoque.service.DadosCSV;

@SpringBootApplication
public class JmcEstoqueApplication implements CommandLineRunner {
	private final DadosCSV dadosCSV;
	private final ProdutoRepository produtoRepository;

	public JmcEstoqueApplication(DadosCSV dadosCSV, ProdutoRepository produtoRepository) {
        this.dadosCSV = dadosCSV;
        this.produtoRepository = produtoRepository;
    }
	
	public static void main(String[] args) {
		SpringApplication.run(JmcEstoqueApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (produtoRepository.count() == 0) {
            System.out.println("Banco de dados vazio. Carregando 'produtos2710.csv'...");
            dadosCSV.salvarProdutos();
        } else {
            System.out.println("Banco de dados preenchido.");
        }
	}

}
