package br.com.sispoli.model;

import java.math.BigDecimal;
import java.time.LocalTime;

public class Turma {
	private int idTurma;
	private int idLocal;
	private String nomeTurma;
	private String nivel;
	private String faixaEtaria;
	private LocalTime horario;
	private String diaSemana;
	private int duracaoAula;
	private int capacidadeMaxima;
	private BigDecimal valorMensalidade;
	private String status;
	private String observacoes;
	private int capacidadeAtipicos;
	private String nomeLocal; // Auxiliar para exibição

	public Turma() {
	}

	public Turma(int idTurma, int idLocal, String nomeTurma, String nivel, String faixaEtaria, LocalTime horario,
			String diaSemana, int duracaoAula, int capacidadeMaxima, BigDecimal valorMensalidade, String status,
			String observacoes, int capacidadeAtipicos, String nomeLocal) {
		this.idTurma = idTurma;
		this.idLocal = idLocal;
		this.nomeTurma = nomeTurma;
		this.nivel = nivel;
		this.faixaEtaria = faixaEtaria;
		this.horario = horario;
		this.diaSemana = diaSemana;
		this.duracaoAula = duracaoAula;
		this.capacidadeMaxima = capacidadeMaxima;
		this.valorMensalidade = valorMensalidade;
		this.status = status;
		this.observacoes = observacoes;
		this.capacidadeAtipicos = capacidadeAtipicos;
		this.nomeLocal = nomeLocal;
	}

	// Getters & Setters
	public int getIdTurma() {
		return idTurma;
	}

	public void setIdTurma(int idTurma) {
		this.idTurma = idTurma;
	}

	public int getIdLocal() {
		return idLocal;
	}

	public void setIdLocal(int idLocal) {
		this.idLocal = idLocal;
	}

	public String getNomeTurma() {
		return nomeTurma;
	}

	public void setNomeTurma(String nomeTurma) {
		this.nomeTurma = nomeTurma;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getFaixaEtaria() {
		return faixaEtaria;
	}

	public void setFaixaEtaria(String faixaEtaria) {
		this.faixaEtaria = faixaEtaria;
	}

	public LocalTime getHorario() {
		return horario;
	}

	public void setHorario(LocalTime horario) {
		this.horario = horario;
	}

	public String getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}

	public int getDuracaoAula() {
		return duracaoAula;
	}

	public void setDuracaoAula(int duracaoAula) {
		this.duracaoAula = duracaoAula;
	}

	public int getCapacidadeMaxima() {
		return capacidadeMaxima;
	}

	public void setCapacidadeMaxima(int cap) {
		this.capacidadeMaxima = cap;
	}

	public BigDecimal getValorMensalidade() {
		return valorMensalidade;
	}

	public void setValorMensalidade(BigDecimal v) {
		this.valorMensalidade = v;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String obs) {
		this.observacoes = obs;
	}

	public int getCapacidadeAtipicos() {
		return capacidadeAtipicos;
	}

	public void setCapacidadeAtipicos(int cap) {
		this.capacidadeAtipicos = cap;
	}

	public String getNomeLocal() {
		return nomeLocal;
	}

	public void setNomeLocal(String nomeLocal) {
		this.nomeLocal = nomeLocal;
	}
}