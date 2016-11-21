/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

import java.io.Serializable;

/**
 * 
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class PaseCortesia implements Serializable{

    public String nombre;
    public String cedula;
    public String telefono;
    public String fecha;
    public String fechaCreado;
    public String invitados;
    public String codigo;
    public String usuario;
    public String generado;

     public String getFechaCreado() {
          return fechaCreado;
     }

     public String getCodigo() {
          return codigo;
     }

     public String getUsuario() {
          return usuario;
     }

     public String getGenerado() {
          return generado;
     }

     public void setFechaCreado(String fechaCreado) {
          this.fechaCreado = fechaCreado;
     }

     public void setCodigo(String codigo) {
          this.codigo = codigo;
     }

     public void setUsuario(String usuario) {
          this.usuario = usuario;
     }

     public void setGenerado(String generado) {
          this.generado = generado;
     }

    public PaseCortesia(String nombre, String cedula, String telefono, String fecha, String invitados,String usuario,String fechaCreado,String codigo) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.fecha = fecha;
        this.invitados = invitados;
        this.usuario=usuario;
        this.fechaCreado=fechaCreado;
        this.codigo=codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getFecha() {
        return fecha;
    }

    public String getInvitados() {
        return invitados;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setInvitados(String invitados) {
        this.invitados = invitados;
    }
    
    
}
