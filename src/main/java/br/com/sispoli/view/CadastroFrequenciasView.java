package br.com.sispoli.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CadastroFrequenciasView extends JFrame {
    private JComboBox<String> cmbAluno, cmbTurma;
    private JSpinner spnDataAula;
    private JTable tblFrequencias;
    private DefaultTableModel tableModel;
    private JButton btnSalvar, btnLimpar, btnExcluir, btnCancelar;
    private JTextField txtBuscaIdAluno;
    private JButton btnBuscarAluno;

    // ✅ Data "vazia" segura para evitar crash do JSpinner com null
    private static final java.util.Date EMPTY_DATE = java.util.Date.from(
        LocalDate.of(1900, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()
    );

    public CadastroFrequenciasView() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Registro de Frequência");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        
     // === INÍCIO DA SUBSTITUIÇÃO NO inicializarComponentes() ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(15, 20, 10, 20));

        // === LINHA 0: Busca por ID ===
        GridBagConstraints gbc0 = new GridBagConstraints();
        gbc0.insets = new Insets(5, 5, 5, 5); gbc0.fill = GridBagConstraints.HORIZONTAL;
        gbc0.gridx = 0; gbc0.gridy = 0; gbc0.weightx = 0.15;
        formPanel.add(new JLabel("Buscar Aluno (ID):"), gbc0);

        gbc0.gridx = 1; gbc0.weightx = 0.25;
        txtBuscaIdAluno = new JTextField(10); txtBuscaIdAluno.setToolTipText("Digite o ID e pressione Enter");
        formPanel.add(txtBuscaIdAluno, gbc0);

        gbc0.gridx = 2; gbc0.weightx = 0.1;
        btnBuscarAluno = new JButton("🔍 Buscar"); btnBuscarAluno.setPreferredSize(new Dimension(90, 28));
        formPanel.add(btnBuscarAluno, gbc0);

        // === LINHA 1: Aluno & Turma ===
        GridBagConstraints gbc1 = new GridBagConstraints(); // ✅ Instância nova = zero vazamento
        gbc1.insets = new Insets(5, 5, 5, 5); gbc1.fill = GridBagConstraints.HORIZONTAL;
        gbc1.gridx = 0; gbc1.gridy = 1; gbc1.weightx = 0.15;
        formPanel.add(new JLabel("Aluno:"), gbc1);

        gbc1.gridx = 1; gbc1.weightx = 0.35;
        cmbAluno = new JComboBox<>(); cmbAluno.addItem("Selecione...");
        formPanel.add(cmbAluno, gbc1);

        gbc1.gridx = 2; gbc1.weightx = 0.15;
        formPanel.add(new JLabel("Turma:"), gbc1);

        gbc1.gridx = 3; gbc1.weightx = 0.35;
        cmbTurma = new JComboBox<>(); cmbTurma.addItem("Selecione...");
        formPanel.add(cmbTurma, gbc1);

        // === LINHA 2: Data da Aula ===
        GridBagConstraints gbc2 = new GridBagConstraints(); // ✅ Nova instância
        gbc2.insets = new Insets(5, 5, 5, 5); gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.gridx = 0; gbc2.gridy = 2; gbc2.weightx = 0.15;
        formPanel.add(new JLabel("Data da Aula:"), gbc2);

        gbc2.gridx = 1; gbc2.gridy = 2; gbc2.weightx = 0.85; gbc2.gridwidth = 3; // Ocupa colunas 1,2,3
        spnDataAula = new JSpinner(new SpinnerDateModel());
        spnDataAula.setEditor(new JSpinner.DateEditor(spnDataAula, "dd/MM/yyyy"));
        spnDataAula.setValue(java.util.Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        formPanel.add(spnDataAula, gbc2);

        // === LINHA 3: Botões ===
        GridBagConstraints gbcBtn = new GridBagConstraints(); // ✅ Instância fresca para os botões
        gbcBtn.insets = new Insets(15, 5, 5, 5);
        gbcBtn.fill = GridBagConstraints.HORIZONTAL;
        gbcBtn.anchor = GridBagConstraints.CENTER;
        gbcBtn.gridx = 0; gbcBtn.gridy = 3;
        gbcBtn.gridwidth = GridBagConstraints.REMAINDER;
        gbcBtn.weightx = 1.0; gbcBtn.weighty = 0;

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnSalvar = new JButton("💾 Salvar"); btnLimpar = new JButton("🧹 Limpar");
        btnExcluir = new JButton("🗑️ Excluir"); btnCancelar = new JButton("❌ Cancelar");
        Dimension btnSize = new Dimension(105, 32);
        for (JButton b : new JButton[]{btnSalvar, btnLimpar, btnExcluir, btnCancelar}) {
            b.setPreferredSize(btnSize); b.setFocusPainted(false);
        }
        btnExcluir.setBackground(new Color(220, 53, 69)); btnExcluir.setForeground(Color.WHITE);
        btnPanel.add(btnSalvar); btnPanel.add(btnLimpar); btnPanel.add(btnExcluir); btnPanel.add(btnCancelar);
        formPanel.add(btnPanel, gbcBtn);

        // Listener do campo de busca (pressione Enter)
        txtBuscaIdAluno.addActionListener(e -> btnBuscarAluno.doClick());
        // === FIM DA SUBSTITUIÇÃO ===
        
        
        // Grid
        JPanel gridPanel = new JPanel(new BorderLayout(10, 10));
        gridPanel.setBorder(new EmptyBorder(0, 20, 15, 20));
        gridPanel.add(new JLabel("📋 Frequências Registradas", SwingConstants.LEFT), BorderLayout.NORTH);
        String[] cols = {"ID", "Aluno", "Turma", "Data Aula"};
        tableModel = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tblFrequencias = new JTable(tableModel);
        tblFrequencias.setRowHeight(25); tblFrequencias.setAutoCreateRowSorter(true);
        gridPanel.add(new JScrollPane(tblFrequencias), BorderLayout.CENTER);
        JButton btnAtt = new JButton("🔄 Atualizar Lista");
        gridPanel.add(btnAtt, BorderLayout.SOUTH);

        add(formPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        btnAtt.addActionListener(e -> refreshTable());
        cmbAluno.requestFocusInWindow();
    }
        
    // === MÉTODOS EXPOSTOS AO CONTROLLER ===
    public void popularComboAlunos(List<String> list) { cmbAluno.removeAllItems(); cmbAluno.addItem("Selecione..."); for(String s : list) cmbAluno.addItem(s); }
    public void popularComboTurmas(List<String> list) { cmbTurma.removeAllItems(); cmbTurma.addItem("Selecione..."); for(String s : list) cmbTurma.addItem(s); }

    private int parseId(JComboBox<String> cb) { if(cb.getSelectedIndex() <= 0) return 0; return Integer.parseInt(cb.getSelectedItem().toString().split(" - ")[0]); }
    private void selectId(JComboBox<String> cb, int id) { for(int i=0; i<cb.getItemCount(); i++) { if(cb.getItemAt(i).startsWith(id + " - ")) { cb.setSelectedIndex(i); return; } } }

    public int getIdAluno() { return parseId(cmbAluno); } public void setIdAluno(int v) { selectId(cmbAluno, v); }
    public int getIdTurma() { return parseId(cmbTurma); } public void setIdTurma(int v) { selectId(cmbTurma, v); }
    
    public LocalDate getDataAula() {
        Object val = spnDataAula.getValue();
        if (val == null || val.equals(EMPTY_DATE)) return null;
        return ((java.util.Date) val).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    public void setDataAula(LocalDate d) {
        if (spnDataAula != null) {
            spnDataAula.setValue(d != null 
                ? java.util.Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant()) 
                : EMPTY_DATE);
        }
    }

    public int getLinha() { return tblFrequencias.getSelectedRow(); }
    public Object getVal(int l, int c) { return tableModel.getValueAt(l, c); }
    public void addSalvar(ActionListener l) { btnSalvar.addActionListener(l); }
    public void addExcluir(ActionListener l) { btnExcluir.addActionListener(l); }
    public void addLimpar(ActionListener l) { btnLimpar.addActionListener(l); }
    public void addCancelar(ActionListener l) { btnCancelar.addActionListener(l); }
    public void addTabela(MouseListener l) { tblFrequencias.addMouseListener(l); }
    public void atualizarTabela(Object[][] d) { tableModel.setRowCount(0); if(d!=null) for(Object[] r:d) tableModel.addRow(r); }
    
    public void limparFormulario() {
        cmbAluno.setSelectedIndex(0); cmbTurma.setSelectedIndex(0);
        setDataAula(LocalDate.now());
        tblFrequencias.clearSelection(); cmbAluno.requestFocusInWindow();
    }
    public void info(String m) { JOptionPane.showMessageDialog(this, m, "Sucesso", JOptionPane.INFORMATION_MESSAGE); }
    public void erro(String m) { JOptionPane.showMessageDialog(this, m, "Erro", JOptionPane.ERROR_MESSAGE); }
    public int confirmar(String m) { return JOptionPane.showConfirmDialog(this, m, "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); }
    public void refreshTable() {}
    
    public String getIdBuscaAluno() { return txtBuscaIdAluno.getText().trim(); }
    public void adicionarListenerBuscarAluno(ActionListener l) { btnBuscarAluno.addActionListener(l); }

    /**
     * Busca e seleciona o aluno pelo ID no combo cmbAluno.
     * @return true se encontrado, false caso contrário.
     */
    public boolean selecionarAlunoPorId(int id) {
        for (int i = 0; i < cmbAluno.getItemCount(); i++) {
            String item = cmbAluno.getItemAt(i);
            if (item.startsWith(id + " - ")) {
                cmbAluno.setSelectedIndex(i);
                return true;
            }
        }
        return false;
    }

    public String getAlunoSelecionadoInfo() {
        Object sel = cmbAluno.getSelectedItem();
        return sel != null ? sel.toString() : "";
    }
    
    
}