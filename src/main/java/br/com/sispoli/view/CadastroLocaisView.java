package br.com.sispoli.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class CadastroLocaisView extends JFrame {
    private JTextField txtNome;
    private JSpinner spnCapacidade;
    private JComboBox<String> cmbStatus;
    private JTextArea txtObservacoes;
    private JTable tblLocais;
    private DefaultTableModel tableModel;
    private JButton btnSalvar, btnLimpar, btnExcluir, btnCancelar;

    public CadastroLocaisView() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Cadastro de Locais");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(15, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Nome
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        formPanel.add(new JLabel("Nome do Local:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        txtNome = new JTextField(25);
        formPanel.add(txtNome, gbc);

        // 2. Capacidade
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2;
        formPanel.add(new JLabel("Capacidade Máxima:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 10000, 1);
        spnCapacidade = new JSpinner(model);
        spnCapacidade.setEditor(new JSpinner.NumberEditor(spnCapacidade, "#"));
        formPanel.add(spnCapacidade, gbc);

        // 3. Status
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.2;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        cmbStatus = new JComboBox<>(new String[]{"Ativo", "Em Manutenção", "Inativo"});
        formPanel.add(cmbStatus, gbc);

        // 4. Observações
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.NORTHWEST; gbc.weighty = 0.3;
        formPanel.add(new JLabel("Observações:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.BOTH;
        txtObservacoes = new JTextArea(4, 25);
        txtObservacoes.setLineWrap(true); txtObservacoes.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(txtObservacoes), gbc);

        // 5. Botões (CORRIGIDO: GridBagConstraints resetado)
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0; gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnSalvar = new JButton("💾 Salvar");
        btnLimpar = new JButton("🧹 Limpar");
        btnExcluir = new JButton("🗑️ Excluir");
        btnCancelar = new JButton("❌ Cancelar");
        Dimension btnSize = new Dimension(105, 32);
        for (JButton b : new JButton[]{btnSalvar, btnLimpar, btnExcluir, btnCancelar}) {
            b.setPreferredSize(btnSize); b.setFocusPainted(false);
        }
        btnExcluir.setBackground(new Color(220, 53, 69)); btnExcluir.setForeground(Color.WHITE);
        btnPanel.add(btnSalvar); btnPanel.add(btnLimpar); btnPanel.add(btnExcluir); btnPanel.add(btnCancelar);
        formPanel.add(btnPanel, gbc);

        // Grid
        JPanel gridPanel = new JPanel(new BorderLayout(10, 10));
        gridPanel.setBorder(new EmptyBorder(0, 20, 15, 20));
        gridPanel.add(new JLabel("📋 Locais Cadastrados", SwingConstants.LEFT), BorderLayout.NORTH);
        String[] colunas = {"ID", "Nome", "Capacidade", "Status", "Observações"};
        tableModel = new DefaultTableModel(colunas, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tblLocais = new JTable(tableModel);
        tblLocais.setRowHeight(25); tblLocais.setAutoCreateRowSorter(true);
        JScrollPane scroll = new JScrollPane(tblLocais);
        gridPanel.add(scroll, BorderLayout.CENTER);
        JButton btnAtualizar = new JButton("🔄 Atualizar Lista");
        gridPanel.add(btnAtualizar, BorderLayout.SOUTH);

        add(formPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);

        btnAtualizar.addActionListener(e -> refreshTable());
        txtNome.requestFocusInWindow();
    }

    // === MÉTODOS EXPOSTOS AO CONTROLLER ===
    public String getNome() { return txtNome.getText().trim(); }
    public void setNome(String v) { txtNome.setText(v); }
    public int getCapacidade() { return (Integer) spnCapacidade.getValue(); }
    public void setCapacidade(int v) { spnCapacidade.setValue(v); }
    public String getStatus() { return (String) cmbStatus.getSelectedItem(); }
    public void setStatus(String v) { cmbStatus.setSelectedItem(v); }
    public String getObservacoes() { return txtObservacoes.getText().trim(); }
    public void setObservacoes(String v) { txtObservacoes.setText(v); }

    public int getLinhaSelecionada() { return tblLocais.getSelectedRow(); }
    public Object getValorTabela(int linha, int coluna) { return tableModel.getValueAt(linha, coluna); }

    public void adicionarListenerSalvar(ActionListener l) { btnSalvar.addActionListener(l); }
    public void adicionarListenerExcluir(ActionListener l) { btnExcluir.addActionListener(l); }
    public void adicionarListenerLimpar(ActionListener l) { btnLimpar.addActionListener(l); }
    public void adicionarListenerCancelar(ActionListener l) { btnCancelar.addActionListener(l); }
    public void adicionarListenerTabela(MouseListener l) { tblLocais.addMouseListener(l); }

    public void atualizarTabela(Object[][] dados) {
        tableModel.setRowCount(0);
        for (Object[] linha : dados) tableModel.addRow(linha);
    }

    public void limparFormulario() {
        txtNome.setText("");
        spnCapacidade.setValue(1);
        cmbStatus.setSelectedIndex(0);
        txtObservacoes.setText("");
        tblLocais.clearSelection();
        txtNome.requestFocusInWindow();
    }

    public void mostrarInfo(String msg) { JOptionPane.showMessageDialog(this, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE); }
    public void mostrarErro(String msg) { JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE); }
    public int confirmar(String msg) { return JOptionPane.showConfirmDialog(this, msg, "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); }
    public void refreshTable() {} // Delegado ao Controller
}