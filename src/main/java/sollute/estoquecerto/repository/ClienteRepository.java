package sollute.estoquecerto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sollute.estoquecerto.entity.Cliente;
import sollute.estoquecerto.entity.Funcionario;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByFkEmpresaIdEmpresa(Integer idEmpresa);

    @Transactional
    @Modifying
    @Query("update Cliente c " +
            "set c.nomeCliente = ?1, c.telefoneCliente = ?2 " +
            "where c.fkEmpresa.idEmpresa = ?3 and c.idCliente = ?4")
    int atualizarCliente(String nomeCliente,
                             String telefoneCliente,
                             Integer idEmpresa,
                             Long idCliente);

}
