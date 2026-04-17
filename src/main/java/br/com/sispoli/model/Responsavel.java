package br.com.sispoli.model;

public class Responsavel {
	private int idResponsavel;
	private int idAluno;
	private String nomeCompleto;
	private String cpf;
	private String rg;
	private String email;
	private String telefone1;
	private String parentesco;

	// Campo auxiliar para exibição (JOIN com aluno)
	private String nomeAluno;

	public Responsavel() {
	}

	public Responsavel(int idResponsavel, int idAluno, String nomeCompleto, String cpf, String rg, String email,
			String telefone1, String parentesco, String nomeAluno) {
		this.idResponsavel = idResponsavel;
		this.idAluno = idAluno;
		this.nomeCompleto = nomeCompleto;
		this.cpf = cpf;
		this.rg = rg;
		this.email = email;
		this.telefone1 = telefone1;
		this.parentesco = parentesco;
		this.nomeAluno = nomeAluno;
	}

	// Getters & Setters
	public int getIdResponsavel() {
		return idResponsavel;
	}

	public void setIdResponsavel(int id) {
		this.idResponsavel = id;
	}

	public int getIdAluno() {
		return idAluno;
	}

	public void setIdAluno(int id) {
		this.idAluno = id;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nome) {
		this.nomeCompleto = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone1() {
		return telefone1;
	}

	public void setTelefone1(String tel) {
		this.telefone1 = tel;
	}

	public String getParentesco() {
		return parentesco;
	}

	public void setParentesco(String parentesco) {
		this.parentesco = parentesco;
	}

	public String getNomeAluno() {
		return nomeAluno;
	}

	public void setNomeAluno(String nome) {
		this.nomeAluno = nome;
	}
}