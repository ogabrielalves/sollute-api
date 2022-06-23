package sollute.estoquecerto.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "fornecedor")
public class Fornecedor {

    @Id
    @Column(name = "id_fornecedor")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFornecedor;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "fk_empresa")
    private Empresa fkEmpresa;

    @NotBlank
    @Column(name = "nome_fornecedor")
    private String nomeFornecedor;

    @NotBlank
    @Column(name = "telefone_fornecedor")
    private String telefoneFornecedor;

    @NotBlank
    @Column(name = "nome_produto")
    private String nomeProduto;

    @PositiveOrZero
    @Column(name = "qtd_fornecidas")
    private Integer qtd;

    public Long getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Long idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public Empresa getFkEmpresa() {
        return fkEmpresa;
    }

    public void setFkEmpresa(Empresa fkEmpresa) {
        this.fkEmpresa = fkEmpresa;
    }

    public String getNomeFornecedor() {
        return nomeFornecedor;
    }

    public void setNomeFornecedor(String nomeFornecedor) {
        this.nomeFornecedor = nomeFornecedor;
    }

    public String getTelefoneFornecedor() {
        return telefoneFornecedor;
    }

    public void setTelefoneFornecedor(String telefoneFornecedor) {
        this.telefoneFornecedor = telefoneFornecedor;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }
}
