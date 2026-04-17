package br.com.sispoli.dao;

import br.com.sispoli.config.DatabaseConfig;
import br.com.sispoli.model.Enturmacao;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EnturmacaoDAO {

	public void salvar(Enturmacao e) {
		boolean isUpdate = e.getIdEnturmacao() > 0;
		String sql = isUpdate
				? "UPDATE tabenturmacoes SET id_aluno=?, id_turma=?, data_enturmacao=?, data_desenturmacao=?, tipo=?, motivo_desenturmacao=?, status=?, observacoes=? WHERE id_enturmacao=?"
				: "INSERT INTO tabenturmacoes (id_aluno, id_turma, data_enturmacao, data_desenturmacao, tipo, motivo_desenturmacao, status, observacoes) VALUES (?,?,?,?,?,?,?,?)";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, e.getIdAluno());
			ps.setInt(2, e.getIdTurma());
			ps.setObject(3, e.getDataEnturmacao()); // JDBC 4.2+

			// ✅ Conversão segura para java.sql.Date
			if (e.getDataDesenturmacao() != null) {
			    ps.setObject(4, java.sql.Date.valueOf(e.getDataDesenturmacao().toString()));
			} else {
			    ps.setNull(4, java.sql.Types.DATE);
			}

			ps.setString(5, e.getTipo());

			if (e.getMotivoDesenturmacao() != null && !e.getMotivoDesenturmacao().trim().isEmpty()) {
				ps.setString(6, e.getMotivoDesenturmacao());
			} else {
				ps.setNull(6, Types.VARCHAR);
			}

			ps.setString(7, e.getStatus());
			ps.setString(8, e.getObservacoes() != null ? e.getObservacoes() : null);
			if (isUpdate)
				ps.setInt(9, e.getIdEnturmacao());

			ps.executeUpdate();
		} catch (SQLException ex) {
			throw new RuntimeException("Erro ao salvar enturmação: " + ex.getMessage(), ex);
		}
	}

	public void excluir(int id) {
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("DELETE FROM tabenturmacoes WHERE id_enturmacao = ?")) {
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException ex) {
			throw new RuntimeException("Erro ao excluir: " + ex.getMessage(), ex);
		}
	}

	public List<Enturmacao> listarTodos() {
		List<Enturmacao> lista = new ArrayList<>();
		// ✅ SQL Corrigido: Aliases claros, colunas exatas do schema, ORDER BY seguro
		String sql = "SELECT e.id_enturmacao, e.id_aluno, e.id_turma, e.data_enturmacao, e.data_desenturmacao, "
				+ "e.tipo, e.motivo_desenturmacao, e.status, e.observacoes, "
				+ "a.nome_completo as nome_aluno, t.nome_turma as nome_turma " + "FROM tabenturmacoes e "
				+ "INNER JOIN tabalunos a ON e.id_aluno = a.id_aluno "
				+ "INNER JOIN tabturmas t ON e.id_turma = t.id_turma " + "ORDER BY e.data_enturmacao DESC";

		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				LocalDate dEnt = rs.getObject("data_enturmacao", LocalDate.class);
				LocalDate dSai = rs.getObject("data_desenturmacao", LocalDate.class);

				lista.add(new Enturmacao(rs.getInt("id_enturmacao"), rs.getInt("id_aluno"), rs.getInt("id_turma"), dEnt,
						dSai, rs.getString("tipo"), rs.getString("motivo_desenturmacao"), rs.getString("status"),
						rs.getString("observacoes"), rs.getString("nome_aluno"), rs.getString("nome_turma")));
			}
		} catch (SQLException ex) {
			throw new RuntimeException("Erro ao listar: " + ex.getMessage(), ex);
		}
		return lista;
	}

	
	public List<Enturmacao> listarAtivos() { // ✅ Nome mais semântico
	    List<Enturmacao> lista = new ArrayList<>();
	    
	    // ✅ FILTRO ADICIONADO: WHERE e.status = 'Ativo'
	    String sql = "SELECT e.id_enturmacao, e.id_aluno, e.id_turma, e.data_enturmacao, e.data_desenturmacao, " +
	                 "e.tipo, e.motivo_desenturmacao, e.status, e.observacoes, " +
	                 "a.nome_completo as nome_aluno, t.nome_turma as nome_turma " +
	                 "FROM tabenturmacoes e " +
	                 "INNER JOIN tabalunos a ON e.id_aluno = a.id_aluno " +
	                 "INNER JOIN tabturmas t ON e.id_turma = t.id_turma " +
	                 "WHERE e.status = 'Ativo' " + // 🎯 FILTRO DE STATUS
	                 "ORDER BY e.data_enturmacao DESC";

	    try (Connection conn = getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        
	        while (rs.next()) {
	            LocalDate dEnt = rs.getObject("data_enturmacao", LocalDate.class);
	            LocalDate dSai = rs.getObject("data_desenturmacao", LocalDate.class);

	            lista.add(new Enturmacao(
	                rs.getInt("id_enturmacao"), rs.getInt("id_aluno"), rs.getInt("id_turma"),
	                dEnt, dSai, rs.getString("tipo"), rs.getString("motivo_desenturmacao"),
	                rs.getString("status"), rs.getString("observacoes"),
	                rs.getString("nome_aluno"), rs.getString("nome_turma")
	            ));
	        }
	    } catch (SQLException ex) { 
	        throw new RuntimeException("Erro ao listar enturmações ativas: " + ex.getMessage(), ex); 
	    }
	    return lista;
	}
	
	
	public List<String> listarAlunosCombo() {
		List<String> lista = new ArrayList<>();
		String sql = "SELECT id_aluno, nome_completo FROM tabalunos ORDER BY nome_completo";
		try (Connection conn = getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next())
				lista.add(rs.getInt("id_aluno") + " - " + rs.getString("nome_completo"));
		} catch (SQLException ignored) {
		}
		return lista;
	}

	public List<String> listarTurmasCombo() {
		List<String> lista = new ArrayList<>();
		String sql = "SELECT id_turma, nome_turma FROM tabturmas ORDER BY nome_turma";
		try (Connection conn = getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next())
				lista.add(rs.getInt("id_turma") + " - " + rs.getString("nome_turma"));
		} catch (SQLException ignored) {
		}
		return lista;
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DatabaseConfig.getInstance().getConnectionUrl(),
				DatabaseConfig.getInstance().getUser(), DatabaseConfig.getInstance().getPass());
	}
}