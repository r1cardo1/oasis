/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Ricardo
 */
public class DataManager {

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

    public Usuario login(Usuario user) {
        Usuario u = null;
        try {
            String query = "SELECT * FROM oasisclub.usuarios WHERE usuario LIKE '" + user + "';";
            rs = st.executeQuery(query);
            if(rs.next())
                u=new Usuario(rs.getString("nombre"),rs.getString("apellido"),rs.getString("usuario"),rs.getString("clave"),rs.getInt("nivel"));
            if(u!=null)
                if(user.getUsuario().equals(u.getUsuario()))
                    if(user.getClave().equals(u.getClave()))
                        return u;
            return null;           
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Cliente> searchClientbyCI(String str) {
        ArrayList<Cliente> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM oasisclub.clientes WHERE cedula LIKE '%" + str + "%'";
            rs = st.executeQuery(sql);
            while(rs.next())
                list.add(new Cliente(rs.getString("cedula"),rs.getString("nombre"),rs.getString("contrato"),
                rs.getString("plan"),rs.getString("banco"),rs.getString("restringido")));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
    }

    public ArrayList<Cliente> searchClientbyContract(String str) {
        ArrayList<Cliente> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM oasisclub.clientes WHERE contrato LIKE '%" + str + "%';";
            rs = st.executeQuery(sql);
            while(rs.next())
                list.add(new Cliente(rs.getString("cedula"),rs.getString("nombre"),rs.getString("contrato"),
                rs.getString("plan"),rs.getString("banco"),rs.getString("restringido")));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
    }

    public ArrayList<Cliente> searchClientbyName(String str) {
        ArrayList<Cliente> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM oasisclub.clientes WHERE nombre LIKE '%" + str + "%';";
            rs = st.executeQuery(sql);
            while(rs.next())
                list.add(new Cliente(rs.getString("cedula"),rs.getString("nombre"),rs.getString("contrato"),
                rs.getString("plan"),rs.getString("banco"),rs.getString("restringido")));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
    }

    public void openTable(String contrato, String ninvitados,String invad, String fecha, String hora, String usuario) {
        try {
            String query = "INSERT INTO oasisclub.asistencias(contrato,num_inv,invad,fecha,hora,user) VALUES ('" + contrato + "','" + ninvitados + "','"+invad+"','" + fecha + "','" + hora + "','" + usuario + "');";
            st.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Asistencia> visits() {
        ArrayList<Asistencia> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM oasisclub.asistencias;";
            rs = st.executeQuery(query);
            while(rs.next())
                list.add(new Asistencia(rs.getString("num_inv"),rs.getString("fecha"),
                        rs.getString("hora"),rs.getString("contrato"),rs.getString("invad"),
                        rs.getString("user")));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
    }

    public ArrayList<Cliente> getRestringidos() {
        ArrayList<Cliente> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM oasisclub.clientes WHERE restringido = 'SI';";
            rs = st.executeQuery(query);
            while(rs.next())
                list.add(new Cliente(rs.getString("cedula"),rs.getString("nombre"),rs.getString("contrato"),rs.getString("plan"),rs.getString("banco"),rs.getString("restringido")));
            return list;
        } catch (Exception e) {
            return list;
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

    public ArrayList<Usuario> getUsuarios() {
        ArrayList<Usuario> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM oasisclub.usuarios;";
            rs = st.executeQuery(query);
            while(rs.next())
                list.add(new Usuario(rs.getString("nombre"),rs.getString("apellido"),rs.getString("usuario"),rs.getString("clave"),rs.getInt("nivel")));
            return list;
        } catch (Exception e) {
            return list;
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
