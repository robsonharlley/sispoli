package br.com.sispoli.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class RelatoriosView extends JFrame {
    private final Map<String, JButton> reportButtons = new HashMap<>();
    private JLabel lblTitulo, lblStatus;

    public RelatoriosView() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("📊 Central de Relatórios - SISPOLI");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(new Color(241, 245, 249));

        // === HEADER ===
        JPanel header = new JPanel(new BorderLayout(15, 5));
        header.setBackground(new Color(30, 41, 59));
        header.setBorder(new EmptyBorder(15, 25, 15, 25));
        
        lblTitulo = new JLabel("📊 Central de Relatórios");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.add(lblTitulo, BorderLayout.WEST);

        JButton btnVoltar = new JButton("← Voltar ao Menu");
        btnVoltar.setBackground(new Color(71, 85, 105));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        header.add(btnVoltar, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // === CONTEÚDO PRINCIPAL (Grid 2 Colunas) ===
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(241, 245, 249));
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;

        String[][] relatorios = {
            {"📋 Rel. Geral Alunos", "Lista completa de todos os alunos ativos", "3B82F6"},
            {"👥 Alunos por Turma", "Alunos organizados por grupo e horário", "10B981"},
            {"👨‍🏫 Rel. Professores", "Cadastro e dados funcionais dos docentes", "8B5CF6"},
            {"🚪 Rel. Salas", "Espaços físicos e capacidades disponíveis", "F59E0B"},
            {"🔗 Lotação Prof.", "Vínculos professores-turmas ativos", "06B6D4"},
            {"💰 Pagamentos (Data)", "Financeiro filtrado por período exato", "EF4444"},
            {"📅 Pagamentos (Mês)", "Conciliação financeira mensal consolidada", "EC4899"},
            {"⚠️ Inadimplentes", "Alunos com pendências financeiras abertas", "F97316"},
            {"🆓 Alunos Isentos", "Bolsistas, descontos e gratuidades", "14B8A6"},
            {"📄 Rel. Individual", "Histórico completo por matrícula do aluno", "6B7280"}
        };

        for (int i = 0; i < relatorios.length; i++) {
            gbc.gridx = i % 2;  // 2 colunas
            gbc.gridy = i / 2;
            JButton btn = criarCardRelatorio(relatorios[i][0], relatorios[i][1], Color.decode("#" + relatorios[i][2]));
            mainPanel.add(btn, gbc);
            reportButtons.put(relatorios[i][0], btn);
        }

        add(mainPanel, BorderLayout.CENTER);

        // === FOOTER ===
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        footer.setBackground(new Color(30, 41, 59));
        lblStatus = new JLabel("✅ Selecione um relatório para gerar");
        lblStatus.setForeground(new Color(148, 163, 184));
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        footer.add(lblStatus);
        add(footer, BorderLayout.SOUTH);

        btnVoltar.addActionListener(e -> dispose());
    }

    private JButton criarCardRelatorio(String titulo, String descricao, Color corPrimaria) {
        JPanel card = new JPanel(new BorderLayout(15, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 2, true),
            new EmptyBorder(20, 25, 20, 25)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setForeground(new Color(30, 41, 59));
        card.add(lblTitulo, BorderLayout.NORTH);

        JLabel lblDesc = new JLabel("<html><span style='color:#64748b;font-size:12px'>" + descricao + "</span></html>");
        card.add(lblDesc, BorderLayout.CENTER);

        JButton btn = new JButton();
        btn.setLayout(new BorderLayout());
        btn.add(card, BorderLayout.CENTER);
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(245, 250, 255));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(corPrimaria, 2, true),
                    new EmptyBorder(20, 25, 20, 25)
                ));
                lblTitulo.setForeground(corPrimaria);
            }
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240), 2, true),
                    new EmptyBorder(20, 25, 20, 25)
                ));
                lblTitulo.setForeground(new Color(30, 41, 59));
            }
        });

        return btn;
    }

    // === MÉTODOS EXPOSTOS AO CONTROLLER ===
    public JButton getBotao(String modulo) { return reportButtons.get(modulo); }
    public void setStatus(String msg) { lblStatus.setText(msg); lblStatus.setForeground(new Color(148, 163, 184)); }
    public void setStatusSucesso(String msg) { lblStatus.setText(msg); lblStatus.setForeground(new Color(34, 197, 94)); }
    public void setStatusErro(String msg) { lblStatus.setText(msg); lblStatus.setForeground(new Color(220, 53, 69)); }
    public void mostrarInfo(String m) { JOptionPane.showMessageDialog(this, m, "Informação", JOptionPane.INFORMATION_MESSAGE); }
    
    public void adicionarListenerBotao(String modulo, java.awt.event.ActionListener l) {
        JButton btn = reportButtons.get(modulo);
        if (btn != null) btn.addActionListener(l);
    }
}