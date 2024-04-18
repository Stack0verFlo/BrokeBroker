package repositories;

import Entities.Stock;

public interface StockRepository {
    Stock findBySymbol(String symbol);
    void save(Stock stock);
    boolean collectionExists();
}
