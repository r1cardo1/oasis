/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;

/**
 *
 * @author Ricardo Marcano
 */
public class Busqueda implements Serializable{
    String usuario;
    String tipo;
    String filtro;
    String fecha;
    String hora;

    public Busqueda(String usuario, String tipo, String filtro, String fecha, String hora) {
        this.usuario = usuario;
        this.tipo = tipo;
        this.filtro = filtro;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public String getFiltro() {
        return filtro;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
    
}
