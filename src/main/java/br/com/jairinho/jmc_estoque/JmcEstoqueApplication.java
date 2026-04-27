package br.com.jairinho.jmc_estoque;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class JmcEstoqueApplication {
	public static void main(String[] args) {
		SpringApplication.run(JmcEstoqueApplication.class, args);
	}

	@PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
	}

}
