package br.com.sispoli.controller;

import br.com.sispoli.dao.ConfigDAO;
import br.com.sispoli.model.DbConfig;
import br.com.sispoli.view.ConfiguracaoDBView;
import java.awt.Color;

public class ConfiguracaoDBController {
    private final ConfiguracaoDBView view;
    private final ConfigDAO dao;

    public ConfiguracaoDBController(ConfiguracaoDBView view, ConfigDAO dao) {
        this.view = view;
        this.dao = dao;
        carregarConfiguracaoAtual();
        configurarListeners();
    }

    private void carregarConfiguracaoAtual() {
        DbConfig cfg = dao.obterConfiguracao();
        view.setUrl(cfg.getUrl());
        view.setUser(cfg.getUser());
        view.setPass(cfg.getPass()); // Em produção, não exibir senha real
        view.setEncrypt(cfg.isEncrypt());
        view.setStatus("⚙️ Configuração carregada. Teste antes de salvar.", new Color(59, 130, 246));
    }

    private void configurarListeners() {
        view.adicionarListenerTestar(e -> testarConexao());
        view.adicionarListenerSalvar(e -> salvarConfiguracao());
        view.adicionarListenerReload(e -> { 
            dao.obterConfiguracao(); // Recarrega do arquivo
            carregarConfiguracaoAtual(); 
            view.setStatus("✅ Configuração recarregada com sucesso!", new Color(34, 197, 94));
        });
        view.adicionarListenerCancelar(e -> view.dispose());
    }

    private void testarConexao() {
        DbConfig testConfig = new DbConfig(
            view.getUrl(), view.getUser(), view.getPass(), view.isEncrypt()
        );
        
        view.setStatus("🔄 Testando conexão...", new Color(139, 92, 246));
        view.repaint();
        
        // Executa em thread separada para não travar a UI
        new Thread(() -> {
            boolean ok = dao.testarConexao(testConfig);
            java.awt.EventQueue.invokeLater(() -> {
                if (ok) {
                    view.setStatus("✅ Conexão estabelecida com sucesso!", new Color(34, 197, 94));
                    view.mostrarInfo("🎉 Conexão com o banco de dados validada!");
                } else {
                    view.setStatus("❌ Falha na conexão. Verifique os parâmetros.", new Color(220, 53, 69));
                    view.mostrarErro("Não foi possível conectar ao banco.\n\nVerifique:\n• URL correta\n• Usuário/senha válidos\n• Servidor acessível\n• Driver MariaDB no classpath");
                }
            });
        }).start();
    }

    private void salvarConfiguracao() {
        if (view.getUrl().isEmpty() || view.getUser().isEmpty()) {
            view.mostrarErro("URL e Usuário são obrigatórios.");
            return;
        }
        
        if (view.confirmar("Salvar nova configuração?\n\n⚠️ O sistema será reiniciado para aplicar as alterações.") != 0) {
            return;
        }

        DbConfig novaConfig = new DbConfig(
            view.getUrl(), view.getUser(), view.getPass(), view.isEncrypt()
        );
        
        try {
            // Testa antes de salvar
            if (!dao.testarConexao(novaConfig)) {
                view.mostrarErro("Não é possível salvar: falha na conexão com os novos parâmetros.");
                return;
            }
            
            dao.atualizarConfiguracao(novaConfig);
            view.mostrarInfo("✅ Configuração salva com sucesso!\n\nO sistema será reiniciado para aplicar as alterações.");
            
            // Reinicia a aplicação para recarregar todas as conexões
            java.awt.EventQueue.invokeLater(() -> {
                view.dispose();
                // Em produção: usar System.exit(0) + script de reinício
                // Ou notificar o Main para recarregar todos os DAOs
                javax.swing.SwingUtilities.invokeLater(() -> {
                    // Recarrega configuração em todos os DAOs existentes
                    br.com.sispoli.config.DatabaseConfig.getInstance().reload();
                });
            });
            
        } catch (Exception ex) {
            view.mostrarErro("Erro ao salvar configuração:\n" + ex.getMessage());
        }
    }
}