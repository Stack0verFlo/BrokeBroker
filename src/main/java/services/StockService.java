package services;

import Entities.Stock;
import repositories.StockRepository;
import repositoriesimpl.StockRepositoryImpl;
import config.MongoDBClient;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class StockService {
    private final StockRepository stockRepository;
    private final Random random;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
        this.random = new Random();
        this.initializeStocks();
    }

    private void initializeStocks() {
        if (stockRepository.isEmpty()) {
            String[] symbols = {"AAPL", "GOOGL", "MSFT", "AMZN", "FB", "TSLA", "NFLX", "INTC", "AMD", "NVDA"};
            for (String symbol : symbols) {
                double initialPrice = 100 + (random.nextDouble() * 100); // Preis zwischen 100 und 200
                stockRepository.save(new Stock(symbol, initialPrice));
            }
        }
    }

    public void updateStockPrice(String symbol) {
        Stock stock = stockRepository.findBySymbol(symbol);
        if (stock != null) {
            double currentPrice = stock.getCurrentPrice();
            double newPrice = currentPrice + (random.nextGaussian() * 10); // Neuer Preis wird im Bereich von -10 bis +10 des aktuellen Preises generiert
            stock.addHistoricalPrice(stock.getCurrentPrice());
            stock.setCurrentPrice(newPrice);
            stockRepository.save(stock);
        }
    }

    public Stock getStock(String symbol) {
        return stockRepository.findBySymbol(symbol);
    }
    public List<String> getAllSymbols() {
        // Diese Methode sollte alle Aktiensymbole aus der Datenbank holen.
        // Hier ein Beispielcode, der eine vordefinierte Liste zurückgibt:
        return stockRepository.findAll().stream()
                .map(Stock::getSymbol)
                .collect(Collectors.toList());
    }
}
