package br.com.sispoli.dao;

import br.com.sispoli.config.DatabaseConfig;
import br.com.sispoli.model.Frequencia;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FrequenciaDAO {

    public void salvar(Frequencia f) {
        String sql = "INSERT INTO tabfrequencia (id_aluno, id_turma, data_aula) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, f.getIdAluno());
            ps.setInt(2, f.getIdTurma());
            if (f.getDataAula() != null) ps.setObject(3, f.getDataAula());
            else ps.setNull(3, Types.DATE);
            ps.executeUpdate();
        } catch (SQLException ex) { throw new RuntimeException(ex.getMessage(), ex); }
    }

    public void excluir(int id) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM tabfrequencia WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) { throw new RuntimeException("Erro ao excluir: " + ex.getMessage(), ex); }
    }

    public List<Frequencia> listarTodas() {
        List<Frequencia> lista = new ArrayList<>();
        String sql = "SELECT f.id, f.id_aluno, f.id_turma, f.data_aula, " +
                     "a.nome_completo as nome_aluno, t.nome_turma as nome_turma " +
                     "FROM tabfrequencia f " +
                     "INNER JOIN tabalunos a ON f.id_aluno = a.id_aluno " +
                     "INNER JOIN tabturmas t ON f.id_turma = t.id_turma " +
                     "ORDER BY f.data_aula DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Frequencia(
                    rs.getInt("id"), rs.getInt("id_aluno"), rs.getInt("id_turma"),
                    rs.getObject("data_aula", LocalDate.class),
                    rs.getString("nome_aluno"), rs.getString("nome_turma")
                ));
            }
        } catch (SQLException ex) { throw new RuntimeException("Erro ao listar frequências: " + ex.getMessage(), ex); }
        return lista;
    }

    public List<String> listarAlunosCombo() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT id_aluno, nome_completo FROM tabalunos WHERE status = 'Ativo' ORDER BY nome_completo";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(rs.getInt("id_aluno") + " - " + rs.getString("nome_completo"));
        } catch (SQLException ignored) {}
        return lista;
    }

    public List<String> listarTurmasCombo() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT id_turma, nome_turma FROM tabturmas WHERE status = 'Ativa' ORDER BY nome_turma";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(rs.getInt("id_turma") + " - " + rs.getString("nome_turma"));
        } catch (SQLException ignored) {}
        return lista;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            DatabaseConfig.getInstance().getConnectionUrl(),
            DatabaseConfig.getInstance().getUser(),
            DatabaseConfig.getInstance().getPass()
        );
    }
}