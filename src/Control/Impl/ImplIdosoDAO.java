/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Control.Impl;

import Control.Impl.Exception.DAOException;
import Control.Interface.IDAO;
import Model.Funcionario;
import Model.Idoso;
import Model.Quarto;
import Util.ConectionManager;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 *
 * @author Bruno
 */
public class ImplIdosoDAO implements IDAO<Idoso> {

    private static ImplIdosoDAO instance;
    
    private ImplIdosoDAO(){
        
    }
    
    public static ImplIdosoDAO getInstance(){
        if(instance == null){
            instance = new ImplIdosoDAO();
        }
        return instance;
    }
    
    @Override
    public void inserir(Idoso idoso) throws DAOException, SQLException {
        Connection con = ConectionManager.getInstance().getConexao();
        
        PreparedStatement prepared;
        //TODO Fazer o insert do idoso aqui
        prepared = con.prepareStatement("insert into idoso ("
                + "COD_IDOSO,"
                + "NOM_IDOSO,"
                + "DAT_NASCIMENTO,"
                + "LOCAL_ORIGEM,"
                + "NOM_PARENT_RESP,"
                + "DSC_END_PARENT,"
                + "NUM_TEL_PARENT,"
                + "ACAMADO,"
                + "NUM_RG,"
                + "NUM_CPF,"
                + "DSC_CUIDADOS_ESP,"
                + "COD_CUIDADOR) "
                + "values (?,?,?,?,?,?,?,?,?,?,?,?)");

        prepared.setInt(1, idoso.getCodIdoso());
        prepared.setString(2, idoso.getNomeIdoso());
        prepared.setDate(3, idoso.getDataNascimento());
        prepared.setString(4, idoso.getLocalOrigem());
        prepared.setString(5, idoso.getNomeParenteResponsavel());
        prepared.setString(6, idoso.getEndParente());
        prepared.setString(7, idoso.getNumTelefoneParente());
        prepared.setBoolean(8, idoso.getAcamado());
        prepared.setInt(9, idoso.getRg());
        prepared.setString(10, idoso.getCpf());
        prepared.setString(11, idoso.getCuidadosEspeciais());
        prepared.setInt(12, idoso.getCuidador().getCodFuncionario());

        prepared.execute();
        
        PreparedStatement prepared2;
        
        prepared2 = con.prepareStatement("insert into idoso_quarto ("
                + " COD_IDOSO,"
                + " NUM_QUARTO,"
                + " NUM_ANDAR)"
                + " values (?,?,?)");
        
        prepared2.setInt(1, idoso.getCodIdoso());
        prepared2.setInt(2, idoso.getQuarto().getNumQuarto());
        prepared2.setInt(3, idoso.getQuarto().getNumAndar());
        
        prepared2.execute();
        
    }

    @Override
    public void atualizar(Idoso idoso) throws DAOException, SQLException {
        Connection con = ConectionManager.getInstance().getConexao();
        
        PreparedStatement prepared;
        ResultSet result;
        //TODO Fazer o insert do idoso aqui
        String sql = "select * from idoso"
                + " where COD_IDOSO = ?";
        prepared = con.prepareStatement(sql);

        prepared.setInt(1, idoso.getCodIdoso());

        result = prepared.executeQuery();

        if(!result.next()){
            inserir(idoso);
        }else{
            sql =  "update idoso "
                    + "set NOM_IDOSO = ?,"
                        + "DAT_NASCIMENTO = ?,"
                        + "LOCAL_ORIGEM = ?, "
                        + "NOM_PARENT_RESP = ?,"
                        + "DSC_END_PARENT = ?,"
                        + "NUM_TEL_PARENT = ?,"
                        + "ACAMADO = ?,"
                        //+ "NUM_RG = ?,"
                        //+ "NUM_CPF = ?,"
                        + "DSC_CUIDADOS_ESP = ?,"
                        + "COD_CUIDADOR = ? " 
                 + " where COD_IDOSO = ?";
            prepared = con.prepareStatement(sql);
            
            prepared.setString(1, idoso.getNomeIdoso());
            prepared.setDate(2, idoso.getDataNascimento());
            prepared.setString(3, idoso.getLocalOrigem());
            prepared.setString(4, idoso.getNomeParenteResponsavel());
            prepared.setString(5, idoso.getEndParente());
            prepared.setString(6, idoso.getNumTelefoneParente());
            prepared.setBoolean(7, idoso.getAcamado());
            //prepared.setInt(8, idoso.getRg());
            //prepared.setString(9, idoso.getCpf());
            prepared.setString(8, idoso.getCuidadosEspeciais());
            prepared.setInt(9, idoso.getCuidador().getCodFuncionario());
            prepared.setInt(10, idoso.getCodIdoso());
            
            prepared.execute();
            
            PreparedStatement prepared2;
            ResultSet result2;
            
            String sql2 = "select num_quarto, num_andar"
                  + " from idoso_quarto "
                 + " where cod_idoso = ?";
            
            prepared2 = con.prepareStatement(sql2);
            
            prepared2.setInt(1, idoso.getCodIdoso());
            
            result2 = prepared2.executeQuery();
            
            if(result2.next()){
                
                int numQuarto = result2.getInt("NUM_QUARTO");
                int numAndar = result2.getInt("NUM_ANDAR");
                
                sql2 = "delete from idoso_quarto "
                    + " where cod_idoso = ?";
            
                prepared2 = con.prepareStatement(sql2);

                prepared2.setInt(1, idoso.getCodIdoso());

                prepared2.execute();

                PreparedStatement prepared3;

                String sql3 = "update quarto "
                        + " set num_capacidade = num_capacidade + 1, "
                        + "     estado = 'disponivel'"
                        + " where NUM_QUARTO = ? "
                        + "  and NUM_ANDAR = ? ";

                prepared3 = con.prepareStatement(sql3);

                prepared3.setInt(1, numQuarto);
                prepared3.setInt(2, numAndar);

                prepared3.execute();
                
                PreparedStatement prepared4;

                String sql4 = ("insert into idoso_quarto ("
                        + " COD_IDOSO,"
                        + " NUM_QUARTO,"
                        + " NUM_ANDAR) "
                        + " values (?,?,?)");
        
                prepared4 = con.prepareStatement(sql4);
                
                prepared4.setInt(1, idoso.getCodIdoso());
                prepared4.setInt(2, idoso.getQuarto().getNumQuarto());
                prepared4.setInt(3, idoso.getQuarto().getNumAndar());

                prepared4.execute();
                
                PreparedStatement prepared5;

                String sql5 = "update quarto "
                        + " set num_capacidade = num_capacidade - 1 ";
                if(idoso.getQuarto().getCapacidade() == 1){
                    sql5 += ", estado = 'indisponivel' ";
                }
                    sql5 += " where NUM_QUARTO = ? "
                         + "   and NUM_ANDAR = ? ";

                prepared5 = con.prepareStatement(sql5);

                prepared5.setInt(1, idoso.getQuarto().getNumQuarto());
                prepared5.setInt(2, idoso.getQuarto().getNumAndar());

                prepared5.execute();
                
            }
            
            
        }
    }

    @Override
    public void remover(Idoso idoso) throws DAOException, SQLException {
        Connection con = ConectionManager.getInstance().getConexao();
        
        PreparedStatement prepared;
        ResultSet result;
        //TODO Fazer o insert do idoso aqui
        String sql = "select * from idoso"
                + " where COD_IDOSO = ?";
        prepared = con.prepareStatement(sql);

        prepared.setInt(1, idoso.getCodIdoso());

        result = prepared.executeQuery();

        if(result.next()){
            sql = "delete IDOSO "
                 + "where COD_IDOSO = ?";
            prepared = con.prepareStatement(sql);
            prepared.setInt(1, idoso.getCodIdoso());
            prepared.execute();
        }else{
            throw new DAOException("Não foi possível encontrar o alimento informado! Cod: " + idoso.getCodIdoso());
        }
    }

    public List<Idoso> encontrarTodosCuidador(int codCuidador) throws DAOException, SQLException {
        Connection con = ConectionManager.getInstance().getConexao();
        TreeSet<Idoso> set = new TreeSet<>();
        PreparedStatement prepared;
        ResultSet result;
        //TODO Fazer o insert do idoso aqui
        String sql = "select * from idoso "
                   + " where COD_CUIDADOR = ? ";
        prepared = con.prepareStatement(sql);
        prepared.setInt(1, codCuidador);

        result = prepared.executeQuery();

        Idoso a = null;
        while(result.next()){
            int codIdoso = result.getInt("COD_IDOSO");
            String nome = result.getString("NOM_IDOSO");
            Date data = result.getDate("DAT_NASCIMENTO");
            String local = result.getString("LOCAL_ORIGEM");
            boolean acamado = result.getBoolean("ACAMADO");
            int rg = result.getInt("NUM_RG");
            String nomeParent = result.getString("NOM_PARENT_RESP");
            String end = result.getString("DSC_END_PARENT");
            String cpf = result.getString("NUM_CPF");
            
            Funcionario cuidador = ImplFuncionarioDAO.getInstance().encontrarPorCodigo(codCuidador);
            String fone = result.getString("NUM_TEL_PARENT");
            String cuidados = result.getString("DSC_CUIDADOS_ESP");
            
            a = new Idoso(codIdoso,nome,data,local,acamado,rg,nomeParent,end,cpf,cuidador,fone,cuidados);

            set.add(a);
        }
        if(set.isEmpty()){
            throw new DAOException("Não foi possível encontrar idosos do cuidador = " + codCuidador);
        }
        ArrayList<Idoso> list = new ArrayList<>();
        for (Iterator<Idoso> it = set.iterator(); it.hasNext();) {
            Idoso idoso = it.next();
            list.add(idoso);
        }
        return list;
    }
    
    public List<Idoso> encontrarTodosIdosos() throws DAOException, SQLException {
        Connection con = ConectionManager.getInstance().getConexao();
        TreeSet<Idoso> set = new TreeSet<>();
        PreparedStatement prepared;
        ResultSet result;
        //TODO Fazer o insert do idoso aqui
        String sql = "select * from idoso";
        prepared = con.prepareStatement(sql);

        result = prepared.executeQuery();

        Idoso a = null;
        while(result.next()){
            int codIdoso = result.getInt("COD_IDOSO");
            String nome = result.getString("NOM_IDOSO");
            Date data = result.getDate("DAT_NASCIMENTO");
            String local = result.getString("LOCAL_ORIGEM");
            boolean acamado = result.getBoolean("ACAMADO");
            int rg = result.getInt("NUM_RG");
            String nomeParent = result.getString("NOM_PARENT_RESP");
            String end = result.getString("DSC_END_PARENT");
            String cpf = result.getString("NUM_CPF");
            int codCuidador = result.getInt("COD_CUIDADOR");
            Funcionario cuidador = ImplFuncionarioDAO.getInstance().encontrarPorCodigo(codCuidador);
            String fone = result.getString("NUM_TEL_PARENT");
            String cuidados = result.getString("DSC_CUIDADOS_ESP");
            
            a = new Idoso(codIdoso,nome,data,local,acamado,rg,nomeParent,end,cpf,cuidador,fone,cuidados);

            Quarto q = ImplQuartoDAO.getInstance().encontraQuartoIdoso(codIdoso);
            a.setQuarto(q);
            set.add(a);
        }
        if(set.isEmpty()){
            throw new DAOException("Não foi possível encontrar alimentos");
        }
        ArrayList<Idoso> list = new ArrayList<>();
        for (Iterator<Idoso> it = set.iterator(); it.hasNext();) {
            Idoso idoso = it.next();
            list.add(idoso);
        }
        return list;
    }
    
    public List<Idoso> encontrarTodosEvento(int evento) throws DAOException, SQLException {
        Connection con = ConectionManager.getInstance().getConexao();
        List<Idoso> lista = new ArrayList<>();
        PreparedStatement prepared;
        ResultSet result;
        //TODO Fazer o insert do idoso aqui
        String sql = "select * from idoso_evento "
                    + "where COD_EVENTO = ?";
        prepared = con.prepareStatement(sql);

        prepared.setInt(1, evento);

        result = prepared.executeQuery();

        Idoso a = null;
        while(result.next()){
            int codIdoso = result.getInt("COD_IDOSO");

            a = encontrarPorCodigo(codIdoso);
            lista.add(a);
        }
        if(lista.isEmpty()){
            throw new DAOException("Não foi possível encontrar idosos do evento");
        }
        return lista;
    }

    public Idoso encontrarPorCodigo(int codigo) throws DAOException, SQLException {
        Connection con = ConectionManager.getInstance().getConexao();
        
        PreparedStatement prepared;
        ResultSet result;
        //TODO Fazer o insert do idoso aqui
        String sql = "select * from idoso "
                    + "where COD_IDOSO = ?";
        prepared = con.prepareStatement(sql);

        prepared.setInt(1, codigo);

        result = prepared.executeQuery();

        Idoso a = null;
        while(result.next()){
            int codIdoso = result.getInt("COD_IDOSO");
            String nome = result.getString("NOM_IDOSO");
            Date data = result.getDate("DAT_NASCIMENTO");
            String local = result.getString("LOCAL_ORIGEM");
            boolean acamado = result.getBoolean("ACAMADO");
            int rg = result.getInt("NUM_RG");
            String nomeParent = result.getString("NOM_PARENT_RESP");
            String end = result.getString("DSC_END_PARENT");
            String cpf = result.getString("NUM_CPF");
            int codCuidador = result.getInt("COD_CUIDADOR");
            Funcionario cuidador = ImplFuncionarioDAO.getInstance().encontrarPorCodigo(codCuidador);
            String fone = result.getString("NUM_TEL_PARENT");
            String cuidados = result.getString("DSC_CUIDADOS_ESP");
            
            a = new Idoso(codIdoso,nome,data,local,acamado,rg,nomeParent,end,cpf,cuidador,fone,cuidados);
        }

        if(a == null){
            throw new DAOException("Não foi possível o encontrar alimento! Cod = " + codigo);
        }
        return a;
    }
    
    public int encontrarCodMax() throws DAOException, SQLException{
        Connection con = ConectionManager.getInstance().getConexao();
        
        PreparedStatement prepared;
        ResultSet result;
        //TODO Fazer o insert do idoso aqui
        String sql = "select max(cod_idoso) + 1 as VAL from idoso ";
        
        prepared = con.prepareStatement(sql);

        result = prepared.executeQuery();

        if(result.next()){
            return result.getInt("VAL");
        }else{
            return 1;
        }
    }
    
    public boolean validaCPF(String CPF) throws SQLException{
        Connection con = ConectionManager.getInstance().getConexao();
        
        PreparedStatement prepared;
        ResultSet result;
        //TODO Fazer o insert do idoso aqui
        String sql = "select 1 as VAL from idoso "
                   + " where NUM_CPF = ?";
        
        prepared = con.prepareStatement(sql);

        prepared.setString(1, CPF);
        
        result = prepared.executeQuery();

        if(result.next()){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean validaRG(int rg) throws SQLException{
        Connection con = ConectionManager.getInstance().getConexao();
        
        PreparedStatement prepared;
        ResultSet result;
        //TODO Fazer o insert do idoso aqui
        String sql = "select 1 as VAL from idoso "
                   + " where NUM_RG = ?";
        
        prepared = con.prepareStatement(sql);

        prepared.setInt(1, rg);
        
        result = prepared.executeQuery();

        if(result.next()){
            return true;
        }else{
            return false;
        }
    }
}
