import java.util.*;

public class StockController {
    private List<String> availableStocks;
    private Map<String, Double> stockPrices;

    public StockController() {
            this.availableStocks = Arrays.asList(
                    "AAPL",  // Apple
                    "MSFT",  // Microsoft
                    "GOOGL", // Alphabet (Google)
                    "AMZN",  // Amazon
                    "TSLA",  // Tesla
                    "FB",    // Meta Platforms (Facebook)
                    "BRK.A", // Berkshire Hathaway
                    "V",     // Visa
                    "JNJ",   // Johnson & Johnson
                    "WMT",   // Walmart
                    "JPM",   // JPMorgan Chase
                    "MA",    // Mastercard
                    "PG",    // Procter & Gamble
                    "UNH",   // UnitedHealth
                    "DIS",   // Disney
                    "NVDIA",  // Nvidia
                    "HD",    // Home Depot
                    "PYPL",  // PayPal
                    "BAC",   // Bank of America
                    "VZ",     // Verizon
                    "SAP"     //SAP
            );
            this.stockPrices = new HashMap<>();
            initializeStojPrices();

        }
    private void initializeStojPrices() {
        Random random = new Random();
        for (String stock : availableStocks) {
            double price = 100 + (1000 - 100) * random.nextDouble();
            stockPrices.put(stock, price);
        }
    }
    public double getPrice(String stockSymbol) {
        return stockPrices.getOrDefault(stockSymbol,0.0);
    }
    public List<String> getAvailableStocks() {
        return availableStocks;
    }

    // Weitere Methoden, z.B. zum Abrufen von Aktienkursen, können hier hinzugefügt werden
}
