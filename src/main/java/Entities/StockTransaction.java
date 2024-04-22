package Entities;

public class StockTransaction {
    private String symbol;
    private final int quantity;
    private final double price;
    private final TransactionType type;

    public StockTransaction(String symbol, int quantity, double price, TransactionType type) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }

    public final double getTotalPrice() {
        return quantity * price;
    }

    public final String getSymbol() {
        return symbol;
    }

    public final void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public final int getQuantity() {
        return quantity;
    }

    public final double getPrice() {
        return price;
    }

    public final TransactionType getType() {
        return type;
    }
}


