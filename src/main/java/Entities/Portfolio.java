package Entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Portfolio {
    private String id;
    private String userId;
    private User owner;
    private Map<Stock, Integer> stocks; // Aktien und ihre Anzahlen

    public Portfolio(String userId) {
        this.userId = userId;
        //this.owner = owner;
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

    public String getUserId() {
        return userId;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return this.id;
    }
}


