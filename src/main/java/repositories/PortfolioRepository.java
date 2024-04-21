package repositories;

import Entities.Portfolio;

public interface PortfolioRepository {
    Portfolio findByUserId(String userId);
    Portfolio findById(String id);
    void save(Portfolio portfolio);
    void createForUserId(String userId);
}
