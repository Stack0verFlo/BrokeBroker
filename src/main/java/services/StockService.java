package services;

import Entities.Stock;
import config.MongoDBClient;
import repositories.StockRepository;
import repositoriesimpl.StockRepositoryImpl;

public class StockService {
    private final StockRepository stockRepository;

    public StockService() {
        this.stockRepository = new StockRepositoryImpl(MongoDBClient.getDatabase());
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
