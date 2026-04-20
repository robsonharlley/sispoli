package br.com.sispoli.service;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import br.com.sispoli.config.DatabaseConfig;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Serviço de relatórios - Versão minimalista e compatível com JasperReports
 * 6.x/7.x
 */
public class RelatorioService {

	private static final String RELATORIOS_PATH = "/relatorios/";

	public static void gerarEExibir(String nomeRelatorio, Map<String, Object> parametros, String tituloJanela) {
		executarRelatorio(nomeRelatorio, parametros, tituloJanela, true, null);
	}

	public static boolean gerarEPdf(String nomeRelatorio, Map<String, Object> parametros,
			String caminhoArquivoDestino) {
		return executarRelatorio(nomeRelatorio, parametros, null, false, caminhoArquivoDestino);
	}

	private static boolean executarRelatorio(String nomeRelatorio, Map<String, Object> parametros, String tituloJanela,
			boolean exibirViewer, String caminhoPdf) {
		Connection conn = null;
		try {
			conn = obterConexaoBanco();
			if (conn == null)
				throw new SQLException("Falha na conexão com o banco.");

			JasperReport report = carregarRelatorio(nomeRelatorio);
			if (report == null)
				throw new JRException("Relatório não encontrado: " + nomeRelatorio);

			if (parametros == null)
				parametros = new HashMap<>();

			JasperPrint print = JasperFillManager.fillReport(report, parametros, conn);

			if (exibirViewer) {
				exibirViewerRelatorio(print, tituloJanela != null ? tituloJanela : "SISPOLI");
				return true;
			} else {
				return exportarParaPdf(print, caminhoPdf);
			}
		} catch (Exception e) {
			tratarErro("Erro ao gerar relatório", e);
			return false;
		} finally {
			fecharConexao(conn);
		}
	}

	private static Connection obterConexaoBanco() throws SQLException {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			return DriverManager.getConnection(DatabaseConfig.getInstance().getConnectionUrl(),
					DatabaseConfig.getInstance().getUser(), DatabaseConfig.getInstance().getPass());
		} catch (ClassNotFoundException e) {
			throw new SQLException("Driver MariaDB não encontrado.", e);
		}
	}
	
	/**
	 * Carrega relatório compilado (.jasper) ou compila fonte (.jrxml) em runtime.
	 * ✅ Com diagnóstico preciso e fallback automático
	 */
	private static JasperReport carregarRelatorio(String nomeRelatorio) throws JRException {
	    // 1️⃣ Tenta carregar versão compilada (.jasper)
	    String caminhoJasper = RELATORIOS_PATH + nomeRelatorio + ".jasper";
	    try (InputStream jasperStream = RelatorioService.class.getResourceAsStream(caminhoJasper)) {
	        if (jasperStream != null && jasperStream.available() > 0) {
	            System.out.println("🔍 [RELATÓRIO] Tentando carregar .jasper: " + caminhoJasper);
	            try {
	                // ✅ Tenta carregar como objeto compilado
	                JasperReport report = (JasperReport) net.sf.jasperreports.engine.util.JRLoader.loadObject(jasperStream);
	                System.out.println("✅ [RELATÓRIO] .jasper carregado com sucesso");
	                return report;
	            } catch (Exception e) {
	                System.err.println("⚠️ [RELATÓRIO] Falha ao carregar .jasper: " + e.getMessage());
	                System.err.println("💡 Tentando compilar .jrxml como fallback...");
	                // Continua para tentar o .jrxml
	            }
	        }
	    } catch (Exception e) {
	        System.err.println("⚠️ [RELATÓRIO] Erro ao ler .jasper: " + e.getMessage());
	    }

	    // 2️⃣ Fallback: compila .jrxml em tempo de execução
	    String caminhoJrxml = RELATORIOS_PATH + nomeRelatorio + ".jrxml";
	    try (InputStream jrxmlStream = RelatorioService.class.getResourceAsStream(caminhoJrxml)) {
	        if (jrxmlStream != null && jrxmlStream.available() > 0) {
	            System.out.println("⚙️ [RELATÓRIO] Compilando .jrxml: " + caminhoJrxml);
	            JasperReport report = JasperCompileManager.compileReport(jrxmlStream);
	            System.out.println("✅ [RELATÓRIO] .jrxml compilado com sucesso");
	            return report;
	        } else {
	            System.err.println("❌ [RELATÓRIO] Arquivo .jrxml não encontrado ou vazio: " + caminhoJrxml);
	        }
	    } catch (Exception e) {
	        System.err.println("❌ [RELATÓRIO] Erro ao compilar .jrxml: " + e.getMessage());
	        e.printStackTrace();
	    }

	    // 3️⃣ Nenhum arquivo encontrado ou válido
	    throw new JRException(
	        "Relatório '" + nomeRelatorio + "' não encontrado ou inválido.\n" +
	        "💡 Verifique:\n" +
	        "  1. O arquivo está em src/main/resources" + RELATORIOS_PATH + "?\n" +
	        "  2. O .jasper foi compilado com a MESMA versão do JasperReports em runtime?\n" +
	        "  3. O arquivo .jrxml está válido e sem erros de schema XML?"
	    );
	}
	
/*
	private static JasperReport carregarRelatorio(String nomeRelatorio) throws JRException {
		// Tenta .jasper primeiro
		String path = RELATORIOS_PATH + nomeRelatorio + ".jasper";
		InputStream stream = RelatorioService.class.getResourceAsStream(path);

		if (stream != null) {
			System.out.println("📄 Carregado: " + path);
			return (JasperReport) net.sf.jasperreports.engine.util.JRLoader.loadObject(stream);
		}

		// Fallback para .jrxml
		path = RELATORIOS_PATH + nomeRelatorio + ".jrxml";
		stream = RelatorioService.class.getResourceAsStream(path);
		if (stream != null) {
			System.out.println("⚙️ Compilando: " + path);
			return JasperCompileManager.compileReport(stream);
		}

		throw new JRException("Relatório não encontrado: " + nomeRelatorio);
	}
*/
	private static void exibirViewerRelatorio(JasperPrint print, String titulo) {
		SwingUtilities.invokeLater(() -> {
			try {
				JasperViewer viewer = new JasperViewer(print, false);
				viewer.setTitle(titulo);
				viewer.setVisible(true);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao exibir: " + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	/**
	 * Exportação PDF simplificada - sem constantes PROPERTY_* removidas
	 */
	/**
	 * Exporta JasperPrint para PDF - ✅ Método universal compatível com JasperReports 5.x a 7.x
	 */
	private static boolean exportarParaPdf(JasperPrint print, String caminhoArquivo) {
	    if (caminhoArquivo == null || caminhoArquivo.isEmpty()) {
	        System.err.println("❌ [EXPORT] Caminho não informado.");
	        return false;
	    }

	    try {
	        File arquivo = new File(caminhoArquivo);
	        
	        // Cria diretório pai se não existir
	        File dir = arquivo.getParentFile();
	        if (dir != null && !dir.exists()) {
	            dir.mkdirs();
	        }

	        // ✅ Método universal: funciona em QUALQUER versão do JasperReports
	        net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfFile(print, arquivo.getAbsolutePath());
	        
	        System.out.println("✅ [EXPORT] PDF gerado: " + caminhoArquivo);
	        
	        // Feedback ao usuário na EDT
	        SwingUtilities.invokeLater(() -> 
	            JOptionPane.showMessageDialog(null, 
	                "✅ Relatório exportado com sucesso!\n" + caminhoArquivo, 
	                "Exportação Concluída", JOptionPane.INFORMATION_MESSAGE)
	        );
	        return true;

	    } catch (net.sf.jasperreports.engine.JRException e) {
	        System.err.println("❌ [EXPORT] Erro ao exportar PDF: " + e.getMessage());
	        e.printStackTrace();
	        SwingUtilities.invokeLater(() -> 
	            JOptionPane.showMessageDialog(null, 
	                "Erro ao exportar PDF:\n" + e.getMessage(), 
	                "Erro na Exportação", JOptionPane.ERROR_MESSAGE)
	        );
	        return false;
	    }
	}

	private static void tratarErro(String msg, Exception e) {
		System.err.println("❌ " + msg + ": " + e.getMessage());
		e.printStackTrace();
		SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, msg + ":\n" + e.getMessage(), "Erro",
				JOptionPane.ERROR_MESSAGE));
	}

	private static void fecharConexao(Connection conn) {
		if (conn != null) {
			try {
				if (!conn.isClosed())
					conn.close();
			} catch (SQLException ignored) {
			}
		}
	}
}