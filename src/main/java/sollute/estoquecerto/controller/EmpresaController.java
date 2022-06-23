package sollute.estoquecerto.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sollute.estoquecerto.entity.*;
import sollute.estoquecerto.repository.*;
import sollute.estoquecerto.request.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping("/cria-empresa")
    public ResponseEntity<ResponseEntity.BodyBuilder> criaEmpresa(@RequestBody @Valid Empresa createEmpresaResponse) {

        String cnpj = createEmpresaResponse.getCnpj();

        if (!empresaRepository.existsByCnpj(cnpj)) {
            empresaRepository.save(createEmpresaResponse);
            return status(HttpStatus.CREATED).build();
        }

        return status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/autenticacao")
    public ResponseEntity postAutenticado(@RequestBody @Valid EmpresaLoginRequest requisicao) {

        List<Empresa> empresa = empresaRepository.findAll();

        for (Empresa e : empresa) {
            if (e.getEmail().equals(requisicao.getLogin()) && e.getSenha().equals(requisicao.getSenha())) {

                empresaRepository.atualizarAutenticado(requisicao.getLogin(), true);
                return status(HttpStatus.OK).body(e);

            }
        }

        return status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping
    public ResponseEntity<List<Empresa>> listarEmpresas() {

        List<Empresa> listaEmpresas = empresaRepository.findAll();

        if (listaEmpresas.isEmpty()) {
            return status(HttpStatus.NO_CONTENT).build();
        }

        return status(HttpStatus.OK).body(listaEmpresas);
    }

    @GetMapping("/calcular-produtos-vendidos/{fkEmpresa}")
    public ResponseEntity<Integer> calcularProdutosVendidos(@PathVariable Integer fkEmpresa) {

        int aux = 0;

        if (empresaRepository.existsById(fkEmpresa)) {

            for (Produto prod : produtoRepository.findAll()) {
                aux += prod.getQtdVendidos();
            }
            return status(HttpStatus.OK).body(aux);

        }

        return status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/calcular-valor-vendidos/{fkEmpresa}")
    public ResponseEntity<Double> calcularValorVendidos(@PathVariable Integer fkEmpresa) {

        double aux = 0;

        if (empresaRepository.existsById(fkEmpresa)) {

            for (Produto prod : produtoRepository.findAll()) {
                aux += prod.getValorVendidos();
            }

            return status(HttpStatus.OK).body(aux);
        }

        return status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/calcular-liquido/{fkEmpresa}")
    public ResponseEntity<Double> lucroLiquido(@PathVariable Integer fkEmpresa) {

        Double bruto = calcularValorVendidos(fkEmpresa).getBody();
        double aux = 0;

        if (empresaRepository.existsById(fkEmpresa)) {

            for (Produto prod : produtoRepository.findAll()) {
                aux += prod.getPrecoCompra() * prod.getQtdVendidos();
            }

            aux = bruto - aux;

            return status(200).body(aux);
        }

        return status(404).build();
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "text/csv"))
    })
    @GetMapping("/relatorio-csv/{fkEmpresa}")
    public ResponseEntity relatorio(@PathVariable Integer fkEmpresa) {

        List<Produto> lista = produtoRepository.findByFkEmpresaIdEmpresa(fkEmpresa);

        String relatorio =
                "CODIGO;NOME;MARCA;CATEGORIA;TAMANHO;PESO;PRECO COMPRA;PRECO VENDA;" +
                        "ESTOQUE;ESTOQUE MINIMO;ESTOQUE MAXIMO;QTD VENDIDOS;\r\n";

        for (Produto prod : lista) {
            relatorio += "" +
                    "" + prod.getCodigo() +
                    ";" + prod.getNome() +
                    ";" + prod.getMarca() +
                    ";" + prod.getCategoria() +
                    ";" + prod.getTamanho() +
                    ";" + prod.getPeso() +
                    ";" + prod.getPrecoCompra() +
                    ";" + prod.getPrecoVenda() +
                    ";" + prod.getEstoque() +
                    ";" + prod.getEstoqueMin() +
                    ";" + prod.getEstoqueMax() +
                    ";" + (prod.getQtdVendidos() == null ? 0 : prod.getQtdVendidos()) + "\r\n";
        }

        return status(HttpStatus.OK)
                .header("content-type", "text/csv")
                .header("content-disposition", "filename=\"relatorio-de-produtos.csv\"")
                .body(relatorio);
    }

    @PatchMapping(value = "/upload-txt/{cnpj}")
    public ResponseEntity patchFoto2(@PathVariable String cnpj,
                                    @RequestParam("file") MultipartFile novaFoto) throws IOException {

        int atualizado = empresaRepository.patchArquivo(novaFoto.getBytes(), cnpj);
        if (atualizado == 0) {
            return status(404).build();
        }
        return status(200).build();
    }

    @GetMapping(value = "/txt/{cnpj}", produces = "file/txt")
    public ResponseEntity<byte[]> getFoto(@PathVariable String cnpj) {

        byte[] foto = empresaRepository.getFoto(cnpj);

        if (foto == null) {
            return status(404).build();
        }

        return status(200).body(foto);
    }

}
