// Camada que encapsula o objeto para ser injetado no Banco de dados
package br.com.sispoli.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Alunos {
	
	
	//Atributos
	private int id_aluno;
	private String nome_completo;
	private LocalDate data_nascimento;
	private String  cpf;
	private String  rg;
	private String  sexo;
	private String  email;
	private String  contato_what;
	private String  id_cep;
	private String  numero;
	private String  complemento;
	private boolean  isento;
	private String  motivo_isencao;
	private boolean  possui_resricao_medica;
	private String  descricao_restricao;
	private String  medicamentos_continuos;
	private String  alergias;
	private String  contato_emergencia_nome;
	private String  contato_emergencia_telefone;
	private String  contato_emergencia_parentesco;
	private LocalDateTime data_matricula;
	private String  status;
	private boolean autorizacao_imagem;
	private boolean autorizacao_divulgacao;
	private boolean aceite_termos;
	private LocalDateTime data_aceite_termos;
	private String  observacoes;
	
	//Getters e Setters
	public int getId_aluno() {
		return id_aluno;
	}
	public void setId_aluno(int id_aluno) {
		this.id_aluno = id_aluno;
	}
	public String getNome_completo() {
		return nome_completo;
	}
	public void setNome_completo(String nome_completo) {
		this.nome_completo = nome_completo;
		
	}
	public LocalDate getData_nascimento() {
		return data_nascimento;
	}
	public void setData_nascimento(LocalDate data_nascimento) {
		this.data_nascimento = data_nascimento;
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
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContato_what() {
		return contato_what;
	}
	public void setContato_what(String contato_what) {
		this.contato_what = contato_what;
	}
	public String getId_cep() {
		return id_cep;
	}
	public void setId_cep(String id_cep) {
		this.id_cep = id_cep;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public boolean isIsento() {
		return isento;
	}
	public void setIsento(boolean isento) {
		this.isento = isento;
	}
	public String getMotivo_isencao() {
		return motivo_isencao;
	}
	public void setMotivo_isencao(String motivo_isencao) {
		this.motivo_isencao = motivo_isencao;
	}
	public boolean isPossui_resricao_medica() {
		return possui_resricao_medica;
	}
	public void setPossui_resricao_medica(boolean possui_resricao_medica) {
		this.possui_resricao_medica = possui_resricao_medica;
	}
	public String getDescricao_restricao() {
		return descricao_restricao;
	}
	public void setDescricao_restricao(String descricao_restricao) {
		this.descricao_restricao = descricao_restricao;
	}
	public String getMedicamentos_continuos() {
		return medicamentos_continuos;
	}
	public void setMedicamentos_continuos(String medicamentos_continuos) {
		this.medicamentos_continuos = medicamentos_continuos;
	}
	public String getAlergias() {
		return alergias;
	}
	public void setAlergias(String alergias) {
		this.alergias = alergias;
	}
	public String getContato_emergencia_nome() {
		return contato_emergencia_nome;
	}
	public void setContato_emergencia_nome(String contato_emergencia_nome) {
		this.contato_emergencia_nome = contato_emergencia_nome;
	}
	public String getContato_emergencia_telefone() {
		return contato_emergencia_telefone;
	}
	public void setContato_emergencia_telefone(String contato_emergencia_telefone) {
		this.contato_emergencia_telefone = contato_emergencia_telefone;
	}
	public String getContato_emergencia_parentesco() {
		return contato_emergencia_parentesco;
	}
	public void setContato_emergencia_parentesco(String contato_emergencia_parentesco) {
		this.contato_emergencia_parentesco = contato_emergencia_parentesco;
	}
	public LocalDateTime getData_matricula() {
		return data_matricula;
	}
	public void setData_matricula(LocalDateTime data_matricula) {
		this.data_matricula = data_matricula;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isAutorizacao_imagem() {
		return autorizacao_imagem;
	}
	public void setAutorizacao_imagem(boolean autorizacao_imagem) {
		this.autorizacao_imagem = autorizacao_imagem;
	}
	public boolean isAutorizacao_divulgacao() {
		return autorizacao_divulgacao;
	}
	public void setAutorizacao_divulgacao(boolean autorizacao_divulgacao) {
		this.autorizacao_divulgacao = autorizacao_divulgacao;
	}
	public boolean isAceite_termos() {
		return aceite_termos;
	}
	public void setAceite_termos(boolean aceite_termos) {
		this.aceite_termos = aceite_termos;
	}
	public LocalDateTime getData_aceite_termos() {
		return data_aceite_termos;
	}
	public void setData_aceite_termos(LocalDateTime data_aceite_termos) {
		this.data_aceite_termos = data_aceite_termos;
	}
	public String getObservacoes() {
		return observacoes;
	}
	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

}
