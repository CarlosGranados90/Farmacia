package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import java.util.List;


public class ProductsDao {

    // instanciar la conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst; // consulta en la DB
    ResultSet rs; // obtener datos de la consulta al DB

    // registrar producto
    public boolean registerProductQuery(Products product) {
        String query = "INSERT INTO products (code, name, description, unit_price, created, updated, category_id) VALUES(?,?,?,?,?,?,?,?)";
        Timestamp datetime = new Timestamp(new Date().getTime());

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, product.getCode());
            pst.setString(2, product.getName());
            pst.setString(3, product.getDescription());
            pst.setDouble(4, product.getUnit_price());
            pst.setTimestamp(5, datetime); // created
            pst.setTimestamp(6, datetime); // updated
            pst.setInt(7, product.getCategory_id());
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el producto ");
            return false;
        }
    }
    
    // listar productos
    public List listProductsQuery (String value){
        List<Products> list_products = new ArrayList();
        String query = "SELECT pro.*, ca.name AS category_name FROM products pro, categories ca WHERE pro.category_id = ca.id"; // consulta con todos los datos
        String query_search_product = "SELECT pro.*, ca.name AS category_name FROM products pro INNER JOIN  categories ca"
                + "ON pro.category_id = ca.id WHERE  pro.name LIKE '%" + value + "%'"; // consulta entre de un producto por su nombre trae todos los datos relacionados al producto, sin el id
        
        try {
            conn = cn.getConnection();
           if (value.equalsIgnoreCase("")){ // si persona no ingresa nada ejecutar esta consulta
               pst = conn.prepareStatement(query);
               rs = pst.executeQuery();
            } else {// si persona ingresa datos ejecutar esta consulta
               pst = conn.prepareStatement(query_search_product);
               rs = pst.executeQuery();
            }
               while (rs.next()){
               Products product = new Products();
               product.setId(rs.getInt("id"));
               product.setCode(rs.getInt ("code"));
               product.setName(rs.getString("name"));
               product.setDescription(rs.getString("description"));
               product.setUnit_price(rs.getDouble("unit_price"));
               product.setProduct_quantity(rs.getInt("product_quantity"));
               product.setCategory_name(rs.getString ("category_name")); 
               list_products.add(product);// pasar toda la información a la lista
               }
        }catch (SQLException e){
                JOptionPane.showMessageDialog(null, e.toString());
        }
        return list_products;
    }
    
    // modificar producto
    public boolean updateProductQuery(Products product) {
        String query = "UPDATE products SET code = ?, name = ?, description = ?, unit_price = ?, updated = ?, category_id = ? WHERE id = ?";
        Timestamp datetime = new Timestamp(new Date().getTime());

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, product.getCode());
            pst.setString(2, product.getName());
            pst.setString(3, product.getDescription());
            pst.setDouble(4, product.getUnit_price());
            pst.setTimestamp(5, datetime); // updated
            pst.setInt(6, product.getCategory_id());
            pst.setInt(7,product.getCategory_id());
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar los datos del producto ");
            return false;
        }
    }
    
    // eliminar producto
    public boolean deleteProductQuery(int id){
        String query = "DELETE FROM products WHERE id = " + id; // eliminar producto por id
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "No puede eliminar un producto que tenga relación con otra tabla");
        }
        return false;
    }
    
    // buscar producto
    public Products searchProduct (int id){
        String query = "SELECT pro.*, ca.name AS category_name FROM products pro INNER JOIN categories ca ON pro.category_id = ca.id WHERE pro.id = ?";
        // instanciar modelo productos
        Products product = new Products ();
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            
            if (rs.next()){
                product.setId(rs.getInt("id"));
                product.setCode(rs.getInt("code"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setUnit_price(rs.getDouble( "unit_price"));
                product.setCategory_id(rs.getInt("category_id"));
                product.setCategory_name(rs.getString("category_name"));
            }
            
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return product;
    }
    
    // buscar producto por codigo
    public Products searchCode (int code){
        String query = "SELECT pro.id, pro.name FROM products pro WHERE code = ?";
        // instanciar modelo productos
        Products product = new Products ();
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, code);
            rs = pst.executeQuery();
            
            if (rs.next()){
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));

            }
            
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return product;
    }
    
    // traer cantidad de productos
    public Products searchId (int id){
        String query = "SELECT pro.product_quantity FROM products pro WHERE pro.id = ?";
        Products product = new Products();
        
        try {
            conn = cn.getConnection ();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            
            if (rs.next()){
                product.setProduct_quantity(rs.getInt("product_quantity"));
            }
            
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return product;
    }
    
    //actualizar stock
    public boolean updateStockQuery (int amount, int product_id){
        String query = "UPDATE products SET products_quantity = ? WHERE id = ?";
        
        try {
            conn = cn.getConnection ();
            pst = conn.prepareStatement(query);
            pst.setInt(1, amount);
            pst.setInt(1, product_id);
            pst.execute();
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
    }
}
