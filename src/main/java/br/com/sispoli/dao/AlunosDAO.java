// Realiza a conecção com os atributos da tabela alunos do Banco de Dados
package br.com.sispoli.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.sisgin.jdbc.ConnectionFactory;
import br.com.sisgin.model.Alunos;
import java.time.LocalDate; // Apenas data (AAAA-MM-DD)
import java.time.LocalTime; // Apenas hora (HH:MM:SS)

import javax.swing.JOptionPane;

import java.time.LocalDateTime; // Data e Hora juntas

public class AlunosDAO {

	private Connection con;

	public AlunosDAO() {
		this.con = new ConnectionFactory().getConnection();
	}

	// Método para cadastrar aluno
	public void cadastarAluno(Alunos obj) {

		try {
			// Comando SQL para inserção de dados no DB
			String sql = "insert into tabalunos(id_aluno,nome_completo,data_nascimento,cpf,rg,sexo,email,contato_what,id_cep,numero,complemento,isento,motivo_isencao,possui_restricao_medica,descricao_restricao,medicamentos_continuos,alergias,contato_emergencia_nome,contato_emergencia_telefone,contato_emergencia_parentesco,data_matricula,status,autorizacao_imagem,autorizacao_divulgacao,aceite_termos,data_aceite_termos,observacoes) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			// Conectar o DB e organizar o comando SQL
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, obj.getNome_completo());
			stmt.setObject(2, obj.getData_nascimento());
			stmt.setString(3, obj.getCpf());
			stmt.setString(4, obj.getRg());
			stmt.setString(5, obj.getSexo());
			stmt.setString(6, obj.getEmail());
			stmt.setString(7, obj.getContato_what());
			stmt.setString(8, obj.getId_cep());
			stmt.setString(9, obj.getNumero());
			stmt.setString(10, obj.getComplemento());
			stmt.setBoolean(11, obj.isIsento());
			stmt.setString(12, obj.getMotivo_isencao());
			stmt.setObject(13, obj.isPossui_resricao_medica());
			stmt.setString(14, obj.getDescricao_restricao());
			stmt.setString(15, obj.getMedicamentos_continuos());
			stmt.setString(16, obj.getAlergias());
			stmt.setString(17, obj.getContato_emergencia_nome());
			stmt.setString(18, obj.getContato_emergencia_telefone());
			stmt.setString(19, obj.getContato_emergencia_parentesco());
			stmt.setObject(20, obj.getData_matricula());
			stmt.setString(21, obj.getStatus());
			stmt.setObject(22, obj.isAutorizacao_imagem());
			stmt.setObject(23, obj.isAutorizacao_divulgacao());
			stmt.setObject(24, obj.isAceite_termos());
			stmt.setObject(25, obj.getData_aceite_termos());
			stmt.setString(26, obj.getObservacoes());

			// Executar o comando sql
			stmt.execute();
			stmt.close();

			JOptionPane.showMessageDialog(null, "Cadastro executado com sucesso!");

		} catch (SQLException e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "Erro" + e);
		}

	}

	// Método para alterar cadastro de aluno
	public void alterarAluno() {

	}

	// Método para excluir cadastro de aluno
	public void excluirAluno() {

	}

}
