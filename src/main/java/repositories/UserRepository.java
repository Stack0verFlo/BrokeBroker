package repositories;

import Entities.User;

public interface UserRepository {
    User findByUsername(String username);
    void save(User user);
    boolean authenticate(String username, String password);
}
