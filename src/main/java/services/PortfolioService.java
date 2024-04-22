package services;

import Entities.Portfolio;
import Entities.Stock;
import repositories.PortfolioRepository;
import repositories.StockRepository;

public class PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final StockRepository stockRepository;

    public PortfolioService(PortfolioRepository portfolioRepository, StockRepository stockRepository) {
        this.portfolioRepository = portfolioRepository;
        this.stockRepository = stockRepository;
    }

    public void addStockToPortfolio(String portfolioId, String symbol, int quantity) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId);
        Stock stock = stockRepository.findBySymbol(symbol);
        if (portfolio != null && stock != null) {
            portfolio.addStock(symbol, quantity, stock.getCurrentPrice());
            portfolioRepository.save(portfolio);
        }
    }

    public void removeStockFromPortfolio(String portfolioId, String symbol, int quantity) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId);
        if (portfolio != null) {
            portfolio.removeStock(symbol, quantity);
            portfolioRepository.save(portfolio);
        }
    }

    public Portfolio getPortfolioByUserId(String userId) {
        return portfolioRepository.findByUserId(userId);
    }
}
