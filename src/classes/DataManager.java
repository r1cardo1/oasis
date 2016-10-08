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
    private ResultSet rs;

    public DataManager() {
        try{
            if((con = conexion.getConexionMYSQL())==null){
                JOptionPane.showMessageDialog(null,"Error con la conexion a la BD");
                return;
            }
            st = con.createStatement();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    

    public ResultSet login(String user){
        try {
            String query = "SELECT * FROM oasisclub.usuarios WHERE usuario LIKE '"+user+"';";
            rs = st.executeQuery(query);     
            
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public ResultSet searchClientbyCI(String str){
        try{
            String sql = "SELECT * FROM oasisclub.clientes WHERE cedula LIKE '%"+str+"%'";
            rs = st.executeQuery(sql);
            return rs;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
      public ResultSet searchClientbyContract(String str){
        try{
            String sql = "SELECT * FROM oasisclub.clientes WHERE contrato LIKE '%"+str+"%';";
            rs = st.executeQuery(sql);
            return rs;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }  
      
    public ResultSet searchClientbyName(String str){
        try{
            String sql = "SELECT * FROM oasisclub.clientes WHERE nombre LIKE '%"+str+"%';";
            rs = st.executeQuery(sql);
            return rs;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }  
    
    public String openTable(String contrato, String ninvitados, String nmesa, String fecha,String hora,String usuario){
        try{
            String query = "INSERT INTO oasisclub.asistencias(contrato,num_inv,fecha,hora,mesa,user) VALUES ('"+contrato+"','"+ninvitados+"','"+fecha+"','"+hora+"','"+nmesa+"','"+usuario+"');";
            st.executeUpdate(query);
            return "OK";
        }catch(Exception e){
            e.printStackTrace();
            return "FAIL";
        }
        
    }
    
    public ResultSet visits(){
        try{
        String query ="SELECT * FROM oasisclub.asistencias;";
        rs = st.executeQuery(query);
        return rs;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
      

    public ResultSet selectXpais(String pais){
        try {
            String query = "SELECT * FROM peliculas WHERE pais = '"+pais+"'";
            rs = st.executeQuery(query);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
        

    public boolean delete(int id){
        try {
            String query = "DELETE FROM peliculas WHERE id = '"+id+"'";
            st.executeUpdate(query);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    

    public boolean update(int id,String nombre,String genero,int anio,String actor,String pais){
        try {
            String query = "UPDATE peliculas SET"
                    + " nombre = '"+nombre+"',"
                    + " genero = '"+genero+"',"
                    + " anio = '"+anio+"',"
                    + " actor = '"+actor+"',"
                    + " pais = '"+pais+"' WHERE id = '"+id+"';";
            st.executeUpdate(query);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    

    public boolean insert(String nombre,String genero,int anio,String actor,String pais){
        try {
            String query = "INSERT INTO peliculas VALUES(NULL,'"+nombre+"','"+genero+"','"+anio+"','"+actor+"','"+pais+"');";
            st.executeUpdate(query);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    

    public ResultSet selectXtodas(){
        try {
            String query = "SELECT * FROM peliculas";
            rs = st.executeQuery(query);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        
    }
    
    public void closers() throws SQLException{
            rs.close();
        }

}