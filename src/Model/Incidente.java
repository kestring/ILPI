/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Date;

/**
 *
 * @author Bruno
 */
public class Incidente {
    
    private int codigoIncidente;
    private Funcionario func;
    private Idoso idoso;
    private Date dataIncidente;
    private String descricaoIncidente;
    private String nomeOcorrido;

    public Incidente() {
        this(0, null, null, null, null, null);
    }
    
    public Incidente(int codigoIncidente, Funcionario func, Idoso idoso, Date dataIncidente, String descricaoIncidente, String nome) {
        this.codigoIncidente = codigoIncidente;
        this.func = func;
        this.idoso = idoso;
        this.dataIncidente = dataIncidente;
        this.descricaoIncidente = descricaoIncidente;
        this.nomeOcorrido = nome;
    }

    public int getCodigoIncidente() {
        return codigoIncidente;
    }

    public void setCodigoIncidente(int codigoIncidente) {
        this.codigoIncidente = codigoIncidente;
    }

    public Funcionario getFunc() {
        return func;
    }

    public void setFunc(Funcionario func) {
        this.func = func;
    }

    public Idoso getIdoso() {
        return idoso;
    }

    public void setIdoso(Idoso idoso) {
        this.idoso = idoso;
    }

    public Date getDataIncidente() {
        return dataIncidente;
    }

    public void setDataIncidente(Date dataIncidente) {
        this.dataIncidente = dataIncidente;
    }

    public String getDescricaoIncidente() {
        return descricaoIncidente;
    }

    public void setDescricaoIncidente(String descricaoIncidente) {
        this.descricaoIncidente = descricaoIncidente;
    }

    public String getNomeOcorrido() {
        return nomeOcorrido;
    }

    public void setNomeOcorrido(String nomeOcorrido) {
        this.nomeOcorrido = nomeOcorrido;
    }
    
}
