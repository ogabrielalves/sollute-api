package sollute.estoquecerto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sollute.estoquecerto.entity.Produto;
import sollute.estoquecerto.repository.EmpresaRepository;
import sollute.estoquecerto.repository.ProdutoRepository;
import sollute.estoquecerto.request.NovoProdutoRequest;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @PostMapping("/criar-produto/{idEmpresa}")
    public ResponseEntity<ResponseEntity.BodyBuilder> adicionaProduto(@RequestBody @Valid Produto novoProduto,
                                                                      @PathVariable Integer idEmpresa) {

        if (empresaRepository.existsById(idEmpresa)) {
            produtoRepository.save(novoProduto);
            return status(HttpStatus.CREATED).build();
        }

        return status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/listar-produtos/{idEmpresa}")
    public ResponseEntity<List<Produto>> listarProdutos(@PathVariable Integer idEmpresa) {

        List<Produto> lista = produtoRepository.findByFkEmpresaIdEmpresaOrderByEstoqueDesc(idEmpresa);

        if (lista.isEmpty()) {
            return status(HttpStatus.NO_CONTENT).build();
        }

        return status(HttpStatus.OK).body(lista);
    }

    @GetMapping("/listar-produtos-ordem-maior/{idEmpresa}")
    public ResponseEntity<List<Produto>> listarProdutosOrdemMaior(@PathVariable Integer idEmpresa) {

        List<Produto> lista = produtoRepository.findFirst5ByFkEmpresaIdEmpresaOrderByQtdVendidosDesc(idEmpresa);

        if (lista.isEmpty()) {
            return status(HttpStatus.NO_CONTENT).build();
        }

        return status(HttpStatus.OK).body(lista);
    }

    @PutMapping("/editar-produto/{idEmpresa}/{codigo}")
    public ResponseEntity<ResponseEntity.BodyBuilder> editarProduto(@RequestBody @Valid NovoProdutoRequest novoProdutoRequest,
                                                                    @PathVariable Integer idEmpresa,
                                                                    @PathVariable String codigo) {

        List<Produto> lista = produtoRepository.findByFkEmpresaIdEmpresa(idEmpresa);

        if (lista.isEmpty()) return status(HttpStatus.BAD_REQUEST).build();

        if (produtoRepository.existsByCodigo(codigo)) {

            Integer estoque = novoProdutoRequest.getEstoque();
            Integer estoqueMin = novoProdutoRequest.getEstoqueMin();
            Integer estoqueMax = novoProdutoRequest.getEstoqueMax();
            Double precoCompra = novoProdutoRequest.getPrecoCompra();
            Double precoVenda = novoProdutoRequest.getPrecoVenda();

            produtoRepository.atualizarProduto(
                    estoque,
                    estoqueMin,
                    estoqueMax,
                    precoCompra,
                    precoVenda,
                    codigo,
                    idEmpresa);

            return status(HttpStatus.OK).build();
        }


        return status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/deletar-produto/{codigo}/{fkEmpresa}")
    public ResponseEntity<ResponseEntity.BodyBuilder> deletarProduto(@PathVariable String codigo,
                                                                     @PathVariable Integer fkEmpresa) {

        if (empresaRepository.existsById(fkEmpresa)) {
            produtoRepository.deleteProdutoByCodigo(codigo);
            return status(HttpStatus.OK).build();
        }

        return status(HttpStatus.NOT_FOUND).build();
    }

}
