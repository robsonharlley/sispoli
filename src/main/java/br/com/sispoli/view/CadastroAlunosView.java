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
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import br.com.sispoli.model.Cep;

public class CadastroAlunosView extends JFrame {
	// === DADOS PESSOAIS ===
	private JTextField txtNome, txtRG, txtEmail, txtNumero, txtComplemento, txtDataNasc;
	private JFormattedTextField txtCPF, txtWhatsApp;
	// private JSpinner spnDataNasc;
	private JComboBox<String> cmbSexo;

	// === ENDEREÇO (tabceps + tabalunos) ===
	private JComboBox<String> cmbCep;
	private JTextField txtLogradouro, txtBairro, txtCidade, txtCepObs, txtEstado;
	// private JComboBox<String> cmbEstado;
	private Map<String, Cep> cacheCeps = new HashMap<>(); // Cache para busca instantânea

	// === MATRÍCULA & STATUS ===
	private JSpinner spnDataMatricula;
	private JComboBox<String> cmbStatus;
	private JCheckBox chkIsento;
	private JComboBox<String> cmbMotivoIsencao;

	// === EMERGÊNCIA ===
	private JTextField txtNomeEmergencia;
	private JFormattedTextField txtTelEmergencia;
	private JComboBox<String> cmbParentesco;

	// === SAÚDE ===
	private JCheckBox chkRestricao;
	private JTextArea txtRestricao, txtMedicamentos, txtAlergias;

	// === AUTORIZAÇÕES & TERMOS ===
	private JCheckBox chkImg, chkDivulg, chkTermos;
	private JSpinner spnDataAceite;

	// === OBSERVAÇÕES & AÇÕES ===
	private JTextArea txtObs;
	private JButton btnSalvar, btnLimpar, btnExcluir, btnCancelar;

	// === GRID ===
	private JTable tblAlunos;
	private DefaultTableModel tableModel;

	private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final Date EMPTY_DATE = Date
			.from(LocalDate.of(1900, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());

	public CadastroAlunosView() {
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		setTitle("Cadastro de Alunos");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(1100, 800);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		// =====================================================================
		// ✅ PASSO 1: Criar painel do formulário COM altura definida para scroll
		// =====================================================================
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		formPanel.setPreferredSize(new Dimension(1050, 1400)); // ✅ Altura > janela = scroll ativado

		// =====================================================================
		// ✅ PASSO 2: Adicionar seções ao formulário (mantenha sua estrutura)
		// =====================================================================
		formPanel.add(criarSeccao("👤 Dados Pessoais", criarPanelDadosPessoais()));
		formPanel.add(Box.createVerticalStrut(10));
		formPanel.add(criarSeccao("📍 Endereço & CEP", criarPanelEndereco()));
		formPanel.add(Box.createVerticalStrut(10));
		formPanel.add(criarSeccao("📅 Matrícula & Status", criarPanelMatricula()));
		formPanel.add(Box.createVerticalStrut(10));
		formPanel.add(criarSeccao("🆘 Contato de Emergência", criarPanelEmergencia()));
		formPanel.add(Box.createVerticalStrut(10));
		formPanel.add(criarSeccao("🏥 Saúde", criarPanelSaude()));
		formPanel.add(Box.createVerticalStrut(10));
		formPanel.add(criarSeccao("📜 Autorizações & Termos", criarPanelAutorizacoes()));
		formPanel.add(Box.createVerticalStrut(10));
		formPanel.add(criarPanelObsEBotoes());
		formPanel.add(Box.createVerticalStrut(20)); // Espaço final

		// =====================================================================
		// ✅ PASSO 3: Envolver formPanel em JScrollPane (CORREÇÃO PRINCIPAL)
		// =====================================================================
		JScrollPane scrollForm = new JScrollPane(formPanel);
		scrollForm.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollForm.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollForm.setBorder(null);
		scrollForm.getVerticalScrollBar().setUnitIncrement(16); // Scroll suave

		// =====================================================================
		// ✅ PASSO 4: Painel da tabela (fora do scroll do formulário)
		// =====================================================================
		JPanel gridPanel = new JPanel(new BorderLayout(10, 10));
		gridPanel.setBorder(new TitledBorder("📋 Alunos Cadastrados"));
		String[] cols = { "ID", "Nome", "CPF", "Status", "WhatsApp", "Email" };
		tableModel = new DefaultTableModel(cols, 0) {
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};
		tblAlunos = new JTable(tableModel);
		tblAlunos.setRowHeight(25);
		tblAlunos.setAutoCreateRowSorter(true);
		gridPanel.add(new JScrollPane(tblAlunos), BorderLayout.CENTER);
		JButton btnAtt = new JButton("🔄 Atualizar Lista");
		gridPanel.add(btnAtt, BorderLayout.SOUTH);

		// =====================================================================
		// ✅ PASSO 5: Usar JSplitPane para dividir formulário (scroll) e tabela
		// =====================================================================
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollForm, gridPanel);
		splitPane.setDividerLocation(650); // Posição inicial: 650px do topo
		splitPane.setOneTouchExpandable(true); // Botão para expandir/recolher
		splitPane.setDividerSize(6);
		splitPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		add(splitPane, BorderLayout.CENTER);

		// Listener de atualização
		btnAtt.addActionListener(e -> refreshTable());

		// Padrão: data da matrícula = hoje
		setDataMatricula(LocalDate.now());
	}

	// ====== HELPERS DE LAYOUT (EVITA SOBREPOSIÇÃO) ======
	private JPanel criarSeccao(String titulo, JComponent content) {
		JPanel p = new JPanel(new BorderLayout(5, 5));
		p.setBorder(new TitledBorder(titulo));
		p.add(content, BorderLayout.CENTER);
		return p;
	}

	private GridBagConstraints gbc(GridBagConstraints base, int x, int y, int w) {
		base.gridx = x;
		base.gridy = y;
		base.gridwidth = w;
		base.gridheight = 1;
		base.weightx = (w > 1) ? 1.0 : 0.1;
		base.weighty = 0;
		base.anchor = GridBagConstraints.WEST;
		base.fill = GridBagConstraints.HORIZONTAL;
		base.insets = new Insets(4, 5, 4, 5);
		return base;
	}

	private JFormattedTextField createMaskField(String mask) {
		try {
			MaskFormatter f = new MaskFormatter(mask);
			f.setPlaceholderCharacter('_');
			return new JFormattedTextField(f);
		} catch (Exception e) {
			return new JFormattedTextField();
		}
	}

	private JSpinner createDateSpinner() {
		JSpinner s = new JSpinner(new SpinnerDateModel());
		s.setEditor(new JSpinner.DateEditor(s, "dd/MM/yyyy"));
		return s;
	}

	private void setLocalDate(JSpinner s, LocalDate d) {
		s.setValue(d != null ? Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant()) : EMPTY_DATE);
	}

	private LocalDate getLocalDate(JSpinner s) {
		Date d = (Date) s.getValue();
		return (d.equals(EMPTY_DATE) || d == null) ? null : d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	// ====== SEÇÕES DO FORMULÁRIO ======
	private JPanel criarPanelDadosPessoais() {
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		p.add(new JLabel("Nome Completo:"), gbc(gbc, 0, 0, 1));
		txtNome = new JTextField(30);
		p.add(txtNome, gbc(gbc, 1, 0, 3));

		p.add(new JLabel("CPF:"), gbc(gbc, 0, 1, 1));
		txtCPF = createMaskField("###.###.###-##");
		txtCPF.setToolTipText("Digite um CPF Válido");
		txtCPF.setColumns(14);
		p.add(txtCPF, gbc(gbc, 1, 1, 1));

		p.add(new JLabel("RG:"), gbc(gbc, 2, 1, 1));
		txtRG = new JTextField(12);
		p.add(txtRG, gbc(gbc, 3, 1, 1));

		p.add(new JLabel("Nascimento:"), gbc(gbc, 0, 2, 1));
		// ✅ Substituir JSpinner por JFormattedTextField com máscara de data
		txtDataNasc = createMaskField("##/##/####");
		txtDataNasc.setToolTipText("Digite a data no formato DD/MM/AAAA");
		txtDataNasc.setColumns(10);
		p.add(txtDataNasc, gbc(gbc, 1, 2, 1));

		/*
		 * p.add(new JLabel("Nascimento:"), gbc(gbc, 0, 2, 1)); spnDataNasc =
		 * createDateSpinner(); p.add(spnDataNasc, gbc(gbc, 1, 2, 1));
		 */
		p.add(new JLabel("Sexo:"), gbc(gbc, 2, 2, 1));
		cmbSexo = new JComboBox<>(new String[] { "", "Masculino", "Feminino", "Outro" });
		p.add(cmbSexo, gbc(gbc, 3, 2, 1));

		p.add(new JLabel("Email:"), gbc(gbc, 0, 3, 1));
		txtEmail = new JTextField(25);
		p.add(txtEmail, gbc(gbc, 1, 3, 3));

		p.add(new JLabel("WhatsApp:"), gbc(gbc, 0, 4, 1));
		txtWhatsApp = createMaskField("(##) #####-####");
		txtWhatsApp.setColumns(14);
		p.add(txtWhatsApp, gbc(gbc, 1, 4, 1));

		return p;
	}

	private JPanel criarPanelEndereco() {
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 8, 5, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// 🔹 LINHA 1: CEP + Logradouro
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.05;
		p.add(new JLabel("CEP:"), gbc);
		gbc.gridx = 1;
		gbc.weightx = 0.25;
		cmbCep = new JComboBox<>();
		cmbCep.setEditable(false); // ✅ Agora só permite seleção via dropdown
		cmbCep.setToolTipText("Selecione um CEP da lista para preencher o endereço automaticamente");
		cmbCep.setMaximumRowCount(15); // ✅ Mostra até 15 itens antes de aparecer scroll
		// p.add(cmbCep, gbc);
		p.add(cmbCep, gbc(gbc, 1, 0, 2));
		gbc.gridx = 2;
		gbc.weightx = 0.05;
		p.add(new JLabel("Logradouro:"), gbc);
		gbc.gridx = 3;
		gbc.weightx = 0.65;
		txtLogradouro = new JTextField();
		p.add(txtLogradouro, gbc);

		// 🔹 LINHA 2: Número + Complemento + Bairro
		gbc = new GridBagConstraints(); // ✅ RESET TOTAL para evitar vazamento
		gbc.insets = new Insets(5, 8, 5, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.05;
		p.add(new JLabel("Número:"), gbc);
		gbc.gridx = 1;
		gbc.weightx = 0.1;
		txtNumero = new JTextField();
		p.add(txtNumero, gbc);
		gbc.gridx = 2;
		gbc.weightx = 0.15;
		p.add(new JLabel("Complemento:"), gbc);
		gbc.gridx = 3;
		gbc.weightx = 0.25;
		txtComplemento = new JTextField();
		p.add(txtComplemento, gbc);
		gbc.gridx = 4;
		gbc.weightx = 0.05;
		p.add(new JLabel("Bairro:"), gbc);
		gbc.gridx = 5;
		gbc.weightx = 0.35;
		txtBairro = new JTextField();
		p.add(txtBairro, gbc);

		// 🔹 LINHA 3: Cidade + Estado + Obs. Endereço
		gbc = new GridBagConstraints(); // ✅ RESET TOTAL
		gbc.insets = new Insets(5, 8, 5, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.05;
		p.add(new JLabel("Cidade:"), gbc);
		gbc.gridx = 1;
		gbc.weightx = 0.4;
		txtCidade = new JTextField();
		p.add(txtCidade, gbc);

		gbc.gridx = 2;
		gbc.weightx = 0.05;
		p.add(new JLabel("Estado:"), gbc);
		gbc.gridx = 3;
		gbc.weightx = 0.15;
		txtEstado = new JTextField(3);
		txtEstado.setHorizontalAlignment(JTextField.CENTER);
		txtEstado.setEditable(false); // ✅ Impede digitação manual (mantém integridade do DB)
		txtEstado.setBackground(new Color(240, 240, 240)); // ✅ Feedback visual de "somente leitura"
		p.add(txtEstado, gbc);

		gbc.gridx = 4;
		gbc.weightx = 0.05;
		p.add(new JLabel("Obs:"), gbc);
		gbc.gridx = 5;
		gbc.weightx = 0.3;
		txtCepObs = new JTextField();
		p.add(txtCepObs, gbc);

		// Listener de auto-preenchimento (mantido)

		cmbCep.addActionListener(e -> {
			String sel = cmbCep.getSelectedItem() != null ? cmbCep.getSelectedItem().toString() : "";
			if (sel.contains(" - "))
				preencherCamposEndereco(sel.split(" - ")[0]);
		});

		return p;
	}

	private JPanel criarPanelMatricula() {
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		p.add(new JLabel("Data Matrícula:"), gbc(gbc, 0, 0, 1));
		spnDataMatricula = createDateSpinner();
		p.add(spnDataMatricula, gbc(gbc, 1, 0, 1));

		p.add(new JLabel("Status:"), gbc(gbc, 2, 0, 1));
		cmbStatus = new JComboBox<>(new String[] { "Ativo", "Inativo", "Aguardando", "Trancado", "Cancelado" });
		p.add(cmbStatus, gbc(gbc, 3, 0, 1));

		p.add(new JLabel("É Isento?"), gbc(gbc, 4, 0, 1));
		chkIsento = new JCheckBox();
		p.add(chkIsento, gbc(gbc, 5, 0, 1));

		p.add(new JLabel("Motivo Isenção:"), gbc(gbc, 6, 0, 1));
		cmbMotivoIsencao = new JComboBox<>(new String[] { "", "Idoso", "Programa Social", "Deficiente" });
		p.add(cmbMotivoIsencao, gbc(gbc, 7, 0, 1));

		// Listener para habilitar campos de cancelamento
		cmbStatus.addActionListener(e -> {
			boolean cancelado = "Cancelado".equals(cmbStatus.getSelectedItem());
			// Controller deve lidar com isso via listener externo ou método público
		});

		return p;
	}

	private JPanel criarPanelEmergencia() {
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		p.add(new JLabel("Nome:"), gbc(gbc, 0, 0, 1));
		txtNomeEmergencia = new JTextField(25);
		p.add(txtNomeEmergencia, gbc(gbc, 1, 0, 2));

		p.add(new JLabel("Telefone:"), gbc(gbc, 3, 0, 1));
		txtTelEmergencia = createMaskField("(##) #####-####");
		txtTelEmergencia.setColumns(14);
		p.add(txtTelEmergencia, gbc(gbc, 4, 0, 1));

		p.add(new JLabel("Parentesco:"), gbc(gbc, 5, 0, 1));
		cmbParentesco = new JComboBox<>(
				new String[] { "", "Pai", "Mãe", "Esposa(o)", "Avó", "Avô", "Tio(a)", "Outro" });
		p.add(cmbParentesco, gbc(gbc, 6, 0, 1));

		return p;
	}

	private JPanel criarPanelSaude() {
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		chkRestricao = new JCheckBox("Possui restrição médica?");
		chkRestricao.addActionListener(e -> txtRestricao.setEnabled(chkRestricao.isSelected()));
		p.add(chkRestricao, gbc(gbc, 0, 0, 2));

		txtRestricao = new JTextArea(2, 25);
		txtRestricao.setLineWrap(true);
		txtRestricao.setWrapStyleWord(true);
		txtRestricao.setEnabled(false);
		p.add(new JScrollPane(txtRestricao), gbc(gbc, 2, 0, 3));

		p.add(new JLabel("Medicamentos Contínuos:"), gbc(gbc, 5, 0, 1));
		txtMedicamentos = new JTextArea(2, 20);
		txtMedicamentos.setLineWrap(true);
		txtMedicamentos.setWrapStyleWord(true);
		p.add(new JScrollPane(txtMedicamentos), gbc(gbc, 6, 0, 2));

		p.add(new JLabel("Alergias:"), gbc(gbc, 0, 1, 8));
		txtAlergias = new JTextArea(2, 80);
		txtAlergias.setLineWrap(true);
		txtAlergias.setWrapStyleWord(true);
		p.add(new JScrollPane(txtAlergias), gbc(gbc, 0, 2, 8));

		return p;
	}

	private JPanel criarPanelAutorizacoes() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));

		chkImg = new JCheckBox("Autorizo uso de imagem");
		chkDivulg = new JCheckBox("Autorizo divulgação");
		chkTermos = new JCheckBox("Aceite dos termos");
		chkTermos.addActionListener(e -> spnDataAceite.setEnabled(chkTermos.isSelected()));

		p.add(chkImg);
		p.add(chkDivulg);
		p.add(chkTermos);
		p.add(new JLabel("Data Aceite:"));
		spnDataAceite = createDateSpinner();
		spnDataAceite.setEnabled(false);
		p.add(spnDataAceite);

		return p;
	}

	private JPanel criarPanelObsEBotoes() {
		JPanel p = new JPanel(new BorderLayout(10, 5));

		p.add(new JLabel("Observações Gerais:"), BorderLayout.NORTH);
		txtObs = new JTextArea(4, 60);
		txtObs.setLineWrap(true);
		txtObs.setWrapStyleWord(true);
		p.add(new JScrollPane(txtObs), BorderLayout.CENTER);

		JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		btnSalvar = new JButton("💾 Salvar");
		btnLimpar = new JButton("🧹 Limpar");
		btnExcluir = new JButton("🗑️ Excluir");
		btnCancelar = new JButton("❌ Cancelar");

		Dimension btnSize = new Dimension(110, 32);
		for (JButton b : new JButton[] { btnSalvar, btnLimpar, btnExcluir, btnCancelar }) {
			b.setPreferredSize(btnSize);
			b.setFocusPainted(false);
		}
		btnExcluir.setBackground(new Color(220, 53, 69));
		btnExcluir.setForeground(Color.WHITE);

		btns.add(btnSalvar);
		btns.add(btnLimpar);
		btns.add(btnExcluir);
		btns.add(btnCancelar);
		p.add(btns, BorderLayout.SOUTH);

		return p;
	}

	// ====== MÉTODOS PARA CEP (CACHE + PREENCHIMENTO) ======
	public void popularComboCeps(List<Cep> lista) {
		cacheCeps.clear();
		cmbCep.removeAllItems();
		// cmbCep.addItem("🔍 Selecione ou digite para filtrar...");
		cmbCep.addItem("📍 Selecione um CEP cadastrado...");
		cmbCep.setToolTipText("Selecione um CEP da lista para preencher o endereço automaticamente");
		for (Cep c : lista) {
			String display = c.getId_cep() + " - " + c.getLogradouro();
			cmbCep.addItem(display);
			cacheCeps.put(c.getId_cep(), c);
		}
	}

	private void preencherCamposEndereco(String idCep) {
		Cep cep = cacheCeps.get(idCep);
		if (cep != null) {
			setLogradouro(cep.getLogradouro());
			setBairro(cep.getBairro());
			setCidade(cep.getCidade());
			setEstado(cep.getEstado());
			setCepObs(cep.getObservacoes());
		}
	}

	public String getCepSelecionadoId() {
		String item = (String) cmbCep.getSelectedItem();
		if (item == null || item.isEmpty())
			return null;
		if (item.contains(" - "))
			return item.split(" - ")[0];
		return item.trim().replaceAll("\\D", "");
	}

	// ====== EXPOSIÇÃO AO CONTROLLER ======
	public void addSalvar(ActionListener l) {
		btnSalvar.addActionListener(l);
	}

	public void addLimpar(ActionListener l) {
		btnLimpar.addActionListener(l);
	}

	public void addExcluir(ActionListener l) {
		btnExcluir.addActionListener(l);
	}

	public void addCancelar(ActionListener l) {
		btnCancelar.addActionListener(l);
	}

	public void addTabela(MouseListener l) {
		tblAlunos.addMouseListener(l);
	}

	public void atualizarTabela(Object[][] d) {
		tableModel.setRowCount(0);
		if (d != null)
			for (Object[] r : d)
				tableModel.addRow(r);
	}

	public int getLinha() {
		return tblAlunos.getSelectedRow();
	}

	public Object getVal(int r, int c) {
		return tableModel.getValueAt(r, c);
	}

	public void refreshTable() {
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

	// ====== GETTERS ======
	public String getNome() {
		return txtNome.getText().trim();
	}

	public String getCpf() {
		return txtCPF.getText().trim();
	}

	public String getRg() {
		return txtRG.getText().trim();
	}

	public LocalDate getDataNasc() {
		return parseDate(txtDataNasc.getText());
	}

	public String getSexo() {
		return (String) cmbSexo.getSelectedItem();
	}

	public String getEmail() {
		return txtEmail.getText().trim();
	}

	public String getWhatsApp() {
		return txtWhatsApp.getText().trim();
	}

	public String getLogradouro() {
		return txtLogradouro.getText().trim();
	}

	public String getNumero() {
		return txtNumero.getText().trim();
	}

	public String getComplemento() {
		return txtComplemento.getText().trim();
	}

	public String getBairro() {
		return txtBairro.getText().trim();
	}

	public String getCidade() {
		return txtCidade.getText().trim();
	}

	// public String getEstado() { return txtEstado.getText().trim() ; }
	public String getEstado() {
		return txtEstado != null ? txtEstado.getText().trim().toUpperCase() : "";
	}

	public String getCepObs() {
		return txtCepObs.getText().trim();
	}

	public LocalDate getDataMatricula() {
		return getLocalDate(spnDataMatricula);
	}

	public String getStatus() {
		return (String) cmbStatus.getSelectedItem();
	}

	public Boolean getIsento() {
		return chkIsento.isSelected();
	}

	public String getMotivoIsencao() {
		return (String) cmbMotivoIsencao.getSelectedItem();
	}

	public String getNomeEmergencia() {
		return txtNomeEmergencia.getText().trim();
	}

	public String getTelEmergencia() {
		return txtTelEmergencia.getText().trim();
	}

	public String getParentesco() {
		return (String) cmbParentesco.getSelectedItem();
	}

	public Boolean getRestricao() {
		return chkRestricao.isSelected();
	}

	public String getDescricaoRestricao() {
		return txtRestricao.getText();
	}

	public String getMedicamentos() {
		return txtMedicamentos.getText();
	}

	public String getAlergias() {
		return txtAlergias.getText();
	}

	public Boolean getImg() {
		return chkImg.isSelected();
	}

	public Boolean getDivulg() {
		return chkDivulg.isSelected();
	}

	public Boolean getAceite() {
		return chkTermos.isSelected();
	}

	public LocalDate getDataAceite() {
		return getLocalDate(spnDataAceite);
	}

	public String getObs() {
		return txtObs.getText();
	}

	// ====== SETTERS ======
	public void setNome(String v) {
		txtNome.setText(v);
	}

	public void setCpf(String v) {
		txtCPF.setText(v);
	}

	public void setRg(String v) {
		txtRG.setText(v);
	}

	public void setDataNasc(LocalDate d) {
		txtDataNasc.setText(d != null ? formatDate(d) : "");
	}

	public void setSexo(String v) {
		cmbSexo.setSelectedItem(v);
	}

	public void setEmail(String v) {
		txtEmail.setText(v);
	}

	public void setWhatsApp(String v) {
		txtWhatsApp.setText(v);
	}

	public void setLogradouro(String v) {
		txtLogradouro.setText(v);
	}

	public void setNumero(String v) {
		txtNumero.setText(v);
	}

	public void setComplemento(String v) {
		txtComplemento.setText(v);
	}

	public void setBairro(String v) {
		txtBairro.setText(v);
	}

	public void setCidade(String v) {
		txtCidade.setText(v);
	}

	// public void setEstado(String v) { txtEstado.setText(v); }
	public void setEstado(String v) {
		if (txtEstado != null) {
			txtEstado.setText(v != null ? v.toUpperCase() : "");
		}
	}

	public void setCepObs(String v) {
		txtCepObs.setText(v);
	}

	public void setDataMatricula(LocalDate d) {
		setLocalDate(spnDataMatricula, d);
	}

	public void setStatus(String v) {
		cmbStatus.setSelectedItem(v);
	}

	public void setIsento(Boolean v) {
		chkIsento.setSelected(v != null && v);
	}

	public void setMotivoIsencao(String v) {
		cmbMotivoIsencao.setSelectedItem(v);
	}

	public void setNomeEmergencia(String v) {
		txtNomeEmergencia.setText(v);
	}

	public void setTelEmergencia(String v) {
		txtTelEmergencia.setText(v);
	}

	public void setParentesco(String v) {
		cmbParentesco.setSelectedItem(v);
	}

	public void setRestricao(Boolean v) {
		chkRestricao.setSelected(v != null && v);
		txtRestricao.setEnabled(v != null && v);
	}

	public void setDescricaoRestricao(String v) {
		txtRestricao.setText(v);
	}

	public void setMedicamentos(String v) {
		txtMedicamentos.setText(v);
	}

	public void setAlergias(String v) {
		txtAlergias.setText(v);
	}

	public void setImg(Boolean v) {
		chkImg.setSelected(v != null && v);
	}

	public void setDivulg(Boolean v) {
		chkDivulg.setSelected(v != null && v);
	}

	public void setAceite(Boolean v) {
		chkTermos.setSelected(v != null && v);
		spnDataAceite.setEnabled(v != null && v);
	}

	public void setDataAceite(LocalDate d) {
		setLocalDate(spnDataAceite, d);
	}

	/*
	 * public void setDataCancelamento(LocalDate d) {
	 * setLocalDate(spnDataCancelamento, d); }
	 * 
	 * public void setMotivoCancelamento(String v) {
	 * txtMotivoCancelamento.setText(v); }
	 */
	public void setObs(String v) {
		txtObs.setText(v);
	}

	// ====== LIMPAR FORMULÁRIO ======
	public void limparFormulario() {
		// Dados Pessoais
		txtNome.setText("");
		txtCPF.setText("");
		txtRG.setText("");
		txtDataNasc.setText(""); // ✅ Limpar campo de data formatado
		cmbSexo.setSelectedIndex(0);
		txtEmail.setText("");
		txtWhatsApp.setText("");

		// Endereço
		cmbCep.setSelectedIndex(0);
		txtLogradouro.setText("");
		txtNumero.setText("");
		txtComplemento.setText("");
		txtBairro.setText("");
		txtCidade.setText("");
		txtEstado.setText("");
		txtCepObs.setText("");

		// Matrícula & Status
		setDataMatricula(LocalDate.now());
		cmbStatus.setSelectedIndex(0);
		chkIsento.setSelected(false);
		cmbMotivoIsencao.setSelectedIndex(0);

		// Emergência
		txtNomeEmergencia.setText("");
		txtTelEmergencia.setText("");
		cmbParentesco.setSelectedIndex(0);

		// Saúde
		chkRestricao.setSelected(false);
		txtRestricao.setText("");
		txtRestricao.setEnabled(false);
		txtMedicamentos.setText("");
		txtAlergias.setText("");

		// Autorizações & Termos
		chkImg.setSelected(false);
		chkDivulg.setSelected(false);
		chkTermos.setSelected(false);
		spnDataAceite.setEnabled(false);
		setDataAceite(null);

		// Observações & Grid
		txtObs.setText("");
		tblAlunos.clearSelection();
		txtNome.requestFocusInWindow();
	}

	// Método para converter String formatada para LocalDate
	private LocalDate parseDate(String dateStr) {
		if (dateStr == null || dateStr.trim().isEmpty() || dateStr.equals("__/__/____")) {
			return null;
		}
		try {
			// Remover caracteres não numéricos e validar formato
			String cleanDate = dateStr.replaceAll("[^0-9]", "");
			if (cleanDate.length() == 8) {
				String formattedDate = cleanDate.substring(0, 2) + "/" + cleanDate.substring(2, 4) + "/"
						+ cleanDate.substring(4, 8);
				return LocalDate.parse(formattedDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			}
		} catch (Exception e) {
			// Data inválida
			return null;
		}
		return null;
	}

	// Método para formatar LocalDate para String
	private String formatDate(LocalDate date) {
		if (date == null) {
			return "";
		}
		return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}