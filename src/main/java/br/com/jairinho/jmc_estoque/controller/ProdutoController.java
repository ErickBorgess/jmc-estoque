package br.com.jairinho.jmc_estoque.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import br.com.jairinho.jmc_estoque.model.Produto;
import br.com.jairinho.jmc_estoque.repository.ProdutoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/produtos")
@RestController
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    @GetMapping("/buscanome")
    public ResponseEntity<List<Produto>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(produtoRepository.findByNomeContainingIgnoreCase(nome));
    }

    @GetMapping("/buscaref")
    public ResponseEntity<List<Produto>> buscarPorReferencia(@RequestParam String referencia) {
        return ResponseEntity.ok(produtoRepository.findByReferenciaContainingIgnoreCase(referencia));
    }

    @GetMapping("/buscacod")
    public ResponseEntity<Produto> buscarPorCodigoSistema(@RequestParam String codigo) {
        return produtoRepository.findByCodigoSistemaFlexible(codigo)
                .map(produto -> ResponseEntity.ok(produto))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscaean")
    public ResponseEntity<Produto> buscarPorCodigoBarras(@RequestParam String codigoBarras) {
        return produtoRepository.findByCodigoBarras(codigoBarras)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ultima-atualizacao")
    public ResponseEntity<String> getUltimaAtualizacao() {
        Optional<LocalDateTime> dataOpcional = produtoRepository.getUltimaAtualizacao();

        if (dataOpcional.isPresent()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
            String dataFormatada = dataOpcional.get().format(formatter);
            return ResponseEntity.ok(dataFormatada);
        }

        return ResponseEntity.ok("Dados ainda não carregados");
    }

}
