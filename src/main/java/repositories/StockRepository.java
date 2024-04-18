package repositories;

import Entities.Stock;

import java.util.List;

public interface StockRepository {
    boolean isEmpty();
    Stock findBySymbol(String symbol);
    void save(Stock stock);
    List<Stock> findAll();
}
