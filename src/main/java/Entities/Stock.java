package Entities;

import java.util.ArrayList;

public class Stock {

    private String symbol;
    private double currentPrice;
    private ArrayList<Double> historicalPrices; // ArrayList zur Speicherung historischer Preise

    public Stock(String symbol, double currentPrice) {
        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.historicalPrices = new ArrayList<>();
        this.historicalPrices.add(currentPrice);
    }

    public void updatePrice(double newPrice) {
        setCurrentPrice(newPrice);
        historicalPrices.add(newPrice);
    }

    public void addHistoricalPrice(double price) {
        historicalPrices.add(price);
    }

    public ArrayList<Double> getHistoricalPrices() {
        return historicalPrices;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
}
