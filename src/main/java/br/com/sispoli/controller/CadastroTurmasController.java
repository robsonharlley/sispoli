package br.com.sispoli.controller;

import br.com.sispoli.dao.TurmaDAO;
import br.com.sispoli.model.Turma;
import br.com.sispoli.view.CadastroTurmasView;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CadastroTurmasController {
    private final CadastroTurmasView view;
    private final TurmaDAO dao;
    private int idEmEdicao = 0;

    public CadastroTurmasController(CadastroTurmasView view, TurmaDAO dao) {
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
            public void mouseClicked(MouseEvent e) { if(e.getClickCount() == 2) carregarLinhaSelecionada(); }
        });
    }

    private void carregarCombos() { view.popularComboLocais(dao.listarLocaisCombo()); }

    private void salvar(ActionEvent e) {
        if (!validarFormulario()) return;

        Turma turma = new Turma();
        turma.setIdTurma(idEmEdicao);
        turma.setIdLocal(view.getIdLocalSelecionado());
        turma.setNomeTurma(view.getNomeTurma());
        turma.setNivel(view.getNivel());
        turma.setFaixaEtaria(view.getFaixaEtaria());
        turma.setHorario(view.getHorario());
        turma.setDiaSemana(view.getDiaSemana());
        turma.setDuracaoAula(view.getDuracaoAula());
        turma.setCapacidadeMaxima(view.getCapacidadeMaxima());
        turma.setValorMensalidade(view.getValorMensalidade());
        turma.setStatus(view.getStatus());
        turma.setObservacoes(view.getObservacoes());
        turma.setCapacidadeAtipicos(view.getCapacidadeAtipicos());

        try {
            dao.salvar(turma);
            view.mostrarInfo(idEmEdicao == 0 ? "✅ Turma cadastrada com sucesso!" : "✅ Turma atualizada com sucesso!");
            view.limparFormulario(); idEmEdicao = 0;
            carregarDados();
        } catch (RuntimeException ex) {
            String msg = ex.getMessage();
            if (msg.contains("foreign key") || msg.contains("1452")) {
                view.mostrarErro("❌ Local informado não existe ou está inativo.");
            } else if (msg.contains("Duplicate entry") || msg.contains("1062")) {
                view.mostrarErro("❌ Já existe uma turma com este nome/local.");
            } else {
                view.mostrarErro("❌ Erro ao salvar:\n" + (msg.startsWith("DB_ERROR:") ? msg.replace("DB_ERROR:", "") : msg));
            }
        }
    }

    private void excluir(ActionEvent e) {
        int linha = view.getLinhaSelecionada();
        if(linha < 0) { view.mostrarErro("⚠️ Selecione uma turma para excluir."); return; }
        int id = (Integer) view.getValorTabela(linha, 0);
        String nome = (String) view.getValorTabela(linha, 1);
        if(view.confirmar("Excluir a turma \"" + nome + "\"?") != 0) return;

        try {
            dao.excluir(id);
            view.mostrarInfo("✅ Turma excluída com sucesso!");
            view.limparFormulario(); idEmEdicao = 0;
            carregarDados();
        } catch (Exception ex) {
            view.mostrarErro(ex.getMessage().contains("1451") ? 
                "❌ Não é possível excluir: turmas com lotações/agendamentos não podem ser removidas." : 
                "❌ Erro ao excluir: " + ex.getMessage());
        }
    }

    private void carregarDados() {
        try {
            List<Turma> lista = dao.listarTodos();
            Object[][] dados = new Object[lista.size()][10];
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
            for(int i=0; i<lista.size(); i++) {
                Turma t = lista.get(i);
                dados[i] = new Object[]{
                    t.getIdTurma(), t.getNomeTurma(), t.getNomeLocal() != null ? t.getNomeLocal() : "N/A",
                    t.getNivel(), t.getFaixaEtaria(), t.getHorario() != null ? t.getHorario().format(fmt) : "-",
                    t.getDiaSemana(), t.getCapacidadeMaxima(), "R$ " + t.getValorMensalidade().toPlainString(), t.getStatus()
                };
            }
            view.atualizarTabela(dados);
        } catch (Exception ex) { view.mostrarErro("❌ Erro ao carregar dados: " + ex.getMessage()); }
    }

    private void carregarLinhaSelecionada() {
        int l = view.getLinhaSelecionada(); if(l < 0) return;
        idEmEdicao = (Integer) view.getValorTabela(l, 0);
        view.setNomeTurma((String)view.getValorTabela(l, 1));
        view.setNivel((String)view.getValorTabela(l, 3));
        view.setFaixaEtaria((String)view.getValorTabela(l, 4));
        try { view.setHorario(java.time.LocalTime.parse((String)view.getValorTabela(l, 5), DateTimeFormatter.ofPattern("HH:mm"))); } catch(Exception ex) { view.setHorario(null); }
        view.setDiaSemana((String)view.getValorTabela(l, 6));
        view.setDuracaoAula((Integer)view.getValorTabela(l, 7));
        String valorStr = ((String)view.getValorTabela(l, 8)).replace("R$ ", "").replace(",", ".");
        view.setValorMensalidade(new BigDecimal(valorStr));
        view.setStatus((String)view.getValorTabela(l, 9));
        view.mostrarInfo("📝 Modo de edição ativado. Altere os campos e clique em Salvar.");
    }

    private boolean validarFormulario() {
        if(view.getNomeTurma().length() < 3) { view.mostrarErro("Nome da turma deve ter pelo menos 3 caracteres."); return false; }
        if(view.getIdLocalSelecionado() == 0) { view.mostrarErro("Selecione um local válido."); return false; }
        if(view.getHorario() == null) { view.mostrarErro("Horário inválido. Use formato HH:MM (ex: 19:30)."); return false; }
        if(view.getDiaSemana().isEmpty()) { view.mostrarErro("Informe o(s) dia(s) da semana."); return false; }
        BigDecimal valor = view.getValorMensalidade();
        if(valor.compareTo(BigDecimal.ZERO) <= 0) { view.mostrarErro("O valor da mensalidade deve ser maior que zero."); return false; }
        return true;
    }
}