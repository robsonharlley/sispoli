package br.com.sispoli.dao;

import br.com.sispoli.model.LotacaoProfessor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LotacaoProfessorDAO {
    private static final String DB_URL = br.com.sispoli.config.DatabaseConfig.getInstance().getConnectionUrl();
    private static final String DB_USER = br.com.sispoli.config.DatabaseConfig.getInstance().getUser();
    private static final String DB_PASS = br.com.sispoli.config.DatabaseConfig.getInstance().getPass();

    public void salvar(LotacaoProfessor lotacao) {
        boolean isUpdate = lotacao.getIdLotacao() > 0;
        String sql = isUpdate
                ? "UPDATE tablotacao_professores SET id_turma=?, id_professor=? WHERE id_lotacao=?"
                : "INSERT INTO tablotacao_professores (id_turma, id_professor) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, lotacao.getIdTurma());
            ps.setInt(2, lotacao.getIdProfessor());
            if (isUpdate) ps.setInt(3, lotacao.getIdLotacao());

            ps.executeUpdate();
        } catch (SQLException e) {
            // Propaga a exceção para o Controller tratar mensagens específicas
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void excluir(int idLotacao) {
        String sql = "DELETE FROM tablotacao_professores WHERE id_lotacao = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLotacao);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir lotação: " + e.getMessage(), e);
        }
    }

    public List<LotacaoProfessor> listarTodos() {
        List<LotacaoProfessor> lista = new ArrayList<>();
        
        // ✅ CORREÇÃO: Alias "nome_professor" para bater com rs.getString()
        String sql = "SELECT l.id_lotacao, l.id_turma, l.id_professor, " +
                     "t.nome_turma as nome_turma, p.nome as nome " +
                     "FROM tablotacao_professores l " +
                     "INNER JOIN tabturmas t ON l.id_turma = t.id_turma " +
                     "INNER JOIN tabprofessores p ON l.id_professor = p.id_professor " +
                     "ORDER BY p.nome"; // ✅ Reativado ORDER BY para melhor UX

        try (Connection conn = DriverManager.getConnection(
                br.com.sispoli.config.DatabaseConfig.getInstance().getConnectionUrl(),
                br.com.sispoli.config.DatabaseConfig.getInstance().getUser(),
                br.com.sispoli.config.DatabaseConfig.getInstance().getPass());
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new LotacaoProfessor(
                        rs.getInt("id_lotacao"),
                        rs.getInt("id_turma"),
                        rs.getInt("id_professor"),
                        rs.getString("nome_turma"),      // ✅ Bate com alias "nome_turma"
                        rs.getString("nome")   // ✅ Bate com alias "nome_professor" (CORRIGIDO)
                ));
            }
        } catch (SQLException e) {
            // ✅ Log detalhado para facilitar debug futuro
            System.err.println("❌ Erro ao listar lotações: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao listar lotações: " + e.getMessage(), e);
        }
        return lista;
    }

    // Métodos auxiliares para popular combos
    public List<String> listarTurmasCombo() {
        List<String> lista = new ArrayList<>();
        // ✅ Removido filtro de status para evitar erro de coluna/valor inexistente
        // Ajuste o nome das colunas conforme seu CREATE TABLE tabturmas
        String sql = "SELECT id_turma, nome_turma FROM tabturmas ORDER BY nome_turma";
        
        try (Connection conn = DriverManager.getConnection(
                br.com.sispoli.config.DatabaseConfig.getInstance().getConnectionUrl(),
                br.com.sispoli.config.DatabaseConfig.getInstance().getUser(),
                br.com.sispoli.config.DatabaseConfig.getInstance().getPass());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int id = rs.getInt("id_turma");
                String nome = rs.getString("nome_turma");
                // ✅ Proteção contra null ou vazio
                if (nome != null && !nome.trim().isEmpty()) {
                    lista.add(id + " - " + nome);
                }
            }
        } catch (SQLException e) {
            // ✅ Em vez de silenciar, propaga o erro com stack trace para debug
            e.printStackTrace();
            throw new RuntimeException("❌ Erro ao carregar turmas para combo: " + e.getMessage(), e);
        }
        return lista;
    }


    public List<String> listarProfessoresCombo() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT id_professor, nome FROM tabprofessores ORDER BY nome";
        
        try (Connection conn = DriverManager.getConnection(
                br.com.sispoli.config.DatabaseConfig.getInstance().getConnectionUrl(),
                br.com.sispoli.config.DatabaseConfig.getInstance().getUser(),
                br.com.sispoli.config.DatabaseConfig.getInstance().getPass());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int id = rs.getInt("id_professor");
                String nome = rs.getString("nome");
                if (nome != null && !nome.trim().isEmpty()) {
                    lista.add(id + " - " + nome);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Erro ao carregar professores para combo: " + e.getMessage(), e);
        }
        return lista;
    }
}