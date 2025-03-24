package com.WHproject;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.WHproject.WarehouseBean.Product;

public class ProductDAO {

	public List<Product> getAllProducts() {
		List<Product> products = new ArrayList<>();
		String query = "SELECT * FROM Products"; // 'Products' tablosunun adı

		try (Connection conn = DatabaseHelper.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				int id = rs.getInt("id");// ID'yi alıyoruz
				String name = rs.getString("name");
				String warehouseInfo = rs.getString("warehouseInfo");
				int quantity = rs.getInt("quantity");
				products.add(new Product(id, name, warehouseInfo, quantity)); // ID'yi de ekliyoruz
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return products;
	}

	public List<String> getAllWarehouseNames() {
        List<String> warehouseNames = new ArrayList<>();
        String query = "SELECT DISTINCT warehouseInfo FROM Products";

        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String warehouseName = rs.getString("warehouseInfo");
                warehouseNames.add(warehouseName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return warehouseNames;
    }
	
	public void addProduct(Product product) {
		String query = "INSERT INTO Products (name,warehouseInfo, quantity) VALUES (?,?, ?)";

		try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, product.getName());
			ps.setString(2, product.getWarehouseInfo());
			ps.setInt(3, product.getQuantity());
			ps.executeUpdate();
			// İşlem kaydı ekleme
	        String action = "Ürün eklendi: " + product.getName();
	        LocalDateTime actionTime = LocalDateTime.now();

	        String transactionQuery = "INSERT INTO transactions (user_id, action, action_time) VALUES (?, ?, ?)";
	        PreparedStatement transactionPs = conn.prepareStatement(transactionQuery);
	        transactionPs.setInt(1, 1); // Kullanıcı ID'sini alın (örnek olarak 1)
	        transactionPs.setString(2, action);
	        transactionPs.setTimestamp(3, Timestamp.valueOf(actionTime));
	        transactionPs.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removeProduct(Product product) {
		String query = "DELETE FROM Products WHERE name = ?";

		try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, product.getName());
			ps.executeUpdate();

			 // İşlem kaydı ekleme
	        String action = "Ürün silindi: " + product.getName();
	        LocalDateTime actionTime = LocalDateTime.now();

	        String transactionQuery = "INSERT INTO transactions (user_id, action, action_time) VALUES (?, ?, ?)";
	        PreparedStatement transactionPs = conn.prepareStatement(transactionQuery);
	        transactionPs.setInt(1, 1); // Kullanıcı ID'sini alın (örnek olarak 1)
	        transactionPs.setString(2, action);
	        transactionPs.setTimestamp(3, Timestamp.valueOf(actionTime));
	        transactionPs.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateProduct(Product product) {
		String query = "UPDATE Products SET name =?, warehouseInfo =?, quantity = ? WHERE id = ?";

		try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, product.getName()); // Ürün adı
			ps.setString(2, product.getWarehouseInfo());
			ps.setInt(3, product.getQuantity()); // Ürün miktarı
			ps.setInt(4, product.getId());
			ps.executeUpdate();

			  // İşlem kaydı ekleme
	        String action = "Ürün güncellendi: " + product.getName();
	        LocalDateTime actionTime = LocalDateTime.now();

	        String transactionQuery = "INSERT INTO transactions (user_id, action, action_time) VALUES (?, ?, ?)";
	        PreparedStatement transactionPs = conn.prepareStatement(transactionQuery);
	        transactionPs.setInt(1, 1); // Kullanıcı ID'sini alın (örnek olarak 1)
	        transactionPs.setString(2, action);
	        transactionPs.setTimestamp(3, Timestamp.valueOf(actionTime));
	        transactionPs.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}