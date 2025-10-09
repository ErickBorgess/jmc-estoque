package br.com.jairinho.jmc_estoque;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import br.com.jairinho.jmc_estoque.service.DadosCSV;

@SpringBootApplication
public class JmcEstoqueApplication implements CommandLineRunner {
	@Autowired
	DadosCSV dadosCSV = new DadosCSV();
	
	public static void main(String[] args) {
		SpringApplication.run(JmcEstoqueApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		dadosCSV.salvarProdutos();
	}

}
