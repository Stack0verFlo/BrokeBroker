package repositories;

import Entities.Stock;

import java.util.List;

public interface StockRepository {
    Stock findBySymbol(String symbol);
    void save(Stock stock);
    boolean collectionExists();
    List<Stock> findAll();
}
