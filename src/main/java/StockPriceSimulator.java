import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class StockPriceSimulator {
    private static final Random random = new Random();

    // Generiert simulierte Preisdaten für eine Aktie für die letzten 30 Tage
    public static Map<String, Double> generatePriceData(String symbol) {
        Map<String, Double> prices = new HashMap<>();
        for (int i = 0; i < 30; i++) {
            double price = 100 + (random.nextDouble() * 50 - 25); // Generiert einen Preis zwischen 75 und 125
            prices.put("Tag " + (i + 1), price);
        }
        return prices;
    }
}
