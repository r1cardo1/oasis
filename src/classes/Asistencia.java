/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

/**
 *
 * @author Ricardo Marcano
 */
public class Asistencia {
    String mesa;
    String invitados;
    String fecha;
    String hora;
    String cedula;
    String nombre;
    String contrato;
    String plan;

    public Asistencia(String invitados, String cedula, String nombre, String contrato, String plan) {
        this.invitados = invitados;
        this.cedula = cedula;
        this.nombre = nombre;
        this.contrato = contrato;
        this.plan = plan;
    }  

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContrato() {
        return contrato;
    }

    public String getPlan() {
        return plan;
    }

    public Asistencia(String mesa, String invitados, String fecha, String hora, String cedula, String nombre, String contrato, String plan) {
        this.mesa = mesa;
        this.invitados = invitados;
        this.fecha = fecha;
        this.hora = hora;
        this.cedula = cedula;
        this.nombre = nombre;
        this.contrato = contrato;
        this.plan = plan;
    }

    public Asistencia(String mesa, String invitados, String fecha, String hora) {
        this.mesa = mesa;
        this.invitados = invitados;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getMesa() {
        return mesa;
    }

    public String getInvitados() {
        return invitados;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public void setInvitados(String invitados) {
        this.invitados = invitados;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
    
    
}
