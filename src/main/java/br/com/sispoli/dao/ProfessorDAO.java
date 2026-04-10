package br.com.sispoli.dao;

import br.com.sispoli.model.Professor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO {
    private static final String DB_URL = "jdbc:mariadb://192.168.18.250/gestao_ginasio?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "robson";
    private static final String DB_PASS = "1202153120";

    public void salvar(Professor p) {
        boolean isUpdate = p.getId() > 0;
        String sql = isUpdate
                ? "UPDATE tabprofessores SET nome=?, matricula=?, telefone=? WHERE id_professor=?"
                : "INSERT INTO tabprofessores (nome, matricula, telefone) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getMatricula() != null && !p.getMatricula().trim().isEmpty() ? p.getMatricula().trim() : null);
            ps.setString(3, p.getTelefone() != null && !p.getTelefone().trim().isEmpty() ? p.getTelefone().trim() : null);
            if (isUpdate) ps.setInt(4, p.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar professor: " + e.getMessage(), e);
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM tabprofessores WHERE id_professor = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir professor: " + e.getMessage(), e);
        }
    }

    public List<Professor> listarTodos() {
        List<Professor> lista = new ArrayList<>();
        String sql = "SELECT id_professor, nome, matricula, telefone FROM tabprofessores ORDER BY nome";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Professor(
                        rs.getInt("id_professor"),
                        rs.getString("nome"),
                        rs.getString("matricula"),
                        rs.getString("telefone")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar professores: " + e.getMessage(), e);
        }
        return lista;
    }
}