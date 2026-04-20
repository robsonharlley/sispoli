package br.com.sispoli.model;
import java.time.LocalDate;

public class Aluno {
    private int idAluno;
    private String nomeCompleto, cpf, rg, sexo, email, contatoWhat, idCep, numero, complemento;
    private LocalDate dataNascimento, dataMatricula, dataCancelamento, dataAceiteTermos;
    private Boolean isento, possuiRestricaoMedica, autorizacaoImagem, autorizacaoDivulgacao, aceiteTermos;
    private String descricaoRestricao, medicamentosContinuos, alergias;
    private String contatoEmergenciaNome, contatoEmergenciaTelefone, contatoEmergenciaParentesco;
    private String motivoCancelamento, observacoes, motivoIsencao, status;

    public Aluno() {}
    // Getters & Setters (padrão, omitidos por brevidade mas necessários)
    // Ex: public int getIdAluno() { return idAluno; } public void setIdAluno(int v) { this.idAluno = v; }
    // ... adicione todos os getters/setters correspondentes ...

	public int getIdAluno() {
		return idAluno;
	}

	public void setIdAluno(int idAluno) {
		this.idAluno = idAluno;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
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

	public String getContatoWhat() {
		return contatoWhat;
	}

	public void setContatoWhat(String contatoWhat) {
		this.contatoWhat = contatoWhat;
	}

	public String getIdCep() {
		return idCep;
	}

	public void setIdCep(String idCep) {
		this.idCep = idCep;
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

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public LocalDate getDataMatricula() {
		return dataMatricula;
	}

	public void setDataMatricula(LocalDate dataMatricula) {
		this.dataMatricula = dataMatricula;
	}

	public LocalDate getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(LocalDate dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	public LocalDate getDataAceiteTermos() {
		return dataAceiteTermos;
	}

	public void setDataAceiteTermos(LocalDate dataAceiteTermos) {
		this.dataAceiteTermos = dataAceiteTermos;
	}

	public Boolean getIsento() {
		return isento;
	}

	public void setIsento(Boolean isento) {
		this.isento = isento;
	}

	public Boolean getPossuiRestricaoMedica() {
		return possuiRestricaoMedica;
	}

	public void setPossuiRestricaoMedica(Boolean possuiRestricaoMedica) {
		this.possuiRestricaoMedica = possuiRestricaoMedica;
	}

	public Boolean getAutorizacaoImagem() {
		return autorizacaoImagem;
	}

	public void setAutorizacaoImagem(Boolean autorizacaoImagem) {
		this.autorizacaoImagem = autorizacaoImagem;
	}

	public Boolean getAutorizacaoDivulgacao() {
		return autorizacaoDivulgacao;
	}

	public void setAutorizacaoDivulgacao(Boolean autorizacaoDivulgacao) {
		this.autorizacaoDivulgacao = autorizacaoDivulgacao;
	}

	public Boolean getAceiteTermos() {
		return aceiteTermos;
	}

	public void setAceiteTermos(Boolean aceiteTermos) {
		this.aceiteTermos = aceiteTermos;
	}

	public String getDescricaoRestricao() {
		return descricaoRestricao;
	}

	public void setDescricaoRestricao(String descricaoRestricao) {
		this.descricaoRestricao = descricaoRestricao;
	}

	public String getMedicamentosContinuos() {
		return medicamentosContinuos;
	}

	public void setMedicamentosContinuos(String medicamentosContinuos) {
		this.medicamentosContinuos = medicamentosContinuos;
	}

	public String getAlergias() {
		return alergias;
	}

	public void setAlergias(String alergias) {
		this.alergias = alergias;
	}

	public String getContatoEmergenciaNome() {
		return contatoEmergenciaNome;
	}

	public void setContatoEmergenciaNome(String contatoEmergenciaNome) {
		this.contatoEmergenciaNome = contatoEmergenciaNome;
	}

	public String getContatoEmergenciaTelefone() {
		return contatoEmergenciaTelefone;
	}

	public void setContatoEmergenciaTelefone(String contatoEmergenciaTelefone) {
		this.contatoEmergenciaTelefone = contatoEmergenciaTelefone;
	}

	public String getContatoEmergenciaParentesco() {
		return contatoEmergenciaParentesco;
	}

	public void setContatoEmergenciaParentesco(String contatoEmergenciaParentesco) {
		this.contatoEmergenciaParentesco = contatoEmergenciaParentesco;
	}

	public String getMotivoCancelamento() {
		return motivoCancelamento;
	}

	public void setMotivoCancelamento(String motivoCancelamento) {
		this.motivoCancelamento = motivoCancelamento;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public String getMotivoIsencao() {
		return motivoIsencao;
	}

	public void setMotivoIsencao(String motivoIsencao) {
		this.motivoIsencao = motivoIsencao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}