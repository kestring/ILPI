/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Control.Impl;

import Control.Impl.Exception.DAOException;
import Control.Interface.IDAO;
import Model.Funcionario;
import Util.ConectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bruno
 */
public class ImplFuncionarioDAO implements IDAO<Funcionario> {

    private static ImplFuncionarioDAO instance;
    
    private ImplFuncionarioDAO(){
        
    }
    
    public static ImplFuncionarioDAO getInstance(){
        if(instance == null){
            instance = new ImplFuncionarioDAO();
        }
        return instance;
    }
    
    @Override
    public void inserir(Funcionario func) throws DAOException, SQLException {
        Connection con = ConectionManager.getInstance().getConexao();
        
        PreparedStatement prepared;
        
        //TODO Fazer o insert do idoso aqui
        prepared = con.prepareStatement("insert into Funcionario ("
                + "COD_FUNCIONARIO,"
                + "NOM_FUNCIONARIO,"
                + "NOM_FUNCAO,"
                + "ENDERECO,"
                + "NUM_TELEFONE) "
                + "values (?,?,?,?,?)");

        prepared.setInt(1, func.getCodFuncionario());
        prepared.setString(2, func.getNomeFuncionario());
        prepared.setString(3, func.getNomeFuncao());
        prepared.setString(4, func.getEndereco());
        prepared.setLong(5, func.getTelefone());

        prepared.execute();
    }

    @Override
    public void atualizar(Funcionario func) throws DAOException, SQLException {
        Connection con = ConectionManager.getInstance().getConexao();
        
        PreparedStatement prepared;
        ResultSet result;
        //TODO Fazer o insert do idoso aqui
        String sql = "select * from funcionario"
                + " where COD_FUNCIONARIO = ?";
        prepared = con.prepareStatement(sql);

        prepared.setInt(1, func.getCodFuncionario());

        result = prepared.executeQuery();

        if(!result.next()){
            inserir(func);
        }else{
            sql =  "update funcionario "
                    + "set COD_FUNCIONARIO = ?,"
                        + "NOM_FUNCIONARIO = ?,"
                        + "NOM_FUNCAO = ?,"
                        + "ENDERECO = ?,"
                        + "NUM_TELEFONE = ? "
                  + "where COD_FUNCIONARIO = ?";
            prepared = con.prepareStatement(sql);

            prepared.setInt(1, func.getCodFuncionario());
            prepared.setString(2, func.getNomeFuncionario());
            prepared.setString(3, func.getNomeFuncao());
            prepared.setString(4, func.getEndereco());
            prepared.setLong(5, func.getTelefone());
            prepared.setInt(6, func.getCodFuncionario());

            prepared.execute();
        }
    }

    @Override
    public void remover(Funcionario func) throws DAOException, SQLException {
        Connection con = ConectionManager.getInstance().getConexao();
        
        PreparedStatement prepared;
        ResultSet result;
        //TODO Fazer o insert do idoso aqui
        String sql = "select * from funcionario"
                + " where COD_FUNCIONARIO = ?";
        prepared = con.prepareStatement(sql);

        prepared.setInt(1, func.getCodFuncionario());

        result = prepared.executeQuery();

        if(result.next()){
            sql = "delete funcionario "
                 + "where COD_FUNCIONARIO = ?";
            prepared = con.prepareStatement(sql);
            prepared.setInt(1, func.getCodFuncionario());
            prepared.execute();
        }else{
            throw new DAOException("Não foi possível encontrar o alimento informado! Cod: " + func.getCodFuncionario());
        }
    }

    public List<Funcionario> encontrarTodos() throws DAOException, SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Funcionario encontrarPorCodigo(int codigo) throws DAOException, SQLException {
        Connection con = ConectionManager.getInstance().getConexao();
        
        PreparedStatement prepared;
        ResultSet result;
        //TODO Fazer o insert do idoso aqui
        String sql = "select * from funcionario "
                    + "where COD_FUNCIONARIO = ?";
        prepared = con.prepareStatement(sql);

        prepared.setInt(1, codigo);

        result = prepared.executeQuery();

        Funcionario a = null;
        while(result.next()){
            int codFuncionario = result.getInt("COD_FUNCIONARIO");
            String nomeFuncionario = result.getString("NOM_FUNCIONARIO").trim();
            String nomFuncao = result.getString("NOM_FUNCAO").trim();
            String endereco = result.getString("ENDERECO").trim();
            long telefone = result.getLong("NUM_TELEFONE");

            a = new Funcionario(codFuncionario, nomeFuncionario, nomFuncao, endereco, telefone);
        }

        if(a == null){
            throw new DAOException("Não foi possível o encontrar alimento! Cod = " + codigo);
        }
        return a;
    }
    
    public List<Funcionario> encontraFuncCuidador() throws SQLException, DAOException{
        Connection con = ConectionManager.getInstance().getConexao();
        List<Funcionario> lista = new ArrayList<>();
        PreparedStatement prepared;
        ResultSet result;
        //TODO Fazer o insert do idoso aqui
        String sql = "select * from funcionario "
                   + " where upper(nom_funcao) = 'CUIDADOR'";
        prepared = con.prepareStatement(sql);

        result = prepared.executeQuery();

        Funcionario a = null;
        while(result.next()){
            int codFuncionario = result.getInt("COD_FUNCIONARIO");
            String nomeFuncionario = result.getString("NOM_FUNCIONARIO");
            String nomFuncao = result.getString("NOM_FUNCAO");
            String endereco = result.getString("ENDERECO");
            long telefone = result.getLong("NUM_TELEFONE");

            a = new Funcionario(codFuncionario, nomeFuncionario, nomFuncao, endereco, telefone);
            lista.add(a);
        }

        if(lista.isEmpty()){
            throw new DAOException("Não foi possível o encontrar Funcionarios!");
        }else{
            return lista;
        }
    }

    public Funcionario encontrarFuncionario(String nomeUser) throws SQLException, DAOException{
        Connection con = ConectionManager.getInstance().getConexao();
        
        PreparedStatement prepared;
        ResultSet result;
        //TODO Fazer o insert do idoso aqui
        String sql = "select f.* "
                   + "  from funcionario f,"
                   + "       usuario u "
                   + " where f.cod_funcionario = u.cod_funcionario"
                   + "   and upper(u.nom_usuario) = upper('?')";
        prepared = con.prepareStatement(sql);
        
        prepared.setString(1, nomeUser);

        result = prepared.executeQuery();

        Funcionario a = null;
        while(result.next()){
            int codFuncionario = result.getInt("COD_FUNCIONARIO");
            String nomeFuncionario = result.getString("NOM_FUNCIONARIO");
            String nomFuncao = result.getString("NOM_FUNCAO");
            String endereco = result.getString("ENDERECO");
            long telefone = result.getLong("NUM_TELEFONE");

            a = new Funcionario(codFuncionario, nomeFuncionario, nomFuncao, endereco, telefone);
        }

        if(a == null){
            throw new DAOException("Não foi possível o encontrar Funcionarios!");
        }else{
            return a;
        }
    }
}
