package repositories;

import Entities.Stock;

import java.util.List;

public interface StockRepository {
    boolean isStockListEmpty();
    Stock findStockBySymbol(String symbol);
    void saveStock(Stock stock);
    List<Stock> findAllStocks();
}
