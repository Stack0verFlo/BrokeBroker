package repositories;

import Entities.User;

public interface UserRepository {
    User findByUsername(String username);
    String save(User user);
    boolean authenticate(String username, String password);
    void updatePortfolioId(String userId, String portfolioId);
}
