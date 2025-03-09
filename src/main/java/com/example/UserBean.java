package com.example;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class UserBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private List<User> users = new ArrayList<>();
    private User selectedUser;
    private Long nextId = 1L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
        if (selectedUser != null) {
            this.name = selectedUser.getName();
        }
    }

    public void addUser() {
        if (name != null && !name.trim().isEmpty()) {
            users.add(new User(nextId++, name));
            name = "";
        }
    }

    public void deleteUser() {
        if (selectedUser != null) {
            users.remove(selectedUser);
            selectedUser = null;
            name = "";
        }
    }

    public void updateUser() {
        if (selectedUser != null && name != null && !name.trim().isEmpty()) {
            selectedUser.setName(name);
            selectedUser = null;
            name = "";
        }
    }

    public static class User implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private Long id;
        private String name;

        public User(Long id, String name) {
            this.id = id;
            this.name = name;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return id != null && id.equals(user.id);
        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }
    }
} 