package br.com.sispoli.controller;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.SwingUtilities;

import br.com.sispoli.dao.AlunoDAO;
import br.com.sispoli.dao.CepDAO;  // ✅ IMPORT OBRIGATÓRIO
import br.com.sispoli.model.Aluno;
import br.com.sispoli.model.Cep;    // ✅ IMPORT OBRIGATÓRIO
import br.com.sispoli.view.CadastroAlunosView;

public class CadastroAlunosController {
    private final CadastroAlunosView view;
    private final AlunoDAO alunoDao;
    private final CepDAO cepDao;
    private List<Aluno> cacheAlunos;
    private int idEmEdicao = 0;

    public CadastroAlunosController(CadastroAlunosView view, AlunoDAO alunoDao) {
        this.view = view;
        this.alunoDao = alunoDao;
        this.cepDao = new CepDAO();
        
        configurarListeners();
        carregarComboCeps();  // Carrega CEPs para o JComboBox
        carregarDados();      // Carrega lista de alunos na grid
    }

    private void configurarListeners() {
        view.addSalvar(this::salvar);
        view.addLimpar(e -> { view.limparFormulario(); idEmEdicao = 0; });
        view.addExcluir(this::excluir);
        view.addCancelar(e -> view.dispose());
        view.addTabela(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) carregarLinha();
            }
        });
    }

    /**
     * Carrega todos os CEPs no JComboBox com cache em memória.
     */
    private void carregarComboCeps() {
        new Thread(() -> {
            try {
                List<Cep> lista = cepDao.listarTodosParaCombo();
                SwingUtilities.invokeLater(() -> view.popularComboCeps(lista));
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> 
                    System.err.println("⚠️ Falha ao carregar CEPs: " + e.getMessage())
                );
            }
        }).start();
    }

    private void salvar(ActionEvent e) {
        if (!validar()) return;

        Aluno a = new Aluno();
        a.setIdAluno(idEmEdicao);
        
        // Dados Pessoais
        a.setNomeCompleto(view.getNome());
        a.setCpf(view.getCpf().replaceAll("\\D", "")); // Salva apenas números
        a.setRg(view.getRg());
        a.setDataNascimento(view.getDataNasc());
        a.setSexo(view.getSexo());
        
        // Contato
        a.setEmail(view.getEmail());
        a.setContatoWhat(view.getWhatsApp().replaceAll("\\D", ""));
        
        // Endereço (CRÍTICO: extrai apenas o ID do CEP)
        String idCep = view.getCepSelecionadoId();
        if (idCep == null || idCep.isEmpty()) {
            view.erro("⚠️ Selecione um CEP válido da lista.");
            return;
        }
        a.setIdCep(idCep);
        a.setNumero(view.getNumero());
        a.setComplemento(view.getComplemento());
        
        // Matrícula & Status
        a.setDataMatricula(view.getDataMatricula());
        a.setStatus(view.getStatus());
        a.setIsento(view.getIsento());
        a.setMotivoIsencao(view.getMotivoIsencao());
        
        // Emergência
        a.setContatoEmergenciaNome(view.getNomeEmergencia());
        a.setContatoEmergenciaTelefone(view.getTelEmergencia().replaceAll("\\D", ""));
        a.setContatoEmergenciaParentesco(view.getParentesco());
        
        // Saúde
        a.setPossuiRestricaoMedica(view.getRestricao());  // Boolean do CheckBox
        a.setDescricaoRestricao(view.getDescricaoRestricao()); // String do TextArea
        a.setMedicamentosContinuos(view.getMedicamentos());
        a.setAlergias(view.getAlergias());
        
        // Autorizações
        a.setAutorizacaoImagem(view.getImg());
        a.setAutorizacaoDivulgacao(view.getDivulg());
        a.setAceiteTermos(view.getAceite());
        a.setDataAceiteTermos(view.getAceite() ? view.getDataAceite() : null);
        
        // Observações
        a.setObservacoes(view.getObs());

        try {
            alunoDao.salvar(a);
            view.info(idEmEdicao == 0 ? "✅ Aluno cadastrado!" : "✅ Aluno atualizado!");
            view.limparFormulario();
            idEmEdicao = 0;
            carregarDados();
        } catch (RuntimeException ex) {
            String msg = ex.getMessage();
            if (msg.contains("Duplicate") || msg.contains("1062")) {
                view.erro("❌ CPF ou Email já cadastrados.");
            } else if (msg.contains("1452")) {
                view.erro("❌ CEP informado não existe na base. Selecione da lista.");
            } else {
                view.erro("❌ Erro ao salvar:\n" + msg);
            }
        }
    }

    private void excluir(ActionEvent e) {
        int l = view.getLinha();
        if (l < 0) { view.erro("⚠️ Selecione um aluno para excluir."); return; }
        
        int id = (int) view.getVal(l, 0);
        String nome = (String) view.getVal(l, 1);
        
        if (view.confirmar("Excluir aluno \"" + nome + "\"?") != 0) return;
        
        try {
            alunoDao.excluir(id);
            view.info("✅ Aluno excluído!");
            view.limparFormulario();
            idEmEdicao = 0;
            carregarDados();
        } catch (Exception ex) {
            view.erro(ex.getMessage().contains("1451") 
                ? "❌ Não é possível: há vínculos ativos (enturmações, pagamentos)." 
                : "❌ Erro: " + ex.getMessage());
        }
    }

    private void carregarDados() {
        try {
            cacheAlunos = alunoDao.listarTodos();
            Object[][] dados = new Object[cacheAlunos.size()][6];
            for (int i = 0; i < cacheAlunos.size(); i++) {
                Aluno a = cacheAlunos.get(i);
                dados[i] = new Object[] {
                    a.getIdAluno(),
                    a.getNomeCompleto(),
                    a.getCpf(), // Já formatado ou não, conforme DAO
                    a.getStatus(),
                    a.getContatoWhat(),
                    a.getEmail()
                };
            }
            SwingUtilities.invokeLater(() -> view.atualizarTabela(dados));
        } catch (Exception ex) {
            view.erro("❌ Erro ao carregar lista: " + ex.getMessage());
        }
    }

    private void carregarLinha() {
        int l = view.getLinha();
        if (l < 0) return;
        
        idEmEdicao = (int) view.getVal(l, 0);
        view.info("📝 Modo edição. Preencha os campos e clique em Salvar.");
        // Para edição completa, implemente busca por ID e preenchimento dos campos
    }

    private boolean validar() {
        if (view.getNome().length() < 3) { view.erro("Nome deve ter pelo menos 3 caracteres."); return false; }
        if (!view.getCpf().matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) { view.erro("CPF inválido. Use 000.000.000-00."); return false; }
        if (view.getEmail().isEmpty() || !view.getEmail().contains("@")) { view.erro("Email inválido."); return false; }
        if (view.getCepSelecionadoId() == null || view.getCepSelecionadoId().length() != 8) { view.erro("CEP inválido. Selecione da lista."); return false; }
        if (view.getDataNasc() == null) { view.erro("Data de nascimento é obrigatória."); return false; }
        return true;
    }
}