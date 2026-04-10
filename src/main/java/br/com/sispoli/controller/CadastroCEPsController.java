package br.com.sispoli.controller;

import br.com.sispoli.dao.CepDAO;
import br.com.sispoli.model.Cep;
import br.com.sispoli.view.CadastroCEPsView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CadastroCEPsController {
    private final CadastroCEPsView view;
    private final CepDAO dao;

    public CadastroCEPsController(CadastroCEPsView view, CepDAO dao) {
        this.view = view;
        this.dao = dao;
        configurarListeners();
        carregarDados();
    }

    private void configurarListeners() {
        view.adicionarListenerSalvar(this::salvar);
        view.adicionarListenerExcluir(this::excluir);
        view.adicionarListenerLimpar(e -> view.limparFormulario());
        view.adicionarListenerCancelar(e -> view.dispose());
        view.adicionarListenerTabela(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) carregarLinhaSelecionada();
            }
        });
    }

    private void salvar(ActionEvent e) {
        if (!validarFormulario()) return;

        Cep cep = new Cep(
            view.getTxtCEP(), view.getTxtLogradouro(), view.getTxtBairro(),
            view.getTxtCidade(), view.getEstadoSelecionado(), view.getTxtObservacoes()
        );

        try {
            dao.salvar(cep);
            view.mostrarInfo("✅ CEP salvo/atualizado com sucesso!");
            view.limparFormulario();
            carregarDados();
        } catch (Exception ex) {
            view.mostrarErro("❌ Erro ao salvar:\n" + ex.getMessage());
        }
    }

    private void excluir(ActionEvent e) {
        int linha = view.getLinhaSelecionada();
        if (linha < 0) {
            view.mostrarErro("⚠️ Selecione um CEP na tabela para excluir.");
            return;
        }

        String cepSelecionado = (String) view.getValorTabela(linha, 0);
        if (view.confirmar("Excluir o CEP " + cepSelecionado + "?") != JOptionPane.YES_OPTION) return;

        try {
            dao.excluir(cepSelecionado);
            view.mostrarInfo("✅ CEP excluído com sucesso!");
            carregarDados();
            view.limparFormulario();
        } catch (Exception ex) {
            if (ex.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException || 
                ex.getMessage().contains("1451")) {
                view.mostrarErro("❌ Não é possível excluir: este CEP está vinculado a outros registros.");
            } else {
                view.mostrarErro("❌ Erro ao excluir:\n" + ex.getMessage());
            }
        }
    }

    private void carregarDados() {
        try {
            List<Cep> lista = dao.listarTodos();
            Object[][] dados = new Object[lista.size()][6];
            for (int i = 0; i < lista.size(); i++) {
                Cep c = lista.get(i);
                dados[i] = new Object[]{
                    c.getId_cep(), c.getLogradouro(), c.getBairro(),
                    c.getCidade(), c.getEstado(), c.getObservacoes() != null ? c.getObservacoes() : ""
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

        view.setTxtCEP((String) view.getValorTabela(linha, 0));
        view.setTxtLogradouro((String) view.getValorTabela(linha, 1));
        view.setTxtBairro((String) view.getValorTabela(linha, 2));
        view.setTxtCidade((String) view.getValorTabela(linha, 3));
        view.setEstadoSelecionado((String) view.getValorTabela(linha, 4));
        view.setTxtObservacoes((String) view.getValorTabela(linha, 5));
        view.setTxtCEP(view.getTxtCEP()); // mantém foco
    }

    private boolean validarFormulario() {
        if (view.getTxtCEP().length() != 8) {
            view.mostrarErro("O CEP deve conter exatamente 8 dígitos.");
            return false;
        }
        if (view.getTxtLogradouro().isEmpty()) {
            view.mostrarErro("O Logradouro é obrigatório.");
            return false;
        }
        if (view.getTxtBairro().isEmpty()) {
            view.mostrarErro("O Bairro é obrigatório.");
            return false;
        }
        if (view.getTxtCidade().isEmpty()) {
            view.mostrarErro("A Cidade é obrigatória.");
            return false;
        }
        return true;
    }
}