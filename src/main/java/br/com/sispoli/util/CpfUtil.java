package br.com.sispoli.util;

/**
 * Validação matemática de CPF brasileiro.
 * ✅ Reutilizável, thread-safe e independente de UI.
 */
public class CpfUtil {

    public static boolean isValid(String cpf) {
        if (cpf == null) return false;
        
        // Remove formatação e verifica tamanho
        String clean = cpf.replaceAll("\\D", "");
        if (clean.length() != 11) return false;
        
        // Rejeita CPFs com todos os dígitos iguais (ex: 111.111.111-11)
        if (clean.matches("(\\d)\\1{10}")) return false;

        try {
            int[] d = new int[11];
            for (int i = 0; i < 11; i++) d[i] = Integer.parseInt(clean.substring(i, i + 1));

            // 🔹 Cálculo do 1º dígito verificador
            int soma1 = 0;
            for (int i = 0; i < 9; i++) soma1 += d[i] * (10 - i);
            int resto1 = soma1 % 11;
            int dv1 = (resto1 < 2) ? 0 : 11 - resto1;

            if (dv1 != d[9]) return false;

            // 🔹 Cálculo do 2º dígito verificador
            int soma2 = 0;
            for (int i = 0; i < 10; i++) soma2 += d[i] * (11 - i);
            int resto2 = soma2 % 11;
            int dv2 = (resto2 < 2) ? 0 : 11 - resto2;

            return dv2 == d[10];
            
        } catch (NumberFormatException e) {
            return false; // Contém caracteres inválidos
        }
    }
}