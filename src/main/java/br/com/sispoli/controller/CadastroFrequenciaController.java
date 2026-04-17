package br.com.sispoli.controller;

import br.com.sispoli.dao.FrequenciaDAO;
import br.com.sispoli.model.Frequencia;
import br.com.sispoli.view.CadastroFrequenciasView;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CadastroFrequenciaController {
    private final CadastroFrequenciasView view;
    private final FrequenciaDAO dao;
    private List<Frequencia> cacheFrequencias;

    public CadastroFrequenciaController(CadastroFrequenciasView view, FrequenciaDAO dao) {
        this.view = view;
        this.dao = dao;
        configurarListeners();
        carregarCombos();
        carregarDados();
    }

    private void configurarListeners() {
        view.addSalvar(this::salvar);
        view.addExcluir(this::excluir);
        view.addLimpar(e -> view.limparFormulario());
        view.addCancelar(e -> view.dispose());
        view.addTabela(new MouseAdapter(){ public void mouseClicked(MouseEvent e){ if(e.getClickCount()==2) view.info("ℹ️ Edição direta não disponível para frequência. Exclua e registre novamente."); }});
     // ... outros listeners ...
        view.adicionarListenerBuscarAluno(e -> buscarAlunoPorId());
    }
    

    private void carregarCombos() {
        view.popularComboAlunos(dao.listarAlunosCombo());
        view.popularComboTurmas(dao.listarTurmasCombo());
    }

    private void salvar(ActionEvent e) {
        if(!validar()) return;
        
        Frequencia freq = new Frequencia();
        freq.setIdAluno(view.getIdAluno());
        freq.setIdTurma(view.getIdTurma());
        freq.setDataAula(view.getDataAula());

        try {
            dao.salvar(freq);
            view.info("✅ Frequência registrada com sucesso!");
            view.limparFormulario();
            carregarDados();
        } catch (RuntimeException ex) {
            String msg = ex.getMessage();
            if(msg.contains("1062") || msg.contains("Duplicate")) {
                view.erro("⚠️ Este aluno já possui frequência registrada nesta turma e data.");
            } else if(msg.contains("1452") || msg.contains("foreign key")) {
                view.erro("❌ Aluno ou Turma informados são inválidos.");
            } else {
                view.erro("❌ Erro ao salvar:\n" + msg);
            }
        }
    }

    private void excluir(ActionEvent e) {
        int l = view.getLinha();
        if(l < 0) { view.erro("⚠️ Selecione um registro para excluir."); return; }
        int id = (int)view.getVal(l, 0);
        String aluno = (String)view.getVal(l, 1);
        String data = (String)view.getVal(l, 3);
        if(view.confirmar("Excluir a frequência de \"" + aluno + "\" na data " + data + "?") != 0) return;
        
        try { 
            dao.excluir(id); 
            view.info("✅ Registro removido!"); 
            carregarDados(); 
        } catch(Exception ex){ 
            view.erro("❌ Erro ao excluir: " + ex.getMessage()); 
        }
    }

    private void carregarDados() {
        try {
            cacheFrequencias = dao.listarTodas();
            Object[][] d = new Object[cacheFrequencias.size()][4];
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for(int i=0; i<cacheFrequencias.size(); i++){
                Frequencia f = cacheFrequencias.get(i);
                d[i] = new Object[]{ 
                    f.getId(), 
                    f.getNomeAluno() != null ? f.getNomeAluno() : "N/A",
                    f.getNomeTurma() != null ? f.getNomeTurma() : "N/A",
                    f.getDataAula() != null ? f.getDataAula().format(fmt) : "-"
                };
            }
            javax.swing.SwingUtilities.invokeLater(() -> view.atualizarTabela(d));
        } catch(Exception ex){ view.erro("❌ Erro ao carregar dados: " + ex.getMessage()); }
    }

    private boolean validar() {
        if(view.getIdAluno() == 0) { view.erro("Selecione um aluno."); return false; }
        if(view.getIdTurma() == 0) { view.erro("Selecione uma turma."); return false; }
        if(view.getDataAula() == null) { view.erro("Informe a data da aula."); return false; }
        return true;
    }
    
    private void buscarAlunoPorId() {
        String idStr = view.getIdBuscaAluno();
        if (idStr.isEmpty()) {
            view.info("ℹ️ Digite o ID do aluno para buscar.");
            return;
        }
        try {
            int id = Integer.parseInt(idStr);
            if (view.selecionarAlunoPorId(id)) {
                view.info("✅ Aluno localizado e selecionado: " + view.getAlunoSelecionadoInfo());
                // Opcional: limpar campo de busca após sucesso
                // view.txtBuscaIdAluno.setText(""); 
            } else {
                view.erro("❌ Aluno com ID " + id + " não encontrado ou está inativo.");
            }
        } catch (NumberFormatException ex) {
            view.erro("⚠️ ID inválido. Digite apenas números.");
        }
    }
}