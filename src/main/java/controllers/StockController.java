package controllers;

import Entities.Stock;
import repositories.PriceUpdateListener;
import services.StockService;

import java.util.List;

public class StockController {
    private final StockService stockService;
    private PriceUpdateListener priceUpdateListener;

    public StockController(StockService stockService) {
        this.stockService = stockService;
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
    public void setPriceUpdateListener(PriceUpdateListener listener) {
        this.priceUpdateListener = listener;
    }
    /*public void updateStockprice(String symbol) {
       if (priceUpdateListener != null) {
           double newPrice = stockService.getStock(symbol).getCurrentPrice();
              priceUpdateListener.onPriceUpdate(symbol, newPrice);
       }
    }*/
    public double getStockPrice(String symbol) {
        return stockService.getStock(symbol).getCurrentPrice();
    }
}
