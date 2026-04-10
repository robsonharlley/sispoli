package br.com.sispoli.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class ConfiguracaoDBView extends JFrame {
    private JTextField txtUrl, txtUser, txtPass;
    private JCheckBox chkEncrypt;
    private JButton btnSalvar, btnTestar, btnCancelar, btnReload;
    private JLabel lblStatus;

    public ConfiguracaoDBView() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("⚙️ Configuração do Banco de Dados");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(248, 250, 252));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 25, 15, 25));
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // URL
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.25;
        formPanel.add(new JLabel("URL do Banco:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.75;
        txtUrl = new JTextField(40);
        txtUrl.setToolTipText("Ex: jdbc:mariadb://192.168.1.100/gestao_ginasio?useSSL=false");
        formPanel.add(txtUrl, gbc);

        // Usuário
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.25;
        formPanel.add(new JLabel("Usuário:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.75;
        txtUser = new JTextField(25);
        formPanel.add(txtUser, gbc);

        // Senha
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.25;
        formPanel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.75;
        txtPass = new JPasswordField(25);
        formPanel.add(txtPass, gbc);

        // Opções
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.weightx = 1;
        chkEncrypt = new JCheckBox("🔐 Criptografar senha no arquivo de configuração");
        chkEncrypt.setSelected(true);
        formPanel.add(chkEncrypt, gbc);

        // Botões
        gbc.gridy = 4; gbc.insets = new Insets(20, 8, 8, 8);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        btnSalvar = new JButton("💾 Salvar Configuração");
        btnTestar = new JButton("🔌 Testar Conexão");
        btnReload = new JButton("🔄 Recarregar");
        btnCancelar = new JButton("❌ Cancelar");
        
        Dimension btnSize = new Dimension(160, 35);
        for (JButton b : new JButton[]{btnSalvar, btnTestar, btnReload, btnCancelar}) {
            b.setPreferredSize(btnSize); b.setFocusPainted(false);
        }
        btnTestar.setBackground(new Color(59, 130, 246)); btnTestar.setForeground(Color.WHITE);
        btnSalvar.setBackground(new Color(34, 197, 94)); btnSalvar.setForeground(Color.WHITE);
        btnReload.setBackground(new Color(139, 92, 246)); btnReload.setForeground(Color.WHITE);
        btnCancelar.setBackground(new Color(220, 53, 69)); btnCancelar.setForeground(Color.WHITE);
        
        btnPanel.add(btnTestar); btnPanel.add(btnSalvar); btnPanel.add(btnReload); btnPanel.add(btnCancelar);
        formPanel.add(btnPanel, gbc);

        // Status
        lblStatus = new JLabel("ℹ️ Preencha os dados e clique em 'Testar Conexão'");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStatus.setForeground(new Color(100, 116, 139));

        add(formPanel, BorderLayout.CENTER);
        add(lblStatus, BorderLayout.SOUTH);

        txtUrl.requestFocusInWindow();
    }

    // === MÉTODOS EXPOSTOS AO CONTROLLER ===
    public String getUrl() { return txtUrl.getText().trim(); }
    public void setUrl(String v) { txtUrl.setText(v); }
    public String getUser() { return txtUser.getText().trim(); }
    public void setUser(String v) { txtUser.setText(v); }
    public String getPass() { 
        return txtPass.getText(); // JPasswordField retorna String direto em Java 8+
    }
    public void setPass(String v) { txtPass.setText(v); }
    public boolean isEncrypt() { return chkEncrypt.isSelected(); }
    public void setEncrypt(boolean v) { chkEncrypt.setSelected(v); }

    public void adicionarListenerSalvar(ActionListener l) { btnSalvar.addActionListener(l); }
    public void adicionarListenerTestar(ActionListener l) { btnTestar.addActionListener(l); }
    public void adicionarListenerReload(ActionListener l) { btnReload.addActionListener(l); }
    public void adicionarListenerCancelar(ActionListener l) { btnCancelar.addActionListener(l); }

    public void setStatus(String msg, Color cor) { 
        lblStatus.setText(msg); 
        lblStatus.setForeground(cor != null ? cor : new Color(100, 116, 139)); 
    }
    
    public void mostrarInfo(String msg) { JOptionPane.showMessageDialog(this, msg, "Informação", JOptionPane.INFORMATION_MESSAGE); }
    public void mostrarErro(String msg) { JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE); }
    public int confirmar(String msg) { return JOptionPane.showConfirmDialog(this, msg, "Confirmação", JOptionPane.YES_NO_OPTION); }
}