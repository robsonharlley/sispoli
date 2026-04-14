package br.com.sispoli.controller;

import br.com.sispoli.dao.LotacaoProfessorDAO;
import br.com.sispoli.model.LotacaoProfessor;
import br.com.sispoli.view.CadastroLotacaoView;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CadastroLotacaoController {
    private final CadastroLotacaoView view;
    private final LotacaoProfessorDAO dao;
    private int idEmEdicao = 0;

    public CadastroLotacaoController(CadastroLotacaoView view, LotacaoProfessorDAO dao) {
        this.view = view;
        this.dao = dao;
        configurarListeners();
        carregarCombos();
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

    private void carregarCombos() {
        view.popularComboTurmas(dao.listarTurmasCombo());
        view.popularComboProfessores(dao.listarProfessoresCombo());
    }

    private void salvar(ActionEvent e) {
        if (!validarFormulario()) return;

        LotacaoProfessor lotacao = new LotacaoProfessor();
        lotacao.setIdLotacao(idEmEdicao);
        lotacao.setIdTurma(view.getIdTurmaSelecionado());
        lotacao.setIdProfessor(view.getIdProfessorSelecionado());

        try {
            dao.salvar(lotacao);
            view.mostrarInfo(idEmEdicao == 0 ? "✅ Lotação registrada com sucesso!" : "✅ Lotação atualizada com sucesso!");
            view.limparFormulario();
            idEmEdicao = 0;
            carregarDados();
        } catch (Exception ex) {
            String msg = ex.getMessage();
            if (msg.contains("1062") || msg.contains("Duplicate entry")) {
                view.mostrarErro("❌ Este professor já está lotado nesta turma.");
            } else if (msg.contains("1452") || msg.contains("foreign key")) {
                view.mostrarErro("❌ Erro de integridade: Turma ou Professor não encontrado.");
            } else {
                view.mostrarErro("❌ Erro ao salvar:\n" + msg);
            }
        }
    }

    private void excluir(ActionEvent e) {
        int linha = view.getLinhaSelecionada();
        if (linha < 0) {
            view.mostrarErro("⚠️ Selecione uma lotação na tabela para excluir.");
            return;
        }
        int id = (Integer) view.getValorTabela(linha, 0);
        String turma = (String) view.getValorTabela(linha, 1);
        String prof = (String) view.getValorTabela(linha, 2);

        if (view.confirmar("Remover a lotação de \"" + prof + "\" na turma \"" + turma + "\"?") != 0) return;

        try {
            dao.excluir(id);
            view.mostrarInfo("✅ Lotação removida com sucesso!");
            view.limparFormulario();
            idEmEdicao = 0;
            carregarDados();
        } catch (Exception ex) {
            view.mostrarErro("❌ Erro ao excluir:\n" + ex.getMessage());
        }
    }

    private void carregarDados() {
        try {
            List<LotacaoProfessor> lista = dao.listarTodos();
            Object[][] dados = new Object[lista.size()][3];
            for (int i = 0; i < lista.size(); i++) {
                LotacaoProfessor l = lista.get(i);
                dados[i] = new Object[]{ l.getIdLotacao(), l.getNomeTurma(), l.getNomeProfessor() };
            }
            view.atualizarTabela(dados);
        } catch (Exception ex) {
            view.mostrarErro("❌ Erro ao carregar dados:\n" + ex.getMessage());
        }
    }

    private void carregarLinhaSelecionada() {
        int linha = view.getLinhaSelecionada();
        if (linha < 0) return;
        // Nota: Para editar, precisaríamos dos IDs originais. 
        // Como a tabela exibe apenas nomes, idealmente buscar pelo ID da lotação.
        // Para simplificar, apenas informamos que a edição deve ser feita via exclusão + novo cadastro
        // ou implementando busca reversa. Aqui faremos a seleção visual apenas.
        view.mostrarInfo("📝 Edição direta desabilitada. Para alterar, exclua e registre novamente.");
    }

    private boolean validarFormulario() {
        if (view.getIdTurmaSelecionado() == 0) {
            view.mostrarErro("Selecione uma turma válida.");
            return false;
        }
        if (view.getIdProfessorSelecionado() == 0) {
            view.mostrarErro("Selecione um professor válido.");
            return false;
        }
        return true;
    }
}