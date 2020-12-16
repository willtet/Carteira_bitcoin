//Classe usuario

package com.sample.trabalho.model;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String cpf;
    private String endereco;
    private String telefone;
    private String senha;
    private Double credito;
    private Double bitcoin;

    public Usuario(String nome, String email, String cpf, String endereco, String telefone, String senha, Double credito, Double bitcoin) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.endereco = endereco;
        this.telefone = telefone;
        this.senha = senha;
        this.credito = credito;
        this.bitcoin = bitcoin;
    }

    public Usuario(int id, String nome, String email, String cpf, String endereco, String telefone, String senha, Double credito, Double bitcoin) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.endereco = endereco;
        this.telefone = telefone;
        this.senha = senha;
        this.credito = credito;
        this.bitcoin = bitcoin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Double getCredito() {
        return credito;
    }

    public void setCredito(Double credito) {
        this.credito = credito;
    }

    public Double getBitcoin() {
        return bitcoin;
    }

    public void setBitcoin(Double bitcoin) {
        this.bitcoin = bitcoin;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", cpf='" + cpf + '\'' +
                ", endereco='" + endereco + '\'' +
                ", telefone='" + telefone + '\'' +
                ", senha='" + senha + '\'' +
                ", credito=" + credito +
                ", bitcoin=" + bitcoin +
                '}';
    }
}
