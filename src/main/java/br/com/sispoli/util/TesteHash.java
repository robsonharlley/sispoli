package br.com.sispoli.util;

import br.com.sispoli.security.SegurancaUtil;

public class TesteHash {
    public static void main(String[] args) {
        // 🔑 Troque "SuaSenhaForte123!" pela senha que deseja usar
        String senha = "admin2024"; 
        String hash = SegurancaUtil.gerarHash(senha);
        
        System.out.println("🔐 Hash gerado para: " + senha);
        System.out.println("📋 Copie APENAS a linha abaixo:");
        System.out.println(hash);
        System.out.println("📏 Tamanho: " + (hash != null ? hash.length() : 0) + " caracteres");
    }
}