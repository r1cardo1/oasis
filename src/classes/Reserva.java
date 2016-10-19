/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;

/**
 *
 * @author Ricardo
 */
public class Reserva implements Serializable{

      public String titular;
      public String cedula;
      public String telefono;
      public String plan;
      public String invitados;
      public String fecha;
      public String observaciones;

      public Reserva(String titular, String cedula, String telefono, String plan, String invitados, String fecha, String observaciones) {
            this.titular = titular;
            this.cedula = cedula;
            this.telefono = telefono;
            this.plan = plan;
            this.invitados = invitados;
            this.fecha = fecha;
            this.observaciones = observaciones;
      }

      public String getObservaciones() {
            return observaciones;
      }

      public void setObservaciones(String observaciones) {
            this.observaciones = observaciones;
      }

      public String getTitular() {
            return titular;
      }

      public String getCedula() {
            return cedula;
      }

      public String getTelefono() {
            return telefono;
      }

      public String getPlan() {
            return plan;
      }

      public String getInvitados() {
            return invitados;
      }

      public String getFecha() {
            return fecha;
      }

      public void setTitular(String titular) {
            this.titular = titular;
      }

      public void setCedula(String cedula) {
            this.cedula = cedula;
      }

      public void setTelefono(String telefono) {
            this.telefono = telefono;
      }

      public void setPlan(String plan) {
            this.plan = plan;
      }

      public void setInvitados(String invitados) {
            this.invitados = invitados;
      }

      public void setFecha(String fecha) {
            this.fecha = fecha;
      }

}
