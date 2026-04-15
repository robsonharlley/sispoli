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
        //view.addLimpar(e -> { view.limparFormulario(); idEmEdicao = 0; });
     // No Controller, dentro do listener de Limpar:
        view.addLimpar(e -> { 
            view.limparFormulario(); 
            view.setDesenturmarAgora(false); // ✅ Reseta o checkbox ao limpar
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
        
     // ✅ Lógica inteligente: só pega a data se o checkbox estiver marcado
        LocalDate dataSaida = null;
        if (view.isDesenturmarAgora()) {
            dataSaida = view.getDataSaida();
            if (dataSaida == null) {
                view.erro("⚠️ Selecione uma data de saída válida.");
                return;
            }
            // Validação extra: saída não pode ser anterior à entrada
            if (dataSaida.isBefore(view.getDataEntrada())) {
                view.erro("⚠️ Data de saída não pode ser anterior à data de entrada.");
                return;
            }
        }
        
        
        Enturmacao ent = new Enturmacao();
        ent.setIdEnturmacao(idEmEdicao);
        ent.setIdAluno(view.getIdAluno());
        ent.setIdTurma(view.getIdTurma());
        ent.setDataEnturmacao(view.getDataEntrada());
        //ent.setDataDesenturmacao(view.getDataSaida());
        ent.setDataDesenturmacao(dataSaida);
        ent.setTipo(view.getTipo());
        ent.setMotivoDesenturmacao(view.getMotivo().isEmpty() ? null : view.getMotivo());
        ent.setStatus(view.getStatus());
        ent.setObservacoes(view.getObservacoes());

        try {
            dao.salvar(ent);
            view.info(idEmEdicao == 0 ? "✅ Enturmação registrada!" : "✅ Enturmação atualizada!");
            view.limparFormulario(); idEmEdicao = 0; carregarDados();
        } catch (RuntimeException ex) {
            String msg = ex.getMessage();
            if(msg.contains("foreign key") || msg.contains("1452")) view.erro("❌ Aluno ou Turma informados são inválidos ou não existem.");
            else view.erro("❌ Erro ao salvar:\n" + msg);
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

    /*
    private void carregarDados() {
        try {
            List<Enturmacao> list = dao.listarTodos();
            Object[][] d = new Object[list.size()][7];
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for(int i=0; i<list.size(); i++){
                Enturmacao ent = list.get(i);
                d[i] = new Object[]{ 
                    ent.getIdEnturmacao(), ent.getNomeAluno(), ent.getNomeTurma(),
                    ent.getDataEnturmacao().format(fmt), 
                    ent.getDataDesenturmacao() != null ? ent.getDataDesenturmacao().format(fmt) : "-",
                    ent.getTipo(), ent.getStatus() 
                };
            }
            view.atualizarTabela(d);
        } catch(Exception ex){ view.erro("❌ Erro ao carregar dados: " + ex.getMessage()); }
    }


    private void carregarDados() {
        try {
            List<Enturmacao> list = dao.listarTodos();
            Object[][] dados = new Object[list.size()][7];
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for(int i = 0; i < list.size(); i++){
                Enturmacao e = list.get(i);
                // ✅ Garante que NENHUM campo da tabela seja null (evita Illegal Value no TableModel)
                dados[i] = new Object[]{
                    e.getIdEnturmacao(),
                    e.getNomeAluno() != null ? e.getNomeAluno() : "N/A",
                    e.getNomeTurma() != null ? e.getNomeTurma() : "N/A",
                    e.getDataEnturmacao() != null ? e.getDataEnturmacao().format(fmt) : "-",
                    e.getDataDesenturmacao() != null ? e.getDataDesenturmacao().format(fmt) : "-",
                    e.getTipo() != null ? e.getTipo() : "",
                    e.getStatus() != null ? e.getStatus() : ""
                };
            }
            
            // ✅ Força atualização na Event Dispatch Thread (EDT)
            javax.swing.SwingUtilities.invokeLater(() -> view.atualizarTabela(dados));
            
        } catch(Exception ex){
            System.err.println("❌ Erro ao carregar dados: " + ex.getMessage());
            ex.printStackTrace(); // Mostra a linha exata no console
            view.erro("Falha ao atualizar lista: " + ex.getMessage());
        }
    }
*/
    
    private void carregarDados() {
        try {
            listaCache = dao.listarTodos(); // ✅ Guarda em cache para acesso rápido
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
/*   
    private void carregarLinha() {
        int l = view.getLinha(); if(l < 0) return;
        idEmEdicao = (int)view.getVal(l, 0);
        // Nota: Para edição completa, seria necessário buscar os IDs pelo nome ou manter cache.
        // Aqui exibimos apenas uma mensagem informativa.
        view.info("📝 Modo edição ativado. Para alterar, preencha novamente os campos e clique em Salvar.");
    }
*/
    
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