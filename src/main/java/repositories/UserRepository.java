package repositories;

import Entities.User;

public interface UserRepository {
    User findByUsername(String username);
    String save(User user);
}
