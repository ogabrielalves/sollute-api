package sollute.estoquecerto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sollute.estoquecerto.entity.Cliente;
import sollute.estoquecerto.repository.ClienteRepository;
import sollute.estoquecerto.repository.EmpresaRepository;
import sollute.estoquecerto.request.NovoClienteRequest;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ClienteRepository clienteRepository;


    @PostMapping("/adicionar-cliente/{idEmpresa}")
    public ResponseEntity<ResponseEntity.BodyBuilder> adicionaCliente(@RequestBody @Valid Cliente novoCliente,
                                                                      @PathVariable Integer idEmpresa) {

        if (empresaRepository.existsById(idEmpresa)) {
            clienteRepository.save(novoCliente);
            return status(HttpStatus.CREATED).build();
        }

        return status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/listar-clientes/{idEmpresa}")
    public ResponseEntity<List<Cliente>> listarCliente(@PathVariable Integer idEmpresa) {

        List<Cliente> lista = clienteRepository.findByFkEmpresaIdEmpresa(idEmpresa);

        if (lista.isEmpty()) {
            return status(204).build();
        }

        return status(200).body(lista);
    }

    @PutMapping("/editar-cliente/{idEmpresa}/{idCliente}")
    public ResponseEntity<ResponseEntity.BodyBuilder> editarCliente(@RequestBody @Valid NovoClienteRequest novoClienteRequest,
                                                                    @PathVariable Integer idEmpresa,
                                                                    @PathVariable Long idCliente) {

        List<Cliente> lista = clienteRepository.findByFkEmpresaIdEmpresa(idEmpresa);

        if (lista.isEmpty()) return status(HttpStatus.NOT_FOUND).build();

        if (clienteRepository.existsById(idCliente)) {

            String nome = novoClienteRequest.getNomeCliente();
            String tele = novoClienteRequest.getTelefoneCliente();

            if (clienteRepository.atualizarCliente(nome, tele, idEmpresa, idCliente) == 1) {
                return status(HttpStatus.OK).build();
            }

            return status(HttpStatus.BAD_REQUEST).build();

        }

        return status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/deletar-cliente/{idCliente}/{idEmpresa}")
    public ResponseEntity<ResponseEntity.BodyBuilder> deletaCliente(@PathVariable Integer idCliente,
                                                                    @PathVariable Integer idEmpresa) {

        if (empresaRepository.existsById(idEmpresa)) {

            if (clienteRepository.existsById(idCliente.longValue())) {
                clienteRepository.deleteById(idCliente.longValue());
                return status(HttpStatus.OK).build();
            }

            return status(HttpStatus.BAD_REQUEST).build();
        }

        return status(HttpStatus.NOT_FOUND).build();
    }

}
