package Entities;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String id;
    private final String username;
    private final String email;
    private String password;
    private final Map<String, Integer> stocks;


    public User(String username, String email, String password) {

        this.username = username;
        this.email = email;
        this.password = password;
        this.stocks = new HashMap<>();
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

    public String getPassword() {
        return password;
    }

}
