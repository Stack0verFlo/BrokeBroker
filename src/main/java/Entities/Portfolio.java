package Entities;

import java.util.HashMap;
import java.util.Map;

public class Portfolio {
    private String id;
    private User owner;
    private Map<Stock, Integer> stocks; // Aktien und ihre Anzahlen

    public Portfolio(String id, User owner) {
        this.id = id;
        this.owner = owner;
        this.stocks = new HashMap<>();
    }

    public void addStock(Stock stock, int quantity) {
        stocks.put(stock, stocks.getOrDefault(stock, 0) + quantity);
    }

    public void removeStock(Stock stock, int quantity) {
        int currentQuantity = stocks.getOrDefault(stock, 0);
        if (currentQuantity <= quantity) {
            stocks.remove(stock);
        } else {
            stocks.put(stock, currentQuantity - quantity);
        }
    }

    public Map<Stock, Integer> getStocks() {
        return stocks;
    }

    public User getOwner() {
        return owner;
    }

    public String getId() {
        return id;
    }
}
