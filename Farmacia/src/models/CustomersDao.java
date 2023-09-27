
package models;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class CustomersDao {
    
    // instanciar la conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst; // consulta en la DB
    ResultSet rs; // obtener datos de la consulta al DB
    
    // registrar clientes
    public boolean registerCustomerQuery (Customers customer){
        String query = "INSERT INTO customers (id, full_name, address, telephone, email, created, updated) VALUES(?,?,?,?,?,?,?)";
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, customer.getId());
            pst.setString(2, customer.getFull_name());
            pst.setString(3, customer.getAddress());
            pst.setString(4, customer.getTelephone());
            pst.setString(5, customer.getEmail());
            pst.setTimestamp(6, datetime); // created
            pst.setTimestamp(7, datetime); // updated
            pst.execute();
            return true;
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error al registrar al cliente " + e );
            return false;
        }
    }
    
    // listar clientes
    public List listCustomerQuery (String value){
        List<Customers> list_customers = new ArrayList();
        String query = "SELECT * FROM customers"; // seleccionar todo de employees - sin busqueda especifica
        String query_search_customer = "SELECT * FROM customers WHERE id LIKE '%" + value + "%'"; // seleccionar todo de customers y ordenarlo de manera ascendente por id- con busqueda especifica
        
        try {
           conn = cn.getConnection();
           if (value.equalsIgnoreCase("")){ // si persona no ingresa nada ejecutar esta consulta
               pst = conn.prepareStatement(query);
               rs = pst.executeQuery();
            } else {// si persona ingresa datos ejecutar esta consulta
               pst = conn.prepareStatement(query_search_customer);
               rs = pst.executeQuery();
            }
           
           while (rs.next()){
               Customers customer = new Customers();
               customer.setId(rs.getInt("id"));
               customer.setFull_name(rs.getString ("full_name"));
               customer.setAddress(rs.getString("address"));
               customer.setTelephone(rs.getString("telephone"));
               customer.setEmail(rs.getString("email"));
               list_customers.add (customer); // pasar toda la información a la lista
           }
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, e.toString());   
        }   return list_customers;   
    }   
    
    // Modificar cliente
    public boolean updateCustomerQuery (Customers customer){
        String query = "UPDATE customers SET full_name = ?, address = ?, telephone = ?, email = ?, updated = ? WHERE id = ?";
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setString(1, customer.getFull_name());
            pst.setString(2, customer.getAddress());
            pst.setString(3, customer.getTelephone());
            pst.setString(4, customer.getEmail());
            pst.setTimestamp(5, datetime); // updated
            pst.setInt(6, customer.getId());
            pst.execute();
            return true;
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error al modificar datos del cliente " + e );
            return false;
        }
    }  
    
        // Eliminar cliente
    public boolean deleteCustomerQuery(int id){
        String query = "DELETE FROM customers WHERE id = " + id; // eliminar cliente por id
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "No puede eliminar un cliente que tenga relación con otra tabla");
        }
        return false;
    }
}
