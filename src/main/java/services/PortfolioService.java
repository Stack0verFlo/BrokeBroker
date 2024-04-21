package services;

import Entities.Portfolio;
import Entities.Stock;
import config.MongoDBClient;
import repositories.PortfolioRepository;
import repositories.StockRepository;
import repositoriesimpl.PortfolioRepositoryImpl;
import repositoriesimpl.StockRepositoryImpl;

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
            portfolio.addStock(String.valueOf(stock), quantity, stock.getCurrentPrice());
            portfolioRepository.save(portfolio);
        }
    }

    public void removeStockFromPortfolio(String portfolioId, String symbol, int quantity) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId);
        Stock stock = stockRepository.findBySymbol(symbol);
        if (portfolio != null && stock != null) {
            portfolio.removeStock(String.valueOf(stock), quantity);
            portfolioRepository.save(portfolio);
        }
    }

    public Portfolio getPortfolio(String portfolioId) {
        return portfolioRepository.findById(portfolioId);
    }
    public Portfolio getPortfolioByUserId(String userId) {
        return portfolioRepository.findByUserId(userId);
    }
}
