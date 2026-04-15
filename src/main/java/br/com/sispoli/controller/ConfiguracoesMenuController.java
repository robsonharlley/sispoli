package br.com.sispoli.controller;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import br.com.sispoli.dao.CepDAO;
import br.com.sispoli.dao.ConfigDAO;
import br.com.sispoli.dao.LocalDAO;
import br.com.sispoli.dao.LotacaoProfessorDAO;
import br.com.sispoli.dao.ProfessorDAO;
import br.com.sispoli.dao.TurmaDAO;
import br.com.sispoli.view.CadastroCEPsView;
import br.com.sispoli.view.CadastroLocaisView;
import br.com.sispoli.view.CadastroLotacaoView;
import br.com.sispoli.view.CadastroProfessoresView;
import br.com.sispoli.view.CadastroTurmasView;
import br.com.sispoli.view.ConfiguracaoDBView;
import br.com.sispoli.view.ConfiguracoesMenuView;
import br.com.sispoli.dao.EnturmacaoDAO;
import br.com.sispoli.view.CadastroEnturmacoesView;
import br.com.sispoli.controller.CadastroEnturmacoesController;


public class ConfiguracoesMenuController {
	private final ConfiguracoesMenuView view;

	public ConfiguracoesMenuController(ConfiguracoesMenuView view) {
		this.view = view;
		configurarNavegacao();
		view.setStatus("✅ Painel administrativo carregado");
	}

	private void configurarNavegacao() {
		// 📍 Locais
		view.adicionarListenerBotao("📍 Locais", e -> abrirTela(() -> {
			CadastroLocaisView v = new CadastroLocaisView();
			new CadastroLocaisController(v, new LocalDAO());
			return v;
		}, "Cadastro de Locais"));

		// 📮 CEPs
		view.adicionarListenerBotao("📮 CEPs", e -> abrirTela(() -> {
			CadastroCEPsView v = new CadastroCEPsView();
			new CadastroCEPsController(v, new CepDAO());
			return v;
		}, "Cadastro de CEPs"));

		// 👨‍🏫 Professores
		view.adicionarListenerBotao("👨‍🏫 Professores", e -> abrirTela(() -> {
			CadastroProfessoresView v = new CadastroProfessoresView();
			new CadastroProfessoresController(v, new ProfessorDAO());
			return v;
		}, "Cadastro de Professores"));

		// 🔐 Configuração do Banco de Dados
		view.adicionarListenerBotao("🔐 Banco de Dados", e -> abrirTela(() -> {
			ConfiguracaoDBView v = new ConfiguracaoDBView();
			new ConfiguracaoDBController(v, new ConfigDAO());
			return v;
		}, "Configuração do Banco de Dados"));
		
        // 👥 Lotação de Professores
        view.adicionarListenerBotao("👥 Lotação", e -> abrirTela(
            () -> {
                CadastroLotacaoView v = new CadastroLotacaoView();
                new CadastroLotacaoController(v, new LotacaoProfessorDAO());
                return v;
            }, "Cadastro de Lotação de Professores"));
        
     // 🎒 Cadastro de Turmas
        view.adicionarListenerBotao("🎒 Turmas", e -> abrirTela(
            () -> {
                CadastroTurmasView v = new CadastroTurmasView();
                new CadastroTurmasController(v, new TurmaDAO());
                return v;
            }, "Cadastro de Turmas"));
        
     // 🎓 Enturmações
        view.adicionarListenerBotao("🎓 Enturmações", e -> abrirTela(
            () -> {
                CadastroEnturmacoesView v = new CadastroEnturmacoesView();
                new CadastroEnturmacoesController(v, new EnturmacaoDAO());
                return v;
            }, "Cadastro de Enturmações"));
	}

	private void abrirTela(JFrameFactory factory, String nomeTela) {
		view.setStatus("🚀 Abrindo " + nomeTela + "...");
		try {
			SwingUtilities.invokeLater(() -> {
				JFrame tela = factory.criar();
				tela.setVisible(true);
				// Atualiza status ao fechar a tela filha
				tela.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosed(java.awt.event.WindowEvent e) {
						view.setStatusSucesso("✅ " + nomeTela + " finalizado");
					}
				});
			});
		} catch (Exception ex) {
			view.setStatusErro("❌ Erro ao abrir " + nomeTela + ": " + ex.getMessage());
			JOptionPane.showMessageDialog(view, "Falha ao iniciar módulo:\n" + ex.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@FunctionalInterface
	public interface JFrameFactory {
		JFrame criar();
	}
}