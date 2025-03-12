package com.WHproject;


import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;

import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("userBean")
@SessionScoped
public class UserBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String password;
	private String loggedInUser;
	private User selectedUser = new User(0L, "", "");
	private List<User> users = new ArrayList<>();
	private Long nextId = 1L;
	
	public UserBean() {
		// Varsayılan kullanıcı
		users.add(new User(nextId++, "admin", "1234"));
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

	public String register() {
		if (name != null && !name.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
			users.add(new User(nextId++, name, password));
			name = "";
			password = "";
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Kullanıcı eklendi!"));
			return null; // Sayfada kal
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
	            if (!FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().endsWith("home.xhtml")) {
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
	            for (int i = 0; i < users.size(); i++) {
	                if (users.get(i).getId().equals(selectedUser.getId())) {
	                    users.set(i, new User(selectedUser.getId(), selectedUser.getName(), selectedUser.getPassword()));
	                    FacesContext.getCurrentInstance().addMessage(null,
	                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Kullanıcı güncellendi!"));
	                    return null;
	                }
	            }
	        }
	        FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Kullanıcı bulunamadı!"));
	        return null;
	    }
	public void removeUser(User user) {
		users.remove(user);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Kullanıcı silindi!"));
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

		public String getName() {
			return name;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
