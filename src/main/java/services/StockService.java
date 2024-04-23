package services;

import Entities.Stock;
import repositories.PriceUpdateListener;
import repositories.StockRepository;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class StockService {
    private final StockRepository stockRepository;
    private final Random random;
    private PriceUpdateListener priceUpdateListener;


    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
        this.random = new Random();
        this.initializeStocks();
    }

    public void initializeStocks() {
        if (stockRepository.isStockListEmpty()) {
            String[] symbols = {"AAPL", "GOOGL", "MSFT", "AMZN", "FB", "TSLA", "NFLX", "INTC", "AMD", "NVDA"};
            for (String symbol : symbols) {
                double initialPrice = 100 + (random.nextDouble() * 100); // Preis zwischen 100 und 200
                stockRepository.saveStock(new Stock(symbol, initialPrice));
            }
        }
    }

    public void updateStockPrice(String symbol) {
        Stock stock = stockRepository.findStockBySymbol(symbol);
        if (stock != null) {
            double currentPrice = stock.getCurrentPrice();
            double newPrice = currentPrice + (random.nextGaussian() * 10); // Neuer Preis wird generiert
            newPrice = Math.max(newPrice, 1.0);
            stock.addHistoricalPrice(currentPrice);
            stock.setCurrentPrice(newPrice);
            stockRepository.saveStock(stock);

            if (priceUpdateListener != null) {
                priceUpdateListener.onPriceUpdate(symbol, newPrice);
            }
        }
    }

    public Stock getStock(String symbol) {
        return stockRepository.findStockBySymbol(symbol);
    }
    public List<String> getAllSymbols() {
        // Diese Methode sollte alle Aktiensymbole aus der Datenbank holen.
        // Hier ein Beispielcode, der eine vordefinierte Liste zurückgibt:
        return stockRepository.findAllStocks().stream()
                .map(Stock::getSymbol)
                .collect(Collectors.toList());
    }
    public void setPriceUpdateListener(PriceUpdateListener listener) {
        this.priceUpdateListener = listener;
    }
    public double getCurrentPrice(String symbol) {
        Stock stock = getStock(symbol);
        return stock != null ? stock.getCurrentPrice() : 0.0;
    }
}
