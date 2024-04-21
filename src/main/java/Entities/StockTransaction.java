package Entities;

import java.util.Date;

public class StockTransaction {
    private String id;
    private String symbol;
    private int quantity;
    private double price;
    private TransactionType type; // Enum für BUY oder SELL

    public StockTransaction(String symbol, int quantity, double price, TransactionType type) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }

    public double getTotalPrice() {
        return quantity * price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
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

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}


