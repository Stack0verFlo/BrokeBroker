package Entities;

import java.util.HashMap;
import java.util.Map;

public class Portfolio {
    private String userId; // Referenz auf den User
    private Map<String, Integer> stocks; // Aktiensymbol zu Anzahl der Aktien

    public Portfolio(String userId) {
        this.userId = userId;
        this.stocks = new HashMap<>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Integer> getStocks() {
        return stocks;
    }

    public void setStocks(Map<String, Integer> stocks) {
        this.stocks = stocks;
    }

    // Fügen Sie hier Methoden hinzu, um Aktien zum Portfolio hinzuzufügen oder zu entfernen
}
