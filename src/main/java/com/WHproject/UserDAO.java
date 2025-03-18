package com.WHproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public List<UserBean.User> getAllUsers() {
        List<UserBean.User> users = new ArrayList<>();
        String query = "SELECT * FROM Users";  // 'Users' tablosunun adı

        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                users.add(new UserBean.User(id, name, password));  // Kullanıcıyı ekliyoruz
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public void addUser(UserBean.User user) {
        String query = "INSERT INTO Users (name, password) VALUES (?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUser(UserBean.User user) {
        String query = "DELETE FROM Users WHERE id = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, user.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(UserBean.User user) {
        String query = "UPDATE Users SET name = ?, password = ? WHERE id = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, user.getName());  // Kullanıcı adı
            ps.setString(2, user.getPassword());  // Şifre
            ps.setLong(3, user.getId());   // ID
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}