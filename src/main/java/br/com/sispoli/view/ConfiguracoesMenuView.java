package br.com.sispoli.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class ConfiguracoesMenuView extends JFrame {

    private final Map<String, JButton> moduleButtons = new HashMap<>();
    private JLabel lblTitulo, lblStatus;

    public ConfiguracoesMenuView() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("⚙️ Painel Administrativo - SISPOLI");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(new Color(241, 245, 249));

        // === HEADER ===
        JPanel header = new JPanel(new BorderLayout(15, 5));
        header.setBackground(new Color(30, 41, 59));
        header.setBorder(new EmptyBorder(15, 25, 15, 25));
        
        lblTitulo = new JLabel("⚙️ Painel Administrativo");
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

        // === CONTEÚDO PRINCIPAL ===
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(241, 245, 249));
        mainPanel.setBorder(new EmptyBorder(40, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;

        // Módulos administrativos
        String[][] modulos = {
            {"📍 Locais", "Gerenciar espaços físicos do ginásio", "3B82F6"},
            {"📮 CEPs", "Base de endereços e logradouros", "10B981"},
            {"👨‍🏫 Professores", "Cadastro e gestão de docentes", "8B5CF6"},
            {"🔐 Banco de Dados", "Configurar conexão e parâmetros", "F59E0B"},
            {"🎒 Turmas", "Organização de grupos e horários", "F59E0B"},
            {"👥 Lotação", "Vincular professores às turmas", "06B6D4"}
        };

        for (int i = 0; i < modulos.length; i++) {
            gbc.gridx = i % 2;  // 2 colunas
            gbc.gridy = i / 2;
            gbc.weightx = 0.5; gbc.weighty = 0.5;

            JButton btn = criarCardModulo(modulos[i][0], modulos[i][1], Color.decode("#" + modulos[i][2]));
            mainPanel.add(btn, gbc);
            moduleButtons.put(modulos[i][0], btn);
        }

        add(mainPanel, BorderLayout.CENTER);

        // === FOOTER ===
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        footer.setBackground(new Color(30, 41, 59));
        lblStatus = new JLabel("✅ Selecione um módulo para administrar");
        lblStatus.setForeground(new Color(148, 163, 184));
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        footer.add(lblStatus);
        add(footer, BorderLayout.SOUTH);

        // Listener do botão Voltar
        btnVoltar.addActionListener(e -> dispose());
    }

    private JButton criarCardModulo(String titulo, String descricao, Color corPrimaria) {
        JPanel card = new JPanel(new BorderLayout(15, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 2, true),
            new EmptyBorder(20, 25, 20, 25)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(30, 41, 59));
        card.add(lblTitulo, BorderLayout.NORTH);

        JLabel lblDesc = new JLabel("<html><span style='color:#64748b;font-size:13px'>" + descricao + "</span></html>");
        card.add(lblDesc, BorderLayout.CENTER);

        JButton btn = new JButton();
        btn.setLayout(new BorderLayout());
        btn.add(card, BorderLayout.CENTER);
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);

        // Efeito Hover
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
    public JButton getBotao(String modulo) { return moduleButtons.get(modulo); }
    
    public void setStatus(String msg) { 
        lblStatus.setText(msg); 
        lblStatus.setForeground(new Color(148, 163, 184));
    }
    
    public void setStatusSucesso(String msg) { 
        lblStatus.setText(msg); 
        lblStatus.setForeground(new Color(34, 197, 94));
    }
    
    public void setStatusErro(String msg) { 
        lblStatus.setText(msg); 
        lblStatus.setForeground(new Color(220, 53, 69));
    }

    public void adicionarListenerBotao(String modulo, java.awt.event.ActionListener l) {
        JButton btn = moduleButtons.get(modulo);
        if (btn != null) btn.addActionListener(l);
    }
    
    public void adicionarListenerVoltar(java.awt.event.ActionListener l) {
        // Já implementado no botão Voltar, mas exposto para flexibilidade
    }
}