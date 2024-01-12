import java.util.*;

public class StockController {
    private List<String> availableStocks;

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
        }
    public List<String> getAvailableStocks() {
        return availableStocks;
    }

    // Weitere Methoden, z.B. zum Abrufen von Aktienkursen, können hier hinzugefügt werden
}
