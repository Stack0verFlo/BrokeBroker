package services;

import Entities.User;
import repositories.UserRepository;
import repositories.PortfolioRepository;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private User currentUser;

    public UserService(UserRepository userRepository, PortfolioRepository portfolioRepository) {
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
    }

    public String register(String username, String email, String password) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            // Benutzer existiert bereits
            return null;
        }
        // Hash das Passwort
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User newUser = new User(username, email, hashedPassword);
        String userId = userRepository.save(newUser);  // Diese Methode sollte die ID zurückgeben

        // Überprüfen, ob die ID nicht null ist, bevor das Portfolio erstellt wird
        if (userId != null && !userId.isEmpty()) {
            portfolioRepository.createForUserId(userId);  // Erstellt das Portfolio
        }

        return userId; // Gibt die Benutzer-ID zurück
    }

    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            currentUser = user; // User-Objekt bereits mit ID aus der Datenbank
            return true;
        }
        return false;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }
}
