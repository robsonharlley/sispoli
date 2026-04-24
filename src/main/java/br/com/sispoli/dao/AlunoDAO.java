package br.com.sispoli.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.sispoli.config.DatabaseConfig;
import br.com.sispoli.model.Aluno;

public class AlunoDAO {
	public void salvar(Aluno a) {
		boolean update = a.getIdAluno() > 0;
		String sql = update
				? "UPDATE tabalunos SET nome_completo=?, cpf=?, rg=?, sexo=?, email=?, contato_what=?, id_cep=?, numero=?, complemento=?, data_nascimento=?, data_matricula=?, status=?, isento=?, motivo_isencao=?, possui_restricao_medica=?, descricao_restricao=?, medicamentos_continuos=?, alergias=?, contato_emergencia_nome=?, contato_emergencia_telefone=?, contato_emergencia_parentesco=?, autorizacao_imagem=?, autorizacao_divulgacao=?, aceite_termos=?, data_aceite_termos=?, observacoes=? WHERE id_aluno=?"
				: "INSERT INTO tabalunos (nome_completo, cpf, rg, sexo, email, contato_what, id_cep, numero, complemento, data_nascimento, data_matricula, status, isento, motivo_isencao, possui_restricao_medica, descricao_restricao, medicamentos_continuos, alergias, contato_emergencia_nome, contato_emergencia_telefone, contato_emergencia_parentesco, autorizacao_imagem, autorizacao_divulgacao, aceite_termos, data_aceite_termos, observacoes) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			int idx = 1;
			ps.setString(idx++, a.getNomeCompleto());
			ps.setString(idx++, a.getCpf());
			ps.setString(idx++, a.getRg());
			ps.setString(idx++, a.getSexo());
			ps.setString(idx++, a.getEmail());
			ps.setString(idx++, a.getContatoWhat());
			ps.setString(idx++, a.getIdCep());
			ps.setString(idx++, a.getNumero());
			ps.setString(idx++, a.getComplemento());
			setLocalDate(ps, idx++, a.getDataNascimento());
			setLocalDate(ps, idx++, a.getDataMatricula());
			//setLocalDate(ps, idx++, a.getDataCancelamento());
			//ps.setString(idx++, a.getMotivoCancelamento());
			ps.setString(idx++, a.getStatus());
			ps.setBoolean(idx++, a.getIsento() != null ? a.getIsento() : false);
			ps.setString(idx++, a.getMotivoIsencao());
			ps.setBoolean(idx++, a.getPossuiRestricaoMedica() != null ? a.getPossuiRestricaoMedica() : false);
			ps.setString(idx++, a.getDescricaoRestricao());
			ps.setString(idx++, a.getMedicamentosContinuos());
			ps.setString(idx++, a.getAlergias());
			ps.setString(idx++, a.getContatoEmergenciaNome());
			ps.setString(idx++, a.getContatoEmergenciaTelefone());
			ps.setString(idx++, a.getContatoEmergenciaParentesco());
			ps.setBoolean(idx++, a.getAutorizacaoImagem() != null ? a.getAutorizacaoImagem() : false);
			ps.setBoolean(idx++, a.getAutorizacaoDivulgacao() != null ? a.getAutorizacaoDivulgacao() : false);
			ps.setBoolean(idx++, a.getAceiteTermos() != null ? a.getAceiteTermos() : false);
			setLocalDate(ps, idx++, a.getDataAceiteTermos());
			ps.setString(idx++, a.getObservacoes());
			if (update)
				ps.setInt(idx, a.getIdAluno());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public void excluir(int id) {
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("DELETE FROM tabalunos WHERE id_aluno=?")) {
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public List<Aluno> listarTodos() {
		List<Aluno> list = new ArrayList<>();
		//String sql = "SELECT * FROM tabalunos ORDER BY nome_completo";
		String sql = "SELECT * FROM tabalunos ORDER BY data_matricula";
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Aluno a = new Aluno();
				a.setIdAluno(rs.getInt("id_aluno"));
				a.setNomeCompleto(rs.getString("nome_completo"));
				a.setCpf(rs.getString("cpf"));
				a.setRg(rs.getString("rg"));
				a.setSexo(rs.getString("sexo"));
				a.setEmail(rs.getString("email"));
				a.setContatoWhat(rs.getString("contato_what"));
				a.setIdCep(rs.getString("id_cep"));
				a.setNumero(rs.getString("numero"));
				a.setComplemento(rs.getString("complemento"));
				a.setDataNascimento(getLocalDate(rs, "data_nascimento"));
				a.setDataMatricula(getLocalDate(rs, "data_matricula"));
				//a.setDataCancelamento(getLocalDate(rs, "data_cancelamento"));
				//a.setMotivoCancelamento(rs.getString("motivo_cancelamento"));
				a.setStatus(rs.getString("status"));
				a.setIsento(rs.getBoolean("isento"));
				a.setMotivoIsencao(rs.getString("motivo_isencao"));
				a.setPossuiRestricaoMedica(rs.getBoolean("possui_restricao_medica"));
				a.setDescricaoRestricao(rs.getString("descricao_restricao"));
				a.setMedicamentosContinuos(rs.getString("medicamentos_continuos"));
				a.setAlergias(rs.getString("alergias"));
				a.setContatoEmergenciaNome(rs.getString("contato_emergencia_nome"));
				a.setContatoEmergenciaTelefone(rs.getString("contato_emergencia_telefone"));
				a.setContatoEmergenciaParentesco(rs.getString("contato_emergencia_parentesco"));
				a.setAutorizacaoImagem(rs.getBoolean("autorizacao_imagem"));
				a.setAutorizacaoDivulgacao(rs.getBoolean("autorizacao_divulgacao"));
				a.setAceiteTermos(rs.getBoolean("aceite_termos"));
				a.setDataAceiteTermos(getLocalDate(rs, "data_aceite_termos"));
				a.setObservacoes(rs.getString("observacoes"));
				list.add(a);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return list;
	}

	// ✅ Métodos auxiliares
	private void setLocalDate(PreparedStatement ps, int idx, LocalDate date) throws SQLException {
		ps.setObject(idx, date);
	}

	private LocalDate getLocalDate(ResultSet rs, String col) throws SQLException {
		return rs.getObject(col, LocalDate.class);
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DatabaseConfig.getInstance().getConnectionUrl(),
				DatabaseConfig.getInstance().getUser(), DatabaseConfig.getInstance().getPass());
	}
}