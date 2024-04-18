package Entities;

public class User {
    private String id;
    private String username;
    private String email;
    private String password; // Hinweis: Passwörter sollten verschlüsselt und sicher gespeichert werden

    public User(String username, String email, String password) {

        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
