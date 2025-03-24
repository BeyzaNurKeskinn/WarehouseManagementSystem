package com.WHproject;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class UserDAO {

	public List<UserBean.User> getAllUsers() {
		List<UserBean.User> users = new ArrayList<>();
		String query = "SELECT * FROM Users"; // 'Users' tablosunun adı

		try (Connection conn = DatabaseHelper.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				Long id = rs.getLong("id");
				String name = rs.getString("name");
				String workingWarehouse = rs.getString("workingWarehouse");
				String password = rs.getString("password");
				users.add(new UserBean.User(id, name,workingWarehouse, password)); // Kullanıcıyı ekliyoruz
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}

	public boolean isUserExists(String username) {
		String query = "SELECT COUNT(*) FROM Users WHERE name = ?";
		try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				return true; // Kullanıcı adı zaten var
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean addUser(UserBean.User user) throws IOException {
		if (isUserExists(user.getName())) {
			return false; // Kullanıcı adı zaten var, ekleme başarısız
		}

		String query = "INSERT INTO Users (name,workingWarehouse, password) VALUES (?,?, ?)";
		try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, user.getName());
			ps.setString(2, user.getWorkingWarehouse());
			ps.setString(3, user.getPassword());
			ps.executeUpdate();
			 String action = "Kullanıcı eklendi: " + user.getName();
		        LocalDateTime actionTime = LocalDateTime.now();

		        String transactionQuery = "INSERT INTO transactions (user_id, action, action_time) VALUES (?, ?, ?)";
		        PreparedStatement transactionPs = conn.prepareStatement(transactionQuery);
		        transactionPs.setInt(1, 1); // Kullanıcı ID'sini alın (örnek olarak 1)
		        transactionPs.setString(2, action);
		        transactionPs.setTimestamp(3, Timestamp.valueOf(actionTime));
		        transactionPs.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		FacesContext.getCurrentInstance().getExternalContext().redirect("users.xhtml");
		return false;
	}

	public void removeUser(UserBean.User selectedUser) {

		String query = "DELETE FROM Users WHERE id = ?";

		try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setLong(1, selectedUser.getId());
			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Kullanıcı silindi!"));
				 // İşlem kaydı ekleme
	            String action = "Kullanıcı silindi: " + selectedUser.getName();
	            LocalDateTime actionTime = LocalDateTime.now();

	            String transactionQuery = "INSERT INTO transactions (user_id, action, action_time) VALUES (?, ?, ?)";
	            PreparedStatement transactionPs = conn.prepareStatement(transactionQuery);
	            transactionPs.setInt(1, 1); // Kullanıcı ID'sini alın (örnek olarak 1)
	            transactionPs.setString(2, action);
	            transactionPs.setTimestamp(3, Timestamp.valueOf(actionTime));
	            transactionPs.executeUpdate();
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Kullanıcı silinemedi!"));
			}
		} catch (SQLException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Silme işlemi sırasında bir hata oluştu!"));
			e.printStackTrace();
		}
	}

	public void updateUser(UserBean.User user) {
		String query = "UPDATE Users SET name = ?, workingWarehouse=?, password = ? WHERE id = ?";

		try (Connection conn = DatabaseHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, user.getName());
			ps.setString(2, user.getWorkingWarehouse());// Kullanıcı adı
			ps.setString(3, user.getPassword()); // Şifre
			ps.setLong(4, user.getId()); // ID
			ps.executeUpdate();
			  // İşlem kaydı ekleme
	        String action = "Kullanıcı güncellendi: " + user.getName();
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
