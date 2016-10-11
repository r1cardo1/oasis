/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                JOptionPane.showMessageDialog(null, "Error con la conexion a la BD");
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

    public String openTable(String contrato, String ninvitados, String nmesa, String fecha, String hora, String usuario) {
        try {
            String query = "INSERT INTO oasisclub.asistencias(contrato,num_inv,fecha,hora,mesa,user) VALUES ('" + contrato + "','" + ninvitados + "','" + fecha + "','" + hora + "','" + nmesa + "','" + usuario + "');";
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
}
