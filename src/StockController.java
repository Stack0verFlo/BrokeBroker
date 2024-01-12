import java.util.*;

public class StockController {
    private List<String> availableStocks;

    public StockController() {
        this.availableStocks = Arrays.asList("AAPL", "MSFT", "GOOGL", "AMZN", "TSLA", "NVDIA", "SAP");
    }

    public List<String> getAvailableStocks() {
        return availableStocks;
    }

    // Weitere Methoden, z.B. zum Abrufen von Aktienkursen, können hier hinzugefügt werden
}
