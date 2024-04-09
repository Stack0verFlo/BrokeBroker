package Entities;

import java.time.LocalDateTime;

public class Transaction {
    private String userId; // Referenz auf den User
    private String stockSymbol; // Das Aktiensymbol
    private int quantity; // Anzahl der gekauften/verkauften Aktien
    private double price; // Preis pro Aktie zum Zeitpunkt der Transaktion
    private LocalDateTime timestamp; // Zeitpunkt der Transaktion

    public Transaction(String userId, String stockSymbol, int quantity, double price, LocalDateTime timestamp) {
        this.userId = userId;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = timestamp;
    }

    // Getter und Setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
