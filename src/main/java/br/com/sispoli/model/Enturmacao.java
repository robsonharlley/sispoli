package br.com.sispoli.model;

import java.time.LocalDate;

public class Enturmacao {
    private int idEnturmacao;
    private int idAluno;
    private int idTurma;
    private LocalDate dataEnturmacao;
    private LocalDate dataDesenturmacao;
    private String tipo;
    private String motivoDesenturmacao;
    private String status;
    private String observacoes;
    // Auxiliares para exibição na grid
    private String nomeAluno;
    private String nomeTurma;

    public Enturmacao() {}

    public Enturmacao(int idEnturmacao, int idAluno, int idTurma, LocalDate dataEnturmacao,
                      LocalDate dataDesenturmacao, String tipo, String motivoDesenturmacao,
                      String status, String observacoes, String nomeAluno, String nomeTurma) {
        this.idEnturmacao = idEnturmacao; this.idAluno = idAluno; this.idTurma = idTurma;
        this.dataEnturmacao = dataEnturmacao; this.dataDesenturmacao = dataDesenturmacao;
        this.tipo = tipo; this.motivoDesenturmacao = motivoDesenturmacao;
        this.status = status; this.observacoes = observacoes;
        this.nomeAluno = nomeAluno; this.nomeTurma = nomeTurma;
    }

    // Getters & Setters
    public int getIdEnturmacao() { return idEnturmacao; } public void setIdEnturmacao(int v) { this.idEnturmacao = v; }
    public int getIdAluno() { return idAluno; } public void setIdAluno(int v) { this.idAluno = v; }
    public int getIdTurma() { return idTurma; } public void setIdTurma(int v) { this.idTurma = v; }
    public LocalDate getDataEnturmacao() { return dataEnturmacao; } public void setDataEnturmacao(LocalDate v) { this.dataEnturmacao = v; }
    public LocalDate getDataDesenturmacao() { return dataDesenturmacao; } public void setDataDesenturmacao(LocalDate v) { this.dataDesenturmacao = v; }
    public String getTipo() { return tipo; } public void setTipo(String v) { this.tipo = v; }
    public String getMotivoDesenturmacao() { return motivoDesenturmacao; } public void setMotivoDesenturmacao(String v) { this.motivoDesenturmacao = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public String getObservacoes() { return observacoes; } public void setObservacoes(String v) { this.observacoes = v; }
    public String getNomeAluno() { return nomeAluno; } public void setNomeAluno(String v) { this.nomeAluno = v; }
    public String getNomeTurma() { return nomeTurma; } public void setNomeTurma(String v) { this.nomeTurma = v; }
}