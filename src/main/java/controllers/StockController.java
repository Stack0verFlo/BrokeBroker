package controllers;

import Entities.Stock;
import services.StockService;

import java.util.List;

public class StockController {
    private final StockService stockService;

    public StockController() {
        this.stockService = new StockService();
    }

    public void updateStockPrice(String symbol, double newPrice) {
        stockService.updateStockPrice(symbol, newPrice);
    }

    public String[] getAllSymbols() {
        // Implementation to get all symbols from the stockService
        // Could be something like this:
        List<String> symbols = stockService.getAllSymbols();
        return symbols.toArray(new String[0]);
    }

    public Stock getStock(String symbol) {
        return stockService.getStock(symbol);
    }
}
