package br.com.sispoli.controller;

import br.com.sispoli.dao.ResponsavelDAO;
import br.com.sispoli.model.Responsavel;
import br.com.sispoli.view.CadastroResponsaveisView;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CadastroResponsaveisController {
    private final CadastroResponsaveisView view;
    private final ResponsavelDAO dao;
    private int idEmEdicao = 0;

    public CadastroResponsaveisController(CadastroResponsaveisView view, ResponsavelDAO dao) {
        this.view = view; this.dao = dao;
        configurarListeners(); carregarComboAlunos(); carregarDados();
    }

    private void configurarListeners() {
        view.adicionarListenerSalvar(this::salvar);
        view.adicionarListenerExcluir(this::excluir);
        view.adicionarListenerLimpar(e -> { view.limparFormulario(); idEmEdicao = 0; });
        view.adicionarListenerCancelar(e -> view.dispose());
        view.adicionarListenerTabela(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { if(e.getClickCount() == 2) carregarLinhaSelecionada(); }
        });
    }

    private void carregarComboAlunos() {
        view.popularComboAlunos(dao.listarAlunosCombo());
    }

    private void salvar(ActionEvent e) {
        if(!validarFormulario()) return;
        Responsavel r = new Responsavel();
        r.setIdResponsavel(idEmEdicao);
        r.setIdAluno(view.getIdAlunoSelecionado());
        r.setNomeCompleto(view.getNomeCompleto());
        r.setCpf(view.getCpf());
        r.setRg(view.getRg());
        r.setEmail(view.getEmail());
        r.setTelefone1(view.getTelefone1());
        r.setParentesco(view.getParentesco());

        try {
            dao.salvar(r);
            view.mostrarInfo(idEmEdicao == 0 ? "✅ Responsável cadastrado!" : "✅ Atualizado!");
            view.limparFormulario(); idEmEdicao = 0; carregarDados();
        } catch(Exception ex) {
            String msg = ex.getMessage();
            if(msg.contains("1452")) view.mostrarErro("❌ Aluno selecionado não encontrado.");
            else if(msg.contains("1062") || msg.contains("Duplicate")) view.mostrarErro("❌ CPF já cadastrado para este aluno.");
            else view.mostrarErro("❌ Erro: " + msg);
        }
    }

    private void excluir(ActionEvent e) {
        int l = view.getLinhaSelecionada(); if(l < 0) { view.mostrarErro("⚠️ Selecione um responsável."); return; }
        int id = (Integer) view.getValorTabela(l, 0); String nome = (String) view.getValorTabela(l, 2);
        if(view.confirmar("Excluir responsável \"" + nome + "\"?") != 0) return;
        try {
            dao.excluir(id); view.mostrarInfo("✅ Excluído!"); view.limparFormulario(); idEmEdicao = 0; carregarDados();
        } catch(Exception ex) { 
            view.mostrarErro(ex.getMessage().contains("1451") ? "❌ Possui vínculos com ocorrências." : "❌ " + ex.getMessage()); 
        }
    }

    private void carregarDados() {
        try {
            List<Responsavel> lista = dao.listarTodos();
            Object[][] dados = new Object[lista.size()][6];
            for(int i=0; i<lista.size(); i++) {
                Responsavel r = lista.get(i);
                dados[i] = new Object[]{ 
                    r.getIdResponsavel(), 
                    r.getNomeAluno() != null ? r.getNomeAluno() : "N/A",
                    r.getNomeCompleto(), 
                    formatarCPF(r.getCpf()), 
                    formatarTelefone(r.getTelefone1()), 
                    r.getParentesco() 
                };
            }
            view.atualizarTabela(dados);
        } catch(Exception ex) { view.mostrarErro("❌ Erro: " + ex.getMessage()); }
    }

    private void carregarLinhaSelecionada() {
        int l = view.getLinhaSelecionada(); if(l < 0) return;
        idEmEdicao = (Integer) view.getValorTabela(l, 0);
        view.setSelectedAluno((Integer) view.getValorTabela(l, 1)); // Nota: ideal buscar ID real
        view.setNomeCompleto((String) view.getValorTabela(l, 2));
        view.setCpf((String) view.getValorTabela(l, 3));
        view.setTelefone1(((String) view.getValorTabela(l, 4)).replaceAll("[^0-9]", ""));
        view.setParentesco((String) view.getValorTabela(l, 5));
        view.mostrarInfo("📝 Edição: ajuste os campos e clique em Salvar.");
    }

    private boolean validarFormulario() {
        if(view.getIdAlunoSelecionado() == 0) { view.mostrarErro("Selecione um aluno."); return false; }
        if(view.getNomeCompleto().length() < 5) { view.mostrarErro("Nome: mínimo 5 caracteres."); return false; }
        if(view.getTelefone1().length() < 10) { view.mostrarErro("Telefone: mínimo 10 dígitos."); return false; }
        if(view.getParentesco().isEmpty()) { view.mostrarErro("Informe o parentesco."); return false; }
        String cpf = view.getCpf();
        if(!cpf.isEmpty() && cpf.length() != 11) { view.mostrarErro("CPF: deve ter 11 dígitos."); return false; }
        return true;
    }

    // Utilitários de formatação para exibição
    private String formatarCPF(String cpf) {
        if(cpf == null || cpf.length() != 11) return cpf;
        return cpf.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
    private String formatarTelefone(String tel) {
        if(tel == null || tel.isEmpty()) return tel;
        if(tel.length() == 11) return tel.replaceFirst("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
        if(tel.length() == 10) return tel.replaceFirst("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
        return tel;
    }
}