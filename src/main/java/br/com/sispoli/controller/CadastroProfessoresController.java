package br.com.sispoli.controller;

import br.com.sispoli.dao.ProfessorDAO;
import br.com.sispoli.model.Professor;
import br.com.sispoli.view.CadastroProfessoresView;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CadastroProfessoresController {
    private final CadastroProfessoresView view;
    private final ProfessorDAO dao;
    private int idEmEdicao = 0;

    public CadastroProfessoresController(CadastroProfessoresView view, ProfessorDAO dao) {
        this.view = view;
        this.dao = dao;
        configurarListeners();
        carregarDados();
    }

    private void configurarListeners() {
        view.adicionarListenerSalvar(this::salvar);
        view.adicionarListenerExcluir(this::excluir);
        view.adicionarListenerLimpar(e -> { view.limparFormulario(); idEmEdicao = 0; });
        view.adicionarListenerCancelar(e -> view.dispose());
        view.adicionarListenerTabela(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) carregarLinhaSelecionada();
            }
        });
    }

    private void salvar(ActionEvent e) {
        if (!validarFormulario()) return;

        Professor prof = new Professor();
        prof.setId(idEmEdicao);
        prof.setNome(view.getNome());
        prof.setMatricula(view.getMatricula());
        prof.setTelefone(view.getTelefone());

        try {
            dao.salvar(prof);
            view.mostrarInfo(idEmEdicao == 0 ? "✅ Professor cadastrado com sucesso!" : "✅ Professor atualizado com sucesso!");
            view.limparFormulario();
            idEmEdicao = 0;
            carregarDados();
        } catch (Exception ex) {
            String msg = ex.getMessage();
            if (msg.contains("1062") || msg.contains("Duplicate entry")) {
                view.mostrarErro("❌ Erro: Matrícula já cadastrada no sistema.");
            } else {
                view.mostrarErro("❌ Erro ao salvar:\n" + msg);
            }
        }
    }

    private void excluir(ActionEvent e) {
        int linha = view.getLinhaSelecionada();
        if (linha < 0) {
            view.mostrarErro("⚠️ Selecione um professor na tabela para excluir.");
            return;
        }

        int id = (Integer) view.getValorTabela(linha, 0);
        String nome = (String) view.getValorTabela(linha, 1);
        if (view.confirmar("Excluir o professor \"" + nome + "\"?\nEsta ação não pode ser desfeita.") != 0) return;

        try {
            dao.excluir(id);
            view.mostrarInfo("✅ Professor excluído com sucesso!");
            view.limparFormulario();
            idEmEdicao = 0;
            carregarDados();
        } catch (Exception ex) {
            if (ex.getMessage().contains("1451")) {
                view.mostrarErro("❌ Não é possível excluir: este professor está vinculado a turmas/ocorrências.");
            } else {
                view.mostrarErro("❌ Erro ao excluir:\n" + ex.getMessage());
            }
        }
    }

    private void carregarDados() {
        try {
            List<Professor> lista = dao.listarTodos();
            Object[][] dados = new Object[lista.size()][4];
            for (int i = 0; i < lista.size(); i++) {
                Professor p = lista.get(i);
                dados[i] = new Object[]{ p.getId(), p.getNome(), p.getMatricula(), p.getTelefone() };
            }
            view.atualizarTabela(dados);
        } catch (Exception ex) {
            view.mostrarErro("❌ Erro ao carregar dados:\n" + ex.getMessage());
        }
    }

    private void carregarLinhaSelecionada() {
        int linha = view.getLinhaSelecionada();
        if (linha < 0) return;

        idEmEdicao = (Integer) view.getValorTabela(linha, 0);
        view.setNome((String) view.getValorTabela(linha, 1));
        view.setMatricula((String) view.getValorTabela(linha, 2));
        view.setTelefone((String) view.getValorTabela(linha, 3));
        view.mostrarInfo("📝 Modo de edição ativado. Altere os campos e clique em Salvar.");
    }

    private boolean validarFormulario() {
        if (view.getNome().length() < 3) {
            view.mostrarErro("O nome deve ter pelo menos 3 caracteres.");
            return false;
        }
        return true;
    }
}