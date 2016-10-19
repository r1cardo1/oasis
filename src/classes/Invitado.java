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
public class Invitado implements Serializable{
      String nombre;
      String apellido;
      String cedula;
      String contrato;
      String fecha;

    public Invitado(String nombre, String apellido, String cedula, String contrato, String fecha) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.contrato = contrato;
        this.fecha = fecha;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

      public String getContrato() {
            return contrato;
      }

      public void setContrato(String contrato) {
            this.contrato = contrato;
      }

      public String getNombre() {
            return nombre;
      }

      public String getApellido() {
            return apellido;
      }

      public String getCedula() {
            return cedula;
      }

      public void setNombre(String nombre) {
            this.nombre = nombre;
      }

      public void setApellido(String apellido) {
            this.apellido = apellido;
      }

      public void setCedula(String cedula) {
            this.cedula = cedula;
      }     
      
}
