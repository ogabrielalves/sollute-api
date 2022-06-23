package sollute.estoquecerto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sollute.estoquecerto.entity.Funcionario;

import java.util.List;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    List<Funcionario> findByFkEmpresaIdEmpresa(Integer idEmpresa);

    @Transactional
    @Modifying
    @Query("update Funcionario f " +
            "set f.nomeFuncionario = ?1, f.telefoneFuncionario = ?2, f.cpfFuncionario = ?3, f.salarioFuncionario = ?4 " +
            "where f.fkEmpresa.idEmpresa = ?5 and f.idFuncionario = ?6")
    int atualizarFuncionario(String nomeFuncionario,
                                 String telefoneFuncionario,
                                 String cpfFuncionario,
                                 Double salarioFuncionario,
                                 Integer idEmpresa,
                                 Long idFuncionario);
}
