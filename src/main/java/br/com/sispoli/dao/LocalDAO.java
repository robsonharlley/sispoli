package br.com.sispoli.dao;

import br.com.sispoli.model.Local;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocalDAO {
    private static final String DB_URL = "jdbc:mariadb://192.168.18.250/gestao_ginasio?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "robson";
    private static final String DB_PASS = "1202153120";

    public void salvar(Local local) {
        boolean isUpdate = local.getId() > 0;
        String sql = isUpdate 
            ? "UPDATE tablocais SET nome_local=?, capacidade_maxima=?, status=?, observacoes=? WHERE id_local=?"
            : "INSERT INTO tablocais (nome_local, capacidade_maxima, status, observacoes) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, local.getNomeLocal());
            ps.setInt(2, local.getCapacidadeMaxima());
            ps.setString(3, local.getStatus());
            ps.setString(4, local.getObservacoes());
            if (isUpdate) ps.setInt(5, local.getId());
            
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar local: " + e.getMessage(), e);
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM tablocais WHERE id_local = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir local: " + e.getMessage(), e);
        }
    }

    public List<Local> listarTodos() {
        List<Local> lista = new ArrayList<>();
        String sql = "SELECT id_local, nome_local, capacidade_maxima, status, observacoes FROM tablocais ORDER BY nome_local";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lista.add(new Local(
                    rs.getInt("id_local"), rs.getString("nome_local"), rs.getInt("capacidade_maxima"),
                    rs.getString("status"), rs.getString("observacoes")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar locais: " + e.getMessage(), e);
        }
        return lista;
    }
}