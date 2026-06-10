package dao;

import model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // GET ALL PRODUCTS
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                p.setImagePath(rs.getString("image_path"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setStock(rs.getInt("stock"));
                p.setSection(rs.getString("section"));
                products.add(p);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("ProductDAO Error: " + e.getMessage());
        }
        return products;
    }

    // GET PRODUCTS BY CATEGORY
    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                p.setImagePath(rs.getString("image_path"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setStock(rs.getInt("stock"));
                p.setSection(rs.getString("section"));
                products.add(p);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("ProductDAO Error: " + e.getMessage());
        }
        return products;
    }
    // SEARCH PRODUCTS BY NAME
public List<Product> searchProducts(String keyword) {
    List<Product> products = new ArrayList<>();
    String sql = "SELECT * FROM products WHERE name LIKE ?";
    try {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Product p = new Product();
            p.setId(rs.getInt("id"));
            p.setName(rs.getString("name"));
            p.setPrice(rs.getDouble("price"));
            p.setImagePath(rs.getString("image_path"));
            p.setCategoryId(rs.getInt("category_id"));
            p.setStock(rs.getInt("stock"));
            p.setSection(rs.getString("section"));
            products.add(p);
        }
        conn.close();
    } catch (SQLException e) {
        System.out.println("Search Error: " + e.getMessage());
    }
    return products;
}
// GET PRODUCTS BY SECTION (popular, men, women, books)
public List<Product> getProductsBySection(String section) {
    List<Product> products = new ArrayList<>();
    String sql = "SELECT * FROM products WHERE section = ? LIMIT 10";
    try {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, section);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Product p = new Product();
            p.setId(rs.getInt("id"));
            p.setName(rs.getString("name"));
            p.setPrice(rs.getDouble("price"));
            p.setImagePath(rs.getString("image_path"));
            p.setCategoryId(rs.getInt("category_id"));
            p.setStock(rs.getInt("stock"));
            p.setSection(rs.getString("section"));
            products.add(p);
        }
        conn.close();
    } catch (SQLException e) {
        System.out.println("Section Error: " + e.getMessage());
    }
    return products;
}

}
