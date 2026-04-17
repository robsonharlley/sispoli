package br.com.sispoli.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import br.com.sispoli.dao.EnturmacaoDAO;
import br.com.sispoli.dao.FrequenciaDAO;
import br.com.sispoli.dao.ResponsavelDAO;
import br.com.sispoli.view.CadastroEnturmacoesView;
import br.com.sispoli.view.CadastroFrequenciasView;
import br.com.sispoli.view.CadastroResponsaveisView;
import br.com.sispoli.view.ConfiguracoesMenuView;
import br.com.sispoli.view.MenuPrincipalView;

public class MenuPrincipalController {
	private final MenuPrincipalView view;

	public MenuPrincipalController(MenuPrincipalView view) {
		this.view = view;
		configurarNavegacao();
		view.setStatus("✅ Sistema inicializado às "
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
	}

	private void configurarNavegacao() {

		// ⚙️ Configurações → ABRE O SUB-MENU (alteração principal)
		view.adicionarListenerBotao("⚙️ Configurações", e -> {
		    // ✅ Verifica senha antes de abrir
		    if (br.com.sispoli.security.SegurancaUtil.verificarAcessoConfiguracoes(view)) {
		        view.setStatus("🔐 Autenticado. Abrindo Painel Administrativo...");
		        SwingUtilities.invokeLater(() -> {
		            ConfiguracoesMenuView subMenu = new ConfiguracoesMenuView();
		            new ConfiguracoesMenuController(subMenu);
		            subMenu.setVisible(true);
		            subMenu.addWindowListener(new java.awt.event.WindowAdapter() {
		                @Override
		                public void windowClosed(java.awt.event.WindowEvent we) {
		                    view.setStatus("✅ Painel administrativo fechado.");
		                }
		            });
		        });
		    } else {
		        view.setStatus("❌ Acesso negado: senha inválida.");
		        // Não mostra dialog se cancelou, só se digitou errado
		        if (e.getSource() instanceof JButton) { // Garante que foi clique, não cancelamento
		            JOptionPane.showMessageDialog(view, "Senha incorreta. Acesso negado.",
		                "Autenticação Falhou", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});
		
		
		
		
/*		view.adicionarListenerBotao("⚙️ Configurações", e -> {
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
*/
		// 🎓 Enturmações (acesso direto pelo menu principal)
		view.adicionarListenerBotao("🎓 Enturmações", e -> {
			javax.swing.SwingUtilities.invokeLater(() -> {
				try {
					CadastroEnturmacoesView v = new CadastroEnturmacoesView();
					new CadastroEnturmacoesController(v, new EnturmacaoDAO());
					v.setVisible(true);

					v.addWindowListener(new java.awt.event.WindowAdapter() {
						public void windowClosed(java.awt.event.WindowEvent we) {
							view.setStatus("✅ Enturmações finalizado.");
						}
					});
				} catch (Throwable t) {
					System.err.println("🔥 FATAL ao abrir Enturmações:");
					t.printStackTrace();
					view.setStatus("❌ Falha crítica ao abrir módulo.");
					javax.swing.JOptionPane.showMessageDialog(view, "Erro interno ao abrir tela:\n" + t.getMessage(),
							"Erro Fatal", javax.swing.JOptionPane.ERROR_MESSAGE);
				}
			});
		});

		// 👨‍👩‍👧 Responsáveis (acesso direto pelo menu principal)
		view.adicionarListenerBotao("👨‍👩‍👧 Responsáveis", e -> abrirTela(() -> {
			CadastroResponsaveisView v = new CadastroResponsaveisView();
			new CadastroResponsaveisController(v, new ResponsavelDAO());
			return v;
		}, "Cadastro de Responsáveis"));

		view.adicionarListenerBotao("📊 Frequência", e -> abrirTela(() -> {
			CadastroFrequenciasView v = new CadastroFrequenciasView();
			new CadastroFrequenciaController(v, new FrequenciaDAO());
			return v;
		}, "Registro de Frequência"));

		// 🔮 Módulos futuros (placeholders)
		String[] modulosFuturos = { "⚠️ Ocorrências" };
		for (String modulo : modulosFuturos) {
			view.adicionarListenerBotao(modulo, e -> {
				view.setStatus("🔄 Módulo " + modulo + " em desenvolvimento...");
				JOptionPane.showMessageDialog(view, "Módulo será integrado na próxima versão.", "Em breve",
						JOptionPane.INFORMATION_MESSAGE);
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
			tela.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosed(java.awt.event.WindowEvent e) {
					view.setStatus("✅ " + nome + " finalizado.");
				}
			});
		});
	}

	@FunctionalInterface
	private interface JFrameFactory {
		JFrame criar();
	}
}