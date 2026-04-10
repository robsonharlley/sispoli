package br.com.sispoli.model;

public class Cep {
    private String id_cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String estado;
    private String observacoes;

    public Cep() {}

    public Cep(String id_cep, String logradouro, String bairro, String cidade, String estado, String observacoes) {
        this.id_cep = id_cep;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.observacoes = observacoes;
    }

    // Getters & Setters
    public String getId_cep() { return id_cep; }
    public void setId_cep(String id_cep) { this.id_cep = id_cep; }
    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}