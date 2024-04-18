package controllers;

import Entities.Portfolio;
import services.PortfolioService;

public class PortfolioController {
    private final PortfolioService portfolioService;

    public PortfolioController() {
        this.portfolioService = new PortfolioService();
    }

    public void addStockToPortfolio(String portfolioId, String symbol, int quantity) {
        portfolioService.addStockToPortfolio(portfolioId, symbol, quantity);
    }

    public void removeStockFromPortfolio(String portfolioId, String symbol, int quantity) {
        portfolioService.removeStockFromPortfolio(portfolioId, symbol, quantity);
    }

    public Portfolio getPortfolio(String portfolioId) {
        return portfolioService.getPortfolio(portfolioId);
    }
}
