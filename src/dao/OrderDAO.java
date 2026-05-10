package dao;

import model.CartItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // PLACE ORDER — saves order + items + clears cart
    public int placeOrder(int userId, List<CartItem> items, double total) {
        String orderSql = "INSERT INTO orders (user_id, total_amount, status) VALUES (?, ?, 'Active')";
        try {
            Connection conn = DBConnection.getConnection();

            // Insert order
            PreparedStatement orderPs = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            orderPs.setInt(1, userId);
            orderPs.setDouble(2, total);
            orderPs.executeUpdate();

            // Get generated order ID
            ResultSet rs = orderPs.getGeneratedKeys();
            int orderId = -1;
            if (rs.next()) orderId = rs.getInt(1);

            // Insert each item
            String itemSql = "INSERT INTO order_items (order_id, product_id, product_name, price, quantity) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement itemPs = conn.prepareStatement(itemSql);
            for (CartItem item : items) {
                itemPs.setInt(1, orderId);
                itemPs.setInt(2, item.getProductId());
                itemPs.setString(3, item.getProductName());
                itemPs.setDouble(4, item.getPrice());
                itemPs.setInt(5, item.getQuantity());
                itemPs.executeUpdate();
            }

            // Clear cart after order placed
            String clearCart = "DELETE FROM cart WHERE user_id = ?";
            PreparedStatement clearPs = conn.prepareStatement(clearCart);
            clearPs.setInt(1, userId);
            clearPs.executeUpdate();

            conn.close();
            return orderId;

        } catch (SQLException e) {
            System.out.println("Order Error: " + e.getMessage());
            return -1;
        }
    }

    // GET ALL ORDERS FOR A USER
    public List<String[]> getOrdersByUser(int userId) {
        List<String[]> orders = new ArrayList<>();
        String sql = "SELECT id, total_amount, status, order_date FROM orders WHERE user_id = ? ORDER BY order_date DESC";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String[] order = {
                    String.valueOf(rs.getInt("id")),
                    String.format("%.2f", rs.getDouble("total_amount")),
                    rs.getString("status"),
                    rs.getString("order_date").substring(0, 10)
                };
                orders.add(order);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("Get Orders Error: " + e.getMessage());
        }
        return orders;
    }

    // GET ITEMS INSIDE ONE ORDER
    public List<String[]> getOrderItems(int orderId) {
        List<String[]> items = new ArrayList<>();
        String sql = "SELECT product_name, price, quantity FROM order_items WHERE order_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String[] item = {
                    rs.getString("product_name"),
                    String.format("%.2f", rs.getDouble("price")),
                    String.valueOf(rs.getInt("quantity")),
                    String.format("%.2f", rs.getDouble("price") * rs.getInt("quantity"))
                };
                items.add(item);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("Get Order Items Error: " + e.getMessage());
        }
        return items;
    }

    // UPDATE ORDER STATUS
    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Update Status Error: " + e.getMessage());
            return false;
        }
    }
}