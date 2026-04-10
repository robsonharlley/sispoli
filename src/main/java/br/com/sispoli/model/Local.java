package br.com.sispoli.model;

public class Local {
    private int id;
    private String nomeLocal;
    private int capacidadeMaxima;
    private String status;
    private String observacoes;

    public Local() {}

    public Local(int id, String nomeLocal, int capacidadeMaxima, String status, String observacoes) {
        this.id = id;
        this.nomeLocal = nomeLocal;
        this.capacidadeMaxima = capacidadeMaxima;
        this.status = status;
        this.observacoes = observacoes;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomeLocal() { return nomeLocal; }
    public void setNomeLocal(String nomeLocal) { this.nomeLocal = nomeLocal; }
    public int getCapacidadeMaxima() { return capacidadeMaxima; }
    public void setCapacidadeMaxima(int capacidadeMaxima) { this.capacidadeMaxima = capacidadeMaxima; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}