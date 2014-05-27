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
public class Cardapio {
    
    private int indice;
    private int codigo;
    private String nome;
    private Date dataCriacao;
    private Date dataFim;

    public Cardapio() {
        this(0, 0,null, null, null);
    }
    
    public Cardapio(int codigo,int indice,String nome, Date dataCriacao, Date dataFim) {
        this.indice = indice;
        this.codigo = codigo;
        this.nome = nome;
        this.dataCriacao = dataCriacao;
        this.dataFim = dataFim;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    @Override
    public String toString() {
        return this.indice + " - " + this.nome;
    }
    
}
