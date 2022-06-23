package sollute.estoquecerto.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEmpresa;

    @Email(message = "Insira um e-mail valido")
    @Column(name = "email")
    private String email;

    @NotBlank
    private String senha;

    @NotBlank
    @Length(min = 3, max = 45)
    @Column(name = "nome_fantasia")
    private String nomeFantasia;

    @Length(min = 3, max = 45)
    @Column(name = "razao_social")
    private String razaoSocial;

    @CNPJ
    private String cnpj;

    @PositiveOrZero
    @Column(name = "qtd_produtos_vendidos")
    private int qtdProdutosVendidos;

    @PositiveOrZero
    @Column(name = "total_produtos_vendidos")
    private double totalProdutosVendidos;

    @NotNull
    private boolean autenticado;

    @NotBlank
    private String cep;

    @NotBlank
    private String uf;

    @NotBlank
    private String cidade;

    @NotBlank
    private String logradouro;

    @NotBlank
    @Column(name = "ponto_referencia")
    private String pontoReferencia;

    @JsonIgnore
    @Column(name = "file_name")
    private byte[] fileName;

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String login) {
        this.email = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public int getQtdProdutosVendidos() {
        return qtdProdutosVendidos;
    }

    public void setQtdProdutosVendidos(int qtdProdutosVendidos) {
        this.qtdProdutosVendidos = qtdProdutosVendidos;
    }

    public double getTotalProdutosVendidos() {
        return totalProdutosVendidos;
    }

    public void setTotalProdutosVendidos(double totalProdutosVendidos) {
        this.totalProdutosVendidos = totalProdutosVendidos;
    }

    public boolean isAutenticado() {
        return autenticado;
    }

    public void setAutenticado(boolean autenticado) {
        this.autenticado = autenticado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getPontoReferencia() {
        return pontoReferencia;
    }

    public void setPontoReferencia(String pontoReferencia) {
        this.pontoReferencia = pontoReferencia;
    }

    public byte[] getFileName() {
        return fileName;
    }

    public void setFileName(byte[] fileName) {
        this.fileName = fileName;
    }
}
