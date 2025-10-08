package br.com.jairinho.jmc_estoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jairinho.jmc_estoque.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
}
