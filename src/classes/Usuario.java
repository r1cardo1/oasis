/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

/**
 *
 * @author Ricardo
 */
public class Usuario {
      private String nombre;
      private String apellido;
      private String usuario;
      private String clave;

      public Usuario(String nombre, String apellido, String usuario, String clave) {
            this.nombre = nombre;
            this.apellido = apellido;
            this.usuario = usuario;
            this.clave = clave;
      }

      public String getNombre() {
            return nombre;
      }

      public String getApellido() {
            return apellido;
      }

      public String getUsuario() {
            return usuario;
      }

      public String getClave() {
            return clave;
      }

      public void setNombre(String nombre) {
            this.nombre = nombre;
      }

      public void setApellido(String apellido) {
            this.apellido = apellido;
      }

      public void setUsuario(String usuario) {
            this.usuario = usuario;
      }

      public void setClave(String clave) {
            this.clave = clave;
      }
              
              
}
