package br.com.sispoli.controller;

import br.com.sispoli.dao.EnturmacaoDAO;
import br.com.sispoli.model.Enturmacao;
import br.com.sispoli.view.CadastroEnturmacoesView;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CadastroEnturmacoesController {
    private final CadastroEnturmacoesView view;
    private final EnturmacaoDAO dao;
    private int idEmEdicao = 0;
    private List<Enturmacao> listaCache = new ArrayList<>();

    public CadastroEnturmacoesController(CadastroEnturmacoesView view, EnturmacaoDAO dao) {
        this.view = view; this.dao = dao;
        configurarListeners(); carregarCombos(); carregarDados();
    }

    private void configurarListeners() {
        view.addSalvar(this::salvar); view.addExcluir(this::excluir);
        view.addLimpar(e -> { 
            view.limparFormulario(); 
            idEmEdicao = 0; 
        });
        view.addCancelar(e -> view.dispose());
        view.addTabela(new MouseAdapter(){ public void mouseClicked(MouseEvent e){ if(e.getClickCount()==2) carregarLinha(); }});
    }

    private void carregarCombos() {
        view.popularComboAlunos(dao.listarAlunosCombo());
        view.popularComboTurmas(dao.listarTurmasCombo());
    }

    private void salvar(ActionEvent e) {
        if(!validar()) return;
        
        Enturmacao ent = new Enturmacao();
        ent.setIdEnturmacao(idEmEdicao);
        ent.setIdAluno(view.getIdAluno());
        ent.setIdTurma(view.getIdTurma());
        ent.setDataEnturmacao(view.getDataEntrada());
        ent.setTipo(view.getTipo());
        ent.setMotivoDesenturmacao(view.getMotivo().isEmpty() ? null : view.getMotivo());
        ent.setObservacoes(view.getObservacoes());

        // ✅ LÓGICA AUTOMÁTICA DE DESENTURMAÇÃO
        String status = view.getStatus();
        LocalDate dataSaida = view.getDataSaida();

        if ("Inativo".equals(status)) {
            if (dataSaida == null) {
                dataSaida = LocalDate.now();
                view.setDataSaida(dataSaida); // Atualiza UI
            }
            ent.setDataDesenturmacao(dataSaida);
        } else {
            ent.setDataDesenturmacao(null); // Garante NULL para outros status
        }
        ent.setStatus(status);

        try {
            dao.salvar(ent);
            view.info(idEmEdicao == 0 ? "✅ Enturmação registrada!" : "✅ Atualizado!");
            view.limparFormulario(); idEmEdicao = 0; carregarDados();
        } catch (RuntimeException ex) {
            view.erro("❌ Erro ao salvar:\n" + ex.getMessage());
        }
    }

    private void excluir(ActionEvent e) {
        int l = view.getLinha(); if(l < 0) { view.erro("⚠️ Selecione um registro para excluir."); return; }
        int id = (int)view.getVal(l, 0); String aluno = (String)view.getVal(l, 1);
        if(view.confirmar("Remover a enturmação do aluno \"" + aluno + "\"?") != 0) return;
        try { 
            dao.excluir(id); 
            view.info("✅ Registro removido!"); 
            view.limparFormulario(); idEmEdicao = 0; carregarDados(); 
        } catch(Exception ex){ 
            view.erro(ex.getMessage().contains("1451") ? "❌ Não é possível: há vínculos dependentes." : "❌ " + ex.getMessage()); 
        }
    }

  
    
    private void carregarDados() {
        try {
           // listaCache = dao.listarTodos(); // ✅ Guarda em cache para acesso rápido
        	 listaCache = dao.listarAtivos();
            Object[][] d = new Object[listaCache.size()][7];
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for(int i=0; i<listaCache.size(); i++){
                Enturmacao e = listaCache.get(i);
                d[i] = new Object[]{ 
                    e.getIdEnturmacao(), e.getNomeAluno(), e.getNomeTurma(),
                    e.getDataEnturmacao() != null ? e.getDataEnturmacao().format(fmt) : "-",
                    e.getDataDesenturmacao() != null ? e.getDataDesenturmacao().format(fmt) : "-",
                    e.getTipo(), e.getStatus() 
                };
            }
            javax.swing.SwingUtilities.invokeLater(() -> view.atualizarTabela(d));
        } catch(Exception ex){ 
            view.erro("❌ Erro ao carregar dados: " + ex.getMessage()); 
        }
    }  

    private void carregarLinha() {
        int l = view.getLinha();
        if(l < 0) { view.erro("⚠️ Selecione um registro na tabela."); return; }
        
        int id = (int) view.getVal(l, 0);
        
        // Busca no cache pelo ID
        Enturmacao ent = null;
        for(Enturmacao e : listaCache) {
            if(e.getIdEnturmacao() == id) { ent = e; break; }
        }

        if(ent != null) {
            idEmEdicao = ent.getIdEnturmacao(); // Controller controla o ID em edição
            view.carregarFormulario(ent);       // ✅ View preenche os campos
            view.info("📝 Registro carregado. Ajuste os campos e clique em Salvar.");
        } else {
            view.erro("Registro não encontrado. Atualize a lista.");
        }
    }
    
    private boolean validar() {
        if(view.getIdAluno() == 0) { view.erro("Selecione um aluno."); return false; }
        if(view.getIdTurma() == 0) { view.erro("Selecione a turma."); return false; }
        LocalDate entrada = view.getDataEntrada();
        LocalDate saida = view.getDataSaida();
        if(entrada == null) { view.erro("Data de entrada é obrigatória."); return false; }
        if(saida != null && saida.isBefore(entrada)) { view.erro("Data de saída não pode ser anterior à entrada."); return false; }
        return true;
    }
}