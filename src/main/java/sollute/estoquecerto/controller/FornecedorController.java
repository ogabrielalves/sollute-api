package sollute.estoquecerto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sollute.estoquecerto.entity.Fornecedor;
import sollute.estoquecerto.repository.EmpresaRepository;
import sollute.estoquecerto.repository.FornecedorRepository;
import sollute.estoquecerto.request.NovoFornecedorRequest;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @PostMapping("/criar-fornecedor/{idEmpresa}")
    public ResponseEntity<ResponseEntity.BodyBuilder> criarFornecedor(@RequestBody @Valid Fornecedor novoFornecedor,
                                                                      @PathVariable Integer idEmpresa) {

        if (empresaRepository.existsById(idEmpresa)) {
            fornecedorRepository.save(novoFornecedor);
            return status(HttpStatus.CREATED).build();
        }

        return status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/listar-fornecedores/{idEmpresa}")
    public ResponseEntity<List<Fornecedor>> listarFornecedor(@PathVariable Integer idEmpresa) {

        List<Fornecedor> lista = fornecedorRepository.findByfkEmpresaIdEmpresa(idEmpresa);

        if (lista.isEmpty()) {
            return status(HttpStatus.NO_CONTENT).build();
        }

        return status(HttpStatus.OK).body(lista);
    }

    @PutMapping("/editar-fornecedor/{idEmpresa}/{idFornecedor}")
    public ResponseEntity<ResponseEntity.BodyBuilder> editarFornecedor(@RequestBody @Valid NovoFornecedorRequest novoFornecedorRequest,
                                                                       @PathVariable Integer idEmpresa,
                                                                       @PathVariable Long idFornecedor) {

        List<Fornecedor> lista = fornecedorRepository.findByfkEmpresaIdEmpresa(idEmpresa);

        if (lista.isEmpty()) return status(HttpStatus.BAD_REQUEST).build();

        for (Fornecedor fornecedor : lista) {
            if (fornecedorRepository.existsById(idFornecedor)) {

                String nome = novoFornecedorRequest.getNomeFornecedor();
                String tele = novoFornecedorRequest.getTelefoneFornecedor();
                String prod = novoFornecedorRequest.getNomeProduto();
                Integer qtd = novoFornecedorRequest.getQtdFornecida();

                if (fornecedorRepository.atualizarFornecedor(nome, tele, prod, qtd, idEmpresa, idFornecedor) == 1) {
                    return status(HttpStatus.OK).build();
                }

                return status(HttpStatus.BAD_REQUEST).build();

            }
        }

        return status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/deletar-fornecedor/{idFornecedor}/{idEmpresa}")
    public ResponseEntity<ResponseEntity.BodyBuilder> deletaFornecedor(@PathVariable Integer idFornecedor,
                                                                       @PathVariable Integer idEmpresa) {

        if (empresaRepository.existsById(idEmpresa)) {

            if (fornecedorRepository.existsById(idFornecedor.longValue())) {
                fornecedorRepository.deleteById(idFornecedor.longValue());
                return status(HttpStatus.OK).build();
            }

            return status(HttpStatus.BAD_REQUEST).build();
        }

        return status(HttpStatus.NOT_FOUND).build();
    }

}
