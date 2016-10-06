/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Ricardo
 */
public class DataConection  {
    //Configuracion de los datos de la BD
    private String usuario = "root";
    private String pass = "";
    private String host = "localhost";
    
    private Connection con = null;
    
    public DataConection() {
    }
    //Metodo que se devuelve la conexion o null si hubo un error
    public Connection getConexionMYSQL(){
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance( );
            String servidor = "jdbc:mysql://"+host+"/";
            con = DriverManager.getConnection(servidor,usuario,pass);
            createDatabase(con);
            createTables(con);
            return con;
        }catch(Exception e){
            e.printStackTrace();
            return con;
        }
    }
    
    public void createDatabase(Connection conn) throws SQLException{
          Statement st;
          st=conn.createStatement();
          String sql="CREATE DATABASE IF NOT EXISTS oasisClub";
          st.executeUpdate(sql);
    }
    
    public void createTables(Connection conn) throws SQLException{
          Statement st;
          st=conn.createStatement();
          String sql="CREATE TABLE IF NOT EXISTS oasisclub.clientes("
                  + "cedula INT(10) NOT NULL,"
                  + "nombre VARCHAR(20) NOT NULL,"
                  + "apellido VARCHAR(20) NOT NULL,"
                  + "contrato VARCHAR(10) NOT NULL,"
                  + "banco VARCHAR(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8;";
          String sql1 = "CREATE TABLE IF NOT EXISTS oasisclub.asistencias("
                  + "contrato VARCHAR(10) NOT NULL,"
                  + "num_inv INT NOT NULL,"
                  + "fecha VARCHAR(20) NOT NULL,"
                  + "hora VARCHAR(10) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8;";
          String sql2 = "CREATE TABLE IF NOT EXISTS oasisclub.usuarios("
                  + "nombre VARCHAR(20) NOT NULL,"
                  + "apellido VARCHAR(20) NOT NULL,"
                  + "usuario VARCHAR(20) NOT NULL,"
                  + "clave VARCHAR(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8;";
          st.executeUpdate(sql);
          st.executeUpdate(sql1);
          st.executeUpdate(sql2);
    }
      
}
