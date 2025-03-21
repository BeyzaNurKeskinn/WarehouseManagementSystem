package com.WHproject;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("userBean")
@SessionScoped
public class UserBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;

	private String password;
	private String confirmPassword;
	private String loggedInUser;
	private User selectedUser = new User(0L, "", "");
	private List<User> users = new ArrayList<>();
	private Long nextId = 1L;

	private UserDAO userDAO = new UserDAO(); // DAO sınıfını ekledik

	public UserBean() {
		users = userDAO.getAllUsers(); // Başlangıçta tüm kullanıcıları çekiyoruz
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		if (selectedUser != null) {
			this.selectedUser = selectedUser;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoggedInUser() {
		return loggedInUser;
	}

	public List<User> getUsers() {
		return users;
	}

	public String register() throws IOException {
		if (name != null && !name.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
			if (!userDAO.addUser(new User(nextId++, name, password))) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Kullanıcı adı zaten mevcut!"));
				return null;
			}
			users = userDAO.getAllUsers(); // Update the user list
			return "login.xhtml?faces-redirect=true";
		}
		return null;
	}

	public String login() {
		for (User user : users) {
			if (user.getName().equals(name) && user.getPassword().equals(password)) {
				FacesContext context = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
				session.setAttribute("loggedInUser", user.getName()); // Oturumu başlat

				loggedInUser = user.getName();

				// Eğer zaten home sayfasında ise yönlendirme yapma
				if (!FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath()
						.endsWith("home.xhtml")) {
					return "home.xhtml?faces-redirect=true"; // Ana sayfaya yönlendir
				}
			}
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Kullanıcı adı veya şifre hatalı!"));
		return null;
	}

	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
		if (session != null) {
			session.invalidate(); // Oturumu kapat
		}
		loggedInUser = null;
		return "login.xhtml?faces-redirect=true"; // Login sayfasına yönlendir
	}

	public boolean isLoggedIn() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
		return session != null && session.getAttribute("loggedInUser") != null; // Session kontrolü
	}

	public String updateUser() {
		if (selectedUser != null) {
			userDAO.updateUser(selectedUser); // Veritabanında güncelliyoruz
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Başarılı !", "Artık Sisteme Yeni Bilgilerinizle Giriş Yapabilirsiniz!"));
			return null;
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Kullanıcı bulunamadı!"));
		return null;
	}

	public void removeUser() throws IOException {
		if (selectedUser != null) {
			// users.remove(selectedUser);

			userDAO.removeUser(selectedUser);
			users = userDAO.getAllUsers();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Kullanıcı silindi!"));
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Silinecek kullanıcı seçilmedi!"));
		}
	}

	public static class User implements Serializable {
		private static final long serialVersionUID = 1L;
		private Long id;
		private String name;
		private String password;

		public User(Long id, String name, String password) {
			this.id = id;
			this.name = name;
			this.password = password;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}
}
