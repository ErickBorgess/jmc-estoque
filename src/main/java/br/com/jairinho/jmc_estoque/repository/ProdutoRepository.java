package br.com.jairinho.jmc_estoque.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import br.com.jairinho.jmc_estoque.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByNomeContainingIgnoreCase(String nome);
    
    List<Produto> findByReferenciaContainingIgnoreCase(String referencia);
    
    @Query("SELECT p FROM Produto p WHERE p.codigoSistema = :codigoSistema OR p.codigoSistema = LPAD(:codigoSistema, 7, '0')")
    Optional<Produto> findByCodigoSistemaFlexible(String codigoSistema);
    
    Optional<Produto> findByCodigoBarras(String codigoBarras);
}
