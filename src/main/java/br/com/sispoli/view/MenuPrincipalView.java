package br.com.sispoli.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class MenuPrincipalView extends JFrame {

    private final Map<String, JButton> moduleButtons = new HashMap<>();
    private JLabel lblStatus;

    public MenuPrincipalView() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("SISPOLI - Gestão de Ginásio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(new Color(241, 245, 249));

        // === HEADER ===
        JPanel header = new JPanel(new BorderLayout(15, 5));
        header.setBackground(new Color(30, 41, 59));
        header.setBorder(new EmptyBorder(15, 25, 15, 25));
        
        JLabel lblTitulo = new JLabel("🏢 SISPOLI | Sistema de Gestão");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.add(lblTitulo, BorderLayout.WEST);

        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userInfo.setBackground(new Color(30, 41, 59));
        
        JLabel lblUser = new JLabel("👤 Admin | v1.0.0");
        lblUser.setForeground(new Color(148, 163, 184));
        userInfo.add(lblUser);
        
        header.add(userInfo, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // === CONTEÚDO PRINCIPAL ===
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(241, 245, 249));
        mainPanel.setBorder(new EmptyBorder(30, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;

        String[][] modulos = {
            {"📍 Locais", "Cadastro e gestão de espaços físicos", "3B82F6"},
            {"📮 CEPs", "Base de endereços e logradouros", "10B981"},
            {"👨‍🏫 Professores", "Cadastro de docentes e especialidades", "8B5CF6"},
            {"🎒 Turmas", "Organização de grupos e horários", "F59E0B"},
            {"🎓 Enturmações", "Vínculo aluno-turma e matrículas", "06B6D4"},
            {"📊 Frequência", "Registro de presenças e faltas", "EC4899"},
            {"⚠️ Ocorrências", "Registro de incidentes e disciplina", "EF4444"},
            {"👨‍👩‍👧 Responsáveis", "Cadastro de pais e responsáveis", "14B8A6"},
            {"⚙️ Configurações", "Parâmetros do sistema", "6B7280"}
        };

        for (int i = 0; i < modulos.length; i++) {
            gbc.gridx = i % 3;
            gbc.gridy = i / 3;
            gbc.weightx = 0.33; gbc.weighty = 0.33;

            JButton btn = criarBotaoModulo(modulos[i][0], modulos[i][1], Color.decode("#" + modulos[i][2]));
            mainPanel.add(btn, gbc);
            moduleButtons.put(modulos[i][0], btn);
        }

        add(mainPanel, BorderLayout.CENTER);

        // === FOOTER ===
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        footer.setBackground(new Color(30, 41, 59));
        lblStatus = new JLabel("✅ Sistema pronto. Selecione um módulo.");
        lblStatus.setForeground(new Color(148, 163, 184));
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        footer.add(lblStatus);
        add(footer, BorderLayout.SOUTH);
    }

    private JButton criarBotaoModulo(String titulo, String descricao, Color corPrimaria) {
        JPanel card = new JPanel(new BorderLayout(15, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 2, true),
            new EmptyBorder(18, 20, 18, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(30, 41, 59));
        card.add(lblTitulo, BorderLayout.NORTH);

        JLabel lblDesc = new JLabel(descricao);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setForeground(new Color(100, 116, 139));
        card.add(lblDesc, BorderLayout.CENTER);

        JButton btn = new JButton();
        btn.setLayout(new BorderLayout());
        btn.add(card, BorderLayout.CENTER);
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);

        // Efeito Hover
        Color corHover = new Color(corPrimaria.getRed(), corPrimaria.getGreen(), corPrimaria.getBlue(), 15);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(245, 250, 255));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(corPrimaria, 2, true),
                    new EmptyBorder(18, 20, 18, 20)
                ));
                lblTitulo.setForeground(corPrimaria);
            }
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240), 2, true),
                    new EmptyBorder(18, 20, 18, 20)
                ));
                lblTitulo.setForeground(new Color(30, 41, 59));
            }
        });

        return btn;
    }

    // === MÉTODOS EXPOSTOS AO CONTROLLER ===
    public JButton getBotao(String modulo) { return moduleButtons.get(modulo); }
    public void setStatus(String msg) { lblStatus.setText(msg); }
    public void adicionarListenerBotao(String modulo, java.awt.event.ActionListener l) {
        JButton btn = moduleButtons.get(modulo);
        if (btn != null) btn.addActionListener(l);
    }
}