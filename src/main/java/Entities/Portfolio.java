package Entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Portfolio {
    private String id;
    private String userId;

    private double balance;
    private List<StockTransaction> transactions;

    private Map<String, StockEntry> stocks;

    public Portfolio(String userId, double balance) {
        this.userId = userId;
        this.balance = balance;

        this.stocks = new HashMap<>();
    }

    public boolean canAfford(double amount) {
        return this.balance >= amount;
    }

    public boolean hasStock(String symbol, int quantity) {
        StockEntry stockEntry = this.stocks.get(symbol);
        return stockEntry != null && stockEntry.getQuantity() >= quantity;
    }

    public void addStock(String symbol, int quantity, double purchasePrice) {
        StockEntry stockEntry = stocks.getOrDefault(symbol, new StockEntry(symbol, 0, purchasePrice));

        double oldPrice = stockEntry.getPurchasePrice();
        int oldQuantity = stockEntry.getQuantity();

        stockEntry.setPurchasePrice(
                (oldPrice * oldQuantity + quantity * purchasePrice) / (oldQuantity + quantity)
        );

        stockEntry.setQuantity(oldQuantity + quantity);
        stocks.put(symbol, stockEntry);
    }

    public void removeStock(String symbol, int quantity) {
        StockEntry stockEntry = stocks.get(symbol);
        if (stockEntry != null && stockEntry.getQuantity() >= quantity) {
            stockEntry.setQuantity(stockEntry.getQuantity() - quantity);
            if (stockEntry.getQuantity() <= 0) {
                stocks.remove(symbol);
            }
        }
    }

    public void performTransaction(StockTransaction transaction) {
        if (transaction.getType() == TransactionType.BUY) {
            addStock(transaction.getSymbol(), transaction.getQuantity(), transaction.getPrice());
            balance -= transaction.getTotalPrice();
        } else if (transaction.getType() == TransactionType.SELL) {
            removeStock(transaction.getSymbol(), transaction.getQuantity());
            balance += transaction.getTotalPrice();
        }
    }

    public List<StockEntry> getStocks() {
        return new ArrayList<>(stocks.values());
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
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
}


