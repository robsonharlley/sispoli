package br.com.sispoli.dao;

import br.com.sispoli.model.Responsavel;
import br.com.sispoli.config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResponsavelDAO {

    public void salvar(Responsavel r) {
        boolean isUpdate = r.getIdResponsavel() > 0;
        String sql = isUpdate
            ? "UPDATE tabresponsaveis SET id_aluno=?, nome_completo=?, cpf=?, rg=?, email=?, telefone1=?, parentesco=? WHERE id_responsavel=?"
            : "INSERT INTO tabresponsaveis (id_aluno, nome_completo, cpf, rg, email, telefone1, parentesco) VALUES (?,?,?,?,?,?,?)";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getIdAluno());
            ps.setString(2, r.getNomeCompleto());
            ps.setString(3, r.getCpf() != null && !r.getCpf().trim().isEmpty() ? r.getCpf().trim() : null);
            ps.setString(4, r.getRg() != null && !r.getRg().trim().isEmpty() ? r.getRg().trim() : null);
            ps.setString(5, r.getEmail() != null && !r.getEmail().trim().isEmpty() ? r.getEmail().trim() : null);
            ps.setString(6, r.getTelefone1());
            ps.setString(7, r.getParentesco());
            if (isUpdate) ps.setInt(8, r.getIdResponsavel());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Erro ao salvar: " + e.getMessage(), e); }
    }

    public void excluir(int id) {
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM tabresponsaveis WHERE id_responsavel = ?")) {
            ps.setInt(1, id); ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Erro ao excluir: " + e.getMessage(), e); }
    }

    public List<Responsavel> listarTodos() {
        List<Responsavel> lista = new ArrayList<>();
        String sql = "SELECT r.*, a.nome_completo as nome_aluno FROM tabresponsaveis r " +
                     "INNER JOIN tabalunos a ON r.id_aluno = a.id_aluno ORDER BY r.nome_completo";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Responsavel(
                    rs.getInt("id_responsavel"), rs.getInt("id_aluno"),
                    rs.getString("nome_completo"), rs.getString("cpf"), rs.getString("rg"),
                    rs.getString("email"), rs.getString("telefone1"), rs.getString("parentesco"),
                    rs.getString("nome_aluno")
                ));
            }
        } catch (SQLException e) { throw new RuntimeException("Erro ao listar: " + e.getMessage(), e); }
        return lista;
    }

    // Combo de alunos (formato "ID - Nome")
    public List<String> listarAlunosCombo() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT id_aluno, nome_completo FROM tabalunos ORDER BY nome_completo";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(rs.getInt(1) + " - " + rs.getString(2));
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