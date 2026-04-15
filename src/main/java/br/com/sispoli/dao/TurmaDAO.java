package br.com.sispoli.dao;

import br.com.sispoli.config.DatabaseConfig;
import br.com.sispoli.model.Turma;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TurmaDAO {

    public void salvar(Turma turma) {
        boolean isUpdate = turma.getIdTurma() > 0;
        String sql = isUpdate
                ? "UPDATE tabturmas SET id_local=?, nome_turma=?, nivel=?, faixa_etaria=?, horario=?, dia_semana=?, duracao_aula=?, capacidade_maxima=?, valor_mensalidade=?, status=?, observacoes=?, capacidade_atipicos=? WHERE id_turma=?"
                : "INSERT INTO tabturmas (id_local, nome_turma, nivel, faixa_etaria, horario, dia_semana, duracao_aula, capacidade_maxima, valor_mensalidade, status, observacoes, capacidade_atipicos) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, turma.getIdLocal());
            ps.setString(2, turma.getNomeTurma());
            ps.setString(3, turma.getNivel());
            ps.setString(4, turma.getFaixaEtaria());
            ps.setObject(5, turma.getHorario()); // JDBC 4.2+
            ps.setString(6, turma.getDiaSemana());
            ps.setInt(7, turma.getDuracaoAula());
            ps.setInt(8, turma.getCapacidadeMaxima());
            ps.setBigDecimal(9, turma.getValorMensalidade());
            ps.setString(10, turma.getStatus());
            ps.setString(11, turma.getObservacoes() != null ? turma.getObservacoes() : null);
            ps.setInt(12, turma.getCapacidadeAtipicos());
            if (isUpdate) ps.setInt(13, turma.getIdTurma());

            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("DB_ERROR: " + e.getMessage(), e); }
    }

    public void excluir(int idTurma) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM tabturmas WHERE id_turma = ?")) {
            ps.setInt(1, idTurma);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("DB_ERROR: " + e.getMessage(), e); }
    }

    public List<Turma> listarTodos() {
        List<Turma> lista = new ArrayList<>();
        String sql = "SELECT t.*, l.nome_local FROM tabturmas t LEFT JOIN tablocais l ON t.id_local = l.id_local ORDER BY t.nome_turma";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                java.sql.Time sqlTime = rs.getTime("horario");
                lista.add(new Turma(
                    rs.getInt("id_turma"), rs.getInt("id_local"), rs.getString("nome_turma"),
                    rs.getString("nivel"), rs.getString("faixa_etaria"), sqlTime != null ? sqlTime.toLocalTime() : null,
                    rs.getString("dia_semana"), rs.getInt("duracao_aula"),
                    rs.getInt("capacidade_maxima"), rs.getBigDecimal("valor_mensalidade"),
                    rs.getString("status"), rs.getString("observacoes"),
                    rs.getInt("capacidade_atipicos"), rs.getString("nome_local")
                ));
            }
        } catch (SQLException e) { throw new RuntimeException("DB_ERROR: " + e.getMessage(), e); }
        return lista;
    }

    public List<String> listarLocaisCombo() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT id_local, nome_local FROM tablocais WHERE status = 'Ativo' ORDER BY nome_local";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(rs.getInt("id_local") + " - " + rs.getString("nome_local"));
        } catch (SQLException e) { /* ignora */ }
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