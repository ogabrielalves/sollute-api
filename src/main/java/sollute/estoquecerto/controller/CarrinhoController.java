package sollute.estoquecerto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sollute.estoquecerto.entity.Carrinho;
import sollute.estoquecerto.entity.Empresa;
import sollute.estoquecerto.entity.Produto;
import sollute.estoquecerto.repository.CaixaRepository;
import sollute.estoquecerto.repository.CarrinhoRepository;
import sollute.estoquecerto.repository.EmpresaRepository;
import sollute.estoquecerto.repository.ProdutoRepository;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private CaixaRepository caixaRepository;

    @PostMapping("/adicionar-carrinho/{codigo}/{cnpj}/{qtdProduto}")
    public ResponseEntity<ResponseEntity.BodyBuilder> addCarrinho(@PathVariable String codigo,
                                                                  @PathVariable String cnpj,
                                                                  @PathVariable Integer qtdProduto) {

        boolean existsEmpresa = empresaRepository.existsByCnpj(cnpj);
        int qtdEstoque = produtoRepository.findProdutoByCodigo(codigo).getEstoque();

        if (existsEmpresa) {

            Carrinho carrinho = new Carrinho();

            if ((qtdEstoque - qtdProduto) >= 0) {

                Produto p = produtoRepository.findProdutoByCodigo(codigo);
                Empresa e = empresaRepository.findByCnpj(cnpj);

                carrinho.setFkEmpresa(e);
                carrinho.setFkProduto(p);
                carrinho.setCodigo(p.getCodigo());
                carrinho.setNome(p.getNome());
                carrinho.setMarca(p.getMarca());
                carrinho.setQtdVenda(qtdProduto);
                carrinho.setValorVenda(qtdProduto * p.getPrecoVenda());

                carrinhoRepository.save(carrinho);

                return status(HttpStatus.OK).build();

            } else {
                return status(HttpStatus.BAD_REQUEST).build();
            }

        }

        return status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/listar-produtos-carrinho/{fkEmpresa}")
    public ResponseEntity listCarrinho(@PathVariable Integer fkEmpresa) {

        List<Carrinho> lista = carrinhoRepository.findByFkEmpresaIdEmpresa(fkEmpresa);

        if (lista.isEmpty()) {
            return status(HttpStatus.NO_CONTENT).build();
        }

        return status(HttpStatus.OK).body(lista);
    }

    @PutMapping("/vender-produtos-carrinho/{fkEmpresa}")
    @Transactional
    public ResponseEntity<ResponseEntity.BodyBuilder> venderCarrinho(@PathVariable Integer fkEmpresa) {

        if (empresaRepository.existsById(fkEmpresa)) {

            List<Carrinho> listaCarrinho = carrinhoRepository.findByFkEmpresaIdEmpresa(fkEmpresa);

            if (!listaCarrinho.isEmpty()) {
                double saldoCaixa = 0.0;

                for (Carrinho c : listaCarrinho) {

                    Produto p = c.getFkProduto();
                    Empresa e = c.getFkEmpresa();
                    Integer qtdVenda = c.getQtdVenda();
                    Integer estoqueAtual = p.getEstoque() - qtdVenda;
                    Integer qtdVendida = c.getQtdVenda() + p.getQtdVendidos();
                    double valor = c.getValorVenda() + p.getValorVendidos();

                    produtoRepository.venderProduto(
                            qtdVendida,
                            valor,
                            estoqueAtual,
                            p.getIdProduto(),
                            e.getIdEmpresa());
                    saldoCaixa += valor;

                    carrinhoRepository.delete(c);
                }

                saldoCaixa += caixaRepository.findCaixaByFkEmpresaIdEmpresa(fkEmpresa).getValor();
                caixaRepository.atualizarValor(saldoCaixa, 1L, fkEmpresa);

                return status(HttpStatus.OK).build();
            }
        }

        return status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/carrinho-apagar-produto/{codigo}/{fkEmpresa}")
    public ResponseEntity<ResponseEntity.BodyBuilder> apagarProdutoCarrinho(@PathVariable String codigo,
                                                                            @PathVariable Integer fkEmpresa) {

        List<Carrinho> lista = carrinhoRepository.findByFkEmpresaIdEmpresa(fkEmpresa);

        if (!lista.isEmpty()) {

            for (Carrinho c : lista) {
                if (c.getFkProduto().getCodigo().equals(codigo)) {
                    carrinhoRepository.deleteById(c.getIdCarrinho());
                    return status(HttpStatus.OK).build();
                }
            }

            return status(HttpStatus.NOT_FOUND).build();
        }

        return status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/atualizar-carrinho/{codigo}/{idEmpresa}/{qtdVenda}")
    public ResponseEntity<ResponseEntity.BodyBuilder> atualizaCarrinho(@PathVariable String codigo,
                                                                       @PathVariable Integer idEmpresa,
                                                                       @PathVariable Integer qtdVenda) {

        List<Carrinho> lista = carrinhoRepository.findByFkEmpresaIdEmpresa(idEmpresa);

        if (!lista.isEmpty()) {

            for (Carrinho c : lista) {
                if (c.getFkProduto().getCodigo().equals(codigo)) {

                    Produto p = c.getFkProduto();

                    if ((p.getEstoque() - qtdVenda) >= 0) {
                        double novoValor = p.getPrecoVenda() * qtdVenda;

                        carrinhoRepository.atualizaCarrinho(
                                qtdVenda,
                                novoValor,
                                idEmpresa,
                                codigo
                        );

                        return status(HttpStatus.OK).build();
                    }
                }

            }

            return status(HttpStatus.BAD_REQUEST).build();
        }

        return status(HttpStatus.NOT_FOUND).build();
    }

}
