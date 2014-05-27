/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Control.Impl;

import Control.Impl.Exception.DAOException;
import Control.Impl.Exception.DAOException;
import Control.Interface.IDAO;
import Model.Evento;
import Model.Funcionario;
import Model.Idoso;
import Util.ConectionManager;
import Util.DataConverter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Bruno
 */
public class ImplEventoDAO implements IDAO<Evento> {

    private static ImplEventoDAO instance;

    private ImplEventoDAO() {
    }

    public static ImplEventoDAO getInstance() {
        if (instance == null) {
            instance = new ImplEventoDAO();
        }
        return instance;
    }

    @Override
    public void inserir(Evento evento) throws DAOException, SQLException {
        Connection con = ConectionManager.getInstance().getConexao();

        PreparedStatement prepared;
        //TODO Fazer o insert do idoso aqui
        prepared = con.prepareStatement("insert into evento ("
                + "COD_EVENTO,"
                + "COD_FUNCIONARIO,"
                + "DAT_EVENTO,"
                + "NOM_EVENTO,"
                + "DSC_DESCRICAO_EVENTO) "
                + "values (?,?,?,?,?)");

        prepared.setInt(1, evento.getCodigo());
        prepared.setInt(2, evento.getFunc().getCodFuncionario());
        prepared.setString(3, evento.getDataEvento());
        prepared.setString(4, evento.getNomeEvento());
        prepared.setString(5, evento.getDescricaoEvento());

        prepared.execute();
        
        PreparedStatement prepared2;
        
        for(Idoso i : evento.getListaIdosos()){
            
            prepared2 = con.prepareStatement("insert into idoso_evento ("
                + "COD_IDOSO,"
                + "COD_EVENTO) "
                + " values (?,?)");

            prepared2.setInt(1, i.getCodIdoso());
            prepared2.setInt(2, evento.getCodigo());

            prepared2.execute();
        }
    }
    
    @Override
    public void atualizar(Evento evento) throws DAOException, SQLException {
        Connection con = ConectionManager.getInstance().getConexao();

        PreparedStatement prepared;
        //TODO Fazer o insert do idoso aqui
        prepared = con.prepareStatement("update evento "
                + " set COD_FUNCIONARIO = ?,"
                    + " DAT_EVENTO = ? ,"
                    + " NOM_EVENTO = ? ,"
                    + " DSC_DESCRICAO_EVENTO = ? "
                    + " where cod_evento = ? ");

        prepared.setInt(1, evento.getFunc().getCodFuncionario());
        prepared.setString(2, evento.getDataEvento());
        prepared.setString(3, evento.getNomeEvento());
        prepared.setString(4, evento.getDescricaoEvento());
        prepared.setInt(5, evento.getCodigo());

        prepared.execute();
        
        PreparedStatement prepared2;
        
        prepared2 = con.prepareStatement("delete from idoso_evento"
                + " where cod_evento = ?");
        
        prepared2.setInt(1, evento.getCodigo());
        
        prepared2.execute();
        
        for(Idoso i : evento.getListaIdosos()){
            
            prepared2 = con.prepareStatement("insert into idoso_evento ("
                + "COD_IDOSO,"
                + "COD_EVENTO) "
                + " values (?,?)");

            prepared2.setInt(1, i.getCodIdoso());
            prepared2.setInt(2, evento.getCodigo());

            prepared2.execute();
        }
    }

    @Override
    public void remover(Evento evento) throws DAOException, SQLException {
        Connection con = ConectionManager.getInstance().getConexao();

        PreparedStatement prepared;
        ResultSet result;
        PreparedStatement prepared2;

        //TODO Fazer o insert do idoso aqui
        String sql = "select * from evento"
                + " where COD_EVENTO = ?";
        prepared = con.prepareStatement(sql);

        prepared.setInt(1, evento.getCodigo());

        result = prepared.executeQuery();

        if (result.next()) {
            sql = "delete evento "
                    + "where COD_EVENTO = ?";
            prepared = con.prepareStatement(sql);
            prepared.setInt(1, evento.getCodigo());

            prepared.execute();

            sql = "delete idoso_evento "
                    + "where COD_EVENTO = ?";
            prepared2 = con.prepareStatement(sql);
            prepared2.setInt(1, evento.getCodigo());

            prepared.execute();

        } else {
            throw new DAOException("Não foi possível encontrar o evento informado! Cod: " + evento.getCodigo());
        }
    }

    public List<Evento> encontrarTodos() throws DAOException, SQLException {
        // IMPLEMENTAR MÉTODO
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public List<Evento> encontrarEventosAposData(String data) throws  DAOException, SQLException{
        Connection con = ConectionManager.getInstance().getConexao();
        List<Evento> lista = new ArrayList<>();
        PreparedStatement prepared;
        ResultSet result;
        //TODO Fazer o insert do idoso aqui
        String sql = "select * from evento "
                   + " where to_date(DAT_EVENTO,'DD/MM/YYYY') >= to_date(?,'DD/MM/YYYY')";
        prepared = con.prepareStatement(sql);

        prepared.setString(1,data);
        
        result = prepared.executeQuery();

        Evento a = null;
        while (result.next()) {
            int codEvento = result.getInt("COD_EVENTO");

            int codFunc = result.getInt("COD_FUNCIONARIO");
            Funcionario func = ImplFuncionarioDAO.getInstance().encontrarPorCodigo(codFunc);
            List<Idoso> listaIdoso;
            try{
                listaIdoso = ImplIdosoDAO.getInstance().encontrarTodosEvento(codEvento);
            }catch(DAOException ex){
                continue;
            }

            String datEvento = result.getString("DAT_EVENTO");
            String nomEvento = result.getString("NOM_EVENTO");
            String dscEvento = result.getString("DSC_DESCRICAO_EVENTO");

            a = new Evento(codEvento, func, listaIdoso, datEvento, nomEvento, dscEvento);
            lista.add(a);
        }

        if (lista.isEmpty()) {
            throw new DAOException("Não foi possível o encontrar Evento!");
        }
        return lista;
    }

    public Evento encontrarPorCodigo(int codigo) throws DAOException, SQLException {
        Connection con = ConectionManager.getInstance().getConexao();

        PreparedStatement prepared;
        ResultSet result;
        //TODO Fazer o insert do idoso aqui
        String sql = "select * from evento "
                + "where COD_EVENTO = ?";
        prepared = con.prepareStatement(sql);

        prepared.setInt(1, codigo);

        result = prepared.executeQuery();

        Evento a = null;
        while (result.next()) {
            int codEvento = result.getInt("COD_EVENTO");

            int codFunc = result.getInt("COD_FUNCIONARIO");
            Funcionario func = ImplFuncionarioDAO.getInstance().encontrarPorCodigo(codFunc);

            List<Idoso> listaIdoso = ImplIdosoDAO.getInstance().encontrarTodosEvento(codEvento);

            String datEvento = result.getString("DAT_EVENTO");
            String nomEvento = result.getString("NOM_EVENTO");
            String dscEvento = result.getString("DSC_DESCRICAO_EVENTO");

            a = new Evento(codEvento, func, listaIdoso, datEvento, nomEvento, dscEvento);
        }

        if (a == null) {
            throw new DAOException("Não foi possível o encontrar evento! Cod = " + codigo);
        }
        return a;
    }
    
    public Evento encontrarProximoEvento(String data) throws DAOException, SQLException {
        SimpleDateFormat format = new SimpleDateFormat("DD/MM/YYYY");
        Date dataMinima;
        Evento proxEvento = null;
        List<Evento> lista = ImplEventoDAO.getInstance().encontrarEventosAposData(data);
        if(lista.get(0) != null) {
            dataMinima = DataConverter.stringTypeToSQLDate(lista.get(0).getDataEvento());
            for (Iterator<Evento> it = lista.iterator(); it.hasNext();) {
                Evento evento = it.next();
                if(dataMinima.before(DataConverter.stringTypeToSQLDate(evento.getDataEvento()))) {
                    dataMinima = DataConverter.stringTypeToSQLDate(evento.getDataEvento());
                    proxEvento = evento;
                }
            }
            return proxEvento;
        }
        else {
            return null;
        }
    }
    
    public int encontrarCodMax() throws DAOException, SQLException{
        Connection con = ConectionManager.getInstance().getConexao();

        PreparedStatement prepared;
        ResultSet result;
        //TODO Fazer o insert do idoso aqui
        String sql = "select max(cod_evento) + 1 as VAL from evento ";
        prepared = con.prepareStatement(sql);

        result = prepared.executeQuery();

        if (result.next()) {
            return result.getInt("VAL");
        }else{
            return 1;
        }
    }
    
}