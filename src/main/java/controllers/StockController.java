package controllers;

import Entities.Stock;
import services.StockService;

import java.util.List;

public class StockController {
    private final StockService stockService;

    public StockController() {
        this.stockService = new StockService();
    }

    public void updateStockPrice(String symbol) {
        stockService.updateStockPrice(symbol);
    }

    public List<String> getAllSymbols() {
        return stockService.getAllSymbols();
    }

    public Stock getStock(String symbol) {
        return stockService.getStock(symbol);
    }
}
