package br.com.sispoli.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import br.com.sispoli.model.Enturmacao;

public class CadastroEnturmacoesView extends JFrame {
	private JComboBox<String> cmbAluno, cmbTurma, cmbTipo, cmbMotivo, cmbStatus;
	private JSpinner spnDataEntrada, spnDataSaida;
	private JTextArea txtObservacoes;
	private JTable tblEnturmacoes;
	private DefaultTableModel tableModel;
	private JButton btnSalvar, btnLimpar, btnExcluir, btnCancelar;
	// ✅ Data "vazia" segura para evitar crash do JSpinner
	private static final java.util.Date EMPTY_DATE = java.util.Date.from(
	    java.time.LocalDate.of(1900, 1, 1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()
	);

	public CadastroEnturmacoesView() {
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		setTitle("Cadastro de Enturmações");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(900, 650);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(15, 15));

		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBorder(new EmptyBorder(15, 20, 10, 20));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Linha 1: Aluno & Turma
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.15;
		formPanel.add(new JLabel("Aluno:"), gbc);
		gbc.gridx = 1;
		gbc.weightx = 0.35;
		cmbAluno = new JComboBox<>();
		cmbAluno.addItem("Selecione...");
		formPanel.add(cmbAluno, gbc);
		gbc.gridx = 2;
		gbc.weightx = 0.15;
		formPanel.add(new JLabel("Turma:"), gbc);
		gbc.gridx = 3;
		gbc.weightx = 0.35;
		cmbTurma = new JComboBox<>();
		cmbTurma.addItem("Selecione...");
		formPanel.add(cmbTurma, gbc);

		// Linha 2: Tipo & Data Entrada
		// === LINHA 1: Tipo & Data Entrada ===
		GridBagConstraints gbcL1 = new GridBagConstraints();
		gbcL1.insets = new Insets(5, 5, 5, 5);
		gbcL1.fill = GridBagConstraints.HORIZONTAL;

		gbcL1.gridx = 0;
		gbcL1.gridy = 1;
		gbcL1.weightx = 0.15;
		formPanel.add(new JLabel("Tipo:"), gbcL1);

		gbcL1.gridx = 1;
		gbcL1.gridy = 1;
		gbcL1.weightx = 0.35;
		//cmbTipo = new JComboBox<>(new String[] { "Matrícula", "Transferência", "Experimental", "Rematrícula" });
		cmbTipo = new JComboBox<>(new String[] { "Matrícula", "Rematrícula" });
		formPanel.add(cmbTipo, gbcL1);

		gbcL1.gridx = 2;
		gbcL1.gridy = 1;
		gbcL1.weightx = 0.15;
		formPanel.add(new JLabel("Data Entrada:"), gbcL1);

		gbcL1.gridx = 3;
		gbcL1.gridy = 1;
		gbcL1.weightx = 0.35;
		spnDataEntrada = new JSpinner(new SpinnerDateModel());
		spnDataEntrada.setEditor(new JSpinner.DateEditor(spnDataEntrada, "dd/MM/yyyy"));
		spnDataEntrada.setValue(java.util.Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		formPanel.add(spnDataEntrada, gbcL1);

		// === LINHA 2: Motivo Saída & Status ===
		GridBagConstraints gbcL2 = new GridBagConstraints(); // ✅ NOVA INSTÂNCIA (evita sobreposição)
		gbcL2.insets = new Insets(5, 5, 5, 5);
		gbcL2.fill = GridBagConstraints.HORIZONTAL;

		gbcL2.gridx = 0;
		gbcL2.gridy = 2;
		gbcL2.weightx = 0.15;
		formPanel.add(new JLabel("Motivo Saída:"), gbcL2);

		gbcL2.gridx = 1;
		gbcL2.gridy = 2;
		gbcL2.weightx = 0.35;
		
		//cmbMotivo = new JComboBox<>(new String[] { "", "Cancelamento", "Transferência", "Conclusão", "Inadimplência",
		//		"Frequência Insuficiente", "Problemas de Saúde", "Mudança de Cidade", "Insatisfação", "Outros" });
		
		cmbMotivo = new JComboBox<>(new String[] { "", "Cancelamento", "Inadimplência", "Frequência Insuficiente", "Problemas de Saúde", "Mudança de Cidade", 
				"Insatisfação", "Outros" });
		
		formPanel.add(cmbMotivo, gbcL2);

		gbcL2.gridx = 2;
		gbcL2.gridy = 2;
		gbcL2.weightx = 0.15;
		formPanel.add(new JLabel("Status:"), gbcL2);

		gbcL2.gridx = 3;
		gbcL2.gridy = 2;
		gbcL2.weightx = 0.35;
		//cmbStatus = new JComboBox<>(new String[] { "Ativo", "Inativo", "Trancado", "Concluído" });
		cmbStatus = new JComboBox<>(new String[] { "Ativo", "Inativo" });
		formPanel.add(cmbStatus, gbcL2);

		// ✅ Listener automático: sincroniza Data Saída + Motivo conforme o Status
		cmbStatus.addActionListener(e -> {
		    String statusSelecionado = (String) cmbStatus.getSelectedItem();
		    
		    if ("Inativo".equals(statusSelecionado)) {
		        // 1️⃣ Habilita e preenche a data de saída com hoje
		        spnDataSaida.setEnabled(true);
		        try {
		            spnDataSaida.setValue(java.util.Date.from(
		                java.time.LocalDate.now().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()
		            ));
		        } catch (Exception ex) { /* ignora */ }
		        
		        // 2️⃣ Define motivo automaticamente como "Cancelamento"
		        // ⚠️ Nota: Usa "Cancelamento" para bater com o ENUM do banco. 
		        // Se seu DB usar "Cancelado", altere a string abaixo.
		        cmbMotivo.setSelectedItem("Cancelamento");
		        
		    } else {
		        // 3️⃣ Desabilita e limpa a data de saída
		        spnDataSaida.setEnabled(false);
		        try { spnDataSaida.setValue(EMPTY_DATE); } catch (Exception ignored) {}
		        
		        // 4️⃣ Limpa o motivo ao sair do status Inativo
		        cmbMotivo.setSelectedItem("");
		    }
		});

		// Linha 4: Data Saída
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Label
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0.15;
		formPanel.add(new JLabel("Data Saída:"), gbc);

		// Spinner (inicia DESABILITADO)
		// Data Saída (inicia desabilitada e vazia)
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0.15;
		formPanel.add(new JLabel("Data Saída:"), gbc);
		gbc.gridx = 1;
		gbc.weightx = 0.35;
		spnDataSaida = new JSpinner(new SpinnerDateModel());
		spnDataSaida.setEditor(new JSpinner.DateEditor(spnDataSaida, "dd/MM/yyyy"));
		spnDataSaida.setValue(EMPTY_DATE); // ✅ Nunca null
		spnDataSaida.setEnabled(false);
		formPanel.add(spnDataSaida, gbc);

		// Linha 5: Observações (JTextArea com Scroll)
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0.15;
		gbc.weighty = 0.3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		formPanel.add(new JLabel("Observações:"), gbc);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 3;
		gbc.weightx = 0.85;
		gbc.weighty = 0.3;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTHWEST;

		txtObservacoes = new JTextArea(3, 25);
		txtObservacoes.setLineWrap(true);
		txtObservacoes.setWrapStyleWord(true);
		txtObservacoes.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		formPanel.add(new JScrollPane(txtObservacoes), gbc);

		// ✅ Botões (GridBagConstraints RESETADO)
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;

		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		btnSalvar = new JButton("💾 Salvar");
		btnLimpar = new JButton("🧹 Limpar");
		btnExcluir = new JButton("🗑️ Excluir");
		btnCancelar = new JButton("❌ Cancelar");
		Dimension btnSize = new Dimension(105, 32);
		for (JButton b : new JButton[] { btnSalvar, btnLimpar, btnExcluir, btnCancelar }) {
			b.setPreferredSize(btnSize);
			b.setFocusPainted(false);
		}
		btnExcluir.setBackground(new Color(220, 53, 69));
		btnExcluir.setForeground(Color.WHITE);
		btnPanel.add(btnSalvar);
		btnPanel.add(btnLimpar);
		btnPanel.add(btnExcluir);
		btnPanel.add(btnCancelar);
		formPanel.add(btnPanel, gbc);

		// Grid
		JPanel gridPanel = new JPanel(new BorderLayout(10, 10));
		gridPanel.setBorder(new EmptyBorder(0, 20, 15, 20));
		gridPanel.add(new JLabel("📋 Enturmações Registradas", SwingConstants.LEFT), BorderLayout.NORTH);
		String[] cols = { "ID", "Aluno", "Turma", "Entrada", "Saída", "Tipo", "Status" };
		tableModel = new DefaultTableModel(cols, 0) {
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};
		tblEnturmacoes = new JTable(tableModel);
		tblEnturmacoes.setRowHeight(25);
		tblEnturmacoes.setAutoCreateRowSorter(true);
		gridPanel.add(new JScrollPane(tblEnturmacoes), BorderLayout.CENTER);
		JButton btnAtt = new JButton("🔄 Atualizar Lista");
		gridPanel.add(btnAtt, BorderLayout.SOUTH);

		add(formPanel, BorderLayout.NORTH);
		add(gridPanel, BorderLayout.CENTER);
		btnAtt.addActionListener(e -> refreshTable());
		cmbAluno.requestFocusInWindow();
	}

	// === MÉTODOS EXPOSTOS AO CONTROLLER ===
	public void popularComboAlunos(List<String> list) {
		cmbAluno.removeAllItems();
		cmbAluno.addItem("Selecione...");
		for (String s : list)
			cmbAluno.addItem(s);
	}

	public void popularComboTurmas(List<String> list) {
		cmbTurma.removeAllItems();
		cmbTurma.addItem("Selecione...");
		for (String s : list)
			cmbTurma.addItem(s);
	}

	private int parseId(JComboBox<String> cb) {
		if (cb.getSelectedIndex() <= 0)
			return 0;
		return Integer.parseInt(cb.getSelectedItem().toString().split(" - ")[0]);
	}

	private void selectId(JComboBox<String> cb, int id) {
		for (int i = 0; i < cb.getItemCount(); i++) {
			if (cb.getItemAt(i).startsWith(id + " - ")) {
				cb.setSelectedIndex(i);
				return;
			}
		}
	}

	public int getIdAluno() {
		return parseId(cmbAluno);
	}

	public void setIdAluno(int v) {
		selectId(cmbAluno, v);
	}

	public int getIdTurma() {
		return parseId(cmbTurma);
	}

	public void setIdTurma(int v) {
		selectId(cmbTurma, v);
	}

	public LocalDate getDataEntrada() {
		return ((java.util.Date) spnDataEntrada.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public void setDataEntrada(LocalDate d) {
		if (d != null)
			spnDataEntrada.setValue(java.util.Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	}

	public LocalDate getDataSaida() {
	    Object val = spnDataSaida.getValue();
	    if (val == null || val.equals(EMPTY_DATE)) return null;
	    return ((java.util.Date) val).toInstant()
	            .atZone(java.time.ZoneId.systemDefault())
	            .toLocalDate();
	}

	public void setDataSaida(LocalDate data) {
	    if (spnDataSaida != null) {
	        spnDataSaida.setValue(data != null 
	            ? java.util.Date.from(data.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant())
	            : EMPTY_DATE
	        );
	    }
	}


	public String getTipo() {
		return (String) cmbTipo.getSelectedItem();
	}

	public void setTipo(String v) {
		cmbTipo.setSelectedItem(v);
	}

	public String getMotivo() {
		return (String) cmbMotivo.getSelectedItem();
	}

	public void setMotivo(String v) {
		cmbMotivo.setSelectedItem(v);
	}

	public String getStatus() {
		return (String) cmbStatus.getSelectedItem();
	}

	public void setStatus(String v) {
		cmbStatus.setSelectedItem(v);
	}

	public String getObservacoes() {
		return txtObservacoes != null ? txtObservacoes.getText().trim() : "";
	}

	public void setObservacoes(String v) {
		if (txtObservacoes != null)
			txtObservacoes.setText(v);
	}

	public int getLinha() {
		return tblEnturmacoes.getSelectedRow();
	}

	public Object getVal(int l, int c) {
		return tableModel.getValueAt(l, c);
	}

	public void addSalvar(ActionListener l) {
		btnSalvar.addActionListener(l);
	}

	public void addExcluir(ActionListener l) {
		btnExcluir.addActionListener(l);
	}

	public void addLimpar(ActionListener l) {
		btnLimpar.addActionListener(l);
	}

	public void addCancelar(ActionListener l) {
		btnCancelar.addActionListener(l);
	}

	public void addTabela(MouseListener l) {
		tblEnturmacoes.addMouseListener(l);
	}

	public void atualizarTabela(Object[][] dados) {
		try {
			tableModel.setRowCount(0); // Limpa de forma segura
			if (dados != null) {
				for (Object[] linha : dados) {
					// Validação extra: garante que a linha tem o tamanho exato das colunas
					if (linha != null && linha.length == 7) {
						tableModel.addRow(linha);
					}
				}
			}
		} catch (IllegalArgumentException e) {
			System.err.println("❌ Illegal Value na tabela: " + e.getMessage());
			System.err.println("💡 Verifique se todos os campos em 'dados' são Strings/Integers válidos.");
		}
	}

	/**
	 * Preenche todos os campos do formulário com base em uma Enturmacao existente.
	 * Ideal para edição ou processo de desenturmação.
	 */
	public void carregarFormulario(Enturmacao ent) {
		setIdAluno(ent.getIdAluno());
		setIdTurma(ent.getIdTurma());
		setDataEntrada(ent.getDataEnturmacao());
		setDataSaida(ent.getDataDesenturmacao());
		setTipo(ent.getTipo());
		setMotivo(ent.getMotivoDesenturmacao());
		setStatus(ent.getStatus());
		setObservacoes(ent.getObservacoes());

		// ✅ Sincroniza estado do campo Data Saída com o status carregado
		boolean desenturmado = ent.getDataDesenturmacao() != null;
		spnDataSaida.setEnabled(desenturmado);

		// Se estiver inativo mas sem data, força hoje na UI
		if ("Inativo".equals(ent.getStatus()) && !desenturmado) {
			setDataSaida(LocalDate.now());
			spnDataSaida.setEnabled(true);
		}
	}


public void limparFormulario() {
    cmbAluno.setSelectedIndex(0); cmbTurma.setSelectedIndex(0);
    cmbTipo.setSelectedIndex(0); cmbMotivo.setSelectedIndex(0); cmbStatus.setSelectedIndex(0);
    
    setDataEntrada(java.time.LocalDate.now());
    setDataSaida(null); // ✅ Usa EMPTY_DATE internamente (seguro)
    spnDataSaida.setEnabled(false);
    
    txtObservacoes.setText(""); tblEnturmacoes.clearSelection(); cmbAluno.requestFocusInWindow();
}
	

	public void info(String m) {
		JOptionPane.showMessageDialog(this, m, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
	}

	public void erro(String m) {
		JOptionPane.showMessageDialog(this, m, "Erro", JOptionPane.ERROR_MESSAGE);
	}

	public int confirmar(String m) {
		return JOptionPane.showConfirmDialog(this, m, "Confirmação", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
	}

	public void refreshTable() {
	}
}
