package br.com.sispoli.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class CadastroTurmasView extends JFrame {
    private JTextField txtNomeTurma, txtFaixaEtaria, txtDiaSemana, txtHorario;
    private JSpinner spnDuracao, spnCapMax, spnCapAtipicos;
    private JComboBox<String> cmbLocal, cmbNivel, cmbStatus;
    private JTextArea txtObservacoes;
    private JTable tblTurmas;
    private DefaultTableModel tableModel;
    private JButton btnSalvar, btnLimpar, btnExcluir, btnCancelar;
    
    // ✅ Formato monetário como variável de instância (NÃO static)
    private NumberFormat currencyFormat;
    private JFormattedTextField txtValor;

    public CadastroTurmasView() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Cadastro de Turmas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(15, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ✅ Inicialização segura do formato monetário
        try {
            this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        } catch (Exception e) {
            this.currencyFormat = NumberFormat.getCurrencyInstance();
        }

        // Linha 1: Nome & Local
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.15;
        formPanel.add(new JLabel("Nome da Turma:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.35;
        txtNomeTurma = new JTextField(20); formPanel.add(txtNomeTurma, gbc);
        gbc.gridx = 2; gbc.weightx = 0.15;
        formPanel.add(new JLabel("Local:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.35;
        cmbLocal = new JComboBox<>(); cmbLocal.addItem("Selecione..."); formPanel.add(cmbLocal, gbc);

        // Linha 2: Nível & Faixa Etária
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.15;
        formPanel.add(new JLabel("Nível:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.35;
        cmbNivel = new JComboBox<>(new String[]{"Iniciante", "Avançado", "Competitivo"}); formPanel.add(cmbNivel, gbc);
        gbc.gridx = 2; gbc.weightx = 0.15;
        formPanel.add(new JLabel("Faixa Etária:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.35;
        txtFaixaEtaria = new JTextField(15); formPanel.add(txtFaixaEtaria, gbc);

        // Linha 3: Horário & Dias
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.15;
        formPanel.add(new JLabel("Horário (HH:MM):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.35;
        txtHorario = new JTextField(10); formPanel.add(txtHorario, gbc);
        gbc.gridx = 2; gbc.weightx = 0.15;
        formPanel.add(new JLabel("Dia(s) da Semana:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.35;
        txtDiaSemana = new JTextField(20); txtDiaSemana.setToolTipText("Ex: Seg, Qua, Sex"); formPanel.add(txtDiaSemana, gbc);

        // Linha 4: Duração, Capacidade Normal, Capacidade Atípicos
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.15;
        formPanel.add(new JLabel("Duração (min):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.15;
        spnDuracao = new JSpinner(new SpinnerNumberModel(60, 15, 180, 5)); formPanel.add(spnDuracao, gbc);
        gbc.gridx = 2; gbc.weightx = 0.15;
        formPanel.add(new JLabel("Cap. Máxima:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.15;
        spnCapMax = new JSpinner(new SpinnerNumberModel(30, 1, 200, 1)); formPanel.add(spnCapMax, gbc);
        gbc.gridx = 4; gbc.weightx = 0.15;
        formPanel.add(new JLabel("Cap. Atípicos:"), gbc);
        gbc.gridx = 5; gbc.weightx = 0.15;
        spnCapAtipicos = new JSpinner(new SpinnerNumberModel(3, 0, 20, 1)); formPanel.add(spnCapAtipicos, gbc);

        // Linha 5: Valor & Status
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.15;
        formPanel.add(new JLabel("Mensalidade (R$):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.35;
        txtValor = new JFormattedTextField(currencyFormat);
        txtValor.setValue(new BigDecimal("15.00")); // Valor padrão do schema
        txtValor.setHorizontalAlignment(SwingConstants.RIGHT);
        formPanel.add(txtValor, gbc);
        gbc.gridx = 2; gbc.weightx = 0.15;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.35;
        cmbStatus = new JComboBox<>(new String[]{"Ativa", "Em Formação", "Inativa"}); formPanel.add(cmbStatus, gbc);

        // Linha 6: Observações
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.15; gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Observações:"), gbc);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 5; gbc.weightx = 0.85; gbc.weighty = 0.3;
        gbc.fill = GridBagConstraints.BOTH; gbc.anchor = GridBagConstraints.NORTHWEST;
        txtObservacoes = new JTextArea(3, 25);
        txtObservacoes.setLineWrap(true); txtObservacoes.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(txtObservacoes), gbc);

        // ✅ Botões (GridBagConstraints RESETADO)
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 5, 5, 5); gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = GridBagConstraints.REMAINDER; gbc.weightx = 1.0; gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.CENTER;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnSalvar = new JButton("💾 Salvar"); btnLimpar = new JButton("🧹 Limpar");
        btnExcluir = new JButton("🗑️ Excluir"); btnCancelar = new JButton("❌ Cancelar");
        Dimension btnSize = new Dimension(105, 32);
        for (JButton b : new JButton[]{btnSalvar, btnLimpar, btnExcluir, btnCancelar}) { b.setPreferredSize(btnSize); b.setFocusPainted(false); }
        btnExcluir.setBackground(new Color(220, 53, 69)); btnExcluir.setForeground(Color.WHITE);
        btnPanel.add(btnSalvar); btnPanel.add(btnLimpar); btnPanel.add(btnExcluir); btnPanel.add(btnCancelar);
        formPanel.add(btnPanel, gbc);

        // Grid
        JPanel gridPanel = new JPanel(new BorderLayout(10, 10));
        gridPanel.setBorder(new EmptyBorder(0, 20, 15, 20));
        gridPanel.add(new JLabel("📋 Turmas Cadastradas", SwingConstants.LEFT), BorderLayout.NORTH);
        String[] colunas = {"ID", "Turma", "Local", "Nível", "Faixa", "Horário", "Dias", "Cap.", "Valor", "Status"};
        tableModel = new DefaultTableModel(colunas, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tblTurmas = new JTable(tableModel);
        tblTurmas.setRowHeight(25); tblTurmas.setAutoCreateRowSorter(true);
        JScrollPane scroll = new JScrollPane(tblTurmas);
        gridPanel.add(scroll, BorderLayout.CENTER);
        JButton btnAtualizar = new JButton("🔄 Atualizar Lista");
        gridPanel.add(btnAtualizar, BorderLayout.SOUTH);

        add(formPanel, BorderLayout.NORTH); add(gridPanel, BorderLayout.CENTER);
        btnAtualizar.addActionListener(e -> refreshTable());
        txtNomeTurma.requestFocusInWindow();
    }

    // === MÉTODOS EXPOSTOS AO CONTROLLER ===
    public void popularComboLocais(List<String> locais) {
        cmbLocal.removeAllItems(); cmbLocal.addItem("Selecione...");
        for(String l : locais) cmbLocal.addItem(l);
    }
    public int getIdLocalSelecionado() {
        if(cmbLocal.getSelectedIndex() <= 0) return 0;
        return Integer.parseInt(cmbLocal.getSelectedItem().toString().split(" - ")[0]);
    }
    public void setSelecaoLocal(int id) {
        for(int i=0; i<cmbLocal.getItemCount(); i++) {
            if(cmbLocal.getItemAt(i).startsWith(id+" - ")) { cmbLocal.setSelectedIndex(i); break; }
        }
    }

    public String getNomeTurma() { return txtNomeTurma.getText().trim(); }
    public void setNomeTurma(String v) { txtNomeTurma.setText(v); }
    public String getNivel() { return (String)cmbNivel.getSelectedItem(); }
    public void setNivel(String v) { cmbNivel.setSelectedItem(v); }
    public String getFaixaEtaria() { return txtFaixaEtaria.getText().trim(); }
    public void setFaixaEtaria(String v) { txtFaixaEtaria.setText(v); }
    
    public LocalTime getHorario() {
        try { return LocalTime.parse(txtHorario.getText().trim(), DateTimeFormatter.ofPattern("HH:mm")); } 
        catch (DateTimeParseException e) { return null; }
    }
    public void setHorario(LocalTime v) { txtHorario.setText(v != null ? v.format(DateTimeFormatter.ofPattern("HH:mm")) : ""); }
    
    public String getDiaSemana() { return txtDiaSemana.getText().trim(); }
    public void setDiaSemana(String v) { txtDiaSemana.setText(v); }
    public int getDuracaoAula() { return (Integer) spnDuracao.getValue(); }
    public void setDuracaoAula(int v) { spnDuracao.setValue(v); }
    public int getCapacidadeMaxima() { return (Integer) spnCapMax.getValue(); }
    public void setCapacidadeMaxima(int v) { spnCapMax.setValue(v); }
    public int getCapacidadeAtipicos() { return (Integer) spnCapAtipicos.getValue(); }
    public void setCapacidadeAtipicos(int v) { spnCapAtipicos.setValue(v); }
    
    public BigDecimal getValorMensalidade() {
        // ✅ Força commit antes de ler
        try { txtValor.commitEdit(); } catch (ParseException ignored) {}
        
        Object val = txtValor.getValue();
        if (val instanceof Number) return new BigDecimal(val.toString());
        
        // Fallback seguro
        try {
            String txt = txtValor.getText().replaceAll("[^0-9,.]", "").replace(',', '.');
            return txt.isEmpty() ? BigDecimal.ZERO : new BigDecimal(txt);
        } catch (Exception e) { return BigDecimal.ZERO; }
    }
    public void setValorMensalidade(BigDecimal v) { 
        txtValor.setValue(v != null ? v : BigDecimal.ZERO); 
    }
    
    public String getStatus() { return (String) cmbStatus.getSelectedItem(); }
    public void setStatus(String v) { cmbStatus.setSelectedItem(v); }
    public String getObservacoes() { return txtObservacoes.getText().trim(); }
    public void setObservacoes(String v) { txtObservacoes.setText(v); }

    public int getLinhaSelecionada() { return tblTurmas.getSelectedRow(); }
    public Object getValorTabela(int l, int c) { return tableModel.getValueAt(l, c); }

    public void adicionarListenerSalvar(ActionListener l) { btnSalvar.addActionListener(l); }
    public void adicionarListenerExcluir(ActionListener l) { btnExcluir.addActionListener(l); }
    public void adicionarListenerLimpar(ActionListener l) { btnLimpar.addActionListener(l); }
    public void adicionarListenerCancelar(ActionListener l) { btnCancelar.addActionListener(l); }
    public void adicionarListenerTabela(MouseListener l) { tblTurmas.addMouseListener(l); }

    public void atualizarTabela(Object[][] dados) { tableModel.setRowCount(0); for(Object[] r:dados) tableModel.addRow(r); }
    
    public void limparFormulario() {
        txtNomeTurma.setText(""); cmbLocal.setSelectedIndex(0); cmbNivel.setSelectedIndex(0);
        txtFaixaEtaria.setText(""); txtHorario.setText(""); txtDiaSemana.setText("");
        spnDuracao.setValue(60); spnCapMax.setValue(30); spnCapAtipicos.setValue(3);
        txtValor.setValue(new BigDecimal("15.00")); // Padrão schema
        cmbStatus.setSelectedIndex(0); txtObservacoes.setText("");
        tblTurmas.clearSelection(); txtNomeTurma.requestFocusInWindow();
    }
    
    public void mostrarInfo(String m) { JOptionPane.showMessageDialog(this, m, "Sucesso", JOptionPane.INFORMATION_MESSAGE); }
    public void mostrarErro(String m) { JOptionPane.showMessageDialog(this, m, "Erro", JOptionPane.ERROR_MESSAGE); }
    public int confirmar(String m) { return JOptionPane.showConfirmDialog(this, m, "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); }
    public void refreshTable() {}
}