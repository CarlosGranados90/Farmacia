package models;

import java.sql.Connection; // import connection DB
import java.sql.DriverManager; // import DiverManager
import java.sql.SQLException; 

public class ConnectionMySQL {
    
    private String database_name = "pharmacy_database"; // nombre base de datos
    private String user = "root"; // nombre de usuario DB
    private String password = "Natha9508?"; // contraseña DB
    private String url = "jdbc:mysql://localhost:3306/" + database_name; // url DB - port = 3306
    Connection conn = null;
    
    // Conexión java - MySQL
    public Connection getConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver"); // obtener valor del driver
            conn = DriverManager.getConnection (url, user, password); // obtener la conexión
        }catch (ClassNotFoundException e){ // este catch pertenece al drive
            System.err.println("Ha ocurrido un ClassNotFoundException " + e.getMessage());
        }catch (SQLException e) { // este catch pertence a la conexión
            System.err.println("Ha ocurrido un SQlExcepion " + e.getMessage());
        }
        return conn;
    }    
}
