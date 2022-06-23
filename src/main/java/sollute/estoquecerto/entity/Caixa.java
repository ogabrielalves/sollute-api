package sollute.estoquecerto.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "caixa")
public class Caixa {

    @Id
    @Column(name = "id_caixa")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCaixa;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "fk_empresa")
    private Empresa fkEmpresa;

    @PositiveOrZero
    private Double valor;

    public Long getIdCaixa() {
        return idCaixa;
    }

    public void setIdCaixa(Long idCaixa) {
        this.idCaixa = idCaixa;
    }

    public Empresa getFkEmpresa() {
        return fkEmpresa;
    }

    public void setFkEmpresa(Empresa fkEmpresa) {
        this.fkEmpresa = fkEmpresa;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
