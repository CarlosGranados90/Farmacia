
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




public class CategoriesDao {
    
    // instanciar la conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst; // consulta en la DB
    ResultSet rs; // obtener datos de la consulta al DB
    
    // registrar categoria
    public boolean registerCategoryQuery (Categories category){
        
        String query = "INSERT INTO categories (name, created, updated) VALUES (?, ?, ?) ";
        Timestamp datetime = new Timestamp (new Date().getTime());
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setString(1, category.getName());
            pst.setTimestamp(2, datetime); // created
            pst.setTimestamp(3, datetime); // updated
            pst.execute();
            return true;
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error al registrar la categoria");
            return false;
        } 
    }
    
    // listar categoria
    public List listCategoriesQuery (String value){
        List<Categories> list_categories = new ArrayList();
        String query = "SELECT * FROM categories"; // seleccionar todo de categories - sin busqueda especifica
        String query_search_category = "SELEC * FROM categories WHERE name LIKE '%" + value + "%'" ; // seleccionar todo de categories por nombre - con busqueda especifica
        
        try {
            conn = cn.getConnection();
           if (value.equalsIgnoreCase("")){ // si persona no ingresa nada ejecutar esta consulta
               pst = conn.prepareStatement(query);
               rs = pst.executeQuery();
            } else {// si persona ingresa datos ejecutar esta consulta
               pst = conn.prepareStatement(query_search_category);
               rs = pst.executeQuery();
            }
           while (rs.next()){
               Categories category = new Categories();
               category.setId(rs.getInt("id"));
               category.setName(rs.getString("name"));
               list_categories.add(category); // pasar toda la información a la lista
           }
            
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error al listar la categoria");
            
        }
        return list_categories;
    }
    
    // modificar categoria
    public boolean updateCategoryQuery (Categories category){
        
        String query = "UPDATE categories SET name = ?, updated = ? WHERE id = ?";
        Timestamp datetime = new Timestamp (new Date().getTime());
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setString(1, category.getName());
            pst.setTimestamp(2, datetime); // updated
            pst.setInt(3, category.getId());
            pst.execute();
            return true;
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error al modificar los datos de la categoria");
            return false;
        } 
    }
    
    // Eliminar categoria
    public boolean deleteCategoryQuery(int id){
        String query = "DELETE FROM categories WHERE id = " + id; // eliminar cliente por id
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "No puede eliminar una categoria que tenga relación con otra tabla");
        }
        return false;
    }
}
