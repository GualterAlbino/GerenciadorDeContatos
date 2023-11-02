package DAO;

import Modelo.ContatoClass;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAOContato {

    /**
     * Insere um objeto TransacaoClass na tabela 'transacao' do banco de dados.
     *
     * @param objContato O objeto TransacaoClass a ser inserido.
     * @return true se a inserção foi bem-sucedida, false caso contrário.
     */
    public int inserir(ContatoClass objContato) {
        int registrosAfetados = 0;
        int id = 0;
        try {
            Connection conexao = FabricaConexao.getConexao();

            // Define a consulta SQL para inserir uma nova transação na tabela
            String sql = "INSERT INTO `agenda`.`contato`"
                    + "(`nome`,\n"
                    + "`tipo`,\n"
                    + "`favorito`,\n"
                    + "`codigo`,\n"
                    + "`telefone`,\n"
                    + "`celular`,\n"
                    + "`fax`,\n"
                    + "`nome_empresa`,\n"
                    + "`cargo_empresa`,\n"
                    + "`observacao`)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

            // Cria um PreparedStatement com a opção de recuperar as chaves geradas
            PreparedStatement st = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Define os valores dos parâmetros na consulta SQL   
            st.setString(1, objContato.getNome());
            st.setString(2, objContato.getTipoContato());
            st.setBoolean(3, objContato.getIsFavorito());
            st.setString(4, objContato.getCodigo());
            st.setString(5, objContato.getTelefone());
            st.setString(6, objContato.getCelular());
            st.setString(7, objContato.getFax());
            st.setString(8, objContato.getObservacao());
            st.setString(9, objContato.getNomeEmpresa());
            st.setString(10, objContato.getCargoEmpresa());

            // Executa a consulta SQL e obtém o número de registros afetados
            registrosAfetados = st.executeUpdate();

            // Verifica se pelo menos um registro foi inserido
            if (registrosAfetados > 0) {
                // Obtém as chaves geradas, incluindo o último ID inserido
                ResultSet generatedKeys = st.getGeneratedKeys();
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                    System.out.println("Último ID inserido: " + id);

                }
            } else {
                System.out.println("Nenhum registro foi inserido.");
            }

            return (id);

        } catch (SQLException ex) {
            // Captura exceções de SQL e lança uma exceção personalizada
            throw new Error("Erro ao inserir registro: ", ex);
        }

    }

  public ContatoClass recuperar(String codigo) {
    ContatoClass contato = null;
    try {
        Connection conexao = FabricaConexao.getConexao();
        String sql = "SELECT * FROM `agenda`.`contato` WHERE codigo = ?";
        PreparedStatement st = conexao.prepareStatement(sql);
        st.setString(1, codigo);
        ResultSet resultado = st.executeQuery();
        
        if (resultado.next()) { // Move o cursor para a primeira linha, se houver resultados
            contato = new ContatoClass();
            contato.setNome(resultado.getString("nome"));
            contato.setTipoContato(resultado.getString("tipo"));
            contato.setIsFavorito(resultado.getBoolean("favorito"));
            contato.setTelefone(resultado.getString("telefone"));
            contato.setCelular(resultado.getString("celular"));
            contato.setFax(resultado.getString("fax"));
            contato.setNomeEmpresa(resultado.getString("cargo_empresa"));
            contato.setCargoEmpresa(resultado.getString("nome_empresa"));
            contato.setObservacao(resultado.getString("observacao"));
        }

    } catch (SQLException ex) {
        System.out.println("Erro ao recuperar: " + ex.getMessage());
    }
    return contato;
}


    public ArrayList<ContatoClass> recuperarTodos() {
        ArrayList<ContatoClass> transacoes = new ArrayList<>();
        try {
            Connection conexao = FabricaConexao.getConexao();
            String sql = "SELECT * FROM `agenda`.`contato`";
            PreparedStatement st = conexao.prepareStatement(sql);
            ResultSet resultado = st.executeQuery();

            while (resultado.next()) {
                var transacao = new ContatoClass();
                transacao.setCodigo(resultado.getString("codigo"));
                transacao.setNome(resultado.getString("nome"));
                transacao.setTipoContato(resultado.getString("tipo"));
                transacao.setIsFavorito(resultado.getBoolean("favorito"));
                transacao.setTelefone(resultado.getString("telefone"));
                transacao.setCelular(resultado.getString("celular"));
                transacao.setFax(resultado.getString("fax"));
                transacao.setNomeEmpresa(resultado.getString("cargo_empresa"));
                transacao.setCargoEmpresa(resultado.getString("nome_empresa"));
                transacao.setObservacao(resultado.getString("observacao"));
                transacoes.add(transacao);
            }

        } catch (SQLException ex) {
            System.out.println("Erro ao recuperar: " + ex.getMessage());
        }
        return transacoes;
    }

    public void editar(ContatoClass transacao) {
        try {

            Connection conexao = FabricaConexao.getConexao();
            String sql = "UPDATE contato SET nome=?, tipo=?, favorito=?, telefone=?, celular=?, fax=?, nome_empresa=?, cargo_empresa=?, observacao=? WHERE codigo = ?";

            PreparedStatement st = conexao.prepareStatement(sql);

            // Define os valores dos parâmetros na instrução SQL
            st.setString(1, transacao.getNome());
            st.setString(2, transacao.getTipoContato());
            st.setBoolean(3, transacao.getIsFavorito());
            st.setString(4, transacao.getTelefone());
            st.setString(5, transacao.getCelular());
            st.setString(6, transacao.getFax());
            st.setString(7, transacao.getNomeEmpresa());
            st.setString(8, transacao.getCargoEmpresa());
            st.setString(9, transacao.getObservacao());

            st.setString(10, transacao.getCodigo());

            // Executa a instrução SQL de atualização
            int linhasAfetadas = st.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Registro atualizado com sucesso!");
            } else {
                System.out.println("Nenhum registro foi atualizado.");
            }

        } catch (SQLException ex) {
            System.out.println("Erro ao atualizar registro: " + ex.getMessage());
        }
    }

    public void excluir(String id) {
        try {
            Connection conexao = FabricaConexao.getConexao();
            String sql = "DELETE FROM contato WHERE codigo=?";
            PreparedStatement st = conexao.prepareStatement(sql);

            st.setString(1, id);

            int linhasAfetadas = st.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Registro excluído com sucesso!");
            } else {
                System.out.println("Nenhum registro foi excluído.");
            }

        } catch (SQLException ex) {
            throw new Error("Erro ao excluir registro: " + ex.getMessage());

        }
    }

}
