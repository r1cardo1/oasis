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
public class Autorizado implements Serializable{
    public String usuario;
     public String cedula;
     public String cliente;
     public String contrato;
     public String plan;
     public String fecha;
     public String hora;
     public String invitados;
     public String autorizado;

    public Autorizado(String usuario, String cedula, String cliente, String contrato, String plan, String fecha, String hora, String invitados, String autorizado) {
        this.usuario = usuario;
        this.cedula = cedula;
        this.cliente = cliente;
        this.contrato = contrato;
        this.plan = plan;
        this.fecha = fecha;
        this.hora = hora;
        this.invitados = invitados;
        this.autorizado = autorizado;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getCedula() {
        return cedula;
    }

    public String getCliente() {
        return cliente;
    }

    public String getContrato() {
        return contrato;
    }

    public String getPlan() {
        return plan;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getInvitados() {
        return invitados;
    }

    public String getAutorizado() {
        return autorizado;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setInvitados(String invitados) {
        this.invitados = invitados;
    }

    public void setAutorizado(String autorizado) {
        this.autorizado = autorizado;
    }
     
     
}
