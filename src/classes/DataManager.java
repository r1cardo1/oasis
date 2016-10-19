/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Ricardo
 */
public class DataManager implements Serializable{

    private DataConection conexion = new DataConection();
    private Connection con;
    private Statement st;
    private ResultSet rs = null;

    public DataManager() {
        try {
            if ((con = conexion.getConexionMYSQL()) == null) {
                JOptionPane.showMessageDialog(null, "-Error con la conexion a la BD");
                return;
            }
            st = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet login(String user) {
        try {
            String query = "SELECT * FROM oasisclub.usuarios WHERE usuario LIKE '" + user + "';";
            rs = st.executeQuery(query);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet searchClientbyCI(String str) {
        try {
            String sql = "SELECT * FROM oasisclub.clientes WHERE cedula LIKE '%" + str + "%'";
            rs = st.executeQuery(sql);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet searchClientbyContract(String str) {
        try {
            String sql = "SELECT * FROM oasisclub.clientes WHERE contrato LIKE '%" + str + "%';";
            rs = st.executeQuery(sql);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet searchClientbyName(String str) {
        try {
            String sql = "SELECT * FROM oasisclub.clientes WHERE nombre LIKE '%" + str + "%';";
            rs = st.executeQuery(sql);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String openTable(String contrato, String ninvitados,String invad, String fecha, String hora, String usuario) {
        try {
            String query = "INSERT INTO oasisclub.asistencias(contrato,num_inv,invad,fecha,hora,user) VALUES ('" + contrato + "','" + ninvitados + "','"+invad+"','" + fecha + "','" + hora + "','" + usuario + "');";
            st.executeUpdate(query);
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }

    }

    public ResultSet visits() {
        try {
            String query = "SELECT * FROM oasisclub.asistencias;";
            rs = st.executeQuery(query);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getRestringidos() {
        try {
            String query = "SELECT * FROM oasisclub.clientes WHERE restringido = 'SI';";
            rs = st.executeQuery(query);
            return rs;
        } catch (Exception e) {
            return rs;
        }
    }

    public void logLogin(String nombre, String apellido, String usuario, String fecha, String hora) {
        try {
            String query = "INSERT INTO oasisclub.login(nombre,apellido,usuario,fecha,hora) VALUES ('" + nombre + "','" + apellido + "','" + usuario + "','" + fecha + "','" + hora + "');";
            st.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logfailLogin(String usuario, String clave, String equipo, String fecha, String hora) {
        try {
            String query = "INSERT INTO oasisclub.faillogin(usuario,clave,equipo,fecha,hora) VALUES ('" + usuario + "','" + clave + "','" + equipo + "','" + fecha + "','" + hora + "');";
            st.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void search(String usuario, String modo, String filtro, String fecha, String hora) {
        try {
            String query = "INSERT INTO oasisclub.search(usuario,modo,filtro,fecha,hora) VALUES('" + usuario + "','" + modo + "','" + filtro + "','" + fecha + "','" + hora + "');";
            st.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet getUsuarios() {
        rs = null;
        try {
            String query = "SELECT * FROM oasisclub.usuarios;";
            rs = st.executeQuery(query);
            return rs;
        } catch (Exception e) {
            return null;
        }
    }

    public void newUser(Usuario u) {
        try {
            String query = "INSERT INTO oasisclub.usuarios(nombre,apellido,usuario,clave,nivel) "
                    + "SELECT * FROM (SELECT '" + u.getNombre() + "','" + u.getApellido() + "','" + u.getUsuario() + "','" + u.getClave() + "'," + Integer.toString(u.getNivel()) + ") AS tmp"
                    + " WHERE NOT EXISTS ("
                    + "SELECT usuario FROM oasisclub.usuarios WHERE usuario = '" + u.getUsuario() + "') LIMIT 1"
                    + ";";
            st.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet searchUserbyUsername(String u) {
        rs = null;
        try {
            String query = "SELECT * FROM oasisclub.usuarios WHERE usuario = '" + u + "'";
            rs = st.executeQuery(query);
            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void deleteUserByUsername(String u) {
        rs = null;
        try {
            String query = "DELETE FROM oasisclub.usuarios WHERE usuario = '" + u + "';";
            st.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public ResultSet getLogins(String u){
        try{
            String query = "SELECT COUNT(usuario) FROM oasisclub.login WHERE usuario = '"+u+"';";
            rs = st.executeQuery(query);
            return rs;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public ResultSet getSearchByUser(String u){
          try{
                String query ="SELECT COUNT(usuario) FROM oasisclub.search WHERE usuario = '"+u+"';";
                rs = st.executeQuery(query);
                return rs;
          }catch(Exception e){
          System.out.println(e.getMessage());
          return null;
    }
    }
    
    public ResultSet getAllSearch(){
        try{
                String query ="SELECT * FROM oasisclub.search;";
                rs = st.executeQuery(query);
                return rs;
          }catch(Exception e){
          System.out.println(e.getMessage());
          return null;
    }
    }
    
    public ResultSet getLogLogins(){
        try{
            String query ="SELECT * FROM oasisclub.login;";
            rs = st.executeQuery(query);
            return rs;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean update(int id, String nombre, String genero, int anio, String actor, String pais) {
        try {
            String query = "UPDATE peliculas SET"
                    + " nombre = '" + nombre + "',"
                    + " genero = '" + genero + "',"
                    + " anio = '" + anio + "',"
                    + " actor = '" + actor + "',"
                    + " pais = '" + pais + "' WHERE id = '" + id + "';";
            st.executeUpdate(query);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

      public ResultSet getPlans() {
            try{
                  String query = "SELECT * FROM oasisclub.planes;";
                  rs = st.executeQuery(query);
                  return rs;
            }catch(Exception e){
                  System.out.println(e.getMessage());
                  return null;
            }
      }

      public void addPlan(Plan plan) {
            try{
                  String query = "INSERT INTO oasisclub.planes(plan,invitados) VALUES ('"+plan.getPlan()+"',"+Integer.toString(plan.getCant())+");";
                  st.executeUpdate(query);
            }catch(Exception ex){
                  System.out.println(ex.getMessage());
            }
      }

      public void deletePlan(Plan plan) {
            try{
                  String query = "DELETE FROM oasisclub.planes WHERE plan = '"+plan.getPlan()+"';";
                  st.executeUpdate(query);
            }catch(Exception ex){
                  System.out.println(ex.getMessage());
            }
      }

      public void updatePlan(Plan neww,Plan old) {
            try{
                  String query = "UPDATE oasisclub.planes SET plan='"+neww.getPlan()+"',invitados="+Integer.toString(neww.getCant())+" WHERE plan = '"+old.getPlan()+"'; ";
                  System.out.println(query);
                  st.executeUpdate(query);
            }catch(Exception e){
                  System.out.println(e.getMessage());
            }
      }

      public ResultSet getCantByPlan(String plan) {
            try{
                  String query = "SELECT * FROM oasisclub.planes WHERE plan = '"+plan+"';";
                  rs = st.executeQuery(query);
                  return rs;
            }catch(Exception e){
                  System.out.println(e.getMessage());
                  return null;
            }
      }

      public void addInvad(String nombre, String apellido, String cedula, String contrato,String fecha) {
            try{
                  String query ="INSERT INTO oasisclub.invad(nombre,apellido,cedula,contrato,fecha) VALUES ('"+nombre+"','"+apellido+"','"+cedula+"','"+contrato+"','"+fecha+"');";
                  st.executeUpdate(query);
            }catch(Exception ex){
                  System.out.println(ex.getMessage());
            }
      }

    public ResultSet getInvitados() {
        try{
            String query = "SELECT * FROM oasisclub.invad";
            rs=st.executeQuery(query);
            return rs;
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public ResultSet getAsistenciaPorContrato(String contrato) {
        try{
            String query="SELECT * FROM oasisclub.asistencias WHERE contrato='"+contrato+"';";
            rs=st.executeQuery(query);
            return rs;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

      public ResultSet getReservas() {
            try{
                  String query = "SELECT * FROM oasisclub.reserva;";
                  rs = st.executeQuery(query);
                  return rs;
            }catch(Exception ex){
                  ex.printStackTrace();
                  return null;
            }
      }

      public ResultSet getReservasByName(String titular) {
            try{
                  String query ="SELECT * FROM oasisclub.reserva WHERE titular LIKE '%"+titular+"%';";
                  rs=st.executeQuery(query);
                  return rs;                  
            }catch(Exception ex){
                  ex.printStackTrace();
                  return null;
            }
      }

      public ResultSet getReservasByCI(String ci) {
            try{
                  String query ="SELECT * FROM oasisclub.reserva WHERE cedula LIKE '%"+ci+"%';";
                  rs=st.executeQuery(query);
                  return rs;                  
            }catch(Exception ex){
                  ex.printStackTrace();
                  return null;
            }
      }

      public void guardaReservacion(Reserva reserva) {
            try{
                  String query="INSERT INTO oasisclub.reserva(titular,cedula,telefono,plan,fecha,observacion,invitados) VALUES ('"+reserva.getTitular()+"','"+reserva.getCedula()+"','"+reserva.getTelefono()+"','"+reserva.getPlan()+"','"+reserva.getFecha()+"','"+reserva.getObservaciones()+"','"+reserva.invitados+"');";
                  st.executeUpdate(query);
            }catch(Exception ex){
                  ex.printStackTrace();
            }
      }

      public void actualizaReservacion(Reserva old, Reserva neww) {
            try{
                  String query ="UPDATE oasisclub.reserva SET titular='"+neww.titular+"', "
                          + "cedula='"+neww.cedula+"', "
                          + "telefono='"+neww.telefono+"', "
                          + "plan='"+neww.plan+"', "
                          + "fecha='"+neww.fecha+"', "
                          + "observacion='"+neww.observaciones+"',"
                          + "invitados='"+neww.invitados+"' "
                          + "WHERE "
                          + "cedula='"+old.cedula+"';";
                  st.executeUpdate(query);
            }catch(Exception ex){
                  ex.printStackTrace();
            }
      }

      public void eliminaReserva(Reserva r) {
            try{
                  String query="DELETE FROM oasisclub.reserva WHERE cedula='"+r.cedula+"' AND fecha='"+r.fecha+"';";
                  st.executeUpdate(query);
            }catch(Exception ex){
                  ex.printStackTrace();
            }
            
      }
      
      
}
