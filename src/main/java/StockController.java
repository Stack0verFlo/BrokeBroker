import java.util.*;

public class StockController {
    private List<String> availableStocks;
    private Map<String, Double> currentPrices;
    private Map<String, List<Double>> historicalPrices;

    public StockController() {
        this.availableStocks = Arrays.asList("AAPL", "MSFT", "GOOGL", "AMZN", "TSLA", "FB", "BRK.A", "V",
                "JNJ", "WMT", "JPM", "MA", "PG", "UNH", "DIS", "NVDIA", "HD",
                "PYPL", "BAC", "VZ", "SAP"); // Beispielaktien
        this.currentPrices = new HashMap<>();
        this.historicalPrices = new HashMap<>();
        initializePrices();
    }

    private void initializePrices() {
        Random random = new Random();
        for (String stock : availableStocks) {
            // Aktuellen Preis generieren
            double currentPrice = 100 + (random.nextDouble() * 900); // Zwischen 100 und 1000
            currentPrices.put(stock, currentPrice);

            // Historische Preise generieren
            List<Double> prices = new ArrayList<>();
            prices.add(currentPrice); // Aktuellen Preis als ersten Eintrag hinzufügen
            for (int i = 1; i < 30; i++) { // Letzte 30 Tage
                prices.add(currentPrice + (random.nextDouble() * 20 - 10)); // Kleine Schwankungen
            }
            historicalPrices.put(stock, prices);
        }
    }


    public double getCurrentPrice(String stock) {
        return currentPrices.getOrDefault(stock, 0.0);
    }

    public List<Double> getHistoricalPrices(String stock) {
        return historicalPrices.getOrDefault(stock, new ArrayList<>());
    }

    public List<String> getAvailableStocks() {
        return availableStocks;
    }
}

