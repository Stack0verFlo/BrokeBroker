package controllers;

import Entities.User;
import config.MongoDBClient;
import repositories.UserRepository;
import repositoriesimpl.UserRepositoryImpl;

public class UserController {
    private final UserRepository userRepository;

    public UserController() {
        this.userRepository = new UserRepositoryImpl(MongoDBClient.getDatabase());
    }

    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    public boolean register(String username, String email, String password) {
        if (userRepository.findByUsername(username) == null) {
            User newUser = new User(username, email, password);
            userRepository.save(newUser); // ID wird intern gesetzt
            return true;
        }
        return false;
    }
}
