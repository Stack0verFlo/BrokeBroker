package Entities;

import java.util.ArrayList;
import java.util.List;

public class Stock {

    private String symbol;
    private double currentPrice;
    private ArrayList<Double> historicalPrices; // ArrayList zur Speicherung historischer Preise

    public List<Double> getHistoricalPrices(int maxEntries) {
        int start = Math.max(0, historicalPrices.size() - maxEntries);
        return historicalPrices.subList(start, historicalPrices.size());
    }
    public void addHistoricalPrice(double price) {
        if (historicalPrices.size() >= 40) {
            historicalPrices.remove(0); // Entfernt den ältesten Preis, wenn das Limit erreicht ist
        }
        historicalPrices.add(price);
    }

    public Stock(String symbol, double currentPrice) {
        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.historicalPrices = new ArrayList<>();
    }

    //public void addHistoricalPrice(double price) {
    //    historicalPrices.add(price);
    //}

    //public ArrayList<Double> getHistoricalPrices() {
    //    return historicalPrices;
    //}

    public String getSymbol() {
        return symbol;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
    public void setHistoricalPrices(List<Double> historicalPrices) {
        this.historicalPrices = new ArrayList<>(historicalPrices);
    }
}
