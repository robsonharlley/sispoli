package br.com.sispoli.controller;

import br.com.sispoli.service.RelatorioService;
import br.com.sispoli.view.RelatoriosView;
import javax.swing.*;
import java.awt.Cursor;
import java.util.HashMap;
import java.util.Map;

public class RelatoriosController {
	private final RelatoriosView view;

	public RelatoriosController(RelatoriosView view) {
		this.view = view;
		configurarRelatorios();
		view.setStatus("✅ Sistema pronto. Selecione um relatório.");
	}

	private void configurarRelatorios() {
		view.adicionarListenerBotao("📋 Rel. Geral Alunos", e -> gerar("Relação Geral de Alunos", "tabalunos"));
		view.adicionarListenerBotao("👥 Alunos por Turma", e -> gerar("Alunos por Turma", "tabenturmacoes"));
		view.adicionarListenerBotao("👨‍🏫 Rel. Professores", e -> gerar("Relação de Professores", "tabprofessores"));
		// view.adicionarListenerBotao("🚪 Rel. Salas", e -> gerar("Relação de
		// Salas/Locais", "tablocais"));

		view.adicionarListenerBotao("🚪 Rel. Salas", e -> {
			// 1. Preparar parâmetros (filtros opcionais)
			Map<String, Object> params = new HashMap<>();
			// params.put("status_filtro", "Ativa"); // Exemplo: filtro por status
			// params.put("data_inicio", java.sql.Date.valueOf("2024-01-01"));

			// 2. Executar em thread separada para não travar a UI
			new Thread(() -> {
				// Opção A: Exibir no viewer
				RelatorioService.gerarEExibir("relatorio_salas", // Nome do arquivo em /relatorios/
						params, // Parâmetros (pode ser null)
						"relatorio_salas - SISPOLI" // Título da janela
				);

				// Opção B: Exportar direto para PDF (descomente para usar)
				
				  boolean sucesso = RelatorioService.gerarEPdf( "relatorio_salas", params,
				  System.getProperty("user.home") + "/Downloads/relatorio de Salas de Aula.pdf" ); if
				  (sucesso) { System.out.println("✅ PDF salvo com sucesso!"); }
				 
			}).start();
		});

		view.adicionarListenerBotao("🔗 Lotação Prof.", e -> gerar("Lotação de Professores", "tablotacao_professores"));
		view.adicionarListenerBotao("💰 Pagamentos (Data)", e -> gerar("Pagamentos por Data", "tabpagamentos"));
		view.adicionarListenerBotao("📅 Pagamentos (Mês)", e -> gerar("Pagamentos por Mês", "tabpagamentos"));
		view.adicionarListenerBotao("⚠️ Inadimplentes", e -> gerar("Alunos Inadimplentes", "tabpagamentos"));
		view.adicionarListenerBotao("🆓 Alunos Isentos", e -> gerar("Alunos Isentos/Bolsistas", "tabalunos"));
		view.adicionarListenerBotao("📄 Rel. Individual", e -> gerar("Relatório Individual do Aluno", "tabalunos"));
		view.adicionarListenerBotao("🎒 Rel. Turmas", e -> gerar("Relatório das Trumas", "tabturmas"));

	}

	private void gerar(String nomeRelatorio, String tabelaOrigem) {
		view.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		view.setStatus("⏳ Gerando " + nomeRelatorio + "...");

		// Simula processamento assíncrono (substituir por chamada real ao Jasper/iText)
		new Thread(() -> {
			try {
				Thread.sleep(800); // Simula tempo de geração

				SwingUtilities.invokeLater(() -> {
					view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					view.setStatusSucesso("✅ " + nomeRelatorio + " gerado com sucesso!");

					// 🟢 AQUI entrará a lógica real de exportação
					// Ex: JasperPrint print = JasperFillManager.fillReport("relatorios/" +
					// nomeRelatorio + ".jasper", params, conn);
					// JasperViewer.viewReport(print, false);

					view.mostrarInfo(
							"📄 Relatório gerado!\n\n💡 Integração com JasperReports/iText pronta para ser adicionada neste bloco.");
				});
			} catch (InterruptedException ex) {
				SwingUtilities.invokeLater(() -> {
					view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					view.setStatusErro("❌ Erro ao gerar relatório.");
				});
			}
		}).start();
	}
}