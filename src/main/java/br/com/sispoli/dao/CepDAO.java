package br.com.sispoli.dao;

import br.com.sispoli.model.Cep;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CepDAO {
    private static final String DB_URL = "jdbc:mariadb://192.168.18.250/gestao_ginasio?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "robson";
    private static final String DB_PASS = "1202153120";

    public void salvar(Cep cep) {
        String sql = "INSERT INTO tabceps (id_cep, logradouro, bairro, cidade, estado, observacoes) " +
                     "VALUES (?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE logradouro=?, bairro=?, cidade=?, estado=?, observacoes=?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, cep.getId_cep());
            ps.setString(2, cep.getLogradouro());
            ps.setString(3, cep.getBairro());
            ps.setString(4, cep.getCidade());
            ps.setString(5, cep.getEstado());
            ps.setString(6, cep.getObservacoes());
            
            // Parâmetros do ON DUPLICATE KEY UPDATE
            ps.setString(7, cep.getLogradouro());
            ps.setString(8, cep.getBairro());
            ps.setString(9, cep.getCidade());
            ps.setString(10, cep.getEstado());
            ps.setString(11, cep.getObservacoes());
            
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar CEP: " + e.getMessage(), e);
        }
    }

    public void excluir(String cep) {
        String sql = "DELETE FROM tabceps WHERE id_cep = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cep);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir CEP: " + e.getMessage(), e);
        }
    }

    public List<Cep> listarTodos() {
        List<Cep> lista = new ArrayList<>();
        String sql = "SELECT id_cep, logradouro, bairro, cidade, estado, observacoes FROM tabceps ORDER BY cidade, bairro";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lista.add(new Cep(
                    rs.getString("id_cep"),
                    rs.getString("logradouro"),
                    rs.getString("bairro"),
                    rs.getString("cidade"),
                    rs.getString("estado"),
                    rs.getString("observacoes")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar CEPs: " + e.getMessage(), e);
        }
        return lista;
    }
}