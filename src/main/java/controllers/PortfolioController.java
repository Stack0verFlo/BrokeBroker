package controllers;

import Entities.Portfolio;
import services.PortfolioService;

public class PortfolioController {
    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    public Portfolio getPortfolioByUserId(String userId) {
        return portfolioService.getPortfolioByUserId(userId);
    }
}
