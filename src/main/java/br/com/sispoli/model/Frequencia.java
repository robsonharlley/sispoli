package br.com.sispoli.model;

import java.time.LocalDate;

public class Frequencia {
    private int id;
    private int idAluno;
    private int idTurma;
    private LocalDate dataAula;
    // Auxiliares para exibição na grid
    private String nomeAluno;
    private String nomeTurma;

    public Frequencia() {}

    public Frequencia(int id, int idAluno, int idTurma, LocalDate dataAula, String nomeAluno, String nomeTurma) {
        this.id = id; this.idAluno = idAluno; this.idTurma = idTurma; this.dataAula = dataAula;
        this.nomeAluno = nomeAluno; this.nomeTurma = nomeTurma;
    }

    // Getters & Setters
    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public int getIdAluno() { return idAluno; } public void setIdAluno(int idAluno) { this.idAluno = idAluno; }
    public int getIdTurma() { return idTurma; } public void setIdTurma(int idTurma) { this.idTurma = idTurma; }
    public LocalDate getDataAula() { return dataAula; } public void setDataAula(LocalDate dataAula) { this.dataAula = dataAula; }
    public String getNomeAluno() { return nomeAluno; } public void setNomeAluno(String nomeAluno) { this.nomeAluno = nomeAluno; }
    public String getNomeTurma() { return nomeTurma; } public void setNomeTurma(String nomeTurma) { this.nomeTurma = nomeTurma; }
}