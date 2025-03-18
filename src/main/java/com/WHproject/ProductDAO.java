package com.WHproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.WHproject.WarehouseBean.Product;
public class ProductDAO {

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Products";  // 'Products' tablosunun adı

        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
            	int id = rs.getInt("id");// ID'yi alıyoruz
                String name = rs.getString("name");
                int quantity = rs.getInt("quantity");
                products.add(new Product(id, name, quantity));  // ID'yi de ekliyoruz
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public void addProduct(Product product) {
        String query = "INSERT INTO Products (name, quantity) VALUES (?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, product.getName());
            ps.setInt(2, product.getQuantity());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeProduct(Product product) {
        String query = "DELETE FROM Products WHERE name = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, product.getName());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(Product product) {
        String query = "UPDATE Products SET name =?, quantity = ? WHERE id = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

        	ps.setString(1, product.getName());  // Ürün adı
            ps.setInt(2, product.getQuantity()); // Ürün miktarı
            ps.setInt(3, product.getId());   
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}