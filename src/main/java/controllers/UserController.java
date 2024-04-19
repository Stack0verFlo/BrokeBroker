package controllers;

import Entities.User;
import config.MongoDBClient;
import repositories.UserRepository;
import repositoriesimpl.UserRepositoryImpl;
import org.mindrot.jbcrypt.BCrypt;

public class UserController {
    private final UserRepository userRepository;

    public UserController() {
        this.userRepository = new UserRepositoryImpl(MongoDBClient.getDatabase());
    }

    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        // Verwende jBCrypt, um das Passwort zu überprüfen
        return user != null && BCrypt.checkpw(password, user.getPassword());
    }

    public boolean register(String username, String email, String password) {
        if (userRepository.findByUsername(username) == null) {
            // Hash das Passwort, bevor du es in die Datenbank speicherst
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            User newUser = new User(username, email, hashedPassword);
            userRepository.save(newUser);
            return true;
        }
        return false;
    }
}