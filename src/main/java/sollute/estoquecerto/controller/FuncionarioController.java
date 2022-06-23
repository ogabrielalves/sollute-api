package sollute.estoquecerto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sollute.estoquecerto.entity.Funcionario;
import sollute.estoquecerto.repository.EmpresaRepository;
import sollute.estoquecerto.repository.FuncionarioRepository;
import sollute.estoquecerto.request.NovoFuncionarioRequest;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @PostMapping("/criar-funcionario/{idEmpresa}")
    public ResponseEntity<ResponseEntity.BodyBuilder> criarFuncionario(@RequestBody @Valid Funcionario novoFuncionario,
                                                                       @PathVariable Integer idEmpresa) {

        if (empresaRepository.existsById(idEmpresa)) {
            funcionarioRepository.save(novoFuncionario);
            return status(HttpStatus.CREATED).build();
        }

        return status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/listar-funcionarios/{idEmpresa}")
    public ResponseEntity<List<Funcionario>> listarFuncionario(@PathVariable Integer idEmpresa) {

        List<Funcionario> lista = funcionarioRepository.findByFkEmpresaIdEmpresa(idEmpresa);

        if (lista.isEmpty()) {
            return status(HttpStatus.NO_CONTENT).build();
        }

        return status(HttpStatus.OK).body(lista);
    }

    @PutMapping("/editar-funcionario/{idEmpresa}/{idFuncionario}")
    public ResponseEntity<ResponseEntity.BodyBuilder> editarFuncionario(@RequestBody @Valid NovoFuncionarioRequest novoFuncionarioRequest,
                                                                        @PathVariable Integer idEmpresa,
                                                                        @PathVariable Long idFuncionario) {

        List<Funcionario> lista = funcionarioRepository.findByFkEmpresaIdEmpresa(idEmpresa);

        if (lista.isEmpty()) return status(HttpStatus.BAD_REQUEST).build();

        for (Funcionario funcionario : lista) {
            if (funcionarioRepository.existsById(idFuncionario)) {

                String nome = novoFuncionarioRequest.getNomeFuncionario();
                String tele = novoFuncionarioRequest.getTelefoneFuncionario();
                String cpf = novoFuncionarioRequest.getCpfFuncionario();
                Double salario = novoFuncionarioRequest.getSalario();

                if (funcionarioRepository.atualizarFuncionario(nome, tele, cpf, salario, idEmpresa, idFuncionario) == 1) {
                    return status(HttpStatus.OK).build();
                }

                return status(HttpStatus.BAD_REQUEST).build();

            }
        }

        return status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/deletar-funcionario/{idFuncionario}/{idEmpresa}")
    public ResponseEntity<ResponseEntity.BodyBuilder> deletaFuncionario(@PathVariable Integer idFuncionario,
                                                                        @PathVariable Integer idEmpresa) {

        if (empresaRepository.existsById(idEmpresa)) {

            if (funcionarioRepository.existsById(idFuncionario.longValue())) {
                funcionarioRepository.deleteById(idFuncionario.longValue());
                return status(HttpStatus.OK).build();
            }

            return status(HttpStatus.BAD_REQUEST).build();
        }

        return status(HttpStatus.NOT_FOUND).build();
    }
}
