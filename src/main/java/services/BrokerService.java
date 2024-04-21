package services;

import Entities.Portfolio;
import Entities.StockTransaction;
import Entities.TransactionType;
import repositories.PortfolioRepository;

public class BrokerService {
    private PortfolioRepository portfolioRepository;
    private StockService stockService;
    private String portfolioId;
    private Portfolio portfolio;

    public BrokerService(PortfolioRepository portfolioRepository, StockService stockService) {
        this.portfolioRepository = portfolioRepository;
        this.stockService = stockService;
    }

    public void buyStock(String portfolioId, String symbol, int quantity) {
        this.portfolioId = portfolioId;
        //Portfolio portfolio = portfolioRepository.findByUserId(userId);
        this.portfolio = portfolioRepository.findById(portfolioId);
        double stockPrice = stockService.getCurrentPrice(symbol);
        double totalPrice = stockPrice * quantity;

        if (portfolio.canAfford(totalPrice)) {
            StockTransaction transaction = new StockTransaction(symbol, quantity, stockPrice, TransactionType.BUY);
            portfolio.performTransaction(transaction);
            portfolioRepository.save(portfolio);
        } else {
            throw new IllegalArgumentException("Not enough balance");
        }
    }

    public void sellStock(String userId, String symbol, int quantity) {
        Portfolio portfolio = portfolioRepository.findByUserId(userId);
        if (portfolio.hasStock(symbol, quantity)) {
            double stockPrice = stockService.getCurrentPrice(symbol);
            StockTransaction transaction = new StockTransaction(symbol, quantity, stockPrice, TransactionType.SELL);
            portfolio.performTransaction(transaction);
            portfolioRepository.save(portfolio);
        } else {
            throw new IllegalArgumentException("Not enough stock");
        }
    }


}
