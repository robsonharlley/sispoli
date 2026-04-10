package br.com.sispoli.controller;

import br.com.sispoli.dao.LocalDAO;
import br.com.sispoli.model.Local;
import br.com.sispoli.view.CadastroLocaisView;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CadastroLocaisController {
    private final CadastroLocaisView view;
    private final LocalDAO dao;
    private int idEmEdicao = 0;

    public CadastroLocaisController(CadastroLocaisView view, LocalDAO dao) {
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

        Local local = new Local();
        local.setId(idEmEdicao);
        local.setNomeLocal(view.getNome());
        local.setCapacidadeMaxima(view.getCapacidade());
        local.setStatus(view.getStatus());
        local.setObservacoes(view.getObservacoes());

        try {
            dao.salvar(local);
            view.mostrarInfo(idEmEdicao == 0 ? "✅ Local cadastrado com sucesso!" : "✅ Local atualizado com sucesso!");
            view.limparFormulario();
            idEmEdicao = 0;
            carregarDados();
        } catch (Exception ex) {
            view.mostrarErro("❌ Erro ao salvar:\n" + ex.getMessage());
        }
    }

    private void excluir(ActionEvent e) {
        int linha = view.getLinhaSelecionada();
        if (linha < 0) {
            view.mostrarErro("⚠️ Selecione um local na tabela para excluir.");
            return;
        }

        int id = (Integer) view.getValorTabela(linha, 0);
        String nome = (String) view.getValorTabela(linha, 1);
        if (view.confirmar("Excluir o local \"" + nome + "\"?\nEsta ação não pode ser desfeita.") != 0) return;

        try {
            dao.excluir(id);
            view.mostrarInfo("✅ Local excluído com sucesso!");
            view.limparFormulario();
            idEmEdicao = 0;
            carregarDados();
        } catch (Exception ex) {
            if (ex.getMessage().contains("1451")) {
                view.mostrarErro("❌ Não é possível excluir: este local está vinculado a outros registros (ex: agendamentos).");
            } else {
                view.mostrarErro("❌ Erro ao excluir:\n" + ex.getMessage());
            }
        }
    }

    private void carregarDados() {
        try {
            List<Local> lista = dao.listarTodos();
            Object[][] dados = new Object[lista.size()][5];
            for (int i = 0; i < lista.size(); i++) {
                Local l = lista.get(i);
                dados[i] = new Object[]{
                    l.getId(), l.getNomeLocal(), l.getCapacidadeMaxima(),
                    l.getStatus(), l.getObservacoes() != null ? l.getObservacoes() : ""
                };
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
        view.setCapacidade((Integer) view.getValorTabela(linha, 2));
        view.setStatus((String) view.getValorTabela(linha, 3));
        view.setObservacoes((String) view.getValorTabela(linha, 4));
        view.mostrarInfo("📝 Modo de edição ativado. Altere os campos e clique em Salvar.");
    }

    private boolean validarFormulario() {
        if (view.getNome().length() < 3) {
            view.mostrarErro("O nome do local deve ter pelo menos 3 caracteres.");
            return false;
        }
        if (view.getCapacidade() < 1) {
            view.mostrarErro("A capacidade deve ser maior que zero.");
            return false;
        }
        return true;
    }
}