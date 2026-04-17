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
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class CadastroResponsaveisView extends JFrame {
    // Campos do formulário
    private JComboBox<String> cmbAluno;
    private JTextField txtNome, txtCpf, txtRg, txtEmail, txtTelefone, txtParentesco;
    
    // Grid e botões
    private JTable tblResponsaveis;
    private DefaultTableModel tableModel;
    private JButton btnSalvar, btnLimpar, btnExcluir, btnCancelar;

    public CadastroResponsaveisView() { inicializarComponentes(); }

    private void inicializarComponentes() {
        setTitle("Cadastro de Responsáveis");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // === FORMULÁRIO ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(15, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 1: Aluno (Combo)
        gbc.gridx=0; gbc.gridy=0; gbc.weightx=0.20; formPanel.add(new JLabel("Aluno:"), gbc);
        gbc.gridx=1; gbc.weightx=0.80;
        cmbAluno = new JComboBox<>(); cmbAluno.addItem("Selecione um aluno...");
        formPanel.add(cmbAluno, gbc);

        // Linha 2: Nome Completo
        gbc.gridx=0; gbc.gridy=1; gbc.weightx=0.20; formPanel.add(new JLabel("Nome Responsável:"), gbc);
        gbc.gridx=1; gbc.weightx=0.80;
        txtNome = new JTextField(30); formPanel.add(txtNome, gbc);

        // Linha 3: CPF e RG
        gbc.gridx=0; gbc.gridy=2; gbc.weightx=0.20; formPanel.add(new JLabel("CPF:"), gbc);
        gbc.gridx=1; gbc.weightx=0.35;
        txtCpf = new JTextField(14); formPanel.add(txtCpf, gbc);
        gbc.gridx=2; gbc.weightx=0.20; formPanel.add(new JLabel("RG:"), gbc);
        gbc.gridx=3; gbc.weightx=0.45;
        txtRg = new JTextField(20); formPanel.add(txtRg, gbc);

        // Linha 4: Email e Telefone
        gbc.gridx=0; gbc.gridy=3; gbc.weightx=0.20; formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx=1; gbc.weightx=0.35;
        txtEmail = new JTextField(25); formPanel.add(txtEmail, gbc);
        gbc.gridx=2; gbc.weightx=0.20; formPanel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx=3; gbc.weightx=0.45;
        txtTelefone = new JTextField(20); formPanel.add(txtTelefone, gbc);

        // Linha 5: Parentesco
        gbc.gridx=0; gbc.gridy=4; gbc.weightx=0.20; formPanel.add(new JLabel("Parentesco:"), gbc);
        gbc.gridx=1; gbc.weightx=0.80; gbc.gridwidth=3;
        txtParentesco = new JTextField(30); 
        txtParentesco.setToolTipText("Ex: Pai, Mãe, Avó, Tutor Legal");
        formPanel.add(txtParentesco, gbc); gbc.gridwidth=1;

        // ✅ BOTÕES (GridBagConstraints RESETADO - padrão das telas anteriores)
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0; gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnSalvar = new JButton("💾 Salvar"); btnLimpar = new JButton("🧹 Limpar");
        btnExcluir = new JButton("🗑️ Excluir"); btnCancelar = new JButton("❌ Cancelar");
        Dimension btnSize = new Dimension(105, 32);
        for (JButton b : new JButton[]{btnSalvar, btnLimpar, btnExcluir, btnCancelar}) {
            b.setPreferredSize(btnSize); b.setFocusPainted(false);
        }
        btnExcluir.setBackground(new Color(220, 53, 69)); btnExcluir.setForeground(Color.WHITE);
        btnPanel.add(btnSalvar); btnPanel.add(btnLimpar); btnPanel.add(btnExcluir); btnPanel.add(btnCancelar);
        formPanel.add(btnPanel, gbc);

        // === GRID ===
        JPanel gridPanel = new JPanel(new BorderLayout(10, 10));
        gridPanel.setBorder(new EmptyBorder(0, 20, 15, 20));
        gridPanel.add(new JLabel("📋 Responsáveis Cadastrados", SwingConstants.LEFT), BorderLayout.NORTH);
        String[] colunas = {"ID", "Aluno", "Nome Responsável", "CPF", "Telefone", "Parentesco"};
        tableModel = new DefaultTableModel(colunas, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tblResponsaveis = new JTable(tableModel);
        tblResponsaveis.setRowHeight(25); tblResponsaveis.setAutoCreateRowSorter(true);
        JScrollPane scroll = new JScrollPane(tblResponsaveis);
        gridPanel.add(scroll, BorderLayout.CENTER);
        JButton btnAtualizar = new JButton("🔄 Atualizar");
        gridPanel.add(btnAtualizar, BorderLayout.SOUTH);

        // Montagem
        add(formPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);

        // Listeners
        btnAtualizar.addActionListener(e -> refreshTable());
        txtNome.requestFocusInWindow();
    }

    // === MÉTODOS EXPOSTOS AO CONTROLLER (Padrão Simplificado) ===
    public void popularComboAlunos(List<String> alunos) {
        cmbAluno.removeAllItems(); cmbAluno.addItem("Selecione um aluno...");
        for(String a : alunos) cmbAluno.addItem(a);
    }

    public int getIdAlunoSelecionado() {
        if(cmbAluno.getSelectedIndex() <= 0) return 0;
        try { return Integer.parseInt(cmbAluno.getSelectedItem().toString().split(" - ")[0]); } catch(Exception e) { return 0; }
    }
    public void setSelectedAluno(int id) {
        for(int i=0; i<cmbAluno.getItemCount(); i++) {
            String item = cmbAluno.getItemAt(i);
            if(item != null && item.startsWith(id + " - ")) { cmbAluno.setSelectedIndex(i); break; }
        }
    }

    // Getters simples
    public String getNomeCompleto() { return txtNome.getText().trim(); }
    public void setNomeCompleto(String v) { txtNome.setText(v); }
    public String getCpf() { return txtCpf.getText().trim().replaceAll("[^0-9]", ""); }
    public void setCpf(String v) { txtCpf.setText(v != null ? v : ""); }
    public String getRg() { return txtRg.getText().trim(); }
    public void setRg(String v) { txtRg.setText(v != null ? v : ""); }
    public String getEmail() { return txtEmail.getText().trim(); }
    public void setEmail(String v) { txtEmail.setText(v != null ? v : ""); }
    public String getTelefone1() { return txtTelefone.getText().trim().replaceAll("[^0-9]", ""); }
    public void setTelefone1(String v) { txtTelefone.setText(v != null ? v : ""); }
    public String getParentesco() { return txtParentesco.getText().trim(); }
    public void setParentesco(String v) { txtParentesco.setText(v != null ? v : ""); }

    // Tabela e listeners (padrão)
    public int getLinhaSelecionada() { return tblResponsaveis.getSelectedRow(); }
    public Object getValorTabela(int l, int c) { return tableModel.getValueAt(l, c); }
    public void adicionarListenerSalvar(ActionListener l) { btnSalvar.addActionListener(l); }
    public void adicionarListenerExcluir(ActionListener l) { btnExcluir.addActionListener(l); }
    public void adicionarListenerLimpar(ActionListener l) { btnLimpar.addActionListener(l); }
    public void adicionarListenerCancelar(ActionListener l) { btnCancelar.addActionListener(l); }
    public void adicionarListenerTabela(MouseListener l) { tblResponsaveis.addMouseListener(l); }
    public void atualizarTabela(Object[][] dados) { tableModel.setRowCount(0); for(Object[] r:dados) tableModel.addRow(r); }
    
    public void limparFormulario() {
        cmbAluno.setSelectedIndex(0); txtNome.setText(""); txtCpf.setText(""); txtRg.setText("");
        txtEmail.setText(""); txtTelefone.setText(""); txtParentesco.setText("");
        tblResponsaveis.clearSelection(); txtNome.requestFocusInWindow();
    }
    
    public void mostrarInfo(String m) { JOptionPane.showMessageDialog(this, m, "Sucesso", JOptionPane.INFORMATION_MESSAGE); }
    public void mostrarErro(String m) { JOptionPane.showMessageDialog(this, m, "Erro", JOptionPane.ERROR_MESSAGE); }
    public int confirmar(String m) { return JOptionPane.showConfirmDialog(this, m, "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); }
    public void refreshTable() {}
}