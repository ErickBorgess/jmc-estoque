package br.com.jairinho.jmc_estoque;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import br.com.jairinho.jmc_estoque.service.DadosCSV;

@SpringBootApplication
public class JmcEstoqueApplication {
	public static void main(String[] args) {
		SpringApplication.run(JmcEstoqueApplication.class, args);
		DadosCSV dadosCSV = new DadosCSV();
		dadosCSV.lerCSV();
		dadosCSV.salvarProdutos();
	}

}
