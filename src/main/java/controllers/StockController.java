package controllers;

import Entities.Stock;
import services.StockService;

public class StockController {
    private final StockService stockService;

    public StockController() {
        this.stockService = new StockService();
    }

    public void updateStockPrice(String symbol, double newPrice) {
        stockService.updateStockPrice(symbol, newPrice);
    }

    public Stock getStock(String symbol) {
        return stockService.getStock(symbol);
    }
}
