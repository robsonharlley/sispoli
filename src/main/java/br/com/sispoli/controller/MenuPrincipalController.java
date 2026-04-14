package br.com.sispoli.controller;

import br.com.sispoli.dao.*;
import br.com.sispoli.view.*;
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MenuPrincipalController {
    private final MenuPrincipalView view;

    public MenuPrincipalController(MenuPrincipalView view) {
        this.view = view;
        configurarNavegacao();
        view.setStatus("✅ Sistema inicializado às " + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    }

    private void configurarNavegacao() {
      
        // ⚙️ Configurações → ABRE O SUB-MENU (alteração principal)
        view.adicionarListenerBotao("⚙️ Configurações", e -> {
            view.setStatus("🔧 Abrindo Painel Administrativo...");
            SwingUtilities.invokeLater(() -> {
                ConfiguracoesMenuView subMenu = new ConfiguracoesMenuView();
                new ConfiguracoesMenuController(subMenu);
                subMenu.setVisible(true);
                subMenu.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent we) {
                        view.setStatus("✅ Painel administrativo fechado");
                    }
                });
            });
        });

        // 🔮 Módulos futuros (placeholders)
        String[] modulosFuturos = {"🎒 Turmas", "🎓 Enturmações", "📊 Frequência", 
                                   "⚠️ Ocorrências", "👨‍👩‍👧 Responsáveis" };
        for (String modulo : modulosFuturos) {
            view.adicionarListenerBotao(modulo, e -> {
                view.setStatus("🔄 Módulo " + modulo + " em desenvolvimento...");
                JOptionPane.showMessageDialog(view, "Módulo será integrado na próxima versão.", 
                        "Em breve", JOptionPane.INFORMATION_MESSAGE);
            });
        }
    }

    private void addModulo(String nome, ConfiguracoesMenuController.JFrameFactory factory) {
        view.adicionarListenerBotao(nome, e -> abrirTela(factory::criar, nome));
    }

    private void abrirTela(JFrameFactory f, String nome) {
        view.setStatus("🚀 Abrindo " + nome + "...");
        SwingUtilities.invokeLater(() -> {
            JFrame tela = f.criar();
            tela.setVisible(true);
            tela.addWindowListener(new java.awt.event.WindowAdapter(){ 
                public void windowClosed(java.awt.event.WindowEvent e){ 
                    view.setStatus("✅ " + nome + " finalizado."); 
                } 
            });
        });
    }

    @FunctionalInterface 
    private interface JFrameFactory { JFrame criar(); }
}