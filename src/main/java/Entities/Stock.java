package Entities;

import java.util.ArrayList;
import java.util.List;

public class Stock {
    private final String name;
    private int currentPrice;
    private List<Integer> historicalPrices;

    public Stock(String name, int currentPrice) {
        this.name = name;
        this.currentPrice = currentPrice;
        this.historicalPrices = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public List<Integer> getHistoricalPrices() {
        return historicalPrices;
    }

    public void setHistoricalPrices(List<Integer> historicalPrices) {
        this.historicalPrices = historicalPrices;
    }
}