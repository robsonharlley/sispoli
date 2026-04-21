package br.com.sispoli.dao;

import br.com.sispoli.config.DatabaseConfig;
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
    
    public List<Cep> listarTodosParaCombo() {
        List<Cep> lista = new ArrayList<>();
        String sql = "SELECT id_cep, logradouro, bairro, cidade, estado, observacoes FROM tabceps ORDER BY logradouro ASC, id_cep ASC";
        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.getInstance().getConnectionUrl(),
                DatabaseConfig.getInstance().getUser(),
                DatabaseConfig.getInstance().getPass());
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Cep c = new Cep();
                c.setId_cep(rs.getString("id_cep"));
                c.setLogradouro(rs.getString("logradouro"));
                c.setBairro(rs.getString("bairro"));
                c.setCidade(rs.getString("cidade"));
                c.setEstado(rs.getString("estado"));
                c.setObservacoes(rs.getString("observacoes"));
                lista.add(c);
            }
        } catch (SQLException e) { System.err.println("❌ Erro ao carregar CEPs: " + e.getMessage()); }
        return lista;
    }
    
    public Cep buscarPorId(String cep) {
        String cepLimpo = cep.replaceAll("\\D", "");
        String sql = "SELECT id_cep, cidade, bairro, logradouro, estado, observacoes " +
                     "FROM tabceps WHERE REPLACE(id_cep, '-', '') = ?";
        
        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.getInstance().getConnectionUrl(),
                DatabaseConfig.getInstance().getUser(),
                DatabaseConfig.getInstance().getPass());
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, cepLimpo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cep c = new Cep();
                    c.setId_cep(rs.getString("id_cep"));
                    c.setCidade(rs.getString("cidade"));
                    c.setBairro(rs.getString("bairro"));
                    c.setLogradouro(rs.getString("logradouro"));
                    c.setEstado(rs.getString("estado"));
                    c.setObservacoes(rs.getString("observacoes"));
                    return c;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ [CepDAO] Erro: " + e.getMessage());
        }
        return null;
    }
    
}