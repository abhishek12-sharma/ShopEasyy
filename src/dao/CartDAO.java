package dao;

import model.CartItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    // ADD ITEM TO CART
    public boolean addToCart(int userId, int productId, int quantity) {
        String checkSql = "SELECT * FROM cart WHERE user_id = ? AND product_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setInt(1, userId);
            checkPs.setInt(2, productId);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                String updateSql = "UPDATE cart SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";
                PreparedStatement updatePs = conn.prepareStatement(updateSql);
                updatePs.setInt(1, quantity);
                updatePs.setInt(2, userId);
                updatePs.setInt(3, productId);
                updatePs.executeUpdate();
            } else {
                String insertSql = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
                PreparedStatement insertPs = conn.prepareStatement(insertSql);
                insertPs.setInt(1, userId);
                insertPs.setInt(2, productId);
                insertPs.setInt(3, quantity);
                insertPs.executeUpdate();
            }
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println("CartDAO Error: " + e.getMessage());
            return false;
        }
    }

    // GET ALL CART ITEMS FOR A USER
    public List<CartItem> getCartByUser(int userId) {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT c.id, c.user_id, c.product_id, p.name, p.price, c.quantity " +
                     "FROM cart c JOIN products p ON c.product_id = p.id " +
                     "WHERE c.user_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartItem item = new CartItem();
                item.setId(rs.getInt("id"));
                item.setUserId(rs.getInt("user_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setProductName(rs.getString("name"));
                item.setPrice(rs.getDouble("price"));
                item.setQuantity(rs.getInt("quantity"));
                items.add(item);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("CartDAO Error: " + e.getMessage());
        }
        return items;
    }

    // REMOVE ITEM FROM CART
    public boolean removeFromCart(int cartItemId) {
        String sql = "DELETE FROM cart WHERE id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cartItemId);
            ps.executeUpdate();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println("CartDAO Error: " + e.getMessage());
            return false;
        }
    }
}