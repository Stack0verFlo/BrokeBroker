package repositories;

import Entities.Portfolio;

public interface PortfolioRepository {
    Portfolio findById(String id);
    void save(Portfolio portfolio);
}
