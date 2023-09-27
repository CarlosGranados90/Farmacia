
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeesDao {
    // instanciar la conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst; // consulta en la DB
    ResultSet rs; // obtener datos de la consulta al DB
    
    // Variables para enviar datos entre interfaces
    public static int id_user = 0;
    public static String full_name_user = "";
    public static String username_user = "";
    public static String address_user = "";
    public static String telephone_user = "";
    public static String email_user = "";
    public static String rol_user = "";

    //método de login
    public Employees loginQuery (String user, String password) {
        String query = "SELECT * FROM  employees WHERE username = ? AND password = ?";
        Employees employee = new Employees ();
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement (query);
            // enviar parametros
            pst.setString(1 , user);
            pst.setString(2, password);
            rs = pst.executeQuery(); // ejecutar consulta
            
            if (rs.next()){
                employee.setId(rs.getInt("id")); //almacenar en setId lo que  trae resultSet
                id_user = employee.getId(); // guardar información del usuario
                employee.setFull_name(rs.getString("full_name")); 
                full_name_user = employee.getFull_name ();
                employee.setUsername(rs.getString("username"));
                username_user = employee.getUsername();
                employee.setAddress(rs.getString("address"));
                address_user = employee.getAddress();
                employee.setTelephone(rs.getString("telephone"));
                telephone_user = employee.getTelephone();
                employee.setEmail(rs.getString("email"));
                email_user = employee.getEmail();
                employee.setRol(rs.getString("rol"));
                rol_user = employee.getRol();
            }
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error al optener el empletado" + e);
        }
        return employee;
    }
    
    // registrar empleado
    public boolean registerEmploeeQuery (Employees employee){
        String query = "INSERT INTO employees(id, full_name, username, address, telephone, email, password, rol, created, updated) VALUES(?,?,?,?,?,?,?,?,?,?)";
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, employee.getId());
            pst.setString(2, employee.getFull_name());
            pst.setString(3, employee.getUsername());
            pst.setString(4, employee.getAddress());
            pst.setString(5, employee.getTelephone());
            pst.setString(6, employee.getEmail());
            pst.setString(7, employee.getPassword());
            pst.setString(8, employee.getRol());
            pst.setTimestamp(9, datetime); // created
            pst.setTimestamp(10, datetime); // updated
            pst.execute(); // ejecutar sentencia String query
            return true;
            
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error al registrar al empleado" + e);
            return false;
        }
    }
    
    // Listar empleado
    public List listEmployeesQuery(String value){
       List<Employees> list_employees = new ArrayList();// variable tipo list
       String query = "SELECT * FROM employees ORDER BY  rol ASC"; // seleccionar todo de employees y ordenarlo de manera ascendente - sin busqueda especifica
       String query_search_employee = "SELECT * FROM employess WHERE id LIKE '%" + value + "%'"; // seleccionar todo de employees y ordenarlo de manera ascendente - con busqueda especifica
       
       try {
           conn = cn.getConnection();
           if (value.equalsIgnoreCase("")){ // si persona no ingresa nada ejecutar esta consulta
               pst = conn.prepareStatement(query);
               rs = pst.executeQuery();
            } else {// si persona ingresa datos ejecutar esta consulta
               pst = conn.prepareStatement(query_search_employee);
               rs = pst.executeQuery();
            }
        while (rs.next()){ // ejecutar este ciclo mientras se encuentren registros
            Employees employee = new Employees ();
            employee.setId(rs.getInt("id"));
            employee.setFull_name(rs.getString ("full_name"));
            employee.setUsername(rs.getString("username"));
            employee.setAddress(rs.getString("address"));
            employee.setTelephone(rs.getString("telephone"));
            employee.setEmail(rs.getString("email"));
            employee.setRol(rs.getString("rol"));
            list_employees.add (employee); // pasar toda la información a la lista
            
        }
       }catch (SQLException e){
             JOptionPane.showMessageDialog(null, e.toString());          
           
       }
       return list_employees;
   }
   
    // Modificar empleado
    public boolean updateEmploeeQuery (Employees employee){
        String query = "UPDATE employees SET full_name = ?, username = ?, address = ?, telephone = ?, email = ?, rol = ?, updated = ? WHERE id = ?";
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setString(1, employee.getFull_name());
            pst.setString(2, employee.getUsername());
            pst.setString(3, employee.getAddress());
            pst.setString(4, employee.getTelephone());
            pst.setString(5, employee.getEmail());
            pst.setString(6, employee.getRol());
            pst.setTimestamp(7, datetime); // updated
            pst.setInt(8, employee.getId());
            pst.execute(); // ejecutar sentencia String query
            return true;
            
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error al modificar los datos del empleado" + e);
            return false;
        }
    }
    
    // Eliminar empleado
    public boolean deleteEmployeeQuery(int id){
        String query = "DELETE FROM employees WHERE id = " + id; // eliminar empleado por id
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "No puede eliminar un empleado que tenga relación con otra tabla");
        }
        return false;
    }
    
    // Modificar contraseña
    public boolean updateEmployeePassword (Employees employee){
        String query = "UPDATE employees SET password = ? WHERE = ''" + username_user + "'"; // solo se mofidica si la coincide con el usuairo logeado
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareCall(query);
            pst.setString(1, employee.getPassword());
            pst.executeUpdate();
            return true;
        }catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar la contraseña " + e);
            return false;
        }
    }
    
}

