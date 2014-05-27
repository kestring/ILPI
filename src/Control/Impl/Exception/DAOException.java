/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Control.Impl.Exception;

/**
 *
 * @author Bruno
 */
public class DAOException extends Exception{
    
    private int codigo;
    
    public DAOException(String message){
        super(message);
    }
    
    public DAOException(String message, int codigo){
        super(message);
        this.codigo = codigo;
    }
    
    @Override
    public String getMessage(){
        return this.codigo + " - " + super.getMessage();
    }
    
    public int getCodigo(){
        return this.codigo;
    }
}
