package Entities;

import java.util.Date;

public class Transaction {
    private String id;
    private Stock stock;
    private int quantity;
    private String type; // "BUY" oder "SELL"
    private Date transactionDate;

    public Transaction(String id, Stock stock, int quantity, String type, Date transactionDate) {
        this.id = id;
        this.stock = stock;
        this.quantity = quantity;
        this.type = type;
        this.transactionDate = transactionDate;
    }

    public String getId() {
        return id;
    }

    public Stock getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getType() {
        return type;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }
}
