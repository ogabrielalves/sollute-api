package sollute.estoquecerto.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "carrinho")
public class Carrinho {

    @Id
    @Column(name = "id_carrinho")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCarrinho;

    @ManyToOne
    @JoinColumn(name = "fk_empresa")
    private Empresa fkEmpresa;

    @ManyToOne
    @JoinColumn(name = "fk_produto")
    private Produto fkProduto;

    @NotBlank
    private String codigo;

    @NotBlank
    @Length(min = 2, max = 45)
    private String nome;

    @NotBlank
    @Length(min = 2, max = 45)
    private String marca;

    @PositiveOrZero
    @Column(name = "qtd_venda")
    private Integer qtdVenda;

    @PositiveOrZero
    @Column(name = "valor_venda")
    private Double valorVenda;

    public Integer getIdCarrinho() {
        return idCarrinho;
    }

    public void setIdCarrinho(Integer idCarrinho) {
        this.idCarrinho = idCarrinho;
    }

    public Empresa getFkEmpresa() {
        return fkEmpresa;
    }

    public void setFkEmpresa(Empresa fkEmpresa) {
        this.fkEmpresa = fkEmpresa;
    }

    public Produto getFkProduto() {
        return fkProduto;
    }

    public void setFkProduto(Produto fkProduto) {
        this.fkProduto = fkProduto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getQtdVenda() {
        return qtdVenda;
    }

    public void setQtdVenda(Integer qtdVenda) {
        this.qtdVenda = qtdVenda;
    }

    public Double getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(Double valorVenda) {
        this.valorVenda = valorVenda;
    }
}
