package br.com.sispoli.dao;

import br.com.sispoli.config.DatabaseConfig;
import br.com.sispoli.model.DbConfig;
import java.sql.*;

public class ConfigDAO {
    
    // ✅ Testa conexão com os parâmetros atuais
    public boolean testarConexao(DbConfig config) {
        try {
            String pass = config.isEncrypt() 
                ? new String(java.util.Base64.getDecoder().decode(config.getPass()))
                : config.getPass();
                
            Connection conn = DriverManager.getConnection(config.getUrl(), config.getUser(), pass);
            boolean ok = conn.isValid(5);
            conn.close();
            return ok;
        } catch (SQLException e) {
            return false;
        }
    }
    
    // ✅ Atualiza configuração e recarrega singleton
    public void atualizarConfiguracao(DbConfig config) {
        DatabaseConfig.getInstance().salvarConfiguracao(config);
    }
    
    // ✅ Retorna configuração atual do singleton
    public DbConfig obterConfiguracao() {
        return DatabaseConfig.getInstance().getConfig();
    }
}