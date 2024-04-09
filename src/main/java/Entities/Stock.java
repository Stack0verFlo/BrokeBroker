package Entities;

import java.util.ArrayList;
import java.util.List;

public class Stock {
    private final String name;
    private double currentPrice;
    private final List<Double> historicalPrices;

    public Stock(String name, double currentPrice) {
        this.name = name;
        this.currentPrice = currentPrice;
        this.historicalPrices = new ArrayList<>();
        // Der aktuelle Preis wird auch als erster historischer Preis hinzugefügt
        this.historicalPrices.add(currentPrice);
    }

    // Getter und Setter
    public String getName() {
        return name;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
        // Wenn sich der Preis ändert, fügen wir ihn zur Historie hinzu
        this.historicalPrices.add(currentPrice);
    }

    public List<Double> getHistoricalPrices() {
        return historicalPrices;
    }

    // Methode zum Hinzufügen historischer Preise
    public void addHistoricalPrice(double price) {
        historicalPrices.add(price);
    }
}
