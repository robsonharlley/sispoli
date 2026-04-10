package br.com.sispoli.config;

import br.com.sispoli.model.DbConfig;
import java.io.*;
import java.util.Properties;

public class DatabaseConfig {
    private static DatabaseConfig instance;
    private DbConfig config;
    private static final String CONFIG_FILE = "config.properties";

    private DatabaseConfig() {
        carregarConfiguracao();
    }

    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) instance = new DatabaseConfig();
        return instance;
    }

    public void carregarConfiguracao() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            props.load(input);
            config = new DbConfig(
                props.getProperty("db.url", "jdbc:mariadb://localhost/gestao_ginasio?useSSL=false"),
                props.getProperty("db.user", "root"),
                props.getProperty("db.pass", ""),
                Boolean.parseBoolean(props.getProperty("db.encrypt", "false"))
            );
        } catch (IOException e) {
            // Fallback para valores padrão se arquivo não existir
            config = new DbConfig(
                "jdbc:mariadb://192.168.18.250/gestao_ginasio?useSSL=false&serverTimezone=UTC",
                "robson", 
                "1202153120", 
                false  // ⚠️ Em produção, considere true + criptografia
            );
            // ✅ CORREÇÃO: passar o objeto config como parâmetro
            salvarConfiguracao(config);
        }
    }

    public void salvarConfiguracao(DbConfig novaConfig) {
        if (novaConfig == null) {
            throw new IllegalArgumentException("Configuração não pode ser nula");
        }
        
        Properties props = new Properties();
        props.setProperty("db.url", novaConfig.getUrl());
        props.setProperty("db.user", novaConfig.getUser());
        
        // ✅ Criptografia condicional + validação
        String passToSave = novaConfig.getPass();
        if (novaConfig.isEncrypt() && passToSave != null && !passToSave.isEmpty()) {
            passToSave = java.util.Base64.getEncoder().encodeToString(passToSave.getBytes());
        }
        props.setProperty("db.pass", passToSave);
        props.setProperty("db.encrypt", String.valueOf(novaConfig.isEncrypt()));

        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            props.store(output, "Configuração do Banco de Dados - SISPOLI");
            this.config = novaConfig; // Atualiza cache em memória
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar configuração: " + e.getMessage(), e);
        }
    }

    public DbConfig getConfig() { return config; }
    
    // Método utilitário para obter URL formatada para DriverManager
    public String getConnectionUrl() { return config.getUrl(); }
    public String getUser() { return config.getUser(); }
    public String getPass() { 
        String pass = config.getPass();
        if (config.isEncrypt()) {
            pass = new String(java.util.Base64.getDecoder().decode(pass));
        }
        return pass;
    }
    
    public void reload() { carregarConfiguracao(); }
}