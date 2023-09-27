package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class SuppliersDao {
    
     // instanciar la conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst; // consulta en la DB
    ResultSet rs; // obtener datos de la consulta al DB
        
    // Variables para enviar datos entre interfaces
    public static int id_user = 0;
    public static String name_user = "";
    public static String description_user = "";
    public static String address_user = "";
    public static String telephone_user = "";
    public static String email_user = "";
    public static String city_user = "";

    // Registrar proveedor
    public boolean registerSupplierQuery (Suppliers supplier){
        String query = "INSERT INTO suppliers(name, description, address, telephone, email, city, created, updated) VALUES(?,?,?,?,?,?,?,?)";
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setString(1, supplier.getName());
            pst.setString(2, supplier.getDescription());
            pst.setString(3, supplier.getAddress());
            pst.setString(4, supplier.getTelephone());
            pst.setString(5, supplier.getEmail());
            pst.setString(6, supplier.getCity());
            pst.setTimestamp(7, datetime); // created
            pst.setTimestamp(8, datetime); // updated
            pst.execute(); // ejecutar sentencia String query
            return true;
            
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error al registrar al proveedor");
            return false;
        }
    }
    
    // Listar proveedor
    public List listSupliersQuery(String value){
       List<Suppliers> list_suppliers = new ArrayList();// variable tipo list
       String query = "SELECT * FROM suppliers ORDER BY  rol ASC"; // seleccionar todo de supplier - sin busqueda especifica
       String query_search_supplier = "SELECT * FROM suppliers WHERE name LIKE '%" + value + "%'"; // seleccionar todo de supplier - con busqueda especifica
       
       try {
           conn = cn.getConnection();
           if (value.equalsIgnoreCase("")){ // si persona no ingresa nada ejecutar esta consulta
               pst = conn.prepareStatement(query);
               rs = pst.executeQuery();
            } else {// si persona ingresa datos ejecutar esta consulta
               pst = conn.prepareStatement(query_search_supplier);
               rs = pst.executeQuery();
            }
        while (rs.next()){ // ejecutar este ciclo mientras se encuentren registros
            Suppliers supplier = new Suppliers ();
            supplier.setId(rs.getInt("id"));
            supplier.setName(rs.getString ("name"));
            supplier.setDescription(rs.getString("description"));
            supplier.setAddress(rs.getString("address"));
            supplier.setTelephone(rs.getString("telephone"));
            supplier.setEmail(rs.getString("email"));
            supplier.setCity(rs.getString("city"));
            list_suppliers.add (supplier); // pasar toda la información a la lista
            
        }
       }catch (SQLException e){
             JOptionPane.showMessageDialog(null, e.getMessage());
           
       }
       return list_suppliers;
   }
   
    // Modificar proveedor
    public boolean updateSupplierQuery (Suppliers supplier){
        String query = "UPDATE suppliers SET name = ?, description = ?, address = ?, telephone = ?, email = ?, city = ?, updated = ? WHERE id = ?";
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setString(1, supplier.getName());
            pst.setString(2, supplier.getDescription());
            pst.setString(3, supplier.getAddress());
            pst.setString(4, supplier.getTelephone());
            pst.setString(5, supplier.getEmail());
            pst.setString(6, supplier.getCity());
            pst.setTimestamp(7, datetime); // updated
            pst.setInt(8, supplier.getId());
            pst.execute(); // ejecutar sentencia String query
            return true;
            
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error al modificar los datos del proveedor");
            return false;
        }
    }
    
    // Eliminar proveedor
    public boolean deleteSupplierQuery(int id){
        String query = "DELETE FROM suppliers WHERE id = " + id; // eliminar proveedor por id
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "No puede eliminar un proveedor que tenga relación con otra tabla");
        }
        return false;
    }
       
}
