package br.com.sispoli.security;

import javax.swing.*;
import java.awt.*;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SegurancaUtil {
    // ✅ Hash SHA-256 da senha padrão: "admin2024"
    // Para gerar outro hash: System.out.println(SegurancaUtil.gerarHash("suaNovaSenha"));
    private static final String SENHA_ADMIN_HASH = 
        "b8b8eb83374c0bf3b1c3224159f6119dbfff1b7ed6dfecdd80d4e8a895790a34";

    /**
     * Exibe diálogo de senha e valida contra o hash armazenado.
     * Limpa a memória automaticamente após uso.
     */
    public static boolean verificarAcessoConfiguracoes(Window parent) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JLabel("🔐 Digite a senha de administrador:"), BorderLayout.NORTH);
        
        JPasswordField pf = new JPasswordField(15);
        pf.setFont(new Font("Monospaced", Font.PLAIN, 14));
        pf.setEchoChar('●');
        panel.add(pf, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(parent, panel,
                "Acesso Restrito - Configurações",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return false;

        char[] input = pf.getPassword();
        boolean valido = verificarHash(input, SENHA_ADMIN_HASH);
        
        // ✅ Limpar senha da memória RAM (boa prática de segurança)
        Arrays.fill(input, ' ');
        return valido;
    }

    private static boolean verificarHash(char[] input, String hashEsperado) {
        try {
            String inputStr = new String(input);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(inputStr.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : digest) hex.append(String.format("%02x", b));
            return hex.toString().equals(hashEsperado);
        } catch (Exception e) { return false; }
    }

    /**
     * Use este método APENAS UMA VEZ para gerar o hash da sua senha real.
     * Ex: main() -> System.out.println(gerarHash("MinhaSenhaForte123!"));
     */
    public static String gerarHash(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : digest) hex.append(String.format("%02x", b));
            return hex.toString();
        } catch (Exception e) { return null; }
    }
}