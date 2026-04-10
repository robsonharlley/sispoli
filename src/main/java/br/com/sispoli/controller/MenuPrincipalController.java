package br.com.sispoli.controller;

import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import br.com.sispoli.dao.CepDAO;
import br.com.sispoli.dao.ConfigDAO;
import br.com.sispoli.dao.LocalDAO;
import br.com.sispoli.dao.ProfessorDAO;
import br.com.sispoli.view.CadastroCEPsView;
import br.com.sispoli.view.CadastroLocaisView;
import br.com.sispoli.view.CadastroProfessoresView;
import br.com.sispoli.view.ConfiguracaoDBView;
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
		// 📍 Locais
		view.adicionarListenerBotao("📍 Locais", this::abrirCadastroLocais);

		// 📮 CEPs
		view.adicionarListenerBotao("📮 CEPs", this::abrirCadastroCeps);

		// 👨‍🏫 Professores
		view.adicionarListenerBotao("👨‍🏫 Professores", this::abrirCadastroProfessores);

		// ⚙️ Configurações
		view.adicionarListenerBotao("⚙️ Configurações", this::abrirCadastroConfiguracoes);

		// 🔮 Módulos futuros (placeholders)
		String[] modulosFuturos = { "🎒 Turmas", "🎓 Enturmações", "📊 Frequência", "⚠️ Ocorrências",
				"👨‍👩‍👧 Responsáveis" };

		for (String modulo : modulosFuturos) {
			view.adicionarListenerBotao(modulo, e -> {
				view.setStatus("🔄 Módulo " + modulo + " em desenvolvimento...");
				JOptionPane.showMessageDialog(view, "Módulo será integrado na próxima versão.", "Em breve",
						JOptionPane.INFORMATION_MESSAGE);
			});
		}
	}

	// ✅ Métodos explícitos para evitar erro de inferência de tipo
	private void abrirCadastroLocais(ActionEvent e) {
		abrirTela(() -> {
			CadastroLocaisView v = new CadastroLocaisView();
			new CadastroLocaisController(v, new LocalDAO());
			return v;
		}, "Cadastro de Locais");
	}

	private void abrirCadastroCeps(ActionEvent e) {
		abrirTela(() -> {
			CadastroCEPsView v = new CadastroCEPsView();
			new CadastroCEPsController(v, new CepDAO());
			return v;
		}, "Cadastro de CEPs");
	}

	private void abrirCadastroProfessores(ActionEvent e) {
		abrirTela(() -> {
			CadastroProfessoresView v = new CadastroProfessoresView();
			new CadastroProfessoresController(v, new ProfessorDAO());
			return v;
		}, "Cadastro de Professores");
	}

	private void abrirCadastroConfiguracoes(ActionEvent e) {
		abrirTela(() -> {
			ConfiguracaoDBView v = new ConfiguracaoDBView();
			new ConfiguracaoDBController(v, new ConfigDAO());
			return v;
		}, " Cadastro de Configurações ");
	}


	// ✅ Método genérico de abertura de telas (CORRIGIDO)
	private void abrirTela(JFrameFactory factory, String nomeTela) {
		view.setStatus("🚀 Abrindo " + nomeTela + "...");
		try {
			SwingUtilities.invokeLater(() -> {
				JFrame tela = factory.criar();
				tela.setVisible(true);
				tela.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosed(java.awt.event.WindowEvent e) {
						view.setStatus("✅ " + nomeTela + " finalizado. Aguardando próxima ação...");
					}
				});
			});
		} catch (Exception ex) {
			view.setStatus("❌ Erro ao abrir " + nomeTela + ": " + ex.getMessage());
			JOptionPane.showMessageDialog(view, "Falha ao iniciar módulo:\n" + ex.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// ✅ Interface funcional corrigida para retornar JFrame
	@FunctionalInterface
	private interface JFrameFactory {
		JFrame criar();
	}
}