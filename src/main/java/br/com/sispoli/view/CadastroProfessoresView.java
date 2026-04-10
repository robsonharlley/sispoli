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

import javax.swing.JButton;
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

public class CadastroProfessoresView extends JFrame {
    private JTextField txtNome, txtMatricula, txtTelefone;
    private JTable tblProfessores;
    private DefaultTableModel tableModel;
    private JButton btnSalvar, btnLimpar, btnExcluir, btnCancelar;

    public CadastroProfessoresView() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Cadastro de Professores");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 580);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(15, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        txtNome = new JTextField(25);
        formPanel.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2;
        formPanel.add(new JLabel("Matrícula:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        txtMatricula = new JTextField(14);
        formPanel.add(txtMatricula, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.2;
        formPanel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        txtTelefone = new JTextField(20);
        formPanel.add(txtTelefone, gbc);

        // ✅ Botões (GridBagConstraints resetado para evitar vazamento de layout)
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 3;
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
        gridPanel.add(new JLabel("📋 Professores Cadastrados", SwingConstants.LEFT), BorderLayout.NORTH);
        String[] colunas = {"ID", "Nome", "Matrícula", "Telefone"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblProfessores = new JTable(tableModel);
        tblProfessores.setRowHeight(25);
        tblProfessores.setAutoCreateRowSorter(true);
        JScrollPane scroll = new JScrollPane(tblProfessores);
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
    public String getMatricula() { return txtMatricula.getText().trim(); }
    public void setMatricula(String v) { txtMatricula.setText(v); }
    public String getTelefone() { return txtTelefone.getText().trim(); }
    public void setTelefone(String v) { txtTelefone.setText(v); }

    public int getLinhaSelecionada() { return tblProfessores.getSelectedRow(); }
    public Object getValorTabela(int linha, int coluna) { return tableModel.getValueAt(linha, coluna); }

    public void adicionarListenerSalvar(ActionListener l) { btnSalvar.addActionListener(l); }
    public void adicionarListenerExcluir(ActionListener l) { btnExcluir.addActionListener(l); }
    public void adicionarListenerLimpar(ActionListener l) { btnLimpar.addActionListener(l); }
    public void adicionarListenerCancelar(ActionListener l) { btnCancelar.addActionListener(l); }
    public void adicionarListenerTabela(MouseListener l) { tblProfessores.addMouseListener(l); }

    public void atualizarTabela(Object[][] dados) {
        tableModel.setRowCount(0);
        for (Object[] linha : dados) tableModel.addRow(linha);
    }

    public void limparFormulario() {
        txtNome.setText(""); txtMatricula.setText(""); txtTelefone.setText("");
        tblProfessores.clearSelection(); txtNome.requestFocusInWindow();
    }

    public void mostrarInfo(String msg) { JOptionPane.showMessageDialog(this, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE); }
    public void mostrarErro(String msg) { JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE); }
    public int confirmar(String msg) { return JOptionPane.showConfirmDialog(this, msg, "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); }
    public void refreshTable() {} // Delegado ao Controller
}