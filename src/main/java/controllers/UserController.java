package controllers;

import Entities.User;
import services.UserService;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public boolean authenticate(String username, String password) {
        return userService.authenticate(username, password);
    }

    public String register(String username, String email, String password) {
        return userService.register(username, email, password);
    }

    public User getCurrentUser() {
        return userService.getCurrentUser();
    }
}