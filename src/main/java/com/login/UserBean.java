package com.login;


import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

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
    private String loggedInUser;
    
    private List<User> users = new ArrayList<>();
    private Long nextId = 1L;
    
    public UserBean() {
        // Varsayılan kullanıcı ekleyelim
        users.add(new User(nextId++, "admin", "1234"));
    }
    @PostConstruct
    public void checkLogin()  {
        if (loggedInUser == null) {
            // Kullanıcı giriş yapmadıysa, login sayfasına yönlendir.
            try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
            return "login.xhtml?faces-redirect=true"; // Kayıt sonrası login sayfasına yönlendirme
        }
        return null; // Kayıt başarısızsa sayfada kal
    }

    public String login() {
        for (User user : users) {
            if (user.getName().equals(name) && user.getPassword().equals(password)) {
                loggedInUser = user.getName();
                return "home.xhtml?faces-redirect=true"; // Başarılı giriş sonrası yönlendirme
            }
        }
     // Hatalı giriş mesajı gösterelim
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Kullanıcı adı veya şifre hatalı!"));
        return "login.xhtml?faces-redirect=true"; // Sayfa yenilenmez Başarısız giriş
    }

    public String logout() {
        loggedInUser = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession(); // Oturumu sonlandır
        return "login.xhtml?faces-redirect=true";
    }
    public boolean isLoggedIn() {
        return loggedInUser != null;
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
    }
}
