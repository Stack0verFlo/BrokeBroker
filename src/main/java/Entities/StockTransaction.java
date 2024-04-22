package Entities;

public class StockTransaction {
    private String symbol;
    private final int quantity;
    private final double price;
    private final TransactionType type; // Enum für BUY oder SELL

    public StockTransaction(String symbol, int quantity, double price, TransactionType type) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }

    public double getTotalPrice() {
        return quantity * price;
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

    public double getPrice() {
        return price;
    }

    public TransactionType getType() {
        return type;
    }
}


