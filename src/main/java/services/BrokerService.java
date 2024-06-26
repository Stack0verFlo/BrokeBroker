package services;

import Entities.Portfolio;
import Entities.StockTransaction;
import Entities.TransactionType;
import repositories.PortfolioRepository;

public class BrokerService {
    private final PortfolioRepository portfolioRepository;
    private final StockService stockService;
    private String portfolioId;
    private Portfolio portfolio;

    public BrokerService(PortfolioRepository portfolioRepository, StockService stockService) {
        this.portfolioRepository = portfolioRepository;
        this.stockService = stockService;
    }

    public void buyStock(String portfolioId, String symbol, int quantity) {
        this.portfolioId = portfolioId;
        this.portfolio = portfolioRepository.findById(portfolioId);
        double stockPrice = stockService.getCurrentPrice(symbol);

        if (stockPrice <= 0) {
            // Handle the case where the stock does not exist or the price is not valid.
            throw new IllegalArgumentException("Stock symbol not found or invalid price");
        }

        double totalPrice = stockPrice * quantity;

        if (portfolio.canAfford(totalPrice)) {
            StockTransaction transaction = new StockTransaction(symbol, quantity, stockPrice, TransactionType.BUY);
            portfolio.performTransaction(transaction);
            portfolioRepository.save(portfolio);
        } else {
            throw new IllegalArgumentException("Not enough balance");
        }
    }

    public void sellStock(String portfolioId, String symbol, int quantity) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId);
        if (portfolio == null) {
            throw new IllegalStateException("No portfolio found for ID: " + portfolioId);
        }

        if (!portfolio.hasStock(symbol, quantity)) {
            throw new IllegalArgumentException("Not enough stock to sell");
        }

        double stockPrice = stockService.getCurrentPrice(symbol);
        StockTransaction transaction = new StockTransaction(symbol, quantity, stockPrice, TransactionType.SELL);
        portfolio.performTransaction(transaction);
        portfolioRepository.save(portfolio);
    }
}
