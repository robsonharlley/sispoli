package br.com.sispoli.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CadastroLotacaoView extends JFrame {
    private JComboBox<String> cmbTurma, cmbProfessor;
    private JTable tblLotacoes;
    private DefaultTableModel tableModel;
    private JButton btnSalvar, btnLimpar, btnExcluir, btnCancelar;

    public CadastroLotacaoView() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Cadastro de Lotação de Professores");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(15, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Turma
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        formPanel.add(new JLabel("Turma:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        cmbTurma = new JComboBox<>();
        cmbTurma.addItem("Selecione uma turma...");
        formPanel.add(cmbTurma, gbc);

        // 2. Professor
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2;
        formPanel.add(new JLabel("Professor:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        cmbProfessor = new JComboBox<>();
        cmbProfessor.addItem("Selecione um professor...");
        formPanel.add(cmbProfessor, gbc);

        // ✅ Botões (GridBagConstraints resetado)
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 2;
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
        gridPanel.add(new JLabel("📋 Lotações Ativas", SwingConstants.LEFT), BorderLayout.NORTH);
        String[] colunas = {"ID", "Turma", "Professor"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblLotacoes = new JTable(tableModel);
        tblLotacoes.setRowHeight(25);
        tblLotacoes.setAutoCreateRowSorter(true);
        JScrollPane scroll = new JScrollPane(tblLotacoes);
        gridPanel.add(scroll, BorderLayout.CENTER);
        JButton btnAtualizar = new JButton("🔄 Atualizar Lista");
        gridPanel.add(btnAtualizar, BorderLayout.SOUTH);

        add(formPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);

        btnAtualizar.addActionListener(e -> refreshTable());
        cmbTurma.requestFocusInWindow();
    }

    // === MÉTODOS EXPOSTOS AO CONTROLLER ===
    public void popularComboTurmas(List<String> turmas) {
        cmbTurma.removeAllItems();
        cmbTurma.addItem("Selecione uma turma...");
        for (String t : turmas) cmbTurma.addItem(t);
    }
    public void popularComboProfessores(List<String> profs) {
        cmbProfessor.removeAllItems();
        cmbProfessor.addItem("Selecione um professor...");
        for (String p : profs) cmbProfessor.addItem(p);
    }

    public int getIdTurmaSelecionado() {
        if (cmbTurma.getSelectedIndex() <= 0) return 0;
        return Integer.parseInt(cmbTurma.getSelectedItem().toString().split(" - ")[0]);
    }
    public int getIdProfessorSelecionado() {
        if (cmbProfessor.getSelectedIndex() <= 0) return 0;
        return Integer.parseInt(cmbProfessor.getSelectedItem().toString().split(" - ")[0]);
    }
    public void setSelecaoTurma(int idTurma) {
        for (int i = 0; i < cmbTurma.getItemCount(); i++) {
            String item = cmbTurma.getItemAt(i);
            if (item.startsWith(idTurma + " - ")) { cmbTurma.setSelectedIndex(i); break; }
        }
    }
    public void setSelecaoProfessor(int idProfessor) {
        for (int i = 0; i < cmbProfessor.getItemCount(); i++) {
            String item = cmbProfessor.getItemAt(i);
            if (item.startsWith(idProfessor + " - ")) { cmbProfessor.setSelectedIndex(i); break; }
        }
    }

    public int getLinhaSelecionada() { return tblLotacoes.getSelectedRow(); }
    public Object getValorTabela(int linha, int coluna) { return tableModel.getValueAt(linha, coluna); }

    public void adicionarListenerSalvar(ActionListener l) { btnSalvar.addActionListener(l); }
    public void adicionarListenerExcluir(ActionListener l) { btnExcluir.addActionListener(l); }
    public void adicionarListenerLimpar(ActionListener l) { btnLimpar.addActionListener(l); }
    public void adicionarListenerCancelar(ActionListener l) { btnCancelar.addActionListener(l); }
    public void adicionarListenerTabela(MouseListener l) { tblLotacoes.addMouseListener(l); }

    public void atualizarTabela(Object[][] dados) {
        tableModel.setRowCount(0);
        for (Object[] linha : dados) tableModel.addRow(linha);
    }

    public void limparFormulario() {
        cmbTurma.setSelectedIndex(0);
        cmbProfessor.setSelectedIndex(0);
        tblLotacoes.clearSelection();
        cmbTurma.requestFocusInWindow();
    }

    public void mostrarInfo(String msg) { JOptionPane.showMessageDialog(this, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE); }
    public void mostrarErro(String msg) { JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE); }
    public int confirmar(String msg) { return JOptionPane.showConfirmDialog(this, msg, "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); }
    public void refreshTable() {}
}