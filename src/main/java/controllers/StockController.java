package controllers;

import Entities.Stock;
import repositories.PriceUpdateListener;
import services.StockService;

import java.util.List;

public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    public List<String> getAllSymbols() {
        return stockService.getAllSymbols();
    }

    public Stock getStock(String symbol) {
        return stockService.getStock(symbol);
    }
}
