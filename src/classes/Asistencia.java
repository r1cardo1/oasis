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
public class Asistencia implements Serializable{

      String invitados;
      String invad;
      String fecha;
      String hora;
      String cedula;
      String nombre;
      String contrato;

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }
      String plan;
      String usuario;

   public Asistencia(String invitados, String fecha, String hora, String contrato, String invad, String usuario) {
        this.invitados=invitados;
        this.fecha=fecha;
        this.hora=hora;
        this.contrato=contrato;
        this.invad=invad;
        this.usuario=usuario;
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

      public Asistencia(String invitados, String fecha, String hora, String cedula, String nombre, String contrato, String plan, String invad) {
            this.invitados = invitados;
            this.fecha = fecha;
            this.hora = hora;
            this.cedula = cedula;
            this.nombre = nombre;
            this.contrato = contrato;
            this.plan = plan;
            this.invad = invad;
      }

    public void setInvad(String invad) {
        this.invad = invad;
    }

    public String getInvad() {
        return invad;
    }

      public Asistencia(String mesa, String invitados, String fecha, String hora) {

            this.invitados = invitados;
            this.fecha = fecha;
            this.hora = hora;
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
