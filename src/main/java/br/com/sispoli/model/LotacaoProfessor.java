package br.com.sispoli.model;

public class LotacaoProfessor {
    private int idLotacao;
    private int idTurma;
    private int idProfessor;
    // Campos auxiliares para exibição na View (não persistidos)
    private String nomeTurma;
    private String nomeProfessor;

    public LotacaoProfessor() {}

    public LotacaoProfessor(int idLotacao, int idTurma, int idProfessor, String nomeTurma, String nomeProfessor) {
        this.idLotacao = idLotacao;
        this.idTurma = idTurma;
        this.idProfessor = idProfessor;
        this.nomeTurma = nomeTurma;
        this.nomeProfessor = nomeProfessor;
    }

    // Getters & Setters
    public int getIdLotacao() { return idLotacao; }
    public void setIdLotacao(int idLotacao) { this.idLotacao = idLotacao; }
    public int getIdTurma() { return idTurma; }
    public void setIdTurma(int idTurma) { this.idTurma = idTurma; }
    public int getIdProfessor() { return idProfessor; }
    public void setIdProfessor(int idProfessor) { this.idProfessor = idProfessor; }
    public String getNomeTurma() { return nomeTurma; }
    public void setNomeTurma(String nomeTurma) { this.nomeTurma = nomeTurma; }
    public String getNomeProfessor() { return nomeProfessor; }
    public void setNomeProfessor(String nomeProfessor) { this.nomeProfessor = nomeProfessor; }
}