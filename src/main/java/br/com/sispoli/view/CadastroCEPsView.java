package br.com.sispoli.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;

public class CadastroCEPsView extends JFrame {
    private JTextField txtCEP, txtLogradouro, txtBairro, txtCidade;
    private JComboBox<String> cmbEstado;
    private JTextArea txtObservacoes;
    private JTable tblCEPs;
    private DefaultTableModel tableModel;
    private JButton btnSalvar, btnLimpar, btnExcluir, btnCancelar;

    public CadastroCEPsView() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Cadastro de CEPs");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(15, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos (mesma lógica visual anterior)
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        formPanel.add(new JLabel("CEP:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        txtCEP = new JTextField(15);
        txtCEP.setDocument(new PlainDocument() {
            public void insertString(int o, String s, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (getLength() + s.replaceAll("[^0-9]", "").length() <= 8) super.insertString(o, s.replaceAll("[^0-9]", ""), a);
            }
        });
        formPanel.add(txtCEP, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Logradouro:"), gbc);
        gbc.gridx = 1; txtLogradouro = new JTextField(30); formPanel.add(txtLogradouro, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Bairro:"), gbc);
        gbc.gridx = 1; txtBairro = new JTextField(25); formPanel.add(txtBairro, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Cidade:"), gbc);
        gbc.gridx = 1; txtCidade = new JTextField(20); formPanel.add(txtCidade, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 3; cmbEstado = new JComboBox<>(new String[]{"AC","AL","AP","AM","BA","CE","DF","ES","GO","MA","MT","MS","MG","PA","PB","PR","PE","PI","RJ","RN","RS","RO","RR","SC","SP","SE","TO"});
        formPanel.add(cmbEstado, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weighty = 0.3; gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Observações:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.BOTH;
        txtObservacoes = new JTextArea(3, 25);
        txtObservacoes.setLineWrap(true); txtObservacoes.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(txtObservacoes), gbc);

        // Botões (Constraints resetadas)
        gbc = new GridBagConstraints(); gbc.insets = new Insets(15, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.CENTER;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnSalvar = new JButton("💾 Salvar"); btnLimpar = new JButton("🧹 Limpar");
        btnExcluir = new JButton("🗑️ Excluir"); btnCancelar = new JButton("❌ Cancelar");
        Dimension btnSize = new Dimension(105, 32);
        for(JButton b : new JButton[]{btnSalvar, btnLimpar, btnExcluir, btnCancelar}) { b.setPreferredSize(btnSize); b.setFocusPainted(false); }
        btnExcluir.setBackground(new Color(220, 53, 69)); btnExcluir.setForeground(Color.WHITE);
        btnPanel.add(btnSalvar); btnPanel.add(btnLimpar); btnPanel.add(btnExcluir); btnPanel.add(btnCancelar);
        formPanel.add(btnPanel, gbc);

        // Grid
        JPanel gridPanel = new JPanel(new BorderLayout(10, 10));
        gridPanel.setBorder(new EmptyBorder(0, 20, 15, 20));
        gridPanel.add(new JLabel("📋 CEPs Cadastrados", SwingConstants.LEFT), BorderLayout.NORTH);
        String[] colunas = {"CEP", "Logradouro", "Bairro", "Cidade", "Estado", "Observações"};
        tableModel = new DefaultTableModel(colunas, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tblCEPs = new JTable(tableModel); tblCEPs.setRowHeight(25); tblCEPs.setAutoCreateRowSorter(true);
        JScrollPane scroll = new JScrollPane(tblCEPs);
        gridPanel.add(scroll, BorderLayout.CENTER);
        JButton btnAtualizar = new JButton("🔄 Atualizar Lista");
        gridPanel.add(btnAtualizar, BorderLayout.SOUTH);

        add(formPanel, BorderLayout.NORTH); add(gridPanel, BorderLayout.CENTER);

        // Listeners internos (delegação para Controller)
        btnAtualizar.addActionListener(e -> refreshTable());
        tblCEPs.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { if(e.getClickCount()==2) onDoubleClickLinha(); }
        });
        txtCEP.requestFocusInWindow();
    }

    // === MÉTODOS EXPOSTOS AO CONTROLLER ===
    public String getTxtCEP() { return txtCEP.getText().trim(); }
    public void setTxtCEP(String v) { txtCEP.setText(v); }
    public String getTxtLogradouro() { return txtLogradouro.getText().trim(); }
    public void setTxtLogradouro(String v) { txtLogradouro.setText(v); }
    public String getTxtBairro() { return txtBairro.getText().trim(); }
    public void setTxtBairro(String v) { txtBairro.setText(v); }
    public String getTxtCidade() { return txtCidade.getText().trim(); }
    public void setTxtCidade(String v) { txtCidade.setText(v); }
    public String getEstadoSelecionado() { return (String) cmbEstado.getSelectedItem(); }
    public void setEstadoSelecionado(String v) { cmbEstado.setSelectedItem(v); }
    public String getTxtObservacoes() { return txtObservacoes.getText().trim(); }
    public void setTxtObservacoes(String v) { txtObservacoes.setText(v); }

    public int getLinhaSelecionada() { return tblCEPs.getSelectedRow(); }
    public Object getValorTabela(int linha, int coluna) { return tableModel.getValueAt(linha, coluna); }

    public void adicionarListenerSalvar(ActionListener l) { btnSalvar.addActionListener(l); }
    public void adicionarListenerExcluir(ActionListener l) { btnExcluir.addActionListener(l); }
    public void adicionarListenerLimpar(ActionListener l) { btnLimpar.addActionListener(l); }
    public void adicionarListenerCancelar(ActionListener l) { btnCancelar.addActionListener(l); }

    public void atualizarTabela(Object[][] dados) {
        tableModel.setRowCount(0);
        for (Object[] linha : dados) tableModel.addRow(linha);
    }

    public void limparFormulario() {
        txtCEP.setText(""); txtLogradouro.setText(""); txtBairro.setText("");
        txtCidade.setText(""); cmbEstado.setSelectedIndex(0); txtObservacoes.setText("");
        tblCEPs.clearSelection(); txtCEP.requestFocusInWindow();
    }

    public void mostrarInfo(String msg) { JOptionPane.showMessageDialog(this, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE); }
    public void mostrarErro(String msg) { JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE); }
    public int confirmar(String msg) { return JOptionPane.showConfirmDialog(this, msg, "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); }
    
    // Callback interno para a View acionar o Controller no duplo clique
    private void onDoubleClickLinha() {
        if(tblCEPs.getSelectedRow() >= 0) {
            // Dispara evento customizado ou chama método público se o Controller registrar
            // Aqui mantemos simples: o Controller pode registrar via getter da tabela
        }
    }
    
    public void refreshTable() {
        // Delegado ao Controller
    }
    
    // Hook para o Controller registrar listener na tabela
    public void adicionarListenerTabela(MouseListener l) { tblCEPs.addMouseListener(l); }
}