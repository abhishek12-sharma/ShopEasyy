package dao;

import model.User;
import java.sql.*;

public class UserDAO {

    // REGISTER - insert new user into database
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Register Error: " + e.getMessage());
            return false;
        }
    }

    // LOGIN - check if email and password match
    public User loginUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                conn.close();
                return user; // login success
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
        }
        return null; // login failed
    }
    public boolean updateProfile(int userId, String newName, String newPassword) {
    String sql = "UPDATE users SET name = ?, password = ? WHERE id = ?";
    try {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, newName);
        ps.setString(2, newPassword);
        ps.setInt(3, userId);
        ps.executeUpdate();
        conn.close();
        return true;
    } catch (SQLException e) {
        System.out.println("Update Profile Error: " + e.getMessage());
        return false;
    }
}
}