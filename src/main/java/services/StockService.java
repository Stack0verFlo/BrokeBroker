package services;

import Entities.Stock;
import config.MongoDBClient;
import repositories.StockRepository;
import repositoriesimpl.StockRepositoryImpl;

import java.util.List;
import java.util.stream.Collectors;

public class StockService {
    private final StockRepository stockRepository;

    public StockService() {
        this.stockRepository = new StockRepositoryImpl(MongoDBClient.getDatabase());
        initializeDatabase();
    }

    private void initializeDatabase() {
        // Prüfen, ob die Collection existiert und leere Collection mit Beispieldaten anlegen
        if (!collectionExists()) {
            createSampleStocks();
        }
    }

    private boolean collectionExists() {
        // Überprüfen, ob die Collection bereits existiert
        return stockRepository.collectionExists();
    }

    private void createSampleStocks() {
        // Erstellen von Beispielsaktien
        String[] symbols = {"AAPL", "MSFT", "GOOGL", "AMZN", "FB", "TSLA", "BRK.A", "V", "JNJ", "WMT"};
        for (String symbol : symbols) {
            Stock stock = new Stock(symbol, generateInitialPrice());
            stockRepository.save(stock);
        }
    }

    public List<String> getAllSymbols() {
        return stockRepository.findAll().stream()
                .map(Stock::getSymbol)
                .collect(Collectors.toList());
    }

    private double generateInitialPrice() {
        // Zufälliger Initialpreis für eine Aktie
        return Math.random() * 100 + 100; // Preise zwischen 100 und 200
    }

    public void updateStockPrice(String symbol, double newPrice) {
        Stock stock = stockRepository.findBySymbol(symbol);
        if (stock != null) {
            stock.setCurrentPrice(newPrice);
            stockRepository.save(stock);
        }
    }

    public Stock getStock(String symbol) {
        return stockRepository.findBySymbol(symbol);
    }
}
