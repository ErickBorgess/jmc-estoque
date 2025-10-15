package br.com.jairinho.jmc_estoque.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.jairinho.jmc_estoque.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByNomeContainingIgnoreCase(String nome);
    List<Produto> findByReferenciaContainingIgnoreCase(String referencia);
    Optional<Produto> findByCodigoSistema(String codigoSistema);
    Optional<Produto> findByCodigoBarras(String codigoBarras);
}
